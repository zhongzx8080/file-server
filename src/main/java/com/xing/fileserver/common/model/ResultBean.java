package com.xing.fileserver.common.model;

import com.xing.fileserver.common.constant.ResultConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xing
 * @date 2019/5/29 23:19
 */
@Data
public class ResultBean<T> implements Serializable {
    private static final long serialVersionUID = 201905292319L;

    // 返回码
    private Integer code = 0;

    // 返回消息
    private String msg = "";

    // 返回结果
    private T data;

    public ResultBean(T data) {
        this.code = ResultConstant.CODE_SUCCESS;
        this.msg = ResultConstant.MSG_SUCCESS;
        this.data = data;
    }

    public ResultBean(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultBean(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultBean success() {
        return new ResultBean(ResultConstant.CODE_SUCCESS, ResultConstant.MSG_SUCCESS);
    }

    public static ResultBean success(Object data) {
        return new ResultBean(data);
    }

    public static ResultBean error() {
        return new ResultBean(ResultConstant.CODE_ERROR, ResultConstant.MSG_ERROR);
    }

    public static ResultBean error(String msg) {
        return new ResultBean(ResultConstant.CODE_ERROR, msg);
    }
}
