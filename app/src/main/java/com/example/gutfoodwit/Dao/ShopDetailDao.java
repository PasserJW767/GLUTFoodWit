package com.example.gutfoodwit.Dao;

import com.example.gutfoodwit.bean.CommodityInfo;
import com.example.gutfoodwit.bean.ShopInfo;
import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopDetailDao {
    public Map<String,Integer> getShopMap(int id) {
        Map<String,Integer> mapResult=new HashMap<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT commodity_type,count(commodity_type) from commodity_info WHERE id=? GROUP BY commodity_type";
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            while (rs.next()){
                String type = rs.getString(1);
                Integer count = rs.getInt(2);
                mapResult.put(type, count);
            }
            return mapResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(connection != null){
                try {
                    connection.close();
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

    public List<CommodityInfo> getShopDetail(String type, int id) {
        List<CommodityInfo> commodityInfoList = new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * from commodity_info where id=? and commodity_type=?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, type);
            rs = ps.executeQuery();
            while (rs.next()){
                CommodityInfo commodityInfo = new CommodityInfo();
                commodityInfo.setId(rs.getInt(1));
                commodityInfo.setCommodity_id(rs.getInt(2));
                commodityInfo.setCommodity_type(rs.getString(3));
                commodityInfo.setCommodity_name(rs.getString(4));
                commodityInfo.setCommodity_price(rs.getDouble(5));
                commodityInfo.setCommodity_sales(rs.getInt(6));
                commodityInfo.setCommodity_img(rs.getString(7));
                commodityInfo.setCommodity_content(rs.getString(8));
                commodityInfoList.add(commodityInfo);
            }
            return commodityInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(connection != null){
                try {
                    connection.close();
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
