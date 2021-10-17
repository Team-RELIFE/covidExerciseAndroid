package com.example.exercise_android1.graph;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import androidx.annotation.Nullable;
import java.util.Date;

public class graphDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "graph_DP.db";
    public static final String TABLE_NAME = "DP_table";
    public static final String TABLE_NAME2 = "bmi_table";


    public static final String COL_1 = "ID";
    public static final String COL_2 = "DATE";
    public static final String COL_3 = "WEIGHT";

    public graphDBHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, DATE DATE, WEIGHT FLOAT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(SQLiteDatabase db, long date, float f){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, date);
        contentValues.put(COL_3, f);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public void updateTable(SQLiteDatabase db, long date, float f){
        String day = Long.toString(date);
        ContentValues values = new ContentValues();
        values.put(COL_2, date);
        values.put(COL_3, f);
        db.update(TABLE_NAME, values, "DATE=?", new String[]{day});
    }

    public void updateData(SQLiteDatabase db, long date, float f) {

        Cursor temp = getAllDate();
        Cursor temp2;

        //DB가 비어있으면 바로 데이터 추가
        if(temp.getCount() == 0){
            insertData(db, date, f);
            return;
        }

        //DB에 입력받은 날짜와 동일한 데이터가 있는지 검색
        String s = Long.toString(date);
        String queryString = "SELECT * FROM DP_table WHERE DATE = " + s;

        temp2 = db.rawQuery(queryString, null);

        //동일한 데이터가 없다면 새로 추가
        if(temp2.getCount() == 0){
            ContentValues values = new ContentValues();
            values.put(COL_2, date);
            values.put(COL_3, f);
            db.insert(TABLE_NAME, null, values);
            return;
        }

        //동일한 데이터가 존재하면 수정
        updateTable(db, date, f);
        return;
    }

    public boolean bmi_insertData(SQLiteDatabase db, float height, float weight){
        ContentValues contentValues = new ContentValues();
        contentValues.put("HEIGHT", height);
        contentValues.put("WEIGHT", weight);
        long result = db.insert(TABLE_NAME2, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public void bmi_updateTable(SQLiteDatabase db, float height, float weight){
        ContentValues contentValues = new ContentValues();
        contentValues.put("HEIGHT", height);
        contentValues.put("WEIGHT", weight);
        db.update(TABLE_NAME2, contentValues, "ID=?", new String[]{"1"});
    }

    public void bmi_updateData(SQLiteDatabase db, float height, float weight) {

        Cursor temp = bmi_getDate();

        if(temp.getCount() == 0){
            bmi_insertData(db, height, weight);
        }
        else{
            bmi_updateTable(db, height, weight);
        }
        return;
    }

    public Cursor getAllDate(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor bmi_getDate(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME2, null);
        return res;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ? ", new String[]{id});
    }
}