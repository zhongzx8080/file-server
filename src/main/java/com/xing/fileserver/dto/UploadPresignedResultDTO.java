package com.xing.fileserver.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UploadPresignedResultDTO implements Serializable {
    // 文件id
    private String id;

    // 预签url
    private String url;

    // 文件名
    private String name;
}
