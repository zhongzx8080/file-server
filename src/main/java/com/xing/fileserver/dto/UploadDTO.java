package com.xing.fileserver.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UploadDTO implements Serializable {

    private String bizId;

    private String module = "default";
}
