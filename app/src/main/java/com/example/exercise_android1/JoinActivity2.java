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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JoinActivity2 extends AppCompatActivity {

    ImageButton joinOkBtn; /*회원가입 완료 버튼*/
    EditText cmEdit,kgEdit; /*키와 체중을 입력받을 EditText(activity_join2)*/
    String id, pw, pw_chk, phone, name, cm, kg;

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
                Intent intent=new Intent(JoinActivity2.this,MainActivity2.class);

                //TODO : Null point 오류나서 주석처리(바로 메인2로 이동)
//                if(pw.equals(pw_chk))
//                {
//                    Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다.", Toast.LENGTH_LONG).show();
//                    try {
//                        String result;
//                        CustomTask task = new CustomTask();
//                        result = task.execute(id, pw, phone, name, cm, kg).get();
//                        Log.i("리턴 값",result);
//                    } catch (Exception e) {
//
//                    }
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "비밀번호가 서로 일치하지 않습니다.", Toast.LENGTH_LONG).show();
//                    intent=new Intent(JoinActivity2.this,JoinActivity1.class);
//                }

                startActivity(intent);
            }
        });
    }
    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        // doInBackground의 매개값이 문자열 배열인데요. 보낼 값이 여러개일 경우를 위해 배열로 합니다.
        protected String doInBackground(String... strings) {
            try {
                String str;
                //TODO : //보낼 jsp 주소를 ""안에 작성합니다. //ip주소는 본인 걸로
                URL url = new URL("192.168.55.141/Model2-Board/userJoin.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                sendMsg = "id="+strings[0]+"&pw="+strings[1]+"&phone="+strings[2]+"&name="+strings[3]+"&cm="+strings[4]+"&kg="+strings[5];
                //회원가입처럼 보낼 데이터가 여러 개일 경우 &로 구분하여 작성합니다.
                osw.write(sendMsg);//OutputStreamWriter에 담아 전송합니다.
                osw.flush();
                //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    //jsp에서 보낸 값을 받겠죠?
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                    // 통신이 실패했을 때 실패한 이유를 알기 위해 로그를 찍습니다.
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //jsp로부터 받은 리턴 값입니다.
            return receiveMsg;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}