package com.example.exercise_android1.graph;

//통계 임시 패키지 입니다

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exercise_android1.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;


public class graph extends AppCompatActivity {

    public CalendarView calendarView;

    String value;
    GraphView graphView;
    LineGraphSeries<DataPoint> series;

    SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");

    Button button;

    Date date;
    long day;
    float f;

    graphDBHelper graphdbHelper;
    SQLiteDatabase db;
    Cursor temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_main);

        graphdbHelper = new graphDBHelper(graph.this);

        graphView = (GraphView) findViewById(R.id.chart);
        graphView.getViewport().setScrollable(true);

        series = new LineGraphSeries<DataPoint>();
        graphView.addSeries(series);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(5);


        db = graphdbHelper.getWritableDatabase();

        button = (Button) findViewById(R.id.button);

        calendarView = findViewById(R.id.calendarView);

        /** 달력에서 날짜 선택하여 데이터를 입력하고 그래프에 추가 **/
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // 달력에서 선택한 날짜 정보를 date -> long 형으로 변환하여 변수에 저장
                date = new Date(year, month, dayOfMonth);
                day = date.getTime();

                final EditText etEdit = new EditText(graph.this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(graph.this);
                dialog.setTitle("입력");
                dialog.setView(etEdit);

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //EditText에서 받은 값을 float형으로 변환하여 저장
                        String inputValue = etEdit.getText().toString();
                        value = inputValue;
                        f = Float.parseFloat(value);

                        graphdbHelper.updateData(db, day, f);

                        //갱신된 DB를 바탕으로 그래프를 갱신
                        series.resetData(grabData());
                        series.setThickness(4); //그래프 선 두께
                        series.setDrawBackground(true); //그래프 배경색 유무
                        series.setDrawDataPoints(true); //그래프내 좌표(점)표시 유무
                        graphView.removeAllSeries();
                        graphView.addSeries(series);

                        //그래프 X축 데이터 포멧변환
                        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                            @Override
                            public String formatLabel(double value, boolean isValueX) {
                                if (isValueX) {
                                    return sdf.format(new Date((long) value));
                                } else {
                                    return super.formatLabel(value, isValueX);
                                }
                            }
                        });
                        graphView.getGridLabelRenderer().setHumanRounding(true);

                        //x축에 표시되는 수치를 DB에 저장된 데이터수만큼 표시
                        temp = db.query("DP_table", null, null, null, null, null, null);
                        graphView.getGridLabelRenderer().setNumHorizontalLabels(temp.getCount());
                        graphView.getViewport().setScrollable(true);
                        graphView.getGridLabelRenderer().setHumanRounding(false);
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        /** 앱실행시 DB에 존재하는 데이터를 참조하여 그래프에 데이터추가 **/
        Cursor res = graphdbHelper.getAllDate();
        if(res.getCount() == 0){
            graphView.addSeries(series);
        }
        else{
            series.resetData(grabData());
            graphView.removeAllSeries();
            graphView.addSeries(series);
            series.setThickness(4); //그래프 선 두께
            series.setDrawBackground(true); //그래프 배경색 유무
            series.setDrawDataPoints(true); //그래프 데이터 점표시 유무

        }
        graphView.getViewport().setScrollable(true);

        /** 그래프 x축의 날짜 데이터 포멧변환 **/
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return sdf.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        temp = db.query("DP_table", null, null, null, null, null, null);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(temp.getCount());
        graphView.getViewport().setScrollable(true);
        graphView.getGridLabelRenderer().setHumanRounding(false);
    }

    /** 내부 DB에 저장된 데이터를 날짜순으로 읽고 저장하여 그래프에 대입하기 위한 메소드 **/
    private DataPoint[] grabData(){
        long date;
        float f;


        Cursor cursor = db.rawQuery("select * from DP_table order by DATE ASC", null);

        DataPoint[] dataPoints = new DataPoint[cursor.getCount()];

        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToNext();
            date = cursor.getLong(1);
            f = cursor.getFloat(2);
            dataPoints[i] = new DataPoint(date, f);
        }
        return dataPoints;
    }
}
