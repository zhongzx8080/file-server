package com.xing.fileserver.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author xing
 * @date 2019/5/29 23:19
 */

public class ResultBean implements Serializable {
  private static final long serialVersionUID = 201905292319L;

  // 返回码
  private Integer code;

  // 返回消息
  private String msg;

  // 返回结果
  private Object data;

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ResultBean that = (ResultBean) o;
    return code.equals(that.code) &&
            msg.equals(that.msg) &&
            data.equals(that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, msg, data);
  }

  @Override
  public String toString() {
    return "ResultBean{" +
            "code=" + code +
            ", msg='" + msg + '\'' +
            ", data=" + data +
            '}';
  }

  private ResultBean(Builder builder) {
    this.code = builder.code;
    this.msg = builder.msg;
    this.data = builder.data;
  }

  public static class Builder {
    private Integer code;

    private String msg;

    private Object data;

    public Builder() {
      this.code = ResultConstant.CODE_SUCCESS;
      this.msg = ResultConstant.MSG_SUCCESS;
    }

    public Builder code(Integer val) {
      this.code = val;
      return this;
    }

    public Builder msg(String val) {
      this.msg = val;
      return this;
    }

    public Builder data(Object val) {
      this.data = val;
      return this;
    }

    public ResultBean build() {
      return new ResultBean(this);
    }
  }


}
