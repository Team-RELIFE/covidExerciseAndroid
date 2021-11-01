package com.example.exercise_android1.graph;

//통계 임시 패키지 입니다

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exercise_android1.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;


public class graph extends AppCompatActivity {

    public CalendarView calendarView;

    String value, bmi_info;
    GraphView graphView;
    LineGraphSeries<DataPoint> series;

    SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
    Button button;

    Date date;
    long day;
    float f, cm, kg, bmi;

    graphDBHelper graphdbHelper;
    SQLiteDatabase db;
    Cursor temp;

    HorizontalBarChart bar_chart;
    int[] colorArray = new int[]{Color.GRAY, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_main);

        TextView textview = (TextView) findViewById(R.id.bmi);
        TextView textView2 = (TextView) findViewById(R.id.bmi_result);

        graphdbHelper = new graphDBHelper(graph.this);

        graphView = (GraphView) findViewById(R.id.chart);
        graphView.getViewport().setScrollable(true);

        series = new LineGraphSeries<DataPoint>();
        graphView.addSeries(series);

        db = graphdbHelper.getWritableDatabase();
        graphdbHelper.onCreate(db); // -> 수정한 부분

        button = (Button) findViewById(R.id.button);

        calendarView = findViewById(R.id.calendarView);
        calendarView.setMaxDate(System.currentTimeMillis());

        bar_chart = (HorizontalBarChart) findViewById(R.id.bar_chart);
        BarDataSet barDataSet = new BarDataSet(dataValues(), "");
        barDataSet.setColors(colorArray);
        barDataSet.setStackLabels(new String[]{"저체중", "정상", "과체중", "비만", "고도비만"});

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.8f);
        barData.setValueTextSize(10);
        barData.setDrawValues(false);

        bar_chart.setData(barData);
        bar_chart.setDescription(null);

        bar_chart.setTouchEnabled(false);
        bar_chart.getXAxis().setEnabled(false);
        bar_chart.getXAxis().setLabelCount(0, false);
        bar_chart.getXAxis().setDrawAxisLine(false);
        bar_chart.getXAxis().setDrawGridLines(false);
        bar_chart.getXAxis().setDrawLabels(false);
        bar_chart.getAxisRight().setDrawGridLines(false);
        bar_chart.getAxisLeft().setDrawAxisLine(false);
        bar_chart.getAxisLeft().setDrawLabels(false);
        bar_chart.getAxisLeft().setDrawGridLines(false);

        bar_chart.getAxis(null).setDrawAxisLine(false);
        bar_chart.getAxis(null).setDrawLabels(false);

        /** BMI 계산을 위한 신체 데이터 입력 **/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(graph.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.graph_bmi_dialog, null);
                builder.setView(view);
                final Button submit = (Button) view.findViewById(R.id.buttonSubmit);
                final EditText height = (EditText) view.findViewById(R.id.edittextheight);
                final EditText weight = (EditText) view.findViewById(R.id.edittextweight);

                final AlertDialog dialog = builder.create();

                submit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String _height = height.getText().toString();
                        String _weight = weight.getText().toString();

                        cm = Float.parseFloat(_height);
                        kg = Float.parseFloat(_weight);
                        graphdbHelper.bmi_updateData(db, cm, kg);

                        cm = cm / 100;
                        bmi = kg / (cm * cm);
                        bmi_info = String.format("BMI = %.1f", bmi);
                        textview.setText(bmi_info);

                        if(bmi >= 30){
                            textView2.setText("고도비만");
                        }
                        else if(bmi < 30 && bmi >= 25){
                            textView2.setText("비만");
                        }
                        else if(bmi < 25 && bmi >= 23){
                            textView2.setText("과체중");
                        }
                        else if(bmi < 23 && bmi >= 18.5){
                            textView2.setText("정상");
                        }
                        else{
                            textView2.setText("저체중");
                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        /** 달력에서 날짜 선택하여 데이터를 입력하고 그래프에 추가 **/
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // 달력에서 선택한 날짜 정보를 date -> long 형으로 변환하여 변수에 저장
                date = new Date(year, month, dayOfMonth);
                day = date.getTime();

                final EditText etEdit = new EditText(graph.this);
                etEdit.setFilters(new InputFilter[]{filterNumAndComma});
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
                        graphView.getViewport().setXAxisBoundsManual(true);
                        graphView.getViewport().setYAxisBoundsManual(true);
                        graphView.getViewport().setMinY(30);
                        graphView.getViewport().setMaxY(120);
                        graphView.getViewport().setScalable(true);  // activate horizontal zooming and scrolling
                        graphView.getViewport().setScrollable(true);  // activate horizontal scrolling
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


        /** 앱실행시 DB에 존재하는 데이터를 참조하여 BMI 정보 입력 **/
        Cursor res = db.rawQuery("select * from bmi_table", null);
        if(res.getCount() != 0){
            res.moveToNext();
            cm = res.getFloat(1);
            kg = res.getFloat(2);
            cm = cm / 100;
            bmi = kg / (cm * cm);
            bmi_info = String.format("BMI = %.1f", bmi);
            textview.setText(bmi_info);

            if(bmi >= 30){
                textView2.setText("고도비만");
            }
            else if(bmi < 30 && bmi >= 25){
                textView2.setText("비만");
            }
            else if(bmi < 25 && bmi >= 23){
                textView2.setText("과체중");
            }
            else if(bmi < 23 && bmi >= 18.5){
                textView2.setText("정상");
            }
            else{
                textView2.setText("저체중");
            }
        }


        /** 앱실행시 DB에 존재하는 데이터를 참조하여 그래프에 데이터추가 **/
        res = graphdbHelper.getAllDate();
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
            temp = db.query("DP_table", null, null, null, null, null, null);
            graphView.getGridLabelRenderer().setNumHorizontalLabels(temp.getCount());
//            graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
            graphView.setHorizontalScrollBarEnabled(true);
            graphView.getViewport().setScrollable(true);  // activate horizontal scrolling
            graphView.getViewport().setScrollableY(true);
            graphView.getViewport().setXAxisBoundsManual(true);
            graphView.getViewport().setYAxisBoundsManual(true);
            graphView.getViewport().setMinY(30);
            graphView.getViewport().setMaxY(120);
            graphView.getGridLabelRenderer().setHumanRounding(false);

        }

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

    private ArrayList dataValues(){
        ArrayList dataVals = new ArrayList<>();

        dataVals.add(new BarEntry(0, new float[]{18.5f, 23, 25, 30, 43}));

        return dataVals;
    }

    /** 몸무게 입력창 필터링 **/
    public InputFilter filterNumAndComma = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[.0-9]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };
}
