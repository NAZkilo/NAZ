package com.test.demo.lifenotebook1.bean;

import java.io.Serializable;


public class Event implements Serializable{

    //属性
    public int ie_type;//收支类型  = 0 收入  < 0 支出
    public String date;//日期
    public String remark;//备注
    public String amonut;//金额

    public Event() {

    }

    public Event(int ie_type, String date, String remark, String amonut) {
        this.ie_type = ie_type;
        this.date = date;
        this.remark = remark;
        this.amonut = amonut;
    }

    public int getIe_type() {
        return ie_type;
    }

    public void setIe_type(int ie_type) {
        this.ie_type = ie_type;
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

    public String getAmonut() {
        return amonut;
    }

    public void setAmonut(String amonut) {
        this.amonut = amonut;
    }

}
