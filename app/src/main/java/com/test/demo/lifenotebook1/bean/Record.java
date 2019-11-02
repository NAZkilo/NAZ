package com.test.demo.lifenotebook1.bean;


import java.io.Serializable;


public class Record implements Serializable{

    //属性
    public String date;//日期
    public String array_text;//包含一天内收支事件的集合转化的json文本
    public int sn;//序号

    public Record() {

    }

    public Record(int sn,String date, String array_text) {
        this.date = date;
        this.array_text = array_text;
        this.sn = sn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArray_text() {
        return array_text;
    }

    public void setArray_text(String array_text) {
        this.array_text = array_text;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

}
