package com.xing.fileserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.xing.fileserver.common.constant.FileStatusEnum;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("file_upload")
public class FileUpload implements Serializable {
    private static final long serialVersionUID = 201905302207L;

    private String id;

    private String name;

    private String uid;

    private String businessId;

    private String module;

    private String path;

    private String url;

    private String ext;

    private long size;

    private FileStatusEnum status;

    private String uploadTime;

    private String remark;
}