package com.example.exercise_android1.trainer_list;

public class Trainer {
    int userAge;
    String userName;



    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Trainer(int userAge, String userName) {
        this.userAge = userAge;
        this.userName = userName;
    }
}