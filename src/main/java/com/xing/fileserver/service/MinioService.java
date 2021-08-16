package com.xing.fileserver.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.xing.fileserver.common.exception.BusinessException;
import com.xing.fileserver.config.MinioConfig;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.PutObjectOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class MinioService {

    @Autowired
    private MinioConfig config;

    private MinioClient minioClient;

    @PostConstruct
    public void initClient() {
        try {
            this.minioClient = new MinioClient(config.getEndpoint(), config.getUser(), config.getPassword());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException("Minio初始化异常");
        }
    }

    public String getBucket() {
        String bucket = Optional.ofNullable(config.getBucket()).orElse("default");
        try {
            boolean isExist = minioClient.bucketExists(bucket);
            if (!isExist) {
                minioClient.makeBucket(bucket);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException("Minio创建桶异常");
        }
        return bucket;
    }

    public void save(MultipartFile file, String path) {
        String bucket = getBucket();
        try {
            InputStream inputStream = file.getInputStream();
            long size = file.getSize();
            PutObjectOptions putObjectOptions = new PutObjectOptions(size, -1);
            putObjectOptions.setContentType(file.getContentType());
            minioClient.putObject(bucket, path, inputStream, putObjectOptions);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException("Minio保存异常");
        }
    }

    public byte[] get(String path) {
        byte[] data = new byte[0];
        try {
            InputStream is = minioClient.getObject(getBucket(), path);
            data = IoUtil.readBytes(is);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException("Minio获取异常");
        }
        return data;
    }

    public void delete(String path) {

        if (StrUtil.isBlank(path)) {
            return;
        }
        String bucket = getBucket();
        try {
            minioClient.removeObject(bucket, path);
        } catch (Exception e) {
            log.error("Minio删除{} {}异常", bucket, path, e.getMessage());
        }
    }

    public void delete(List<String> paths) {
        if (Objects.isNull(paths) || paths.size() == 0) {
            return;
        }
        for (String path : paths) {
            delete(path);
        }

    }

    public String presignedPut(String path) {
        String url = null;
        String bucket = getBucket();
        try {
            url = minioClient.presignedPutObject(bucket, path, config.getExpire());
        } catch (Exception e) {
            log.error("Minio put  {} 预签异常", path);
            throw new BusinessException("Minio保存异常");
        }
        return url;
    }

    public String presignedGet(String path) {
        String url = null;
        String bucket = getBucket();
        try {
            url = minioClient.presignedGetObject(bucket, path, config.getExpire());
        } catch (Exception e) {
            log.error("Minio put  {} 预签异常", path);
            throw new BusinessException("Minio保存异常");
        }

        return url;
    }

    public ObjectStat stat(String path) {
        String bucket = getBucket();
        ObjectStat objectStat = null;

        try {
            objectStat = minioClient.statObject(bucket, path);
        } catch (Exception e) {
            log.error("Minio stat  {} 异常", path);
        }

        return objectStat;
    }

}
