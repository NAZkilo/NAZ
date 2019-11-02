package com.test.demo.lifenotebook1.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;



public class NoActionBarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ActionBar actionBar = getSupportActionBar();
            if (null!=actionBar){
                getSupportActionBar().hide();//隐藏ActionBar
            }
        }catch (Exception e){

        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
    }
}
