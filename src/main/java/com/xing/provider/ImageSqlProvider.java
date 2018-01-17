package com.xing.provider;

import com.xing.entity.Image;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class ImageSqlProvider {

    public String addImage(@Param("image") final Image image) {
        return new SQL() {
            {
                INSERT_INTO("image");
                VALUES("id", "'" + image.getId() + "'");
                VALUES("filename", "'" + image.getFilename() + "'");
                VALUES("gmt_upload", "now()");
            }
        }.toString();
    }


    public String getImage(@Param("id") final String id) {
        return new SQL() {
            {
                SELECT("id", "filename", "gmt_upload");
                FROM("image");
                WHERE("id = '" + id + "'");
            }
        }.toString();
    }
}
