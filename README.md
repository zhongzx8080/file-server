# SpringBoot 文件微服务

## 简介

> 基于`Minio`实现的文件微服务

## 开发环境

- `JDK 1.8`
- `SpringBoot 2.5.3`
- `MySQL 8` 
- `MyBatis Plus 3.4.3`

## 说明

- 序列化器说明

```java
FillFileSerializer序列化器配合@FillFiles注解实现自动填充文件返回
```

- 配置说明

```yaml
download:
  # java服务访问地址
  host: http://localhost:9090
  # 下载文件的api接口
  api: files/download
  # true返回的文件链接使用Minio的预签url;false走 files/download 接口
  presigned: true
```

- `Nginx`配置

```shell
server {
 
	# 略
    # 配置桶前缀转发
    location ^~ /file-server {
        proxy_pass http://file_server;
		#!!!  host转发必须配置，与Minio签名校验有关
        proxy_set_header Host $host;
    	# 略
    }

}
```



## 接口

> http://localhost:9090/swagger-ui/

## 上传接口

### 1. 使用预签上传

> 避免中转，提升上传速度

- 前端先`POST`请求`/files/presigned`，生成一个给`HTTP PUT`请求用的`presigned URL`。前端可以用这个`URL`进行上传
- 前端直接通过预签`url`上传文件
- 最后`POST` 请求`/files/finished`上传完毕

### 2. 使用服务上传

- 文件上传到服务，服务再上传到`Minio`服务