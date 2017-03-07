package com.system.personalaccount.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/7/7.
 * 支出对象
 */
public class Expend implements Serializable {

    /** 支出ID **/
    private int expendId;
    /** 记录ID **/
    private int recordId;
    /** 支出名称 **/
    private String name;
    /** 支出金钱 **/
    private double money;
    /** 支出类型 **/
    private int typeId;
    /** 支出日期 **/
    private String date;
    /** 支出备注 **/
    private String remark;

    public int getExpendId() {
        return expendId;
    }

    public void setExpendId(int expendId) {
        this.expendId = expendId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
