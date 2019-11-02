package com.test.demo.lifenotebook1.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;


import com.test.demo.lifenotebook1.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class SystemUtils {

    /*
     * 将传入的时间（例如："2019-09-24"）转换为时间戳
     */
    public static long dateToLong(String time,SimpleDateFormat sdr) {
        Date date;
        long l = 0;
        try {
            date = sdr.parse(time);
            l = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }

    /*
     * 时间戳转换
     */
    public static String getSimpleDateTime(long createTime,SimpleDateFormat dateFormat) {
        if ((createTime+"").length()==10){
            createTime=createTime*1000;
        }
        try {
            long issueTime = new Date(createTime).getTime();//发布时毫秒
            String timeStamp = dateFormat.format(new Date(issueTime));//例 yyyy-MM-dd HH:mm:ss 格式
            return timeStamp;
        }catch (NumberFormatException e){
            Log.e("NumberFormatException:", "getSimpleDateTime: 传入的时间参数格式有误" );
        }
        return createTime+"";

    }

    /*
     * 根据当前日期获得是星期几
     */
    public static String getWeek(String time,SimpleDateFormat dateFormat) {
        String Week = "";
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(dateFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Week += "星期天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Week += "星期一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            Week += "星期二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            Week += "星期三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            Week += "星期四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Week += "星期五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            Week += "星期六";
        }
        return Week;
    }

    /*
     * 获取屏幕宽度
     */
    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) MyApplication.getInstance()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int screenWidth=outMetrics.widthPixels;
        return screenWidth;
    }

    /*
     * 获取屏幕高度
     */
    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) MyApplication.getInstance()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int screenHeight=outMetrics.heightPixels;
        return screenHeight;
    }

}
