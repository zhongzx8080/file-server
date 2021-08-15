package com.xing.fileserver.dto;

import com.xing.fileserver.common.constant.FileStatusEnum;
import com.xing.fileserver.common.model.PageDTO;
import lombok.Data;

@Data
public class UploadFilePageDTO extends PageDTO {

    // 模糊查询关键字
    private String keyword;

    private FileStatusEnum status;

    private String module;
}
