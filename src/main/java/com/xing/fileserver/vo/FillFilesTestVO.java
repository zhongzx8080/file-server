package com.xing.fileserver.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xing.fileserver.common.annotation.FillFiles;
import com.xing.fileserver.common.serializer.FillFileSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FillFilesTestVO implements Serializable {

    private String bizId;

    @FillFiles("bizId")
    @JsonSerialize(nullsUsing = FillFileSerializer.class)
    private List<UploadedFileVO> files;
}
