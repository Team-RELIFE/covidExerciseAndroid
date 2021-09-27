package com.example.exercise_android1.TodaySchedules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exercise_android1.Calendar.DBHelper;
import com.example.exercise_android1.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;

public class PassedScheduleAdapter extends RecyclerView.Adapter<PassedScheduleAdapter.Holder> implements DeleteSwipeListener {

    ArrayList<TodayScheduleItems> arrayList=new ArrayList<>();
    Context context;
    SQLiteDatabase db1, db2;
    final static String dbName="calendar.db";
    final static String dbName2="calendar_monthDay";
    DBHelper dbHelper;
    com.example.exercise_android1.Calendar.dotspanDBHelper dotspanDBHelper;
    int year= CalendarDay.today().getYear();
    int Month= CalendarDay.today().getMonth()+1;
    int Day=CalendarDay.today().getDay();
    String sYear=Integer.toString(year);
    String sMonth=Integer.toString(Month);
    String sDay=Integer.toString(Day);
    String tableName="schedule"+sMonth+sDay;

    public PassedScheduleAdapter(Context context){
        this.context=context;
    }

    @NonNull
    @Override
    public PassedScheduleAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.todayschedules, parent, false);
        return new PassedScheduleAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PassedScheduleAdapter.Holder holder, int position) {
        holder.onBind(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
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
        return false;
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
