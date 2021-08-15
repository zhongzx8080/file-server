package com.xing.fileserver.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
public class UploadFinishedDTO implements Serializable {

    @NotEmpty(message = "文件id不能为空")
    private List<String> fileIds;
}
