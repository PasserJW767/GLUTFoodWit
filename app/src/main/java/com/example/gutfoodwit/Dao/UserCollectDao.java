package com.example.gutfoodwit.Dao;

import android.util.Log;

import com.example.gutfoodwit.bean.UserCollect;
import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserCollectDao {

    public List<UserCollect> getUserCollectShop(String userTel){
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<UserCollect> userCollects = new ArrayList<>();
        String sql = "select * from user_collect where userTel = ?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,userTel);
            rs = ps.executeQuery();
            while (rs.next()){
                int id = rs.getInt(1);
                int shopId = rs.getInt(3);
                List<String> shopInfoList = getShopInfo(shopId);

                UserCollect userCollect = new UserCollect();
                userCollect.setId(id);
                userCollect.setShopId(shopId);

                userCollect.setUserTel(userTel);
                userCollect.setShopName(shopInfoList.get(0));
                userCollect.setShopIcon(shopInfoList.get(1));
                userCollect.setShopStar(shopInfoList.get(2));
                userCollect.setShopSale(Integer.valueOf(shopInfoList.get(3)));
                userCollect.setShopAverage(Integer.valueOf(shopInfoList.get(4)));

                userCollects.add(userCollect);
            }
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
        if(userCollects != null){
            return userCollects;
        } else
            return null;
    }

    private List<String> getShopInfo(int shopId){
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> list = null;
        String sql = "select * from shop_info where id = ?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,shopId);
            rs = ps.executeQuery();
            while (rs.next()){
                list = new ArrayList<>();
                String shopName = rs.getString(2);
                String shopIcon = rs.getString(4);
                String starSize = rs.getString(5);
                int monthlySales = rs.getInt(6);
                int averagePrice = rs.getInt(7);
                list.add(shopName);
                list.add(shopIcon);
                list.add(String.valueOf(starSize));
                list.add(String.valueOf(monthlySales));
                list.add(String.valueOf(averagePrice));
            }
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
        if(list == null){
            return null;
        } else
            return list;
    }

    public boolean deleteCollectShop(String userTel,int shopId){
        PreparedStatement ps = null;
        String sql = "delete from user_collect where userTel=? and shopId=?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            ps.setInt(2,shopId);
            result = ps.executeUpdate();
            Log.e("result", String.valueOf(result));
            if(result == 1){
                return true;
            }
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
        return false;
    }

    public boolean judgeCollect(int shopId,String userTel) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM user_collect WHERE userTel = ? AND shopId = ?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            ps.setInt(2,shopId);
            rs = ps.executeQuery();
            while (rs.next()) {
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
        }
        return false;
    }

    public boolean insertCollectShop(String user_tel,int id){
        PreparedStatement ps = null;
        String sql = "INSERT INTO user_collect(userTel,shopId) VALUES(?,?)";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, user_tel);
            ps.setInt(2,id);
            result = ps.executeUpdate();
            if(result == 1){
                return true;
            }
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
        return false;
    }


}
