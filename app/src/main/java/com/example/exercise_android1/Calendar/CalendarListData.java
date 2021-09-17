package com.example.exercise_android1.Calendar;

public class CalendarListData {

    String t1; /*scheduleTitle*/
    String t2; /*scheduleContent*/
    String t3; /*timeContent*/

    CalendarListData(String t1,String t2,String t3){
        this.t1=t1;
        this.t2=t2;
        this.t3=t3;
    }

    public String getT1() {
        return t1;
    }

    public String getT2() {
        return t2;
    }

    public String getT3() {
        return t3;
    }

}
