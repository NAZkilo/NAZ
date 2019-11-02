package com.test.demo.lifenotebook1.utils;



import com.test.demo.lifenotebook1.R;
import com.test.demo.lifenotebook1.bean.IeTypeBean;
import com.test.demo.lifenotebook1.bean.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LifeUtils {
    /*
     * 根据ieTypeName获取ieType
     */
    public static int getIeType(String ieTypeName){
        int ieType=10000;
        List<IeTypeBean> typeList = getIeTypeList();
        int size=typeList.size();
        for (int i=0;i<size;i++){
            if (typeList.get(i).getType_name().equals(ieTypeName)){
                ieType=typeList.get(i).getIe_type();
            }
        }
        return ieType;
    }

    /*
     * 根据ieType获取ieTypeName
     */
    public static String getIeTypeName(int ie_type){
        String ieTypeName="";
        List<IeTypeBean> typeList = getIeTypeList();
        int size=typeList.size();
        for (int i=0;i<size;i++){
            if (typeList.get(i).getIe_type()==ie_type){
                ieTypeName=typeList.get(i).getType_name();
            }
        }
        return ieTypeName;
    }


    /*
     * 获取收支类型集合  （当前为写死固定的类型 用户不可自主添加）
     */
    public static List<IeTypeBean> getIeTypeList(){
        List<IeTypeBean> tList=new ArrayList<>();
        tList.add(new IeTypeBean("收入",0));
        tList.add(new IeTypeBean("饮食(支出)",-1));
        tList.add(new IeTypeBean("住宿(支出)",-2));
        tList.add(new IeTypeBean("乘车(支出)",-3));
        tList.add(new IeTypeBean("购物(支出)",-4));
        tList.add(new IeTypeBean("旅游(支出)",-5));
        tList.add(new IeTypeBean("教育(支出)",-6));

        return tList;
    }

    /*
     * 根据ie_type获取对应的本地icon资源resId
     */
    public static int getIeTypeImage(int ie_type){
        int resId;
        switch (ie_type){
            case 0:{
                //收入
                resId= R.drawable.ie_type_income;
            }break;
            case -1:{
                //饮食
                resId= R.drawable.ie_type_eat;
            }break;
            case -2:{
                //住宿
                resId= R.drawable.ie_type_accommodation;
            }break;
            case -3:{
                //乘车
                resId= R.drawable.ie_type_by_bus;
            }break;
            case -4:{
                //购物
                resId= R.drawable.ie_type_shopping;
            }break;
            case -5:{
                //旅游
                resId= R.drawable.ie_type_travel;
            }break;
            case -6:{
                //教育
                resId= R.drawable.ie_type_edu;
            }break;
            default:{
                //其他
                resId= R.drawable.ie_type_other;
            }
        }

        return resId;
    }

    /*
     * 将记账数据按照日期进行排序
     * 从大到小
     * 注意：大量数据时记得测试先将sortList倒序再去排序是否可以让算法计算更快
     */
    public static List<Record> quickSort(List<Record> sortList, boolean isReverse){
        //android自带的集合排序类
        Collections.sort(sortList, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                //比较日期的大小 大的在前
                long time1= SystemUtils.dateToLong(o1.getDate(),new SimpleDateFormat("yyyy年MM月dd日"));
                long time2= SystemUtils.dateToLong(o2.getDate(),new SimpleDateFormat("yyyy年MM月dd日"));
                if (time1>time2){
                    return -1;
                }else {
                    return 1;
                }
            }
        });
        if (isReverse){
            Collections.reverse(sortList);//逆序  从小到大
        }
        return sortList;
    }
}
