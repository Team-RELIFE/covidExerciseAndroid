package com.example.exercise_android1.TodaySchedules;

public class TodayScheduleItems {

    String title;
    String alarm;

    public TodayScheduleItems(String title, String alarm){
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
