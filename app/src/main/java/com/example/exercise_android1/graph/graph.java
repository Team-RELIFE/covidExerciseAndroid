package com.example.exercise_android1.graph;

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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


public class graph extends AppCompatActivity {

    public CalendarView calendarView;

    String value, bmi_info;

    Button button;

    Date date;
    long day;
    float f, cm, kg, bmi;

    graphDBHelper graphdbHelper;
    SQLiteDatabase db;
    Cursor temp;

    LineChart lineChart;
    LineDataSet linedataset = new LineDataSet(null ,null);
    LineData lineData;
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
    ArrayList<Entry> dataEntry = new ArrayList<>();

    HorizontalBarChart bar_chart;
    int[] colorArray = new int[]{Color.GRAY, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_main);

        TextView textview = (TextView) findViewById(R.id.bmi);
        TextView textView2 = (TextView) findViewById(R.id.bmi_result);

        graphdbHelper = new graphDBHelper(graph.this);

        lineChart = (LineChart) findViewById(R.id.chart);

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

                        String inputValue = etEdit.getText().toString();
                        value = inputValue;
                        f = Float.parseFloat(value);

                        graphdbHelper.updateData(db, day, f);

                        temp = graphdbHelper.getAllDate();

                        linedataset.setValues(getDataValues());
                        linedataset.setDrawFilled(true);
                        linedataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                        dataSets.clear();
                        dataSets.add(linedataset);
                        lineData = new LineData(dataSets);

                        lineChart.clear();
                        lineChart.setData(lineData);
                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setValueFormatter(new MyXAxisValueFormatter());
                        xAxis.setDrawLabels(true);
                        xAxis.setDrawAxisLine(true);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                        temp = graphdbHelper.getAllDate();
                        if(temp.getCount() < 8){
                            xAxis.setLabelCount(temp.getCount(), true);
                            lineChart.setVisibleXRangeMaximum(temp.getCount());
                            xAxis.setSpaceMax(0.05f);
                        }
                        else{
                            xAxis.setLabelCount(8, true);
                            lineChart.setVisibleXRangeMaximum(7);
                            lineChart.setHorizontalScrollBarEnabled(true);
                        }
                        lineChart.getAxisRight().setDrawAxisLine(false);
                        lineChart.getAxisRight().setDrawLabels(false);
                        lineChart.setScaleEnabled(false);

                        lineChart.setHorizontalScrollBarEnabled(true);
                        lineChart.setHorizontalScrollBarEnabled(true);
                        lineChart.getDescription().setEnabled(false);
                        lineChart.getLegend().setEnabled(false);
                        lineChart.invalidate();

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
            f = 0;
        }
        else{
            linedataset.setValues(getDataValues());
            linedataset.setDrawFilled(true);
            linedataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSets.clear();
            dataSets.add(linedataset);
            lineData = new LineData(dataSets);

            lineChart.clear();
            lineChart.setData(lineData);
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new MyXAxisValueFormatter());
            xAxis.setDrawLabels(true);
            xAxis.setDrawAxisLine(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            if(res.getCount() < 8){
                xAxis.setLabelCount(res.getCount(), true);
                lineChart.setVisibleXRangeMaximum(res.getCount());
                xAxis.setSpaceMax(0.05f);
            }
            else{
                xAxis.setLabelCount(8, true);
                lineChart.setVisibleXRangeMaximum(7);
                lineChart.setHorizontalScrollBarEnabled(true);
            }
            lineChart.getAxisRight().setDrawAxisLine(false);
            lineChart.getAxisRight().setDrawLabels(false);
            lineChart.setScaleEnabled(false);

            lineChart.getDescription().setEnabled(false);
            lineChart.getLegend().setEnabled(false);
            lineChart.invalidate();

        }

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

    /** 내부 DB에 저장된 데이터를 날짜순으로 읽고 저장하여 그래프에 대입하기 위한 메소드 **/
    private ArrayList<Entry> getDataValues(){
        ArrayList<Entry> dataVals = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from DP_table order by DATE ASC", null);
        long date;
        float f;

        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToNext();
            date = cursor.getLong(1);
            f = cursor.getFloat(2);
            dataVals.add(new Entry(TimeUnit.MILLISECONDS.toDays((long)date), f));
        }

        return dataVals;
    }
}
