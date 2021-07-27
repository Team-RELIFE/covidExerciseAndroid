package com.example.exercise_android1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JoinActivity2 extends AppCompatActivity {

    private final String TAG = "JoinActivity2";
    ImageButton joinOkBtn; /*회원가입 완료 버튼*/
    EditText cmEdit,kgEdit; /*키와 체중을 입력받을 EditText(activity_join2)*/
    String id, pw, pw_chk, phone, name;
    double height = 0, weight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);
        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        pw = intent.getStringExtra("pw");
        pw_chk = intent.getStringExtra("pw_chk");
        phone = intent.getStringExtra("phone");
        name = intent.getStringExtra("name");
        joinOkBtn=(ImageButton)findViewById(R.id.JoinOkBtn);
        cmEdit=(EditText)findViewById(R.id.cmEditText);
        kgEdit=(EditText)findViewById(R.id.kgEditText);

        /**/
        /*jsp와 연동하여 db에 회원정보 저장하는 알고리즘 필요*/
        /*회원가입 성공 토스트메세지 필요*/
        /*위 과정 완료 후 joinokBtn 누르면 MainActivity2로 이동*/
        joinOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cm=cmEdit.getText().toString().trim();
                String kg=kgEdit.getText().toString().trim();

                if (!cm.isEmpty()) {
                    height = Double.parseDouble(cm);
                    weight = Double.parseDouble(kg);
                }

                Intent intent=new Intent(JoinActivity2.this,MainActivity2.class);

                ConnectServer();
                startActivity(intent);
            }
        });
    }

    private void ConnectServer(){

        //                         http://서버 ip:포트번호(tomcat 8080포트 사용)/DB연동하는 jsp파일
        final String SIGNIN_URL = getString(R.string.db_server)+"signup2.jsp";
        final String urlSuffix = "?id=" + id + "&pw=" + pw + "&phone=" + phone + "&name=" + name + "&height=" + height + "&weight=" + weight;
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

                if (s != null) { //리턴 값이 null이 아니면 jsonArray로 값 목록을 받음

                    try{
                        if (!s.contains("success")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "입력한 정보를 다시 확인해주세요.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(), "가입이 완료되었습니다.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "서버와의 통신에 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
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
                    String strParams = "id=" + id + "&pw=" + pw + "&phone=" + phone + "&name=" + name + "&height=" + height + "&weight=" + weight;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}