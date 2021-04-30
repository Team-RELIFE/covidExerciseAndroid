package com.example.exercise_android1.trainer_list;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.exercise_android1.MainActivity2;
import com.example.exercise_android1.R;
import com.example.exercise_android1.SessionActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TrainerList extends AppCompatActivity{
    private ListView listView;
    private TrainerListAdapter adapter;
    private List<com.example.exercise_android1.trainer_list.Trainer> trainerList;

    @BindView(R.id.go_pt_btn) Button go_pt_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_list_layout);
        ButterKnife.bind(this);


        Intent intent = getIntent();

        go_pt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TrainerList.this, SessionActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView)findViewById(R.id.listView);
        trainerList = new ArrayList<com.example.exercise_android1.trainer_list.Trainer>();

        //어댑터 초기화 : trainerList와 어댑터를 연결
        adapter = new TrainerListAdapter(getApplicationContext(), trainerList);
        listView.setAdapter(adapter);

        try{
            //intent로 JSONObject타입 값을 가져옴
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("trainerList"));

            //trainer_DB이라는 JSON배열이 존재한다고 가정
            JSONArray jsonArray = jsonObject.getJSONArray("trainer_DB");
            int count = 0;

            String userName;
            int userAge;

            //JSON 배열 길이만큼 반복문을 실행
            while(count < jsonArray.length()){
                //count : 배열의 인덱스
                JSONObject object = jsonArray.getJSONObject(count);

                userName = object.getString("userName");
                userAge = object.getInt("userAge");

                //받아온 데이터를 trainer 클래스 객체를 생성하여 객체에 입력
                com.example.exercise_android1.trainer_list.Trainer trainer = new com.example.exercise_android1.trainer_list.Trainer(userAge, userName);
                trainerList.add(trainer);//리스트뷰에 값을 추가
                count++;
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}