package com.example.exercise_android1.mypage;

import android.graphics.drawable.Drawable;

public class PointItem {

    String point_title;
    String point_content;

    public PointItem(String point_title, String point_content){
        this.point_title = point_title;
        this.point_content = point_content;
    }

    public String getPoint_title() {
        return point_title;
    }

    public void setPoint_title(String point_title) {
        this.point_title = point_title;
    }

    public String getPoint_content() {
        return point_content;
    }

    public void setPoint_content(String point_content) {
        this.point_content = point_content;
    }
}
