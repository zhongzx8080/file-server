package com.xing.mapper;

import com.xing.entity.Image;
import com.xing.provider.ImageSqlProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface ImageMapper {
    @InsertProvider(type = ImageSqlProvider.class, method = "addImage")
//    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "id", keyProperty = "id", before = false, resultType = int.class)
    void addImage(@Param("image") Image image);

    @SelectProvider(type = ImageSqlProvider.class, method = "getImage")
    @Results(id = "baseColumn", value = {
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "filename", property = "filename"),
            @Result(column = "gmt_upload", property = "gmtUpload", jdbcType = JdbcType.TIMESTAMP)
    })
    Image getImage(@Param("id") String id);
}
