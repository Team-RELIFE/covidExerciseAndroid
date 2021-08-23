package com.example.exercise_android1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.exercise_android1.trainer_list.TrainerList;
import com.google.android.material.navigation.NavigationView;
import com.nhn.android.naverlogin.OAuthLogin;


public class MainActivity2 extends AppCompatActivity {

    ImageButton menuIcon; /*메뉴버튼*/
    Toolbar toolbar; /*툴바*/
    DrawerLayout drawerLayout; /*드로어레이아웃*/
    NavigationView navigationView; /*내비게이션뷰*/
    OAuthLogin mOAuthLogin;
    Context nContext;
    Button ptListBtn;
    private long time=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        menuIcon=(ImageButton)findViewById(R.id.menuIcon);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        navigationView=(NavigationView)findViewById(R.id.navi_view);
        
        ptListBtn = (Button)findViewById(R.id.pt_list_Btn);
        nContext=this;
        
        
        /*액션바 대신 툴바 사용*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); /*앱 타이틀 안보이게*/
        //getActionBar사용시 오류//


        //drawer header :: 로그인한 상태라면 유저 이름을 표시, 아니라면(게스트 로그인) "Guest"로 표시
        View drawerHeader = navigationView.getHeaderView(0);
        TextView header_userName = (TextView) drawerHeader.findViewById(R.id.nameTV);
        User currentUser = new User().getCurrentUser();

        if (currentUser.id != null) {
            header_userName.setText(currentUser.name+" 님");
        }
        else {
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

//                int id=item.getItemId();
//
//                /*로그아웃*/
//                if (id==R.id.main_logout){
//                    mOAuthLogin=OAuthLogin.getInstance();
//                    mOAuthLogin.logout(nContext);
//                    currentUser.setCurrentUser(null, "", "", 0, 0, 0);
//                    Toast.makeText(nContext,"로그아웃",Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(MainActivity2.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//                if(id==R.id.menu_calendar){
//                    Intent intent=new Intent(MainActivity2.this,CalendarActivity.class);
//                    startActivity(intent);
//                }
//
//                if(id==R.id.menu_health_record){
//                    Intent intent=new Intent(MainActivity2.this,HealthRecordActivity.class);
//                    startActivity(intent);
//                }
//                if (id==R.id.menu_myInfo){
//                    Intent intent = new Intent(MainActivity2.this, PointActivity.class);
//                    startActivity(intent);
//                }
                return false;
            }
        });

        ptListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity2.this, SessionActivity.class);
                Intent intent=new Intent(MainActivity2.this, TrainerList.class);
                startActivity(intent);
            }
        });
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
}