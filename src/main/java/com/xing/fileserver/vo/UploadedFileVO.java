package com.xing.fileserver.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xing.fileserver.common.serializer.FillUrlSerializer;
import lombok.Data;

import java.io.Serializable;

@Data
public class UploadedFileVO implements Serializable {

    private String id;

    private String module;

    @JsonSerialize(using = FillUrlSerializer.class)
    private String url;

    private String name;

    private long size;

    private String uploadTime;
}
