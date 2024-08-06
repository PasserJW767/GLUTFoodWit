package com.example.gutfoodwit.Dao;

import android.util.Log;

import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommodityDetailDao {

    /**
     * 根据商品Id获得商品数据
     * */
    public List<String> getCommodityDetailById(int commodityId){
        List<String> resultList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * " +
                "from commodity_info,commodity_detail " +
                "where commodity_info.commodity_id = commodity_detail.commodity_id " +
                "AND commodity_info.commodity_id = ?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,commodityId);
            rs = ps.executeQuery();
            while (rs.next()){
                resultList.add(0, String.valueOf(rs.getInt(1)));
                resultList.add(1, String.valueOf(rs.getInt(2)));
                resultList.add(2, rs.getString(3));
                resultList.add(3, rs.getString(4));
                resultList.add(4, String.valueOf(rs.getDouble(5)));
                resultList.add(5, String.valueOf(rs.getInt(6)));
                resultList.add(6, rs.getString(7));
                resultList.add(7, rs.getString(8));
                resultList.add(8,rs.getString(10));
                resultList.add(9,rs.getString(11));
                resultList.add(10,rs.getString(12));
            }
            Log.e("resultListLength", String.valueOf(resultList.size()));
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
