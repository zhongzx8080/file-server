package com.xing.fileserver.mapper;

import com.xing.fileserver.model.FileUpload;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface FileUploadMapper {
  int deleteByPrimaryKey(String id);

  int insert(FileUpload record);

  int insertSelective(FileUpload record);

  FileUpload selectByPrimaryKey(String id);

  int updateByPrimaryKeySelective(FileUpload record);

  int updateByPrimaryKey(FileUpload record);
}