package com.xing.fileserver.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


import java.io.Serializable;
import java.util.List;

@Data
public class UploadBindDTO implements Serializable {
    @NotBlank(message = "业务id不能为空")
    private String bizId;

    @NotEmpty(message = "文件不能为空")
    private List<String> fileIds;
}
