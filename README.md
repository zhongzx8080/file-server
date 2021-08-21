## 前言

基于`Minio`,使用`Spring Boot`搭建文件微服务

## 开发环境

- `JDK 1.8`
- `SpringBoot 2.5.3`
- `MySQL 8` 
- `Minio`

## 说明

- 执行`file_server.sql`初始数据库及表
- 执行`docker-compose.yml`启动`Minio`服务
- 启动程序,访问http://localhost:9090/swagger-ui/  查看已实现接口

- 配置

```yml
# 主要配置说明
# minio
minio:
  #  Minio 服务地址,账号密码
  endpoint: http://localhost:9000
  user: minio
  password: minio123
  bucket: ${spring.application.name:default}
  # 单位秒,预签url有效期(默认一天)
  expire: 86400
# 下载相关
download:
  # 对外访问的url是否使用Minio预签url,否使用 host+api拼接地址
  presigned: true
  # java服务访问地址
  host: http://localhost:9090
  # 下载文件的api接口,须与Controller定义的下载url相同;若不同自行代理转发处理
  api: files/download
```

- 预签上传

  > 直接往`Minio`服务器上传文件,避免中转,提高上传速度

  - 前端先`POST`请求`/files/presigned`，生成一个给`HTTP PUT`请求用的`presigned URL`。前端可以用这个`URL`进行上传
  - 前端通过`PUT`请求预签`url`上传文件
  - 最后`POST` 请求`/files/finished`上传完毕

- `@FillFiles`注解

  > 配合`FillFileSerializer`序列化器,自动填充业务id对应文件列表

  ```java
  public class FillFilesTestVO implements Serializable {
  
      private String bizId;
  
      @FillFiles("bizId")
      @JsonSerialize(nullsUsing = FillFileSerializer.class)
      private List<UploadedFileVO> files;
  }
  ```

## 接口实现

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/4228d580d83c44b29a31869994b2c3b7~tplv-k3u1fbpfcp-watermark.awebp)