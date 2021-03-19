package com.example.exercise_android1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.nhn.android.naverlogin.OAuthLogin;

public class MainActivity2 extends AppCompatActivity {

    ImageButton menuIcon; /*메뉴버튼*/
    Toolbar toolbar; /*툴바*/
    DrawerLayout drawerLayout; /*드로어레이아웃*/
    NavigationView navigationView; /*내비게이션뷰*/
    OAuthLogin mOAuthLogin;
    Context nContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        menuIcon=(ImageButton)findViewById(R.id.menuIcon);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.navi_view);
        nContext=this;

        /*액션바 대신 툴바 사용*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); /*앱 타이틀 안보이게*/
        //getActionBar사용시 오류//

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

                int id=item.getItemId();

                /*로그아웃*/
                if (id==R.id.main_logout){
                    mOAuthLogin=OAuthLogin.getInstance();
                    mOAuthLogin.logout(nContext);
                    Toast.makeText(nContext,"로그아웃",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity2.this,MainActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }
}