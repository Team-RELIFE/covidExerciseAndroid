package com.example.exercise_android1.graph;

//통계 임시 패키지 입니다

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import com.example.exercise_android1.DBHelper;
import com.example.exercise_android1.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


public class graph extends AppCompatActivity {

    public CalendarView calendarView;
    float f;
    String value;
    GraphView graphView;
    LineGraphSeries<DataPoint> series;
    SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd");
    Button button;
    Date date;
    graphDBHelper graphdbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_main);

        graphdbHelper = new graphDBHelper(this);
        graphView = (GraphView) findViewById(R.id.chart);
        series = new LineGraphSeries<DataPoint>();
        graphView.addSeries(series);

        button = (Button) findViewById(R.id.button);
        /** 그래프 좌측상단 버튼 클릭시 데이터 입력 및 그래프에 추가 (미작동, 임시코드) **/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText etEdit = new EditText(graph.this);

                AlertDialog.Builder dialog = new AlertDialog.Builder(graph.this);
                dialog.setTitle("입력");
                dialog.setView(etEdit);

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputValue = etEdit.getText().toString();
                        value = inputValue;
                        f = Float.parseFloat(value);
                        series.appendData(new DataPoint(date, f), true, 500);
                        graphView.addSeries(series);
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
        calendarView = findViewById(R.id.calendarView);
        /** 달력에서 날짜 선택하여 데이터를 입력하고 그래프에 추가 **/
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = new Date(year, month, dayOfMonth);
                final EditText etEdit = new EditText(graph.this);

                AlertDialog.Builder dialog = new AlertDialog.Builder(graph.this);
                dialog.setTitle("입력");
                dialog.setView(etEdit);

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ContentValues addRowValue = new ContentValues();

                        String inputValue = etEdit.getText().toString();
                        value = inputValue;
                        f = Float.parseFloat(value);

                        graphdbHelper.insertData(date, f);

                        series.appendData(new DataPoint(date, f), true, 500);
                        graphView.addSeries(series);
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

        /** 앱실행시 db에 존재하는 데이터를 참조하여 그래프에 데이터추가 **/
        Cursor res = graphdbHelper.getAllDate();
        if(res.getCount() == 0){
            f = 0;
        }
        else{
            while(res.moveToNext()){
                date = new Date(res.getString(1));
                f = res.getFloat(2);
                series.appendData(new DataPoint(date, f), true, 500);
            }
            graphView.addSeries(series);
        }

        /** 그래프 x축의 날짜 데이터 포멧변환 **/
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {
            @Override
            public String formatLabel (double value, boolean isValueX){
                if(isValueX){
                    return sdf.format(new Date((long) value));
                }
                else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        graphView.getGridLabelRenderer().setHumanRounding(true);
    }
}
