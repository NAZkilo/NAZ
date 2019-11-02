package com.test.demo.lifenotebook1.utils;


public class Constant {
    //表名
    public static final String TABLE="LifeNotebook";

    //表的各字段
    public static final String KEY_ID="id";
    public static final String KEY_DATE="date";//日期
    public static final String KEY_ARRAY_TEXT="array_text";//收支事件集合json（集合内含有一天的收支时间）

    public static final int REQUEST_CODE_ADD_RECORD = 10001;//添加收支记录请求码
    public static final int RESULT_CODE_ADD_RECORD_NEW_ADD = 10002;//添加收支记录 新增记录返回码
    public static final int RESULT_CODE_ADD_RECORD_UPDATE = 10003;//添加收支记录  更新记录返回码
    public static final String RECORD_NEW_ADD = "record_new_add";
    public static final String RECORD_UPDATE = "record_update";
    public static final String RECORD_PASS = "record_pass";
    public static final String EVENT_PASS = "event_pass" ;
    public static final String EVENT_INDEX = "event_index";

    public static final String INTENT_PASS = "intent_pass";
    public static final int ADD_RECORD=10004;//添加
    public static final int EDITOR_RECORD=10005;//编辑


}
