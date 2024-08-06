package com.example.gutfoodwit.bean;

import java.util.Date;

public class UserLottery {

    int id;
    String userTel;
    Date time;

    public UserLottery() {
    }

    public UserLottery(int id, String userTel, Date time) {
        this.id = id;
        this.userTel = userTel;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
