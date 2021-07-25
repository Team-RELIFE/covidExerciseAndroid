package com.example.exercise_android1;

public class User {
    public static String id = null;
    public static String phone;
    public static String name;
    public static double height = 0;
    public static double weight = 0;
    public static double bmi = 0;

    //생성자
    public User() {
    }

    //생성자 (신체 정보 O)
    public User(String id, String phone, String name, double height, double weight, double bmi) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
    }

    //생성자 (신체 정보 X)
    public User(String id, String phone, String name) {
        this.id = id;
        this.phone = phone;
        this.name = name;
    }

    //현재 접속 중인 유저 정보 업데이트 (신체 정보 O)
    public void setCurrentUser(String id, String phone, String name, double height, double weight, double bmi) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
    }

    //현재 접속 중인 유저 정보 업데이트 (신체 정보 X)
    public void setCurrentUser(String id, String phone, String name) {
        this.id = id;
        this.phone = phone;
        this.name = name;
    }

    //현재 접속 중인 유저 정보 얻기
    public User getCurrentUser() {
        User user = new User(this.id, this.phone, this.name, this.height, this.weight, this.bmi);
        return user;
    }
}
