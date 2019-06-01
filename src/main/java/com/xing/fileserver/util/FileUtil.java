package com.xing.fileserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author xing
 * @date 2019/5/31 23:40
 */

public class FileUtil {

  private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

  /*
   *
   *  获取文件后缀名或文件名
   *  @param path 路径
   *  @param symbol 截取字符
   *
   * */
  public static String getBySymbol(String path, String symbol) {
    String ext = "";
    if (Objects.isNull(path) || Objects.equals("", path)) {
      return ext;
    }
    int index = path.lastIndexOf(symbol);
    if (index > -1) {
      ext = path.substring(index + 1);
    }
    return ext;
  }

  /*
   *
   * 判断是否是图片类型
   * @param ext 图片后缀名
   *
   * */
  public static boolean isPicture(String ext) {
    String[] picExtArr = {"png", "jpg", "jpeg", "gif", "bmp"};
    for (int i = 0, len = picExtArr.length; i < len; i++) {
      String picExt = picExtArr[i];
      if (picExt.equalsIgnoreCase(ext)) {
        return true;
      }
    }
    return false;
  }

  /*
   *
   *  获取图片宽高
   *  @param path 文件路径
   *  返回结果 [宽，高]
   * */
  public static int[] getWidthHeight(String path) {
    int[] result = {0, 0};
    Path filePath = Paths.get(path);
    if (Files.notExists(filePath)) {
      logger.error("获取图片宽高, 文件不存在 {}", path);
      return result;
    }
    try {
      Image srcImage = ImageIO.read(filePath.toFile());
      result[0] = srcImage.getWidth(null);
      result[1] = srcImage.getHeight(null);
    } catch (IOException e) {
      logger.error("获取图片宽高, 文件读取异常 {}", e);
    }
    return result;
  }
}
