package com.xing.fileserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xing.fileserver.model.FileUpload;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileUploadMapper extends BaseMapper<FileUpload> {
}