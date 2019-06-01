package com.xing.fileserver.util;

/**
 * @author xing
 * @date 2019/5/29 23:23
 */

public class ResultConstant {
  // 服务器异常
  public static Integer CODE_SERVER_EXCEPTION = -1;
  public static String MSG_SERVER_EXCEPTION = "服务器异常";

  // 请求成功
  public static Integer CODE_SUCCESS = 0;
  public static String MSG_SUCCESS = "ok";

  // 上传失败
  public static Integer CODE_UPLOAD_FAIL = 1;
  public static String MSG_UPLOAD_FAIL = "上传失败";

  // 文件不存在
  public static Integer CODE_FILE_NOT_EXIST = 2;
  public static String MSG_FILE_NOT_EXIST = "文件不存在";

}
