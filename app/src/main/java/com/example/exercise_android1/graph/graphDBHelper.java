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

    public boolean insertData(long date, float f){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, date);
        contentValues.put(COL_3, f);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    /** DB 업데이트 메소드(임시) **/
    public void updateData(SQLiteDatabase db, long date, float f) {

        int i = 0;

        //db테이블을 검색할 커서 선언
        Cursor temp = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        //db에 데이터가 없다면 입력받은 값을 db에 추가
        if(temp == null){
            insertData(date, f);
            return;
        }

        //db에 데이터가 있다면 입력받은 날짜 데이터가 존재하는지 검색
        if (temp != null) {
            if (temp.moveToFirst()) {
                do {
                    long search_day = temp.getLong(1);
                    //테이블을 1행씩 순차적으로 검색하여 입력받은 날짜와 동일한 데이터가 있는지 검색하고
                    //동일한 데이터가 존재한다면 해당 행의 몸무게 값을 새로 받은 값으로 업데이트
                    if (search_day == date) {
                        ContentValues values = new ContentValues();
                        values.put(COL_2, date);
                        values.put(COL_3, f);
                        db.insert(TABLE_NAME, null, values);
                        return;
                    } else {
                        i++;
                    }
                } while (temp.moveToNext());
                //동일한 날짜 데이터가 존재하지 않는다면 입력받은 데이터를 db에 새로 추가
                if(i == temp.getCount()) {
                    ContentValues values = new ContentValues();
                    values.put(COL_2, date);
                    values.put(COL_3, f);
                    db.insert(TABLE_NAME, null, values);
                }
            }
        }
    }

    public Cursor getAllDate(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Integer deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ? ", new String[]{id});
    }
}