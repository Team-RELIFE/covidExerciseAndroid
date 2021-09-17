package com.example.exercise_android1.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.exercise_android1.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class CalendarActivity extends AppCompatActivity {

    EditText titleText,contentText; /*제목, 내용 입력*/
    MaterialCalendarView calendarView; /*Material 캘린더뷰*/
    TextView alarmText; /*선택한 시간 정보를 텍스트로 입력받을 textView*/
    public static Context context;

    /*일정*/
    //일정 저장
    TextView monthText,dateText;
    ImageButton scheduleAddBtn, listOpen; //일정 저장 및 보기
    Button timeselectBtn; /*시간 선택*/
    CalendarDay datee;
    String title,content,time; /*일정_제목, 내용, 알람 시간*/
    String sYear,sMonth,sDay; /*특정 날짜의 db 테이블 생성*/
    String schedule="schedule";
    RadioGroup radioGroup1; /*알람 ON/OFF 설정, 수정용 라디오버튼 그룹*/
    //알람 설정
    public String setUpAlarmString; /*알람 요청코드_문자열*/
    public int requestCode1; /*알람 요청코드_정수*/
    String originHour,originMinute; /*알람 수정용(원래 시/분)*/
    String sHour,sMinute,alarm; /*알람 시간*/
    private AlarmManager alarmManager;
    //알람 노티피케이션
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;

    /*일정db*/
    DBHelper dbHelper; /*데이터베이스 테이블 생성/업뎃 클래스*/
    SQLiteDatabase db; /*SQLiteDatabase*/
    final static String dbName="calendar.db"; /*db이름*/
    final static int dbVersion=2; /*db 버전*/

    /*일정목록*/
    CalendarAdapter calendarAdapter;
    RecyclerView.LayoutManager manager;
    RecyclerView recyclerView;
    AlertDialog scheduleListDialog; //일정 목록 다이얼로그
    ItemTouchHelper itemTouchHelper;

    /*도트*/
    com.example.exercise_android1.Calendar.dotspanDBHelper dotspanDBHelper; /*테이블명 저장용 db 클래스*/
    SQLiteDatabase dotDB;
    final static String dotDBName="calendar_monthDay"; /*테이블명 저장용 db 이름*/
    public HashMap<String, EventDecorator> eventMap=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        context=this;
        titleText=(EditText)findViewById(R.id.calendarTitle);
        contentText=(EditText)findViewById(R.id.calendarContent);
        monthText=(TextView)findViewById(R.id.monthText1);
        dateText=(TextView)findViewById(R.id.dateText1);
        radioGroup1=(RadioGroup)findViewById(R.id.alarmGroup);
        scheduleAddBtn=(ImageButton)findViewById(R.id.scheduleSaveBtn);
        listOpen=(ImageButton)findViewById(R.id.listOpen);
        timeselectBtn=(Button)findViewById(R.id.timeselectBtn);
        calendarView=(MaterialCalendarView) findViewById(R.id.mCalendarView);
        alarmText=(TextView)findViewById(R.id.alarmText);

        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE); //NotificationManager를 운영체제로부터 호출
        notificationBuilder=null;
        alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE); //AlarmManager를 운영체제로부터 호출

        calendarView.setSelectedDate(CalendarDay.today());
        /*오늘 날짜를 월/일로 텍스트뷰에 출력*/
        int Year=CalendarDay.today().getYear();
        int Month=CalendarDay.today().getMonth()+1;
        int Day=CalendarDay.today().getDay();
        sYear=Integer.toString(Year);
        sMonth=Integer.toString(Month);
        sDay=Integer.toString(Day);
        monthText.setText(sMonth);
        dateText.setText(sDay);

        dotspanDBHelper=new dotspanDBHelper(CalendarActivity.this,dotDBName,null,dbVersion);
        dotDB=dotspanDBHelper.getWritableDatabase();
        dotspanDBHelper.onCreate(dotDB);
        dotDB=dotspanDBHelper.getReadableDatabase();
        Cursor cursor=dotDB.rawQuery("SELECT * FROM"+" "+"tablenameList",null);
        try {
            if (cursor!=null){
                if (cursor.moveToFirst()){
                    do {
                        String m=cursor.getString(cursor.getColumnIndex("Month"));
                        String d=cursor.getString(cursor.getColumnIndex("Day"));
                        Log.i("month",m);
                        Log.i("Day",d);
                        int iMonth=Integer.parseInt(m);
                        int iDay=Integer.parseInt(d);
                        EventDecorator ev=new EventDecorator(Color.BLUE,Collections.singleton(CalendarDay.from(Year,iMonth-1,iDay)));
                        calendarView.addDecorator(ev);
                        String evKey=sYear+m+d;
                        eventMap.put(evKey,ev);
                    }while (cursor.moveToNext());
                }
            }
        }catch (Exception e){
            Log.i("calendar events", "dot print error");
        }

        timeselectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c=Calendar.getInstance();
                int mhour=c.get(Calendar.HOUR);
                int mMinute=c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog=new TimePickerDialog(CalendarActivity.this, R.style.themeOnverlay_timePicker, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sHour=Integer.toString(hourOfDay);
                        sMinute=Integer.toString(minute);
                        alarmText.setText(hourOfDay+":"+minute);
                        alarm=sHour+sMinute;
                    }
                },mhour,mMinute,false);
                timePickerDialog.show();
            }
        });

        /*달력의 주말 텍스트 색상 변환*/
        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
        );

        /*달력에서 날짜 선택 시 해당 월/일을 각각의 텍스트뷰에 출력*/
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int month=date.getMonth()+1;
                int day=date.getDay();
                datee=date;
                sMonth=Integer.toString(month);
                sDay=Integer.toString(day);
                monthText.setText(sMonth);
                dateText.setText(sDay);
            }
        });

        scheduleAddBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                dbHelper=new DBHelper(CalendarActivity.this,dbName,null,dbVersion,sMonth,sDay);
                db=dbHelper.getWritableDatabase();
                dbHelper.onCreate(db); /*데이터베이스 내에 테이블 생성*/

                title=titleText.getText().toString().trim();
                content=contentText.getText().toString().trim();
                time=alarmText.getText().toString().trim();

                int iMonth=Integer.parseInt(sMonth);
                int iDay=Integer.parseInt(sDay);

                int id=radioGroup1.getCheckedRadioButtonId();

                if (title.isEmpty() || content.isEmpty()){
                    Toast.makeText(CalendarActivity.this,"제목 또는 내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!time.isEmpty()){ //알람 시간을 설정했을 경우
                        if (id==R.id.alarmOnCheck){ //체크박스 on 선택 시 제목,내용,알람시간 모두 db에 저장
                            setAlarm();
                            dbHelper.insertDBcontent(db,title,content,time, requestCode1); //수정
                            dotspanDBHelper.insertDBcontent(dotDB,sMonth,sDay);
                            EventDecorator ev=new EventDecorator(Color.BLUE,Collections.singleton(CalendarDay.from(Year,iMonth-1,iDay)));
                            calendarView.addDecorator(ev);
                            String evKey=sYear+sMonth+sDay;
                            eventMap.put(evKey,ev);
                            Toast.makeText(CalendarActivity.this,"일정 및 알람 저장",Toast.LENGTH_SHORT).show();
                        }
                        else{ //체크박스 off 선택 시
                            Toast.makeText(CalendarActivity.this,"On에 체크해주세요.",Toast.LENGTH_SHORT).show();
                        }
                    }else{ //알람 시간을 설정하지 않았을 경우
                        if (id==R.id.alarmOnCheck){
                            Toast.makeText(CalendarActivity.this,"알람 시간을 설정해주세요.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ContentValues values=new ContentValues();
                            values.put("Title",title);
                            values.put("Content",content);
                            db.insert(schedule+sMonth+sDay,null,values); /*db에 저장*/
                            dotspanDBHelper.insertDBcontent(dotDB,sMonth,sDay);
                            EventDecorator ev=new EventDecorator(Color.BLUE,Collections.singleton(CalendarDay.from(Year,iMonth-1,iDay)));
                            calendarView.addDecorator(ev);
                            String evKey=sYear+sMonth+sDay;
                            eventMap.put(evKey,ev);
                            Toast.makeText(CalendarActivity.this,"일정 및 알람 저장",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        listOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScheduleList();
            }
        });
    }

    public void showScheduleList(){
        AlertDialog.Builder builder=new AlertDialog.Builder(CalendarActivity.this);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.alert_dialog,null);
        builder.setPositiveButton("닫기",null);
        builder.setView(view);
        scheduleListDialog=builder.create();
        String tableName=schedule+sMonth+sDay;

        dbHelper=new DBHelper(CalendarActivity.this,dbName,null,dbVersion,sMonth,sDay);
        db=dbHelper.getReadableDatabase(); /*읽기 모드*/
        dbHelper.onCreate(db); //db 생성

        Cursor c=db.rawQuery("SELECT * FROM"+" "+tableName,null);

        try {
            if (c!=null){
                recyclerView=(RecyclerView)view.findViewById(R.id.listview_alterdialog_list); //추가
                manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(manager);
                calendarAdapter=new CalendarAdapter(context,sMonth,sDay,alarm); //db 내 테이블 삭제를 위해 테이블명도 전달!!
                if(c.moveToFirst()){ //핸드폰 내부 저장소로부터 해당 테이블 명으로 테이블 내의 데이터(제목, 내용, 알람시간) 끌어오기
                    do {
                        String title2=c.getString(c.getColumnIndex("Title"));
                        String content2=c.getString(c.getColumnIndex("Content"));
                        String alarm2 = c.getString(c.getColumnIndex("Alarm"));
                        calendarAdapter.addItem(new CalendarListData(title2,content2,alarm2)); //어댑터에 생성한 ListViewData4 객체 저장하고
                        recyclerView.setAdapter(calendarAdapter); //리사이클러뷰에 어댑터 연결
                    }while (c.moveToNext()); //삭제&수정 스와이프 기능
                    itemTouchHelper=new ItemTouchHelper(new swipeController(calendarAdapter)); //어댑터 객체를 전달해서 ItemTouchHelper객체 생성한 다음
                    itemTouchHelper.attachToRecyclerView(recyclerView); //ItemTouchHelper 객체를 리사이클러뷰에 부착
                }
            }
        }catch (Exception e){
            Log.w("TAG","no such table",e); //테이블이 존재하지 않으면 로그 메세지 출력
        }
        scheduleListDialog.setCancelable(false);
        scheduleListDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setAlarm(){
        Intent receiverIntent=new Intent(CalendarActivity.this, AlarmReceiver.class); //리시버로 전달될 인텐트 설정

        setUpAlarmString=sMonth+sDay+sHour+sMinute+"00"; /*추가, requestCode를 날짜+시간으로 수정*/
        requestCode1=Integer.parseInt(setUpAlarmString); //요청코드를 정수로 변환
        receiverIntent.putExtra("requestCode",requestCode1); //요청 코드를 리시버에 전달
        receiverIntent.putExtra("alarmTitle",title); //수정_일정 제목을 리시버에 전달

        PendingIntent pendingIntent=PendingIntent.getBroadcast(CalendarActivity.this,requestCode1,receiverIntent,PendingIntent.FLAG_UPDATE_CURRENT); /*getBroadcast(fromContext,customRequestcode,toIntent,flag)*/
        Log.w("InCalendarActivity_RC", String.valueOf(requestCode1));

        String from=sYear+"-"+sMonth+"-"+sDay+" "+sHour+":"+sMinute+":"+"00"; /*알람으로 설정한 날짜*/
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
        Date datetime=new Date();
        try {
            datetime=dateFormat.parse(from);
        }catch (ParseException e){
            e.printStackTrace();
        }
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(datetime);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void modifyCalendarDialog(String originTitle, String originContent, String originAlarm){

        AlertDialog.Builder builder=new AlertDialog.Builder(CalendarActivity.this);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.modifycalendar,null);
        builder.setPositiveButton("취소",null);
        builder.setView(view);

        dbHelper=new DBHelper(CalendarActivity.this,dbName,null,dbVersion,sMonth,sDay);
        db=dbHelper.getWritableDatabase();
        String tableName=schedule+sMonth+sDay;

        EditText edt1=(EditText)view.findViewById(R.id.modiEditTitle);
        EditText edt2=(EditText)view.findViewById(R.id.modiEditContent);
        //수정 시 원래의 제목과 내용을 에디트텍스트에 출력
        edt1.setText(originTitle);
        edt2.setText(originContent);

        TimePicker timePicker=(TimePicker)view.findViewById(R.id.timePicker);

        if (originAlarm!=null){ //기존의 알람 시간이 설정되어 있다면 기존 시간으로 타임피커 설정
            stringSplit(originAlarm);
            timePicker.setHour(Integer.parseInt(originHour));
            timePicker.setMinute(Integer.parseInt(originMinute));
        }else{ //설정되어 있지 않다면 현재 시간으로 타임피커 설정, if(originalAlarm==null) ->ok
            Calendar calendar=Calendar.getInstance();
            timePicker.setCurrentHour(calendar.get(Calendar.HOUR));
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        }

        builder.setNeutralButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //수정된 제목과 내용을 스트링 형태로 변환
                title=edt1.getText().toString();
                content=edt2.getText().toString();
                String time;
                RadioGroup radioGroup2=(RadioGroup)view.findViewById(R.id.modiAlarmGroup);
                int id=radioGroup2.getCheckedRadioButtonId();

                if (id==R.id.modiAlarmOnCheck){ //수정 시 알람 ON
                    if (originAlarm!=null){ //기존에 설정했던 알람이 있는 경우
                        stringSplit(originAlarm);
                        cancelAlarm(originHour,originMinute); //기존 알람 취소
                    }
                    int hour=timePicker.getHour(); //타임피커로부터 시간,분 얻어옴
                    int minute=timePicker.getMinute();
                    sHour=Integer.toString(hour);
                    sMinute=Integer.toString(minute);
                    time=sHour+":"+sMinute;
                    if (originAlarm==null){ //기존의 것 삭제하고 새로 생성
                        dbHelper.deleteDBcontent(db,originTitle);
                        dbHelper.insertDBcontent(db,title,content,time,requestCode1); // 0914 수정
                    }else{ //기존 일정 업데이트
                        dbHelper.updateDBcontent(db,title,originTitle,content,originContent,time,originAlarm);
                    }
                    setAlarm(); //새 알람 설정
                }else{ //수정 시 알람 OFF, 기존에 설정했던 알람은 취소
                    if (originAlarm!=null){
                        stringSplit(originAlarm);
                        cancelAlarm(originHour,originMinute); //기존 알람 취소
                    }
                    db.execSQL("UPDATE "+tableName+" SET "+"Title="+"'"+title+"'"+" WHERE Title="+"'"+originTitle+"'"+";");
                    db.execSQL("UPDATE "+tableName+" SET "+"Content="+"'"+content+"'"+" WHERE Content="+"'"+originContent+"'"+";");
                }
                scheduleListDialog.dismiss();
                Toast.makeText(CalendarActivity.this,"수정되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

        final AlertDialog alertDialog=builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    public void cancelAlarm(String originHour,String originMinute){
        String rqCodeInModi=sMonth+sDay+originHour+originMinute+"00"; //기존에 설정했던 알람을 취소하기 위해 요청 코드 재설정
        int requestCode=Integer.parseInt(rqCodeInModi);
        Intent intent=new Intent(context,AlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT); //0
        alarmManager.cancel(pendingIntent);
    }

    public void stringSplit(String s){
        String[] splitString=s.split(":");
        originHour=splitString[0];
        originMinute=splitString[1];
    }

}