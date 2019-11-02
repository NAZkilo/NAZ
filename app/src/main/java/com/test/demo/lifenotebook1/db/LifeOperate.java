package com.test.demo.lifenotebook1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.test.demo.lifenotebook1.bean.Record;
import com.test.demo.lifenotebook1.utils.Constant;

import java.util.ArrayList;


public class LifeOperate {

    private DBHelper dbHelper;

    public LifeOperate(Context context){
        dbHelper=new DBHelper(context);
    }

    public int insert(Record record){
        //打开连接，写入数据
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constant.KEY_DATE,record.date);
        values.put(Constant.KEY_ARRAY_TEXT,record.array_text);

        //新增数据的 _id
        long event_id=db.insert(Constant.TABLE,null,values);
        db.close();
        return (int)event_id;
    }

    public void delete(int event_id){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete(Constant.TABLE,Constant.KEY_ID+"=?", new String[]{String.valueOf(event_id)});
        db.close();
    }
    public void update(Record record){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(Constant.KEY_DATE,record.date);
        values.put(Constant.KEY_ARRAY_TEXT,record.array_text);
        //t： 更新数据受影响的行数
        int t=db.update(Constant.TABLE,values,Constant.KEY_ID+"=?",new String[] { String.valueOf(record.sn) });

        db.close();
    }

    /*
     * 获取表中所有数据并以集合形式返回
     *  包含表中数据实体类的集合 Record
     */
    public ArrayList<Record> getDataList(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Constant.KEY_ID+","+
                Constant.KEY_DATE+","+
                Constant.KEY_ARRAY_TEXT+ " FROM "+Constant.TABLE;
        ArrayList<Record> studentList=new ArrayList<>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                int sn=cursor.getInt(cursor.getColumnIndex(Constant.KEY_ID));
                String date=cursor.getString(cursor.getColumnIndex(Constant.KEY_DATE));
                String array_text=cursor.getString(cursor.getColumnIndex(Constant.KEY_ARRAY_TEXT));
                studentList.add(new Record(sn,date,array_text));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return studentList;
    }

    /*
     * 根据日期查询
     * 如果数据库中不存在对应数据则返回的Record为null
     */
    public Record getRecordByDate(String date){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String selectQuery="SELECT "+
                Constant.KEY_DATE + ","+
                Constant.KEY_ID + ","+
                Constant.KEY_ARRAY_TEXT +
                " FROM " + Constant.TABLE
                + " WHERE " +
                Constant.KEY_DATE + "=?";
        Record record=null;
        Cursor cursor=db.rawQuery(selectQuery,new String[]{date});
        if(cursor.moveToFirst()){
            do{
                record=new Record();
                record.sn =cursor.getInt(cursor.getColumnIndex(Constant.KEY_ID));
                record.date =cursor.getString(cursor.getColumnIndex(Constant.KEY_DATE));
                record.array_text =cursor.getString(cursor.getColumnIndex(Constant.KEY_ARRAY_TEXT));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return record;
    }

    /*
     * 查询数据库中数据总条数
     */
    public long allCaseNum( ){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        String sql = "select count(*) from "+Constant.TABLE;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }
}
