package com.example.gutfoodwit.Dao;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.gutfoodwit.bean.SearchInfo;
import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchDao {
    public static List<SearchInfo> getAllSearchByCommodityName(String search) {
        List<SearchInfo> searchInfoList = new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql1 = "select commodity_img,commodity_name,commodity_price,commodity_sales,shop_icon,shop_name,star_size,monthly_sales,average_price from commodity_info,shop_info where commodity_info.id=shop_info.id and commodity_name like '%"+search+"%'";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                //Log.e("getAllSearch_conn", "不为NULL");
                ps = connection.prepareStatement(sql1);
                // 执行sql查询语句并返回结果集
                rs = ps.executeQuery();
                while (rs.next()) {
                    // 注意：下标是从1开始的
                    SearchInfo searchInfo= new SearchInfo();
                    String commodity_img=rs.getString(1);//商品图片路径
                    String commodity_name=rs.getString(2);//类型名称
                    int commodity_price=rs.getInt(3);//价钱
                    int commodity_sales=rs.getInt(4);//销量
                    String shop_icon=rs.getString(5);//店铺照片路径
                    String shop_name=rs.getString(6);//店铺名字
                    String star_size=rs.getString(7);//评分
                    int monthly_sales=rs.getInt(8);//月售
                    int average_price=rs.getInt(9);//人均花费

                    searchInfo.setCommodity_img(commodity_img);
                    searchInfo.setCommodity_name(commodity_name);
                    searchInfo.setCommodity_price(commodity_price);
                    searchInfo.setCommodity_sales(commodity_sales);
                    searchInfo.setShop_icon(shop_icon);
                    searchInfo.setShop_name(shop_name);
                    searchInfo.setStar_size(star_size);
                    searchInfo.setMonthly_sales(monthly_sales);
                    searchInfo.setAverage_price(average_price);
                    searchInfoList.add(searchInfo);
                }
                return searchInfoList;
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

    public static List<SearchInfo> getAllSearchByCommodityNameOrderByStar(String search) {
        List<SearchInfo> searchInfoList = new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql1 = "select commodity_img,commodity_name,commodity_price,commodity_sales,shop_icon,shop_name,star_size,monthly_sales,average_price from commodity_info,shop_info where commodity_info.id=shop_info.id and commodity_name like '%"+search+"%' ORDER BY star_size DESC";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                //Log.e("getAllSearch_conn", "不为NULL");
                ps = connection.prepareStatement(sql1);
                // 执行sql查询语句并返回结果集
                rs = ps.executeQuery();
                while (rs.next()) {
                    // 注意：下标是从1开始的
                    SearchInfo searchInfo= new SearchInfo();
                    String commodity_img=rs.getString(1);//商品图片路径
                    String commodity_name=rs.getString(2);//类型名称
                    int commodity_price=rs.getInt(3);//价钱
                    int commodity_sales=rs.getInt(4);//销量
                    String shop_icon=rs.getString(5);//店铺照片路径
                    String shop_name=rs.getString(6);//店铺名字
                    String star_size=rs.getString(7);//评分
                    int monthly_sales=rs.getInt(8);//月售
                    int average_price=rs.getInt(9);//人均花费

                    searchInfo.setCommodity_img(commodity_img);
                    searchInfo.setCommodity_name(commodity_name);
                    searchInfo.setCommodity_price(commodity_price);
                    searchInfo.setCommodity_sales(commodity_sales);
                    searchInfo.setShop_icon(shop_icon);
                    searchInfo.setShop_name(shop_name);
                    searchInfo.setStar_size(star_size);
                    searchInfo.setMonthly_sales(monthly_sales);
                    searchInfo.setAverage_price(average_price);
                    searchInfoList.add(searchInfo);
                }
                return searchInfoList;
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

    public static List<SearchInfo> getAllSearchByCommodityNameOrderBySales(String search) {
        List<SearchInfo> searchInfoList = new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql1 = "select commodity_img,commodity_name,commodity_price,commodity_sales,shop_icon,shop_name,star_size,monthly_sales,average_price from commodity_info,shop_info where commodity_info.id=shop_info.id and commodity_name like '%"+search+"%' ORDER BY commodity_sales DESC";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                //Log.e("getAllSearch_conn", "不为NULL");
                ps = connection.prepareStatement(sql1);
                // 执行sql查询语句并返回结果集
                rs = ps.executeQuery();
                while (rs.next()) {
                    // 注意：下标是从1开始的
                    SearchInfo searchInfo= new SearchInfo();
                    String commodity_img=rs.getString(1);//商品图片路径
                    String commodity_name=rs.getString(2);//类型名称
                    int commodity_price=rs.getInt(3);//价钱
                    int commodity_sales=rs.getInt(4);//销量
                    String shop_icon=rs.getString(5);//店铺照片路径
                    String shop_name=rs.getString(6);//店铺名字
                    String star_size=rs.getString(7);//评分
                    int monthly_sales=rs.getInt(8);//月售
                    int average_price=rs.getInt(9);//人均花费

                    searchInfo.setCommodity_img(commodity_img);
                    searchInfo.setCommodity_name(commodity_name);
                    searchInfo.setCommodity_price(commodity_price);
                    searchInfo.setCommodity_sales(commodity_sales);
                    searchInfo.setShop_icon(shop_icon);
                    searchInfo.setShop_name(shop_name);
                    searchInfo.setStar_size(star_size);
                    searchInfo.setMonthly_sales(monthly_sales);
                    searchInfo.setAverage_price(average_price);
                    searchInfoList.add(searchInfo);
                }
                return searchInfoList;
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

    public static List<SearchInfo> getAllSearchByCommodityNameOrderByAvg(String search) {
        List<SearchInfo> searchInfoList = new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql1 = "select commodity_img,commodity_name,commodity_price,commodity_sales,shop_icon,shop_name,star_size,monthly_sales,average_price from commodity_info,shop_info where commodity_info.id=shop_info.id and commodity_name like '%"+search+"%' ORDER BY average_price DESC";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                //Log.e("getAllSearch_conn", "不为NULL");
                ps = connection.prepareStatement(sql1);
                // 执行sql查询语句并返回结果集
                rs = ps.executeQuery();
                while (rs.next()) {
                    // 注意：下标是从1开始的
                    SearchInfo searchInfo= new SearchInfo();
                    String commodity_img=rs.getString(1);//商品图片路径
                    String commodity_name=rs.getString(2);//类型名称
                    int commodity_price=rs.getInt(3);//价钱
                    int commodity_sales=rs.getInt(4);//销量
                    String shop_icon=rs.getString(5);//店铺照片路径
                    String shop_name=rs.getString(6);//店铺名字
                    String star_size=rs.getString(7);//评分
                    int monthly_sales=rs.getInt(8);//月售
                    int average_price=rs.getInt(9);//人均花费

                    searchInfo.setCommodity_img(commodity_img);
                    searchInfo.setCommodity_name(commodity_name);
                    searchInfo.setCommodity_price(commodity_price);
                    searchInfo.setCommodity_sales(commodity_sales);
                    searchInfo.setShop_icon(shop_icon);
                    searchInfo.setShop_name(shop_name);
                    searchInfo.setStar_size(star_size);
                    searchInfo.setMonthly_sales(monthly_sales);
                    searchInfo.setAverage_price(average_price);
                    searchInfoList.add(searchInfo);
                }
                return searchInfoList;
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


}
