package com.system.personalaccount.model;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/7/7.
 * 收入
 */
public class Income implements Serializable {

    /** 收入ID **/
    private int incomeId;
    /** 记录ID **/
    private int recordId;
    /** 收入名称 **/
    private String name;
    /** 收入金钱 **/
    private double money;
    /** 收入类型 **/
    private int typeId;
    /** 收入日期 **/
    private String date;
    /** 收入备注 **/
    private String remark;

    public int getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(int incomeId) {
        this.incomeId = incomeId;
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
