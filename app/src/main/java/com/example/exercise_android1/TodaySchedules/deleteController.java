package com.example.exercise_android1.TodaySchedules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exercise_android1.Calendar.DBHelper;
import com.example.exercise_android1.Calendar.dotspanDBHelper;
import com.example.exercise_android1.TodaySchedules.PassedScheduleAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class deleteController extends ItemTouchHelper.Callback {
    private PassedScheduleAdapter listener;
    Context context;
    private final RecyclerView.ViewHolder currenrtItemViewHolder = null;
    SQLiteDatabase db1,db2;
    final static String dbName="calendar.db";
    final static String dbName2="calendar_monthDay";
    DBHelper dbHelper;
    com.example.exercise_android1.Calendar.dotspanDBHelper dotspanDBHelper;
    int Month= CalendarDay.today().getMonth()+1;
    int Day=CalendarDay.today().getDay();
    String sMonth=Integer.toString(Month);
    String sDay=Integer.toString(Day);
    
    public deleteController(PassedScheduleAdapter listener){
        this.listener=listener;
        this.context=listener.context;
    }
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int draw_flags= ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipe_flags= ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(draw_flags,swipe_flags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return listener.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        String title=listener.arrayList.get(position).getTitle();
        dbHelper=new DBHelper(context,dbName,null,2,sMonth,sDay);
        db1=dbHelper.getReadableDatabase();
        dbHelper.deleteDBcontent(db1,title);
        listener.arrayList.remove(position);
        listener.notifyItemRemoved(position);
        if (listener.arrayList.size()==0){ //리스트 개수가 0개면 캘린더뷰에서 오늘 날짜에 찍힌 도트 지우기
            dotspanDBHelper=new dotspanDBHelper(context,dbName2,null,2);
            db2=dotspanDBHelper.getReadableDatabase();
            dotspanDBHelper.deleteDBcontent(db2,sMonth,sDay);
            Log.i("Calendar Events","dot is eliminated");
        }
    }
}
