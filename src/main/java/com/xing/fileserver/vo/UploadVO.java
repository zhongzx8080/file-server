package com.xing.fileserver.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xing.fileserver.common.serializer.FillUrlSerializer;
import lombok.Data;

import java.io.Serializable;

@Data
public class UploadVO implements Serializable {

    private String id;

    @JsonSerialize(using = FillUrlSerializer.class)
    private String url;

    private String name;
}
