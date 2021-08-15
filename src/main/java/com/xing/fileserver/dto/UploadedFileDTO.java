package com.xing.fileserver.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UploadedFileDTO implements Serializable {

    private String id;

    private String module;

    private String url;

    private String name;

    private long size;

    private String uploadTime;
}
