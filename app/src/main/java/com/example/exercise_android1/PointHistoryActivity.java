package com.example.exercise_android1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.nhn.android.naverlogin.OAuthLogin;

//포인트 적립내역
public class PointHistoryActivity extends AppCompatActivity {

    ImageButton menuIcon;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    OAuthLogin mOAuthLogin;
    Context context;
    RecyclerView recyclerView;
    PointAdapter pointAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        context = this;
        menuIcon = (ImageButton) findViewById(R.id.menuIcon);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);
        navigationView = (NavigationView) findViewById(R.id.navi_view);
        User currentUser = new User().getCurrentUser();
        recyclerView = (RecyclerView) findViewById(R.id.point_recyclerView);
        pointAdapter = new PointAdapter(context);

        //recyclerview + adapter 연결
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pointAdapter.addItem(new PointItem("2021","200 point")); //아이템 추가
        recyclerView.setAdapter(pointAdapter);
        pointAdapter.notifyDataSetChanged();

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NaviMenu naviMenu = new NaviMenu(context, item, currentUser);
                naviMenu.selectMenu();
//                int id = item.getItemId();
//                /*로그아웃*/
//
//                if (id==R.id.main_logout){
//                    mOAuthLogin= OAuthLogin.getInstance();
//                    mOAuthLogin.logout(context);
//                    currentUser.setCurrentUser(null, "", "", 0, 0, 0);
//                    Toast.makeText(context,"로그아웃",Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(PointHistoryActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//                if(id==R.id.menu_calendar){
//                    Intent intent=new Intent(PointHistoryActivity.this,CalendarActivity.class);
//                    startActivity(intent);
//                }
//
//                if(id==R.id.menu_health_record){
//                    Intent intent=new Intent(PointHistoryActivity.this,HealthRecordActivity.class);
//                    startActivity(intent);
//                }
//                if (id==R.id.menu_myInfo){
//                    Intent intent = new Intent(PointHistoryActivity.this, PointActivity.class);
//                    startActivity(intent);
//                }
//                if (id==R.id.menu_point){
//                    Intent intent = new Intent(PointHistoryActivity.this, PointHistoryActivity.class);
//                    startActivity(intent);
//                }
                return false;
            }
        });

    }
}
