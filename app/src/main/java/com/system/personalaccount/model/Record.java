package com.system.personalaccount.model;

/**
 * Created by mine on 2016/7/6.
 * 记录
 */
public class Record {

    /** 记录ID **/
    private int recordId;
    /** 记录名称 **/
    private String recordName;

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }
}
