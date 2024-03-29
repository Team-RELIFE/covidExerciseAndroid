package com.example.exercise_android1.board;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exercise_android1.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetPostsActivity extends AppCompatActivity {

    public ArrayList<Post> posts = new ArrayList<Post>();
    String page = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        connectDB();
    }

    public void sendData(String s) {
        Intent intent=new Intent(getApplicationContext(), CustomListActivity.class);
        intent.putExtra("result", s);
        startActivity(intent);
    }

    public void connectDB(){

        ArrayList<Post> posts = new ArrayList<Post>();

        //                         http://서버 ip:포트번호(tomcat 8080포트 사용)/DB연동하는 jsp파일
        final String SIGNIN_URL = getString(R.string.db_server)+"getPosts.jsp";

        class UserPost extends AsyncTask<String, Void, String> {

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
                        if (s.length() <= 2) { //검색된 값이 없음
                            System.out.println("from getposts activity :" +s);
                            Toast toast = Toast.makeText(getApplicationContext(), "작성된 글이 없습니다.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else {
                            sendData(s);
                        }

                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }

                else {
                    //System.out.println("from getposts activity :" +s);
                    Toast.makeText(getApplicationContext(), "서버와의 통신에 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                BufferedReader bufferedReader = null;
                String line = null;
                page = "";

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
                    String strParams = "";

                    OutputStream os = conn.getOutputStream();
                    os.write(strParams.getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    // 통신 체크 : 연결 실패시 null 반환하고 종료
                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.d("GetPostsActivity", "통신 오류");
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

        UserPost up = new UserPost();
        up.execute();

    }
}
