package com.example.exercise_android1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.exercise_android1.Calendar.AlarmReceiver;
import com.example.exercise_android1.Calendar.CalendarActivity;
import com.example.exercise_android1.Calendar.DBHelper;
import com.example.exercise_android1.Calendar.swipeController;
import com.example.exercise_android1.TodaySchedules.PassedScheduleAdapter;
import com.example.exercise_android1.TodaySchedules.TodayScheduleAdapter;
import com.example.exercise_android1.TodaySchedules.TodayScheduleItems;
import com.example.exercise_android1.TodaySchedules.deleteController;
import com.example.exercise_android1.board.GetPostsActivity;
import com.google.android.material.navigation.NavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity2 extends AppCompatActivity {

    ImageButton menuIcon; /*메뉴버튼*/
    Toolbar toolbar; /*툴바*/
    DrawerLayout drawerLayout; /*드로어레이아웃*/
    NavigationView navigationView; /*내비게이션뷰*/

    public static Context nContext;

    TextView notice_title, no_text; /*최근 업데이트된 게시판글 제목*/
    public ConstraintLayout constraintLayout;
    private long time=0;

    /*오늘 일정*/
    ImageButton schedule_addBtn; //오늘 일정 추가 버튼
    ItemTouchHelper itemTouchHelper;
    ItemTouchHelper itemTouchHelper2;
    RecyclerView todayschedule_rcView;
    TodayScheduleAdapter todayScheduleAdapter;
    ArrayList<TodayScheduleItems> arrayList=new ArrayList<>();
        /*일정의 제목과 알람 시간을 가져오기 위한 DB관련 클래스 & 변수*/
        DBHelper dbHelper;
        SQLiteDatabase db;
        final static String dbName="calendar.db";
        final static int dbVersion=2;
        int Year=CalendarDay.today().getYear();
        int Month= CalendarDay.today().getMonth()+1;
        int Day=CalendarDay.today().getDay();
        String sYear=Integer.toString(Year);
        String sMonth=Integer.toString(Month);
        String sDay=Integer.toString(Day);
        String tableName="schedule"+sMonth+sDay;

   //알람 수정
   String originHour;
   String originMinute;
   AlarmManager alarmManager;
   int mRequestCode;

   //공지사항
    ViewPager2 notice_list;
    NoticeTitleAdapter noticeTitleAdapter;
    Handler slideHandler;
    int currentNum=0;
    ArrayList<NoticeTitleItem> notice_array;

   //시간 지난 일정 지우기
    long now = System.currentTimeMillis(); //현재시간 가져옴
    Date dateNow = new Date(now); // Date 형식으로 convert
    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat = new SimpleDateFormat("k:mm"); // 시간을 원하는 포맷으로 변경
    String currentTime = dateFormat.format(dateNow); // 원하는 포맷의 현재시간을 문자열로 포맷팅

    //지나간 일정 확인
    Button passedSchedulesBtn;
    PassedScheduleAdapter passedScheduleAdapter;
    RecyclerView passedSchedulesRcView;
    AlertDialog passedDialog;
    Date date1;

    //이벤트 버튼
    ImageButton evBtn1, evBtn2, evBtn3, evBtn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        try {
            date1=dateFormat.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        currentTime=currentTime.replace(":","");
        System.out.println(currentTime);
        menuIcon = (ImageButton) findViewById(R.id.menuIcon);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navi_view);
        notice_title = (TextView) findViewById(R.id.notice_title);
        no_text = (TextView) findViewById(R.id.schedulesOfToday_Text2);
        nContext = this;
        constraintLayout = (ConstraintLayout)findViewById(R.id.null_scheduleView);
        schedule_addBtn = (ImageButton)findViewById(R.id.scheduleOfToday_addBtn);
        alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE); //AlarmManager를 운영체제로부터 호출

        schedule_addBtn.setColorFilter(Color.parseColor("#7FCCEF"));
        passedSchedulesBtn=(Button)findViewById(R.id.passed_schedules);

        notice_list=(ViewPager2)findViewById(R.id.nt_viewpager);
        slideHandler=new Handler(Looper.getMainLooper());

        dbHelper=new DBHelper(nContext,dbName,null,dbVersion,sMonth,sDay);
        db=dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);

        evBtn1 = (ImageButton)findViewById(R.id.eventImg_1);
        evBtn2 = (ImageButton)findViewById(R.id.eventImg_2);
        evBtn3 = (ImageButton)findViewById(R.id.eventImg_3);
        evBtn4 = (ImageButton)findViewById(R.id.eventImg_5);

        /*액션바 대신 툴바 사용*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); /*앱 타이틀 안보이게*/
        //getActionBar사용시 오류//

        //drawer header :: 로그인한 상태라면 유저 이름을 표시, 아니라면(게스트 로그인) "Guest"로 표시
        View drawerHeader = navigationView.getHeaderView(0);
        TextView header_userName = (TextView) drawerHeader.findViewById(R.id.nameTV);
        User currentUser = new User().getCurrentUser();

        if (currentUser.id != null) {
            header_userName.setText(currentUser.name + " 님");
        } else {
            header_userName.setText("Guest 님");
        }

