//package com.example.exercise_android1;
//
//import android.app.AlertDialog;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.error.VolleyError;
//import com.android.volley.request.SimpleMultiPartRequest;
//import com.android.volley.toolbox.Volley;
//
//public class imageActivity {
//    //동적퍼미션 작업
//if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//
//        int permissionResult= checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if(permissionResult== PackageManager.PERMISSION_DENIED){
//            String[] permissions= new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE};
//            requestPermissions(permissions,10);
//        }
//    }else{
//        //cv.setVisibility(View.VISIBLE);
//    }
////동적퍼미션 작업
//if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//
//        int permissionResult= checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if(permissionResult== PackageManager.PERMISSION_DENIED){
//            String[] permissions= new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE};
//            requestPermissions(permissions,10);
//        }
//    }else{
//        //cv.setVisibility(View.VISIBLE);
//    }
//    //갤러리 or 사진 앱 실행하여 사진을 선택하도록
//    Intent intent= new Intent(Intent.ACTION_PICK);
//intent.setType("image/*");
//    startActivityForResult(intent,10);
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode){
//            case 10:
//                if(resultCode==RESULT_OK){
//                    //선택한 사진의 경로(Uri)객체 얻어오기
//                    Uri uri= data.getData();
//                    if(uri!=null){
//                        이미지뷰변수명.setImageURI(uri);
//
//                        //갤러리앱에서 관리하는 DB정보가 있는데, 그것이 나온다 [실제 파일 경로가 아님!!]
//                        //얻어온 Uri는 Gallery앱의 DB번호임. (content://-----/2854)
//                        //업로드를 하려면 이미지의 절대경로(실제 경로: file:// -------/aaa.png 이런식)가 필요함
//                        //Uri -->절대경로(String)로 변환
//                        String imgPath= getRealPathFromUri(uri);   //임의로 만든 메소드 (절대경로를 가져오는 메소드)
//
//                        //이미지 경로 uri 확인해보기
//                        //new AlertDialog.Builder(this).setMessage(uri.toString()+"\n"+imgPath).create().show();
//                        Log.d("이미지 경로",uri.toString()+"\n"+imgPath);
//
//                    }
//
//                }else
//                {
//                    Toast.makeText(this, "이미지가 선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }//onActivityResult()
//
//    //Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
//    String getRealPathFromUri(Uri uri){
//        String[] proj= {MediaStore.Images.Media.DATA};
//        CursorLoader loader= new CursorLoader(this, uri, proj, null, null, null);
//        Cursor cursor= loader.loadInBackground();
//        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String result= cursor.getString(column_index);
//        cursor.close();
//        return  result;
//    }
//    //서버로 보낼 데이터
//    String name= etName.getText().toString();
//    String msg= etMsg.getText().toString();
//
//    //안드로이드에서 보낼 데이터를 받을 php 서버 주소
//    String serverUrl="http://192.168.28.1/insertDB.php";
//
//    //Volley plus Library를 이용해서
//    //파일 전송하도록..
//    //Volley+는 AndroidStudio에서 검색이 안됨 [google 검색 이용]
//
//    //파일 전송 요청 객체 생성[결과를 String으로 받음]
//    SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
//        @Override
//        public void onResponse(String response) {
//            //new AlertDialog.Builder(MainActivity2.this).setMessage("응답:"+response).create().show();
//        }
//    }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//            //Toast.makeText(MainActivity2.this, "ERROR", Toast.LENGTH_SHORT).show();
//
//        }
//
//
//    });
//    //요청 객체에 보낼 데이터를 추가
//        smpr.addStringParam("name", name);
//        smpr.addStringParam("msg", msg);
//    //이미지 파일 추가
//        smpr.addFile("img", imgPath);
//
//    //요청객체를 서버로 보낼 우체통 같은 객체 생성
//    RequestQueue requestQueue= Volley.newRequestQueue(this);
//        requestQueue.add(smpr);
//
//}
