package com.example.gutfoodwit.Dao;

import android.util.Log;

import com.example.gutfoodwit.bean.CommodityInfo;
import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommodityDao {
    public static List<CommodityInfo> getAllCommodity(int id) {
        List<CommodityInfo> commodityInfoList = new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据commodity_info表的id字段来查询
            String sql = "select * from commodity_info where id=? ";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getAllCommodity_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                // 设置上面的sql语句中的？的值
                // 执行sql查询语句并返回结果集
                ps.setInt(1, id);
                rs = ps.executeQuery();
                if (rs != null) {
                    /*int count = rs.getMetaData().getColumnCount();
                    Log.e("DBUtils", "列总数：" + count);*/
                    while (rs.next()) {
                        // 注意：下标是从1开始的
                        CommodityInfo commodityInfo = new CommodityInfo();
                        int id1 = rs.getInt(1);
                        int commodity_id=rs.getInt(2);
                        String commodity_type = rs.getString(3);
                        String commodity_name = rs.getString(4);
                        double commodity_price= rs.getDouble(5);
                        int commodity_sales= rs.getInt(6);
                        String commodity_img= rs.getString(7);
                        String commodity_content=rs.getString(8);
                        commodityInfo.setId(id1);
                        commodityInfo.setCommodity_id(commodity_id);
                        commodityInfo.setCommodity_type(commodity_type);
                        commodityInfo.setCommodity_name(commodity_name);
                        commodityInfo.setCommodity_price(commodity_price);
                        commodityInfo.setCommodity_sales(commodity_sales);
                        commodityInfo.setCommodity_img(commodity_img);
                        commodityInfo.setCommodity_content(commodity_content);
                        commodityInfoList.add(commodityInfo);
                    }
                }
                return commodityInfoList;
            } else return null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
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

    public int getShopIdByCommodityId(int commodity_id){
        int result = 0;
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT id FROM commodity_info WHERE commodity_id = ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, commodity_id);
            rs = ps.executeQuery();
            while (rs.next()){
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
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
        return 0;
    }
}
