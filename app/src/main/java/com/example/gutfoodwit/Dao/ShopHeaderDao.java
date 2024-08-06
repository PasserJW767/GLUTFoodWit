package com.example.gutfoodwit.Dao;

import android.util.Log;

import com.example.gutfoodwit.bean.ShopHeader;
import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShopHeaderDao {
    public static ShopHeader getShopHeaderByid(int id){
        ShopHeader shopHeader=new ShopHeader();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql ="select head_icon,shop_name,star_size,monthly_sales,shop_icon,shop_describe from shop_info,shop_header where shop_info.id=shop_header.id and shop_info.id=?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            while (rs.next()){
                shopHeader.setIv_head_icon(rs.getString(1));
                shopHeader.setTv_shopname(rs.getString(2));
                shopHeader.setTv_star_size(rs.getString(3));
                shopHeader.setTv_monthly_sales(rs.getInt(4));
                shopHeader.setIv_shop_icon(rs.getString(5));
                shopHeader.setTv_shop_describe(rs.getString(6));
                return shopHeader;
            }
            return null;
        } catch (SQLException throwables) {
            return null;
        }finally {
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
