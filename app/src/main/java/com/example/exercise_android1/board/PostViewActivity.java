package com.example.exercise_android1.board;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exercise_android1.R;
import com.example.exercise_android1.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostViewActivity extends AppCompatActivity {

    String id, title, writer, content, date = "";
    int opResult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        //이전 activity로부터 받은 상세정보를 layout의 TextView에 나타내기
        Intent intent = new Intent(this.getIntent());
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        writer = intent.getStringExtra("writer");
        content = intent.getStringExtra("content");
        date = intent.getStringExtra("date");

        TextView titleTv = (TextView) findViewById(R.id.titleTv);
        TextView dateTv = (TextView) findViewById(R.id.dateTv);
        TextView trainerName = (TextView) findViewById(R.id.trainerInfo_name);
        TextView contentTv = (TextView) findViewById(R.id.contentTv);

        trainerName.setText(writer);
        titleTv.setText(title);
        contentTv.setText(content);
        dateTv.setText(date+" 작성");

        //본인이 작성한 글만 '수정', '삭제' 표시
        if (writer.equals(User.id)) {
            TextView updateTv = (TextView) findViewById(R.id.updateTv);
            TextView deleteTv = (TextView) findViewById(R.id.deleteTv);
            updateTv.setVisibility(View.VISIBLE);
            deleteTv.setVisibility(View.VISIBLE);
            updateTv.setOnClickListener(this::onClick);
            deleteTv.setOnClickListener(this::onClick);
        }

        //예약하기
        Button reserve_btn = (Button) findViewById(R.id.reserve_btn);
        reserve_btn.setOnClickListener(this::onClick);
    }


    public void onClick(View v) {
        switch (v.getId()){
            case R.id.updateTv :
                updatePost();
                break;
            case R.id.deleteTv :
                deletePost();
                break;
            case R.id.reserve_btn :
                reservePT();
                break;
        }
    }

    void updatePost() {
        Intent intent = new Intent(getApplicationContext(), UpdatePostActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("writer", writer);
        intent.putExtra("content", content);
        startActivity(intent);
    }

    void deletePost(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("글 삭제");
        builder.setMessage("글을 삭제하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ConnectServer("deletePost.jsp", "id="+id);
            }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    void reservePT() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("PT 예약");
        builder.setMessage("아래의 내용으로 예약합니다.\n" + "클래스명 : " + title + "\n강사명 : " + writer + "\n예약자명 : " + User.id);
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ConnectServer("reservePT.jsp", "postId="+id+"&trainerId="+writer+"&traineeId="+User.id);
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    private void ConnectServer(String jspFile, String dburl){

        opResult = 0;
        String SIGNIN_URL = getString(R.string.db_server)+jspFile;
        final String urlSuffix = "?" + dburl;

        class SearchHealthRecord extends AsyncTask<String, Void, String> {

            //스레드 관련 및 ui와의 통신을 위한 함수들이 구현되어 있음

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s); // s : DB로부터 리턴된 값
                if (s.contains("success")) { //리턴 값이 null이 아니면 jsonArray로 값 목록을 받음
                    opResult = 1;
                    Log.d("opresult", String.valueOf(opResult));
                    Toast.makeText(getApplicationContext(), "정상적으로 처리되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    System.out.println("from post view activity :" + s);
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
                    String strParams = dburl;

                    OutputStream os = conn.getOutputStream();
                    os.write(strParams.getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    // 통신 체크 : 연결 실패시 null 반환하고 종료
                    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        Log.d("post-connect server", "통신 오류");
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

        SearchHealthRecord shr = new SearchHealthRecord();
        shr.execute(urlSuffix);
    }

}
