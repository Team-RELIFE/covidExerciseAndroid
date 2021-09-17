package com.example.exercise_android1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exercise_android1.Calendar.CalendarActivity;
import com.example.exercise_android1.Calendar.CalendarAdapter;
import com.example.exercise_android1.Calendar.CalendarListData;
import com.example.exercise_android1.Calendar.DBHelper;
import com.example.exercise_android1.Calendar.swipeController;
import com.example.exercise_android1.trainer_list.TrainerList;
import com.google.android.material.navigation.NavigationView;
import com.nhn.android.naverlogin.OAuthLogin;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

import butterknife.OnClick;


public class MainActivity2 extends AppCompatActivity {

    ImageButton menuIcon; /*메뉴버튼*/
    Toolbar toolbar; /*툴바*/
    DrawerLayout drawerLayout; /*드로어레이아웃*/
    NavigationView navigationView; /*내비게이션뷰*/
    OAuthLogin mOAuthLogin;
    Context nContext;
    Button ptListBtn;
    TextView notice_title, no_text; /*최근 업데이트된 게시판글 제목*/
    private long time=0;

    /*오늘 일정*/
    ItemTouchHelper itemTouchHelper;
    RecyclerView todayschedule_rcView;
    TodayScheduleAdapter todayScheduleAdapter;
    ArrayList<TodayScheduleItems> arrayList=new ArrayList<>();
        /*일정의 제목과 알람 시간을 가져오기 위한 DB관련 클래스 & 변수*/
        DBHelper dbHelper;
        SQLiteDatabase db;
        final static String dbName="calendar.db";
        final static int dbVersion=2;
        int Month= CalendarDay.today().getMonth()+1;
        int Day=CalendarDay.today().getDay();
        String sMonth=Integer.toString(Month);
        String sDay=Integer.toString(Day);
        String tableName="schedule"+sMonth+sDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        menuIcon = (ImageButton) findViewById(R.id.menuIcon);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navi_view);
        notice_title = (TextView) findViewById(R.id.notice_title);
        no_text = (TextView) findViewById(R.id.schedulesOfToday_Text2);
        nContext = this;

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

        /*메뉴버튼 눌렀을 때 내비게이션 드로어 오픈*/
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

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

    public void showTodaySchedule(){
        dbHelper=new DBHelper(nContext,dbName,null,dbVersion,sMonth,sDay);
        db=dbHelper.getReadableDatabase();
        dbHelper.onCreate(db);
        Cursor c=db.rawQuery("SELECT * FROM"+" "+tableName,null);
        Log.i("MainActivity2", tableName);
        try {
            if (c!=null){
                todayschedule_rcView=(RecyclerView)findViewById(R.id.schedulesOfToday_recyclerView);
                todayschedule_rcView.setLayoutManager(new LinearLayoutManager(nContext));
                todayScheduleAdapter=new TodayScheduleAdapter(nContext);
                if (c.moveToFirst()){
                    do {
                        String title=c.getString(c.getColumnIndex("Title"));
                        String alarm=c.getString(c.getColumnIndex("Alarm"));
                        todayScheduleAdapter.addItem(new TodayScheduleItems(title,alarm));
                        todayschedule_rcView.setAdapter(todayScheduleAdapter);
                    }while (c.moveToNext());
                    itemTouchHelper=new ItemTouchHelper(new swipeController(todayScheduleAdapter));
                    itemTouchHelper.attachToRecyclerView(todayschedule_rcView);
                }
            }
        }catch (Exception e){
            Log.w("MainActivity2","no such table",e);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        drawerLayout.closeDrawer(Gravity.LEFT); // 내비게이션 드로어 닫기
        Toast.makeText(MainActivity2.this, "onStop called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent=getIntent();
        startActivity(intent);
        Toast.makeText(MainActivity2.this,"onRestart called", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(MainActivity2.this, "onResume called", Toast.LENGTH_SHORT).show();
    }

}
