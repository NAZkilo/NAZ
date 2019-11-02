package com.test.demo.lifenotebook1;

import android.app.Application;



public class MyApplication extends Application {
    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }


    public static Application getInstance(){
        return instance;
    }

}
