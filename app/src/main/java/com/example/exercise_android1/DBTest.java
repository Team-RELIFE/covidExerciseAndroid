package com.example.exercise_android1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DBTest extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "MainActivity";

    Button btn ;
    TextView tv;
    EditText userid_et,name_et;
    String userid = "", name = "", age = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btn = (Button) findViewById(R.id.start_button); //데이터베이스 접속 버튼
        btn.setOnClickListener(this);

        tv = (TextView) findViewById(R.id.resulttv); //검색 결과 텍스트뷰

        userid_et = (EditText) findViewById(R.id.userid_et); //userid 입력칸
        name_et = (EditText) findViewById(R.id.name_et); //name 입력칸

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.start_button:
                userid = userid_et.getText().toString();
                name = name_et.getText().toString();

                //빈 칸이 있을 경우 토스트 메시지 출력
                if(userid.length()<=1 || name.length()<=1){
                    Toast toast = Toast.makeText(DBTest.this, "데이터를 입력하세요", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    ConnectServer();
                }

                break;
            default:
                break;
        }
    }


    private void ConnectServer(){

        //                         http://서버 ip:포트번호(tomcat 8080포트 사용)/DB연동하는 jsp파일
        final String SIGNIN_URL = getString(R.string.db_server);
        final String urlSuffix = "?userid=" + userid + "&name=" + name;
        //Log.d("urlSuffix", urlSuffix);

        class SignupUser extends AsyncTask<String, Void, String> {

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
                            Toast toast = Toast.makeText(DBTest.this, "검색된 값이 없습니다.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else {
                            JSONArray jArr = new JSONArray(s);;
                            JSONObject json = new JSONObject();

                            //jsonArray의 길이(리턴된 값 길이)만큼 jsonObject에 담아 텍스트뷰에 출력

                            for (int i = 0; i < jArr.length(); i++) {
                                json = jArr.getJSONObject(i);

                                userid = json.getString("userid");
                                name = json.getString("name");
                                age = json.getString("age");

                                tv.setText("USERID : "+userid + "\nNAME : " + name+"\nAGE : "+age);
                            }
                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(DBTest.this, "서버와의 통신에 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
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
                    String strParams = "userid=" + userid + "&name=" + name;

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

        SignupUser su = new SignupUser();
        su.execute(urlSuffix);
    }
}

