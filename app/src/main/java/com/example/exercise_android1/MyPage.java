package com.example.exercise_android1;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MyPage extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        User currentUser = new User().getCurrentUser();

//        EditText userName = (EditText) findViewById(R.id.userName);
//        TextView userEmail = (TextView) findViewById(R.id.userEmail);
//
//        if (currentUser.id != null) {
//            userName.setText(currentUser.name);
//            userEmail.setText(currentUser.id);
//        }
//        else {
//            userName.setText("Guest");
//            userEmail.setText("로그인이 필요합니다.");
//        }
    }
}
