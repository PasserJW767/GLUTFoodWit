package com.example.gutfoodwit.Dao;

import android.util.Log;


import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLotteryDao {
    /**
     * 向user_lottery表中插入用户的抽奖数据
     *
     * */
    public void insertIntoTable(String userTel){
        PreparedStatement ps = null;
        Log.e("userTel", userTel);
        String sql = "insert into user_lottery (userTel,time,state) VALUES (?,LOCALTIME(),'未中奖'); ";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            ps.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断用户今日是否已经抽过奖
     * */
    public boolean lotteryOrNot(String userTel){
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from user_lottery where " +
                "userTel = ?" +
                "AND time<LOCALTIME() " +
                "AND time>(select DATE_FORMAT(CURDATE(),'%Y-%m-%d %H:%i:%s'))";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        Log.e("conn", String.valueOf(conn));
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            rs = ps.executeQuery();
            while (rs.next()){
                return true;
            }
            return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return false;
    }
}
