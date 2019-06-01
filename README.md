# SpringBoot 文件微服务

## 简介

> 基于`SpringBoot`文件微服务，可`通过参数获取图片`,`上传文件`，`删除文件`等

## 开发环境

- `JDK 1.8`
- `SpringBoot 2.1.5.RELEASE`
- `MySQL 8` [数据库脚本](<https://github.com/zhongzhixing/file-server/blob/master/file-server.sql>)
- `MyBatis 3.5`

## 接口

### 获取文件`/api/file/get`

```java
METHOD GET
@param path 文件路径
@param w 图片宽度;只对图片生效
@param h 图片高度;只对图片生效
@param q 图片质量 [0,1];只对图片生效
```

```http
curl http://localhost:9999/api/file/get?path=other%5C2019%5C6%5C1%5Cf171072a-ee5c-422c-ba43-fb99fbeb77a4.jpg
```

### 通过文件id获取文件信息 `/api/file/{id}`

```java
METHOD GET
@param id 文件id
```

```http
curl http://localhost:9999/api/file/6128ca97-be09-418e-874e-f89169e2d79f
```

### 上传文件`/api/file/upload`

```java
METHOD POST
前端指定 name 为 `file` ,服务端可以通过这个 `name` 获取文件的二进制内容
```

```http
curl -F "file=@hello.png" http://localhost:9999/api/file/upload
```

### 删除文件`/api/file/{id}`

```java
METHOD DELETE
@param id 文件id
```

```http
curl -X DELETE http://localhost:9999/api/file/6128ca97-be09-418e-874e-f89169e2d79f
```