/* 2 */
//        Intent intent = new Intent(nContext, GetPostsActivity2.class);
//        startActivityForResult(intent, 1000);

//        try {
//            GetPostsActivity2 getPostsActivity2 = new GetPostsActivity2();
//            ((GetPostsActivity2)GetPostsActivity2.context).connectDB(); // -> null pointer error
//            // MainActivity2에서 직접 connectDB 구현
//            Intent intent = getIntent();
//            String titles = intent.getStringExtra("titles");
//            ArrayList<NoticeTitleItem> titleItems = new ArrayList<NoticeTitleItem>();
//        }catch (NullPointerException e){
//            Log.i("MainActivity2", "error");
//        }
        //System.out.println(titles);

//        JSONArray jArr = null;
//        try {
//            jArr = new JSONArray(titles);
//            JSONObject json = new JSONObject();
//
//            for (int i=0; i<jArr.length();i++) {
//                json = jArr.getJSONObject(i);
//
//                String title = json.getString("title");
//
//                titleItems.add(new NoticeTitleItem(title));
//
//                System.out.println(title);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        noticeTitleAdapter=new NoticeTitleAdapter(nContext);
        noticeTitleAdapter.addItem(new NoticeTitleItem("9월 업데이트"));
        noticeTitleAdapter.addItem(new NoticeTitleItem("이번달 이벤트"));
        notice_list.setAdapter(noticeTitleAdapter);
        notice_list.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        notice_list.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(slideRunnable);
                slideHandler.postDelayed(slideRunnable, 3000);
            }
        });


        View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.menuIcon:
                        drawerLayout.openDrawer(GravityCompat.START);
                        break;
                    case R.id.scheduleOfToday_addBtn:
                        Intent intent = new Intent(MainActivity2.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.passed_schedules:
                        showPassedSchedules();
                        break;
                    case R.id.eventImg_1:
                        Intent evIntent1 = new Intent(Intent.ACTION_VIEW);
                        evIntent1.setData(Uri.parse("https://dshop.dietshin.com/event/monthly_202110.asp"));
                        startActivity(evIntent1);
                        break;
                    case R.id.eventImg_2:
                        Intent evIntent2 = new Intent(Intent.ACTION_VIEW);
                        evIntent2.setData(Uri.parse("https://dshop.dietshin.com/event/appreview.asp"));
                        startActivity(evIntent2);
                        break;
                    case R.id.eventImg_3:
                        Intent evIntent3 = new Intent(Intent.ACTION_VIEW);
                        evIntent3.setData(Uri.parse("https://dshop.dietshin.com/event/benefit.asp"));
                        startActivity(evIntent3);
                        break;
                    case R.id.eventImg_5:
                        Intent evIntent5 = new Intent(Intent.ACTION_VIEW);
                        evIntent5.setData(Uri.parse("https://dshop.dietshin.com/event/coupon_zone.asp"));
                        startActivity(evIntent5);
                        break;
                }
            }
        };
        menuIcon.setOnClickListener(onClickListener);
        schedule_addBtn.setOnClickListener(onClickListener);
        passedSchedulesBtn.setOnClickListener(onClickListener);
        evBtn1.setOnClickListener(onClickListener);
        evBtn2.setOnClickListener(onClickListener);
        evBtn3.setOnClickListener(onClickListener);
        evBtn4.setOnClickListener(onClickListener);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NaviMenu naviMenu = new NaviMenu(nContext, item, currentUser);
                naviMenu.selectMenu();
                return false;
            }
        });

        showTodaySchedule();
    }

    /*뒤로가기 버튼 두 번 누르면 앱 종료*/
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        /*currentTimeMills : 현재 시간 불러옴, 현재시간-사용시간 = 실행시간*/
        if (System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"한번더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        else if (System.currentTimeMillis()-time<2000){
            finishAffinity(); /*어느 액티비티에서든 모든 부모 액티비티 종료*/
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1000){
//            if (resultCode == RESULT_OK){
//                String title = data.getStringExtra("titles");
//                System.out.print(title);
//            }else{
//
//            }
//        }
//    }

    public void showTodaySchedule(){
        Cursor c=db.rawQuery("SELECT * FROM"+" "+tableName,null);
        Log.i("MainActivity2", tableName);
        try {
            if (c!=null){
                todayschedule_rcView=(RecyclerView)findViewById(R.id.schedulesOfToday_recyclerView);
                todayschedule_rcView.setLayoutManager(new LinearLayoutManager(nContext));
                todayScheduleAdapter=new TodayScheduleAdapter(nContext);
                if (c.moveToFirst()){
                    do {
                        constraintLayout.setVisibility(View.GONE);
                        String title=c.getString(c.getColumnIndex("Title"));
                        String alarm=c.getString(c.getColumnIndex("Alarm"));
                        if (alarm != null){ // 알람이 null이 아닌 경우
                            if (date1.after(dateFormat.parse(alarm))){ // 알람시간과 현재 시간 비교, 일정의 알람시간이 이미 지난 경우 출력X
                            }
                            else{ // 일정의 알람시간이 지나지 않은 경우 리스트에 띄움
                                todayScheduleAdapter.addItem(new TodayScheduleItems(title,alarm));
                                todayschedule_rcView.setAdapter(todayScheduleAdapter);
                            }
                        }
                        else{ // 알람이 null인 경우에도 리스트에 그냥 띄움
                            todayScheduleAdapter.addItem(new TodayScheduleItems(title,alarm));
                            todayschedule_rcView.setAdapter(todayScheduleAdapter);
                        }
                    }while (c.moveToNext());
                    itemTouchHelper=new ItemTouchHelper(new swipeController(todayScheduleAdapter));
                    itemTouchHelper.attachToRecyclerView(todayschedule_rcView);
                }
            }
        }catch (Exception e){
            Log.w("MainActivity2","no such table",e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void modifyCalendarDialog(String originTitle, String originContent, String originAlarm,int requestCode){

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity2.this);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.modifycalendar,null);
        builder.setPositiveButton("취소",null);
        builder.setView(view);

        db=dbHelper.getWritableDatabase();
        db=dbHelper.getReadableDatabase();
        String tableName="schedule"+sMonth+sDay;

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
                String title=edt1.getText().toString();
                String content=edt2.getText().toString();
                String time;
                RadioGroup radioGroup2=(RadioGroup)view.findViewById(R.id.modiAlarmGroup);
                int id=radioGroup2.getCheckedRadioButtonId();

                if (id==R.id.modiAlarmOnCheck){ //수정 시 알람 ON
                    if (originAlarm!=null){ //기존에 설정했던 알람이 있는 경우
                        cancelAlarm(requestCode);
                    }
                    int hour=timePicker.getHour(); //타임피커로부터 시간,분 얻어옴
                    int minute=timePicker.getMinute();
                    String sHour=Integer.toString(hour);
                    String sMinute=Integer.toString(minute);
                    time=sHour+":"+sMinute;
                    if (originAlarm==null){ //기존의 것 삭제하고 새로 생성
                        String rqCodeInModi=sMonth+sDay+sHour+sMinute+"00";
                        int mRequestCode=Integer.parseInt(rqCodeInModi);
                        dbHelper.deleteDBcontent(db,originTitle);
                        dbHelper.insertDBcontent(db,title,content,time,mRequestCode); // 0914 수정
                    }else{ //기존 일정 업데이트 -> 여기서 오류?
                        String rqCodeInModi=sMonth+sDay+sHour+sMinute+"00";
                        mRequestCode=Integer.parseInt(rqCodeInModi);
                        String sRequestCode=String.valueOf(requestCode);
                        dbHelper.modifyDBcontent(db,title,originTitle,content,originContent,time,originAlarm, rqCodeInModi, sRequestCode);
                    }
                    setAlarm(title, mRequestCode, sHour, sMinute); //새 알람 설정
                }else{ //수정 시 알람 OFF, 기존에 설정했던 알람은 취소
                    if (originAlarm!=null){
                        //stringSplit(originAlarm);
                        cancelAlarm(requestCode); //기존 알람 취소
                    }
                    db.execSQL("UPDATE "+tableName+" SET "+"Title="+"'"+title+"'"+" WHERE Title="+"'"+originTitle+"'"+";");
                    db.execSQL("UPDATE "+tableName+" SET "+"Content="+"'"+content+"'"+" WHERE Content="+"'"+originContent+"'"+";");
                }
                Toast.makeText(MainActivity2.this,"수정되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent=getIntent();
                startActivity(intent);
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void cancelAlarm(int requestCode){
        Intent intent=new Intent(nContext, AlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(nContext,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT); //0
        alarmManager.cancel(pendingIntent);
        Log.w("alarm is canceled", String.valueOf(requestCode));
    }

    public void stringSplit(String s){
        String[] splitString=s.split(":");
        originHour=splitString[0];
        originMinute=splitString[1];
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setAlarm(String title, int requestCode, String sHour, String sMinute){
        Intent receiverIntent=new Intent(MainActivity2.this, AlarmReceiver.class); //리시버로 전달될 인텐트 설정

        receiverIntent.putExtra("requestCode",requestCode); //요청 코드를 리시버에 전달
        receiverIntent.putExtra("alarmTitle",title); //수정_일정 제목을 리시버에 전달

        PendingIntent pendingIntent=PendingIntent.getBroadcast(MainActivity2.this,requestCode,receiverIntent,PendingIntent.FLAG_UPDATE_CURRENT); /*getBroadcast(fromContext,customRequestcode,toIntent,flag)*/
        Log.w("InCalendarActivity_RC", String.valueOf(requestCode));

        String from=sYear+"-"+sMonth+"-"+sDay+" "+sHour+":"+sMinute+":"+"00"; /*알람으로 설정한 날짜*/
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
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

    public void showPassedSchedules(){ //지나간 일정 보기
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity2.this);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.passedschedules_dialog,null);
        builder.setPositiveButton("취소",null);
        builder.setView(view);
        passedDialog=builder.create();

        Cursor c=db.rawQuery("SELECT * FROM"+" "+tableName,null);
        System.out.println(tableName);
        try {
            if (c!=null){
                passedSchedulesRcView=(RecyclerView)view.findViewById(R.id.passed_scehedulesRecyclerView);
                passedSchedulesRcView.setLayoutManager(new LinearLayoutManager(nContext));
                passedScheduleAdapter=new PassedScheduleAdapter(nContext);
                if (c.moveToFirst()){
                    do {
                        String title=c.getString(c.getColumnIndex("Title"));
                        String alarm=c.getString(c.getColumnIndex("Alarm"));
                        if (alarm != null){ // 알람이 null이 아닌 경우
                            if (date1.after(dateFormat.parse(alarm))){ // 알람시간, 현재 시간을 정수로 변환 후 비교, 일정의 알람시간이 이미 지난 경우
                                passedScheduleAdapter.addItem(new TodayScheduleItems(title,alarm));
                                passedSchedulesRcView.setAdapter(passedScheduleAdapter);
                            }
                            else{ // 일정의 알람시간이 지나지 않은 경우 리스트에 띄움
                            }
                        }
                    }while (c.moveToNext());
                    itemTouchHelper2=new ItemTouchHelper(new deleteController(passedScheduleAdapter));
                    itemTouchHelper2.attachToRecyclerView(passedSchedulesRcView);
                }
            }
        }catch (Exception e){
            Log.w("MainActivity2","no such table",e);
        }
        passedDialog.setCancelable(false);
        passedDialog.show();
    }

    public final Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentNum==1)
                currentNum=-1;
            notice_list.setCurrentItem(++currentNum,true);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        drawerLayout.closeDrawer(Gravity.LEFT); // 내비게이션 드로어 닫기
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent=getIntent();
        startActivity(intent);
    }
}
