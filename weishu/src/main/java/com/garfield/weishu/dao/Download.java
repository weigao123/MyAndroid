package com.garfield.weishu.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by gaowei on 2017/6/12.
 */
@Entity
public class Download {

    @Id
    private String downloadId;
    private Long toolSize;
    @Generated(hash = 587032506)
    public Download(String downloadId, Long toolSize) {
        this.downloadId = downloadId;
        this.toolSize = toolSize;
    }
    @Generated(hash = 1462805409)
    public Download() {
    }
    public String getDownloadId() {
        return this.downloadId;
    }
    public void setDownloadId(String downloadId) {
        this.downloadId = downloadId;
    }
    public Long getToolSize() {
        return this.toolSize;
    }
    public void setToolSize(Long toolSize) {
        this.toolSize = toolSize;
    }


}
