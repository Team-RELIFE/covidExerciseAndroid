package com.example.exercise_android1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class JoinActivity1 extends AppCompatActivity {

    ImageButton joinNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join1);

        joinNextBtn=(ImageButton)findViewById(R.id.joinNextBtn); /*회원가입 다음단계로 이동*/

        /*버튼 수가 적어서 switch문 안쓰고 setOnClickListener 사용*/
        joinNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(JoinActivity1.this,JoinActivity2.class);
                startActivity(intent);
            }
        });

    }
}