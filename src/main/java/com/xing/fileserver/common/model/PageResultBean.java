package com.xing.fileserver.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResultBean<T> implements Serializable {

    private long total;

    private List<T> data;

    public PageResultBean() {
    }

    public PageResultBean(long total, List<T> data) {
        this.total = total;
        this.data = data;
    }
}
