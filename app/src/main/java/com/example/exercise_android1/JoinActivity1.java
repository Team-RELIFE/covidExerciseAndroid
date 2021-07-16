package com.example.exercise_android1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class JoinActivity1 extends AppCompatActivity {

    ImageButton joinNextBtn;
    /*아이디,비밀번호,비밀번호확인,이름,폰번호에 대한 Edittext(activity_join1에서 확인 가능)*/
    EditText idEdit,pwEdit,pwOkEdit,nameEdit,phoneEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join1);

        joinNextBtn=(ImageButton)findViewById(R.id.joinNextBtn); /*회원가입 다음단계로 이동*/
        idEdit=(EditText)findViewById(R.id.joinIdEdit);
        pwEdit=(EditText)findViewById(R.id.joinPassEdit1);
        pwOkEdit=(EditText)findViewById(R.id.joinPassEdit2);
        nameEdit=(EditText)findViewById(R.id.joinNameEdit);
        phoneEdit=(EditText)findViewById(R.id.joinPhoneEdit);

        /*버튼 수가 적어서 switch문 안쓰고 setOnClickListener 사용*/
        /*joinNextBtn(회원가입 다음 단계로 이동)버튼 누르면 db로 위 editText로부터 입력된 string 데이터들을 저장*/
        joinNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*editText로부터 입력받은 각 항목들을 string형태로 변환*/
                String id=idEdit.getText().toString().trim();
                String pw=pwEdit.getText().toString().trim();
                String pw_chk=pwOkEdit.getText().toString().trim();
                String name=nameEdit.getText().toString().trim();
                String phone=phoneEdit.getText().toString().trim();

                // 모든 정보 입력 여부 체크
                //TODO : 글자수 체크, 기본키(아이디) 존재 여부 체크
                if(id.length()==0 || pw.length()==0 || name.length()==0 || phone.length()==0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "입력하지 않은 정보가 있습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                // 비밀번호 체크
                else if (!pw.contentEquals(pw_chk)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                else {
                    Intent intent=new Intent(JoinActivity1.this,JoinActivity2.class);

                    intent.putExtra("id", id);
                    intent.putExtra("pw", pw);
                    intent.putExtra("pw_chk", pw_chk);
                    intent.putExtra("name", name);
                    intent.putExtra("phone", phone);

                    startActivity(intent);
                }
            }
        });

    }

    /*뒤로가기 버튼 눌렀을 때 이전 페이지로 돌아간 다음 finish()호출*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}