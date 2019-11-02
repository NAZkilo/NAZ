package com.test.demo.lifenotebook1.base;

import android.os.Bundle;

import com.test.demo.lifenotebook1.utils.StatusBarUtils;




public class LucencyNoActionBarActivity extends NoActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸式状态栏
        StatusBarUtils.useTransSteepState(LucencyNoActionBarActivity.this);
    }


}
