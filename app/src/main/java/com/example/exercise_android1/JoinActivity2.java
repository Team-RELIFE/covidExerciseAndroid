package com.example.exercise_android1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class JoinActivity2 extends AppCompatActivity {

    ImageButton joinOkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join2);

        joinOkBtn=(ImageButton)findViewById(R.id.JoinOkBtn);
        joinOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(JoinActivity2.this,MainActivity2.class);
                startActivity(intent);
            }
        });
    }
}