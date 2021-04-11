package com.example.exercise_android1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class JoinActivity2 extends AppCompatActivity {

    ImageButton joinOkBtn; /*회원가입 완료 버튼*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);

        joinOkBtn=(ImageButton)findViewById(R.id.JoinOkBtn);

        /*jsp와 연동하여 db에 회원정보 저장하는 알고리즘 필요*/
        /*회원가입 성공 토스트메세지 필요*/
        /*위 과정 완료 후 joinokBtn 누르면 MainActivity2로 이동*/
        joinOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(JoinActivity2.this,MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}