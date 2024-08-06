package com.example.gutfoodwit.Dao;

import android.util.Log;


import com.example.gutfoodwit.bean.UserTrack;
import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserTrackDao {

    public List<UserTrack> getAllTrackInfo(String userTel){
        List<UserTrack> resultList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from user_track where userTel = ? ORDER BY id DESC";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            rs = ps.executeQuery();
            while (rs.next()){
                int id = rs.getInt(1);
                int shopId = rs.getInt(3);
                List<String> shopInfoList = getShopInfo(shopId);

                UserTrack userTrack = new UserTrack();

                userTrack.setId(id);
                userTrack.setShopId(shopId);

                userTrack.setUserTel(userTel);
                userTrack.setShopName(shopInfoList.get(0));
                userTrack.setShopIcon(shopInfoList.get(1));
                userTrack.setShopStar(shopInfoList.get(2));
                userTrack.setShopSale(Integer.valueOf(shopInfoList.get(3)));
                userTrack.setShopAverage(Integer.valueOf(shopInfoList.get(4)));

                resultList.add(userTrack);
            }
            return resultList;
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

    public void insertTrack(int shop_id,String userTel){
        int condition = trackNum(userTel) == 10 ? 1 : 0;
            if(condition == 1){
    //          说明已经10条了，就需要删除一条再插入
                boolean flag = judgeExist(shop_id, userTel);
                if(flag == true){
    //              说明当前访问的是在足迹库中存在的，那就删除这条再重新插入
                    deleteTrackRecord(shop_id, userTel);
                    insertTrackRecord(shop_id, userTel);
                }
                else {
    //              说明当前访问在足迹库中不存在，删除最旧的一条，插入新的
                    int oldRecordId = checkOld(userTel);
                    deleteOldRecord(oldRecordId);
                    insertTrackRecord(shop_id,userTel);
                }
            }
            else {
    //          说明没有满10条
                boolean flag = judgeExist(shop_id, userTel);
                if(flag == true){
    //              说明当前访问的是在足迹库中存在的，那就删除这条再重新插入
                    deleteTrackRecord(shop_id, userTel);
                    insertTrackRecord(shop_id, userTel);
                }
                else{
    //              说明当前访问在足迹库中不存在，就直接插入新的
                    insertTrackRecord(shop_id,userTel);
                }
            }
    }

    public int trackNum(String userTel){
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(id) from user_track where userTel = ?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        int result = -1;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,userTel);
            rs = ps.executeQuery();
            while (rs.next()){
                result = rs.getInt(1);
            }
            return result;
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
        return result;
    }

    public boolean judgeExist(int shop_id,String userTel){
        boolean flag = false;
        String sql = "select * from user_track " +
                "where userTel= ? " +
                "and ? IN " +
                "(SELECT DISTINCT shopId from user_track where userTel = ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            ps.setInt(2, shop_id);
            ps.setString(3, userTel);
            rs = ps.executeQuery();
            while (rs.next()){
                flag = true;
                break;
            }
            return flag;
        } catch (SQLException throwables){
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
        return flag;
    }

    public void deleteTrackRecord(int shop_id,String userTel){
        String sql = "delete from user_track where userTel = ? and shopId = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,userTel);
            ps.setInt(2,shop_id);
            ps.executeUpdate();
        } catch (SQLException throwables){
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
    }

    public void insertTrackRecord(int shop_id,String userTel){
        String sql = "insert into user_track(userTel,shopId) VALUES(?,?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,userTel);
            ps.setInt(2,shop_id);
            ps.executeUpdate();
        } catch (SQLException throwables){
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
    }

    public int checkOld(String userTel){
        int result = 0;
        String sql = "SELECT min(id) from user_track where userTel = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,userTel);
            rs = ps.executeQuery();
            while (rs.next()){
                result = rs.getInt(1);
            }
            return result;
        } catch (SQLException throwables){
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
        return result;
    }

    public void deleteOldRecord(int id){
        String sql = "delete from user_track where id = ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException throwables){
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
    }

}
