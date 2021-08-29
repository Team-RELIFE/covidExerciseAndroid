package com.example.exercise_android1.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exercise_android1.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.HashMap;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.Holder> implements ItemTouchHelperListener, OnDialogListener {

    private ArrayList<CalendarListData> arrayList=new ArrayList<>();
    Context context;
    String year,month,day,alarm;
    int requestCode1=((CalendarActivity)CalendarActivity.context).requestCode1;
    int iYear= CalendarDay.today().getYear();
    int iMonth,iDay;

    SQLiteDatabase db1,db2;
    final static String dbName="calendar.db";
    final static String dbName2="calendar_monthDay";
    DBHelper dbHelper;
    com.example.exercise_android1.Calendar.dotspanDBHelper dotspanDBHelper;

    public CalendarAdapter(Context context, String month, String day, String alarm){
        this.context=context;
        this.month=month;
        this.day=day;
        this.alarm=alarm;
        iMonth=Integer.parseInt(month);
        iDay=Integer.parseInt(day);
        year=Integer.toString(iYear);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.calendaritem4, parent, false);
        return new CalendarAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.onBind(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void addItem(CalendarListData data){
        arrayList.add(data);
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        CalendarListData data=arrayList.get(from_position);
        arrayList.remove(from_position);
        arrayList.add(to_position,data);
        return true;
    }

    @Override
    public void onItemSwipe(int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {
        String originTitle=arrayList.get(position).getT1();
        String originContent=arrayList.get(position).getT2();
        String originAlarm=arrayList.get(position).getT3();
        ((CalendarActivity)CalendarActivity.context).modifyCalendarDialog(originTitle,originContent,originAlarm);
    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        //도트 삭제를 위해 CalendarActivity의 캘린더뷰 + 데코레이터에 접근 -> 최근에 저장한 날짜가 삭제되는 오류
        MaterialCalendarView calendarView=((CalendarActivity)CalendarActivity.context).calendarView;
        HashMap<String, EventDecorator> eventMap=((CalendarActivity)CalendarActivity.context).eventMap;
        String evKey=year+month+day;

        dbHelper=new DBHelper(context,dbName,null,2,month,day);
        db1=dbHelper.getReadableDatabase();
        dbHelper.deleteDBcontent(db1,arrayList.get(position).getT1());

        if (alarm!=null){ //알람 시간을 설정했을 경우
            //알람 취소, 요청코드 : 월+일+시+00
            String alarmText=arrayList.get(position).getT3(); //선택한 위치의 알람 문자열 get
            if (alarmText!=null){
                alarmText=alarmText.replace(":",""); //알람 문자열 사이의 : 문자 제거
                String srequestCode=month+day+alarmText+"00";
                requestCode1=Integer.parseInt(srequestCode); //요청코드 문자열을 정수로 변환

                AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                Intent intent=new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(context,requestCode1,intent,PendingIntent.FLAG_UPDATE_CURRENT); //0
                alarmManager.cancel(pendingIntent);
            }
        }
        Log.w("alarm is canceled", String.valueOf(requestCode1));
        arrayList.remove(position);
        notifyItemRemoved(position);

        if (arrayList.size()==0 && eventMap.get(evKey)!=null){
            dotspanDBHelper=new dotspanDBHelper(context,dbName2,null,2);
            db2=dotspanDBHelper.getReadableDatabase();
            dotspanDBHelper.deleteDBcontent(db2,month,day);
            calendarView.removeDecorator(eventMap.get(evKey)); //도트 삭제
            eventMap.remove(evKey);
            //System.out.println("Deleted Event Decorator:" + eventMap.get(evKey));
            Log.i("Event Delete","dot is eliminated");
        }
    }

    @Override
    public void onFinish(int position, CalendarListData item) {

    }

    public class Holder extends RecyclerView.ViewHolder{

        TextView textView1;
        TextView textView2;
        TextView textView3;

        public Holder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.scheduleTitle);
            textView2=itemView.findViewById(R.id.scheduleContent);
            textView3=itemView.findViewById(R.id.timeContent);
        }

        void onBind(CalendarListData data){
            textView1.setText(data.getT1());
            textView2.setText(data.getT2());
            textView3.setText(data.getT3());
        }
    }
}
