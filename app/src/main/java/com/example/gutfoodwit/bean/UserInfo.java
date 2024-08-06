package com.example.gutfoodwit.bean;
public class UserInfo {
    public long rowid; // 行号
    public int xuhao; // 序号
    public String name; // 姓名
    public String phone; // 手机号
    public String password; // 密码
    public String user_icon;//图片路径
    public String id_number;//身份证号
    public String gender;// 性别
    public UserInfo() {
    }

    public void setRowid(long rowid) {
        this.rowid = rowid;
    }

    public void setXuhao(int xuhao) {
        this.xuhao = xuhao;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getId_number() {
        return id_number;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public long getRowid() {
        return rowid;
    }

    public int getXuhao() {
        return xuhao;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}