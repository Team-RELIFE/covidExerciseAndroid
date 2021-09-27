package com.example.exercise_android1.TodaySchedules;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exercise_android1.Calendar.AlarmReceiver;
import com.example.exercise_android1.Calendar.CalendarListData;
import com.example.exercise_android1.Calendar.DBHelper;
import com.example.exercise_android1.Calendar.ItemTouchHelperListener;
import com.example.exercise_android1.Calendar.OnDialogListener;
import com.example.exercise_android1.Calendar.dotspanDBHelper;
import com.example.exercise_android1.MainActivity2;
import com.example.exercise_android1.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class TodayScheduleAdapter extends RecyclerView.Adapter<TodayScheduleAdapter.Holder> implements ItemTouchHelperListener, OnDialogListener {

    ArrayList<TodayScheduleItems> arrayList=new ArrayList<>();
    Context context;
    SQLiteDatabase db1, db2;
    final static String dbName="calendar.db";
    final static String dbName2="calendar_monthDay";
    DBHelper dbHelper;
    com.example.exercise_android1.Calendar.dotspanDBHelper dotspanDBHelper;
    int year=CalendarDay.today().getYear();
    int Month= CalendarDay.today().getMonth()+1;
    int Day=CalendarDay.today().getDay();
    String sMonth=Integer.toString(Month);
    String sDay=Integer.toString(Day);
    String tableName="schedule"+sMonth+sDay;

    public TodayScheduleAdapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.todayschedules, parent, false);
        return new TodayScheduleAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.onBind(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        Log.i("list_size", String.valueOf(arrayList.size()));
        return arrayList.size();
    }

    public void addItem(TodayScheduleItems item){
        arrayList.add(item);
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        TodayScheduleItems data=arrayList.get(from_position);
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
        String title=arrayList.get(position).getTitle();
        String alarm=arrayList.get(position).getAlarm();
        String content = null;
        int requestCode = 0;
        dbHelper=new DBHelper(context,dbName,null,2,sMonth,sDay);
        db1=dbHelper.getReadableDatabase();
        Cursor c=db1.rawQuery("SELECT * FROM " + tableName + " WHERE Title=" + "'" + title + "'" + ";",null);
        try {
            if (c!=null){
                if(c.moveToFirst()){
                    do {
                        content=c.getString(c.getColumnIndex("Content"));
                        requestCode=c.getInt(c.getColumnIndex("RQ_code"));
                    }while (c.moveToNext());
                }
            }
        }catch (Exception e){
            Log.w("sqlite error","no such table",e); //테이블이 존재하지 않으면 로그 메세지 출력
        }
        ((MainActivity2)MainActivity2.nContext).modifyCalendarDialog(title,content,alarm,requestCode);
    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {

        String title=arrayList.get(position).getTitle();
        dbHelper=new DBHelper(context,dbName,null,2,sMonth,sDay);
        db1=dbHelper.getReadableDatabase();

        Cursor c=db1.rawQuery("SELECT * FROM " + tableName + " WHERE Title=" + "'" + title + "'" + ";",null);

        try {
            if (c!=null){
                if(c.moveToFirst()){
                    do {
                        if (c.getString(c.getColumnIndex("Alarm"))!=null){ //만약 알람 요청코드가 존재한다면
                            int requestCode=c.getInt(c.getColumnIndex("RQ_code"));
                            AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                            Intent intent=new Intent(context, AlarmReceiver.class); 
                            PendingIntent pendingIntent=PendingIntent.getBroadcast(context,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT); //해당 요청코드를 리시버에 전달해 알람 취소
                            alarmManager.cancel(pendingIntent);
                            Log.w("alarm is canceled", String.valueOf(requestCode));
                        }
                    }while (c.moveToNext());
                }
            }
        }catch (Exception e){
            Log.w("sqlite error","no such table",e); //테이블이 존재하지 않으면 로그 메세지 출력
        }
        //데이터베이스에서 삭제
        dbHelper.deleteDBcontent(db1,arrayList.get(position).getTitle());
        //리스트뷰에서 삭제
        arrayList.remove(position);
        notifyItemRemoved(position);
        Toast.makeText(context,"일정 삭제",Toast.LENGTH_SHORT).show();

        if (arrayList.size()==0){ //리스트 개수가 0개면 캘린더뷰에서 오늘 날짜에 찍힌 도트 지우기
            ((MainActivity2)MainActivity2.nContext).constraintLayout.setVisibility(View.VISIBLE);
            dotspanDBHelper=new dotspanDBHelper(context,dbName2,null,2);
            db2=dotspanDBHelper.getReadableDatabase();
            dotspanDBHelper.deleteDBcontent(db2,sMonth,sDay);
            Log.i("Calendar Events","dot is eliminated");
        }
    }

    @Override
    public void onFinish(int position, CalendarListData item) {


    }

    public class Holder extends RecyclerView.ViewHolder{

        TextView title;
        TextView alarm;

        public Holder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.tdSchedule_title);
            alarm=itemView.findViewById(R.id.tdSchedule_alarm);
        }

        void onBind(TodayScheduleItems item){
            title.setText(item.getTitle());
            alarm.setText(item.getAlarm());
        }
    }
}
