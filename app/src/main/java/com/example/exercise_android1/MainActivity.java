package com.example.exercise_android1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinBtn=(Button)findViewById(R.id.JoinBtn);

        joinBtn.setOnClickListener(this::onClick);

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.JoinBtn:
                Intent intent=new Intent(MainActivity.this,JoinActivity1.class);
                startActivity(intent);
                break;
        }
    }

}