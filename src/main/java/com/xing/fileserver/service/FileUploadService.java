package com.xing.fileserver.service;

import com.xing.fileserver.mapper.FileUploadMapper;
import com.xing.fileserver.model.FileUpload;
import com.xing.fileserver.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @author xing
 * @date 2019/5/30 21:55
 */

@Service
@Transactional
public class FileUploadService {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Value("${file.upload.path}")
  private String fileUploadPath;

  @Value("${get.file.api}")
  private String getFileApi;

  private String defaultModule = "other";

  @Autowired
  private FileUploadMapper fileUploadMapper;

  public FileUpload store(MultipartFile file, HttpServletRequest request) {
    logger.info("文件保存路径 {}", fileUploadPath);
    FileUpload fileUpload = new FileUpload();
    String id = UUID.randomUUID().toString();
    fileUpload.setId(id);

    String name = file.getOriginalFilename();
    fileUpload.setName(name);
    String ext = FileUtil.getBySymbol(name, ".");
    fileUpload.setExt(ext);

    Long size = file.getSize();
    fileUpload.setSize(size.intValue());

    LocalDateTime now = LocalDateTime.now();
    fileUpload.setUploadTime(now.toString());
    int year = now.getYear();
    int month = now.getMonthValue();
    int day = now.getDayOfMonth();
    String module = request.getParameter("module");
    if (Objects.isNull(module) || Objects.equals("", module)) {
      module = defaultModule;
    }
    fileUpload.setModule(module);
    Path storePath = Paths.get(module, String.valueOf(year), String.valueOf(month), String.valueOf(day), String.format("%s.%s", id, ext));
    fileUpload.setPath(storePath.toString());
    Path absPath = Paths.get(fileUploadPath, storePath.toString());
    Path storeDir = Paths.get(fileUploadPath, module, String.valueOf(year), String.valueOf(month), String.valueOf(day));
    if (Files.notExists(storeDir)) {
      try {
        Files.createDirectories(storeDir);
        logger.info("文件夹创建成功: {}", storeDir);
      } catch (IOException e) {
        logger.error("文件夹创建失败:{}", e);
        return null;
      }
    }

    try {
      String url = String.format("%s?path=%s", getFileApi, URLEncoder.encode(storePath.toString(), "UTF-8"));
      fileUpload.setUrl(url);
    } catch (UnsupportedEncodingException e) {
      logger.error("url编码异常:{}", e);
    }

    try {
      Files.copy(file.getInputStream(), Paths.get(absPath.toAbsolutePath().toString()));
      logger.info("file-upload {}", fileUpload);
      int result = fileUploadMapper.insertSelective(fileUpload);
      if (result > 0) {
        logger.info("保存文件成功 {}", fileUpload);
      } else {
        logger.error("保存文件失败");
      }
    } catch (IOException e) {
      logger.error("保存文件异常 {}", e);
      return null;
    }


    return fileUpload;
  }

  @Cacheable(cacheNames = "fileUpload", key = "#id")
  public FileUpload getById(String id) {
    logger.info("调用 getById {}", id);
    FileUpload fileUpload = fileUploadMapper.selectByPrimaryKey(id);
    return fileUpload;
  }

  @CachePut(cacheNames = "fileUpload", key = "#id")
  public FileUpload deleteById(String id) {
    FileUpload fileUpload = new FileUpload();
    fileUpload.setId(id);
    fileUpload.setStatus("1");
    fileUploadMapper.updateByPrimaryKeySelective(fileUpload);
    return fileUpload;
  }
}
