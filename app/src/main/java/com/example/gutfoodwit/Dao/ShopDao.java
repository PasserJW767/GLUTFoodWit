package com.example.gutfoodwit.Dao;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.gutfoodwit.bean.ShopInfo;
import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopDao {
    public static List<ShopInfo> getAllShop(int canteen_id) {
        List<ShopInfo> shopInfoList = new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据shop_info表的canteen_id字段来查询
            String sql = "select * from shop_info where canteen_id=? ORDER BY level DESC ";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getAllShop_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                // 设置上面的sql语句中的？的值
                // 执行sql查询语句并返回结果集
                ps.setInt(1, canteen_id);
                rs = ps.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        // 注意：下标是从1开始的
                        ShopInfo shopInfo = new ShopInfo();
                        int id = rs.getInt(1);
                        String shop_name = rs.getString(2);
                        String shop_icon = rs.getString(4);
                        String star_size= rs.getString(5);
                        int monthly_sales= rs.getInt(6);
                        int average_price= rs.getInt(7);
                        String shop_describe=rs.getString(8);
                        shopInfo.setId(id);
                        shopInfo.setShop_name(shop_name);
                        shopInfo.setStar_size(star_size);
                        shopInfo.setMonthly_sales(monthly_sales);
                        shopInfo.setAverage_price(average_price);
                        shopInfo.setShop_describe(shop_describe);
                        shopInfo.setShop_icon(shop_icon);
                        shopInfoList.add(shopInfo);
                    }
                }
                return shopInfoList;
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

    @SuppressLint("LongLogTag")
    public static List<ShopInfo> getAllShopOrderByStar(int canteen_id) {
        List<ShopInfo> shopInfoList = new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据shop_info表的canteen_id字段来查询
            String sql = "select * from shop_info where canteen_id=? order by level DESC,star_size desc";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getAllShopOrderByStar_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                // 设置上面的sql语句中的？的值
                // 执行sql查询语句并返回结果集
                ps.setInt(1, canteen_id);
                rs = ps.executeQuery();
                if (rs != null) {
                    /*int count = rs.getMetaData().getColumnCount();
                    Log.e("DBUtils", "列总数：" + count);*/
                    while (rs.next()) {
                        // 注意：下标是从1开始的
                        ShopInfo shopInfo = new ShopInfo();
                        int id = rs.getInt(1);
                        String shop_name = rs.getString(2);
                        String shop_icon = rs.getString(4);
                        String star_size= rs.getString(5);
                        int monthly_sales= rs.getInt(6);
                        int average_price= rs.getInt(7);
                        String shop_describe=rs.getString(8);
                        shopInfo.setId(id);
                        shopInfo.setShop_name(shop_name);
                        shopInfo.setStar_size(star_size);
                        shopInfo.setMonthly_sales(monthly_sales);
                        shopInfo.setAverage_price(average_price);
                        shopInfo.setShop_describe(shop_describe);
                        shopInfo.setShop_icon(shop_icon);
                        shopInfoList.add(shopInfo);
                    }
                }
                return shopInfoList;
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

    @SuppressLint("LongLogTag")
    public static List<ShopInfo> getAllShopOrderBySale(int canteen_id) {
        List<ShopInfo> shopInfoList = new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据shop_info表的canteen_id字段来查询
            String sql = "select * from shop_info where canteen_id=? order by level DESC,monthly_sales  desc";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getAllShopOrderBySale_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                // 设置上面的sql语句中的？的值
                // 执行sql查询语句并返回结果集
                ps.setInt(1, canteen_id);
                rs = ps.executeQuery();
                if (rs != null) {
                    /*int count = rs.getMetaData().getColumnCount();
                    Log.e("DBUtils", "列总数：" + count);*/
                    while (rs.next()) {
                        // 注意：下标是从1开始的
                        ShopInfo shopInfo = new ShopInfo();
                        int id = rs.getInt(1);
                        String shop_name = rs.getString(2);
                        String shop_icon = rs.getString(4);
                        String star_size= rs.getString(5);
                        int monthly_sales= rs.getInt(6);
                        int average_price= rs.getInt(7);
                        String shop_describe=rs.getString(8);
                        shopInfo.setId(id);
                        shopInfo.setShop_name(shop_name);
                        shopInfo.setStar_size(star_size);
                        shopInfo.setMonthly_sales(monthly_sales);
                        shopInfo.setAverage_price(average_price);
                        shopInfo.setShop_describe(shop_describe);
                        shopInfo.setShop_icon(shop_icon);
                        shopInfoList.add(shopInfo);
                    }
                }
                return shopInfoList;
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

    @SuppressLint("LongLogTag")
    public static List<ShopInfo> getAllShopOrderByAvg(int canteen_id) {
        List<ShopInfo> shopInfoList = new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据shop_info表的canteen_id字段来查询
            String sql = "select * from shop_info where canteen_id=? order by level DESC,average_price";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("aagetAllShopOrderByAvg_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                // 设置上面的sql语句中的？的值
                // 执行sql查询语句并返回结果集
                ps.setInt(1, canteen_id);
                rs = ps.executeQuery();
                if (rs != null) {
                    /*int count = rs.getMetaData().getColumnCount();
                    Log.e("DBUtils", "列总数：" + count);*/
                    while (rs.next()) {
                        // 注意：下标是从1开始的
                        ShopInfo shopInfo = new ShopInfo();
                        int id = rs.getInt(1);
                        String shop_name = rs.getString(2);
                        String shop_icon = rs.getString(4);
                        String star_size= rs.getString(5);
                        int monthly_sales= rs.getInt(6);
                        int average_price= rs.getInt(7);
                        String shop_describe=rs.getString(8);
                        shopInfo.setId(id);
                        shopInfo.setShop_name(shop_name);
                        shopInfo.setStar_size(star_size);
                        shopInfo.setMonthly_sales(monthly_sales);
                        shopInfo.setAverage_price(average_price);
                        shopInfo.setShop_describe(shop_describe);
                        shopInfo.setShop_icon(shop_icon);
                        shopInfoList.add(shopInfo);
                    }
                }
                return shopInfoList;
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

    @SuppressLint("LongLogTag")
    public static int getShopIdByShopname(String shop_name) {
        ResultSet id = null;
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps=null;
        try{
            String sql = "select id from shop_info where shop_name=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getShopIdByShopname_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                // 设置上面的sql语句中的？的值
                ps.setString(1,shop_name);;
                // 执行sql查询语句并返回结果集
                id = ps.executeQuery();
                while (id.next()){
                    Log.i("id", String.valueOf(id.getInt(1)));
                    return id.getInt(1);
                }
            }
        } catch (Exception e) {
            Log.i("获取店铺id失败", e.getMessage());
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
            if(id != null){
                try {
                    id.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return 0;
    }

    @SuppressLint("LongLogTag")
    public String getShopNameById(int id) {
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps=null;
        ResultSet rs = null;
        try{
            String sql = "select shop_name from shop_info where id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getShopIdByShopname_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                // 设置上面的sql语句中的？的值
                ps.setInt(1,id);;
                // 执行sql查询语句并返回结果集
                rs = ps.executeQuery();
                while (rs.next()){
                    return rs.getString(1);
                }
            }
        } catch (Exception e) {
            Log.i("获取店铺id失败", e.getMessage());
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
        return null;
    }


}
