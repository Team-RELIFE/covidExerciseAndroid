package com.example.exercise_android1.mypage;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exercise_android1.NaviMenu;
import com.example.exercise_android1.R;
import com.example.exercise_android1.User;
import com.google.android.material.navigation.NavigationView;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//포인트 적립내역
public class PointHistoryActivity extends AppCompatActivity {

    private final String TAG = "PointHistoryActivity";
    ImageButton menuIcon;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    OAuthLogin mOAuthLogin;
    Context context;
    RecyclerView recyclerView;
    PointAdapter pointAdapter;
    String userid = "";
    User currentUser = new User().getCurrentUser();
    String point, date;
    Double u_point, u_userpoint;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        context = this;
        menuIcon = (ImageButton) findViewById(R.id.menuIcon);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);
        navigationView = (NavigationView) findViewById(R.id.navi_view);
        recyclerView = (RecyclerView) findViewById(R.id.point_recyclerView);
        pointAdapter = new PointAdapter(context);


        if (currentUser.id != null) {
            userid = currentUser.id;
            ConnectServer();
        }

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

    private void ConnectServer(){

        //                       http://서버 ip:포트번호(tomcat 8080포트 사용)/DB연동하는 jsp파일
        final String SIGNIN_URL = getString(R.string.db_server)+"point.jsp";  //jsp 파일 연동
        final String urlSuffix = "?userId=" + userid;
        Log.d("urlSuffix", urlSuffix);

        class HoldPoint extends AsyncTask<String, Void, String> {

            //스레드 관련 및 ui와의 통신을 위한 함수들이 구현되어 있음

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) { //리턴값이 null
                super.onPostExecute(s); // s : DB로부터 리턴된 값

                //s == null -> db 통신 오류, s의 길이는 기본 2이므로 s.length == 2일 때 검색된 값이 없는 것으로 취급

                if (s != null) { //리턴 값이 null이 아니면 jsonArray로 값 목록을 받음

                    try{
                        if (s.length() <= 2) { //검색된 값이 없음
                            Toast.makeText(getApplicationContext(), "서버와의 통신에 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            JSONArray jArr = new JSONArray(s);;
                            JSONObject json = new JSONObject();

                            //jsonArray의 길이(리턴된 값 길이)만큼 jsonObject에 담아 텍스트뷰에 출력

                            for (int i = 0; i < jArr.length(); i++) {
                                json = jArr.getJSONObject(i);

                                // userid = json.getString("userId");
                                u_point = json.getDouble("point");
                                //u_userpoint = json.getDouble("userpoint");
                                date = json.getString("date");
                                //recyclerview + adapter 연결
                                recyclerView.setLayoutManager(new LinearLayoutManager(PointHistoryActivity.this));
                                pointAdapter.addItem(new PointItem(u_point.toString(),date)); //아이템 추가
                                recyclerView.setAdapter(pointAdapter);
                                pointAdapter.notifyDataSetChanged();

//                                point.append("날짜 : "+ date + "\n누적 포인트 : " + u_point + "\n\n");
//                                userpoint.append("\n보유 포인트 : " + u_userpoint +"\n\n");
                            }
                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.d(TAG, "검색된 값이 없음");
                    Toast.makeText(getApplicationContext(), "서버와의 통신에 문제가 발생했습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                String line = null, page = "";

                try {
                    HttpURLConnection conn = null;

                    URL url = new URL(SIGNIN_URL); //요청 URL을 입력
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST"); //요청 방식을 설정 (default : GET)
                    conn.setRequestProperty("Accept-Charset", "UTF-8");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setDoInput(true); //input을 사용하도록 설정 (default : true)
                    conn.setDoOutput(true); //output을 사용하도록 설정 (default : false)


                    //strParams에 데이터를 담아 서버로 보냄
                    String strParams = "userId=" + userid;

                    OutputStream os = conn.getOutputStream();
                    os.write(strParams.getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    // 통신 체크 : 연결 실패시 null 반환하고 종료 --> 여기서 오류
                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.d(TAG, "통신 오류");
                        return null;
                    }

                    else {
                        BufferedReader bufreader = new BufferedReader(
                                new InputStreamReader(
                                        conn.getInputStream(), "utf-8"));
                        while ((line = bufreader.readLine()) != null) {
                            page += line;
                        }

                        return page;
                    }
                }

                catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

        HoldPoint shr = new HoldPoint();
        shr.execute(urlSuffix);
    }
}
