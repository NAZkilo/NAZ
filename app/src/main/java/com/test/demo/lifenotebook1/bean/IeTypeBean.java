package com.test.demo.lifenotebook1.bean;



public class IeTypeBean {
    private String type_name;
    private int ie_type;

    public IeTypeBean(String type_name, int ie_type) {
        this.type_name = type_name;
        this.ie_type = ie_type;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public int getIe_type() {
        return ie_type;
    }

    public void setIe_type(int ie_type) {
        this.ie_type = ie_type;
    }
}
