package com.example.exercise_android1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exercise_android1.Calendar.AlarmReceiver;
import com.example.exercise_android1.Calendar.CalendarActivity;
import com.example.exercise_android1.Calendar.CalendarAdapter;
import com.example.exercise_android1.Calendar.CalendarListData;
import com.example.exercise_android1.Calendar.DBHelper;
import com.example.exercise_android1.Calendar.ItemTouchHelperListener;
import com.example.exercise_android1.Calendar.OnDialogListener;
import com.example.exercise_android1.Calendar.swipeController;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class TodayScheduleAdapter extends RecyclerView.Adapter<TodayScheduleAdapter.Holder> implements ItemTouchHelperListener, OnDialogListener {

    ArrayList<TodayScheduleItems> arrayList=new ArrayList<>();
    Context context;
    SQLiteDatabase db;
    final static String dbName="calendar.db";
    DBHelper dbHelper;
    int Month= CalendarDay.today().getMonth()+1;
    int Day=CalendarDay.today().getDay();
    String sMonth=Integer.toString(Month);
    String sDay=Integer.toString(Day);
    String tableName="schedule"+sMonth+sDay;

    //int requestCode1=((CalendarActivity)CalendarActivity.context).requestCode1;

    TodayScheduleAdapter(Context context){
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

    @Override
    public void onLeftClick(int position, RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void onRightClick(int position, RecyclerView.ViewHolder viewHolder) {
        dbHelper=new DBHelper(context,dbName,null,2,sMonth,sDay);
        db=dbHelper.getReadableDatabase();
        //if (db.Alarm != null)
        Cursor c=db.rawQuery("SELECT * FROM"+" "+tableName,null);
        System.out.println(c);
        try {
            if (c!=null){
                if(c.moveToFirst()){
                    do {
                        if (c.getString(c.getColumnIndex("Alarm"))!=null){
                            int requestCode=c.getInt(c.getColumnIndex("RQ_code"));
                            AlarmManager alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                            Intent intent=new Intent(context, AlarmReceiver.class); //알람 요청코드가 리시버로 전달이 안됨
                            PendingIntent pendingIntent=PendingIntent.getBroadcast(context,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT); //0
                            alarmManager.cancel(pendingIntent);
                            Log.w("alarm is canceled", String.valueOf(requestCode));
                        }
                    }while (c.moveToNext());
                }
            }
        }catch (Exception e){
            Log.w("TAG","no such table",e); //테이블이 존재하지 않으면 로그 메세지 출력
        }
        dbHelper.deleteDBcontent(db,arrayList.get(position).getTitle());
        arrayList.remove(position);
        notifyItemRemoved(position);
        Log.w("rigth_click","ok");
        Toast.makeText(context,"일정 삭제",Toast.LENGTH_SHORT).show();
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
