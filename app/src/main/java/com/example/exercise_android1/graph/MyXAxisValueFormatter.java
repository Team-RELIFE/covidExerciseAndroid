package com.example.exercise_android1.graph;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MyXAxisValueFormatter extends IndexAxisValueFormatter {

    public String getFormattedXValue(float value) {

        // Convert float value to date string
        // Convert from days back to milliseconds to format time  to show to the user
        long emissionsMilliSince1970Time = TimeUnit.DAYS.toMillis((long)value);
        // Show time in local version
        Date timeMilliseconds = new Date(emissionsMilliSince1970Time);
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM.dd");

        return dateTimeFormat.format(timeMilliseconds);
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // Convert float value to date string
        // Convert from days back to milliseconds to format time  to show to the user
        long emissionsMilliSince1970Time = TimeUnit.DAYS.toMillis((long)value);
        // Show time in local version
        Date timeMilliseconds = new Date(emissionsMilliSince1970Time);
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM.dd");

        return dateTimeFormat.format(timeMilliseconds);
    }
}
