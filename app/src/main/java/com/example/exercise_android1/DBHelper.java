package com.example.exercise_android1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TITLE="Title";
    public static final String CONTENT="Content";
    public static final String ALARM="Alarm";
    public static String month;
    public static String day;

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, String month, String day) {
        super(context, name, factory, version);
        this.month=month;
        this.day=day;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableName=month+day;
        String schedule="schedule";
        /*테이블 이름 설정 방법*/
        /*데이터 삽입*/
        String sql="CREATE TABLE if not exists"+" "+"schedule"+tableName+"("+TITLE+" TEXT,"+CONTENT+" TEXT,"+ALARM+" TEXT);";
        db.execSQL(sql); /*sql문 실행*/
    }

    /*데이터베이스 업그레이드*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="DROP TABLE if exists calendarContents";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insertDBcontent(SQLiteDatabase db, String title,String content,String alarm){ //데이터 삽입
        String tableName="schedule"+month+day;
        ContentValues values=new ContentValues();
        values.put("Title",title);
        values.put("Content",content);
        values.put("Alarm",alarm);
        db.insert(tableName,null,values);
        Log.w("TAG","Table row insert execute");
    }

    public void deleteDBcontent(SQLiteDatabase db, String i){ //동일한 제목의 일정이 있을 때, 하나의 일정을 삭제하면 동일한 제목을 가진 일정이 모두 다 삭제되는 오류 발생
        String tableName="schedule"+month+day;
        String sql="DELETE FROM " + "schedule" + month + day + " WHERE Title=" + "'" + i + "'" + ";";
        db.execSQL(sql);
        Log.i("TAG","Table row delete execute in calendar3.db");
    }

    public void updateDBcontent(SQLiteDatabase db,String title,String originTitle,String content,String originContent,String alarm,String originAlarm){ //데이터 변경
        String tableName="schedule"+month+day;
        db.execSQL("UPDATE "+tableName+" SET "+"Title="+"'"+title+"'"+" WHERE Title="+"'"+originTitle+"'"+";");
        db.execSQL("UPDATE "+tableName+" SET "+"Content="+"'"+content+"'"+" WHERE Content="+"'"+originContent+"'"+";");
        db.execSQL("UPDATE "+tableName+" SET "+"Alarm="+"'"+alarm+"'"+" WHERE Alarm="+"'"+originAlarm+"'"+";");
    }
}
