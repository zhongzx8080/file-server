package com.xing.fileserver.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UploadResultDTO implements Serializable {
    private String id;

    private String url;

    private String name;

    private long size;

}
