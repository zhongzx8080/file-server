package com.xing.fileserver.controller;

import com.xing.fileserver.model.FileUpload;
import com.xing.fileserver.service.FileUploadService;
import com.xing.fileserver.util.FileUtil;
import com.xing.fileserver.util.ResultBean;
import com.xing.fileserver.util.ResultConstant;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;

/**
 * @author xing
 * @date 2019/5/29 23:15
 */

@Controller
@RequestMapping("file")
public class FileUploadController {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private FileUploadService fileUploadService;

  @Value("${file.upload.path}")
  private String fileUploadPath;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Value("${spring.redis.host}")
  private String redisHost;

  /*
   *
   * 获取文件
   * @param 文件路径
   * @param w 图片宽度(默认为原图片宽度)
   * @param h 图片高度(默认为原图片高度)
   * @param q 图片质量(0.0 - 1.0)
   *
   * */
  @GetMapping("get")
  public void getFile(@RequestParam("path") String path, @RequestParam(value = "w", required = false) String w, @RequestParam(value = "h", required = false) String h, @RequestParam(value = "q", defaultValue = "1.0") String q, HttpServletRequest request, HttpServletResponse response) {
    logger.info("redis host {}", redisHost);
    // url 作为键
    String requestUrl = String.format("%s?%s", request.getRequestURI(), request.getQueryString());
    try {
      path = Paths.get(fileUploadPath, URLDecoder.decode(path, "UTF-8")).toAbsolutePath().toString();
    } catch (UnsupportedEncodingException e) {
      logger.error("url解码异常 {}", e);
    }
    String ext = FileUtil.getBySymbol(path, ".");
    String filename = FileUtil.getBySymbol(path, "\\");
    Path filePath = Paths.get(path);
    if (Files.notExists(filePath)) {
      logger.error("获取失败,文件不存在");
      return;
    }
    try {
      boolean isPicture = FileUtil.isPicture(ext);
      byte[] data = new byte[0];
      if (isPicture) {
        response.setContentType(String.format("image/%s", "JPEG"));

        String base64Data = redisTemplate.opsForValue().get(requestUrl);
        if (Objects.nonNull(base64Data) && !Objects.equals("", base64Data)) {
          logger.info("缓存加载, {}", requestUrl);
          data = Base64.getDecoder().decode(base64Data);
          response.getOutputStream().write(data);
          response.getOutputStream().flush();
          response.getOutputStream().close();
          return;
        }
//
//        logger.info("没有缓存, {}", requestUrl);

        // 压缩图片
        Integer srcWidth = FileUtil.getWidthHeight(path)[0];
        Integer srcHeight = FileUtil.getWidthHeight(path)[1];
        Integer width, height;
        Double quality = 1.0;
        try {
          width = Integer.parseInt(w);
          if (Objects.isNull(width) || width <= 0) {
            width = srcWidth;
          }
        } catch (Exception e) {
          width = srcWidth;
        }
        try {
          height = Integer.parseInt(h);
          if (Objects.isNull(height) || height <= 0) {
            height = srcHeight;
          }
        } catch (Exception e) {
          height = srcHeight;
        }
        try {
          quality = Double.parseDouble(q);
          if (quality < 0 || quality > 1 || Objects.isNull(quality)) {
            quality = 1.0;
          }
        } catch (Exception e) {
          quality = 1.0;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Thumbnails.of(path).size(width, height).keepAspectRatio(false).outputFormat("JPG").outputQuality(quality).toOutputStream(bos);
        data = bos.toByteArray();

        // 转成base64 保存到 redis
        base64Data = Base64.getEncoder().encodeToString(data);
        redisTemplate.opsForValue().set(requestUrl, base64Data);
      } else {
        data = Files.readAllBytes(filePath);
        response.setHeader("Content-Disposition", String.format("attachment;filename=%s", filename));
        response.setContentLength(data.length);
      }
      response.getOutputStream().write(data);
      response.getOutputStream().close();

    } catch (IOException e) {
      logger.error("读取文件异常 {}", e);
    }
  }


  /*
   * 上传文件
   *
   * */
  @PostMapping("upload")
  @ResponseBody
  public ResultBean uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

    FileUpload fileUpload = fileUploadService.store(file, request);
    if (Objects.isNull(fileUpload)) {
      return new ResultBean.Builder().code(ResultConstant.CODE_SERVER_EXCEPTION).msg(ResultConstant.MSG_SERVER_EXCEPTION).build();
    }

    return new ResultBean.Builder().data(fileUpload).build();
  }

  /*
   *
   * 通过id获取文件信息
   *
   * */
  @GetMapping("{id}")
  @ResponseBody
  public ResultBean getFileById(@PathVariable String id) {
    FileUpload fileUpload = fileUploadService.getById(id);
    if (Objects.isNull(fileUpload)) {
      return new ResultBean.Builder().code(-1).msg("文件不存在").build();
    }
    return new ResultBean.Builder().data(fileUpload).build();
  }

  /*
   *
   * 通过id 删除文件
   *
   * */
  @DeleteMapping("{id}")
  @ResponseBody
  public ResultBean deleteFileById(@PathVariable String id) {
    FileUpload fileUpload = fileUploadService.deleteById(id);
    return new ResultBean.Builder().data(fileUpload).build();
  }


}
