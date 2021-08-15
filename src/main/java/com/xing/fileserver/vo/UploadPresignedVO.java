package com.xing.fileserver.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UploadPresignedVO implements Serializable {

    // 文件id
    private String id;

    // 预签url
    private String url;

    // 文件名
    private String name;
}
