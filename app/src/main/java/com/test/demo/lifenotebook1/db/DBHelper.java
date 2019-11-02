package com.test.demo.lifenotebook1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.test.demo.lifenotebook1.utils.Constant;



public class DBHelper extends SQLiteOpenHelper{
    //数据库版本号
    private static final int DATABASE_VERSION=1;

    //数据库名称
    private static final String DATABASE_NAME="life.db";
    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        String CREATE_TABLE_STUDENT="CREATE TABLE "+ Constant.TABLE+"("
                + Constant.KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                +Constant.KEY_DATE+" TEXT, "
                +Constant.KEY_ARRAY_TEXT+" TEXT)";
        db.execSQL(CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果旧表存在则删除，因此原来数据将会清空
        db.execSQL("DROP TABLE IF EXISTS "+ Constant.TABLE);
        //再次创建表
        onCreate(db);
    }
}
