package com.xing.fileserver.model;

import java.io.Serializable;
import java.util.Objects;

public class FileUpload implements Serializable {
  private static final long serialVersionUID = 201905302207L;

  private String id;

  private String name;

  private String uid;

  private String businessId;

  private String module;

  private String path;

  private String url;

  private String ext;

  private Integer size;

  private String status;

  private String uploadTime;

  private String remark;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getBusinessId() {
    return businessId;
  }

  public void setBusinessId(String businessId) {
    this.businessId = businessId;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getExt() {
    return ext;
  }

  public void setExt(String ext) {
    this.ext = ext;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getUploadTime() {
    return uploadTime;
  }

  public void setUploadTime(String uploadTime) {
    this.uploadTime = uploadTime;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FileUpload that = (FileUpload) o;
    return id.equals(that.id) &&
            name.equals(that.name) &&
            uid.equals(that.uid) &&
            businessId.equals(that.businessId) &&
            module.equals(that.module) &&
            path.equals(that.path) &&
            url.equals(that.url) &&
            ext.equals(that.ext) &&
            size.equals(that.size) &&
            status.equals(that.status) &&
            uploadTime.equals(that.uploadTime) &&
            remark.equals(that.remark);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, uid, businessId, module, path, url, ext, size, status, uploadTime, remark);
  }

  @Override
  public String toString() {
    return "FileUpload{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", uid='" + uid + '\'' +
            ", businessId='" + businessId + '\'' +
            ", module='" + module + '\'' +
            ", path='" + path + '\'' +
            ", url='" + url + '\'' +
            ", ext='" + ext + '\'' +
            ", size=" + size +
            ", status='" + status + '\'' +
            ", uploadTime='" + uploadTime + '\'' +
            ", remark='" + remark + '\'' +
            '}';
  }
}