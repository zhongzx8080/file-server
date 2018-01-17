package com.xing.entity;

import java.util.Date;

public class Image {
    private String id;

    private String filename;

    private Date gmtUpload;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getGmtUpload() {
        return gmtUpload;
    }

    public void setGmtUpload(Date gmtUpload) {
        this.gmtUpload = gmtUpload;
    }
}
