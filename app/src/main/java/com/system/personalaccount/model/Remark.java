package com.system.personalaccount.model;

import java.io.Serializable;

/**
 * Created by mine on 2016/7/6.
 * 备注
 */
public class Remark implements Serializable {

    /** 备注ID **/
    private int remarkId;
    /** 备注内容 **/
    private String content;
    /** 备注日期 **/
    private String date;
    /** 备注标题 **/
    private String title;

    public int getRemarkId() {
        return remarkId;
    }

    public void setRemarkId(int remarkId) {
        this.remarkId = remarkId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
