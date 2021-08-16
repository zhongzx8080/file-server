package com.xing.fileserver.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResultBean<T> implements Serializable {

    private long total;

    private List<T> items;

    public PageResultBean() {
    }

    public PageResultBean(long total, List<T> items) {
        this.total = total;
        this.items = items;
    }
}
