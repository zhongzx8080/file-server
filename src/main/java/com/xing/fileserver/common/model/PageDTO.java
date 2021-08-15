package com.xing.fileserver.common.model;

import lombok.Data;

import java.io.Serializable;

/*
 *
 * 分页查询
 * */
@Data
public class PageDTO implements Serializable {
    // 当前页
    private int page = 1;
    // 行数
    private int rows = 10;

    // 排序字段
    private String sort;

    // 顺序(默认升序)
    private boolean ascending = true;


}
