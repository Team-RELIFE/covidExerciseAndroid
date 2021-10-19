package com.example.exercise_android1.mypage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exercise_android1.R;
import com.example.exercise_android1.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PointActivity extends AppCompatActivity {

    private final String TAG = "PointActivity";

    TextView nicknameText;
    TextView EmailText;
    TextView pointMainText;
    TextView pointText1;
    TextView pointText2;
    ImageButton pointbutton;

    User currentUser = new User().getCurrentUser();

    String userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        nicknameText = (TextView) findViewById(R.id.nicknameText);
        EmailText = (TextView) findViewById(R.id.EmailText);
        pointMainText = (TextView) findViewById(R.id.pointMainText);
        pointText1 = (TextView) findViewById(R.id.pointText1);
        pointText2 = (TextView) findViewById(R.id.pointText2);
        pointbutton= (ImageButton) findViewById(R.id.pointbutton); /*포인트리스트로 이동*/


        if (currentUser.id != null) {
            userid = currentUser.id;
            pointMainText.setText(currentUser.name+"님의 포인트");
            nicknameText.setText(currentUser.name+"");
            EmailText.setText(currentUser.id+"");
            ConnectServer();
        }

        else {
            pointMainText.setText("로그인이 필요한 서비스입니다.");
            nicknameText.setText("로그인이 필요한 서비스입니다.");
            EmailText.setText("로그인이 필요한 서비스입니다.");
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.pointbutton:
                    Intent intent = new Intent(PointActivity.this, PointHistoryActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void ConnectServer(){

        //                         http://서버 ip:포트번호(tomcat 8080포트 사용)/DB연동하는 jsp파일
        final String SIGNIN_URL = getString(R.string.db_server)+"point.jsp";  //jsp 파일 연동
        final String urlSuffix = "?id=" + userid;
        //Log.d("urlSuffix", urlSuffix);

        class HoldPoint extends AsyncTask<String, Void, String> {

            //스레드 관련 및 ui와의 통신을 위한 함수들이 구현되어 있음

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
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

                                userid = json.getString("id");
                                Double sumpoint = json.getDouble("sumpoint");

                                /*StringBuilder sql = new StringBuilder();
                                sql.append("SELECT userId ,sum(point) FROM capstone.point GROUP BY userId"); */

                                pointText1.append(""+ sumpoint);
                                pointText2.append("" + sumpoint);

                            }
                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
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
                    String strParams = "id=" + userid;

                    OutputStream os = conn.getOutputStream();
                    os.write(strParams.getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    // 통신 체크 : 연결 실패시 null 반환하고 종료
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