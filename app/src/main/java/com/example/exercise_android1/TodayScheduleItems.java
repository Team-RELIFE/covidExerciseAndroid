package com.example.exercise_android1;

public class TodayScheduleItems {

    String title;
    String alarm;

    TodayScheduleItems(String title, String alarm){
        this.title=title;
        this.alarm=alarm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }
}
