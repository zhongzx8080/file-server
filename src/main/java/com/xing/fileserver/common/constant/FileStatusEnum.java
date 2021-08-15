package com.xing.fileserver.common.constant;

public enum FileStatusEnum {

    DELETED(-1, "已删除"),
    PRESIGNED(0, "预处理签名"),
    UPLOADED(1, "已上传完");

    private int code;

    private String name;

    FileStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
