package com.example.gutfoodwit.Dao;


import android.util.Log;

import com.example.gutfoodwit.bean.CarCommodities;
import com.example.gutfoodwit.bean.CommodityInfo;
import com.example.gutfoodwit.bean.OrderInfo;
import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class OrderInfoDao {

    public int getAllOrderCount(String userTel){
        int count = -1;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select count(*) " +
                "from " +
                "(SELECT COUNT(order_id) " +
                "FROM order_info " +
                "where userTel = ? " +
                "GROUP BY order_id) tb1";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            rs = ps.executeQuery();
            while (rs.next()){
                count = rs.getInt(1);
            }
            return count;
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
        return count;
    }

    public int getAllOrderCounts(String userTel,String content){
        int count = -1;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select count(*) from " +
                "(SELECT COUNT(order_id) " +
                "FROM order_info,shop_info " +
                "where userTel = ? " +
                "and order_info.shop_id = shop_info.id " +
                "and shop_name like '% "+ content +"%' " +
                "GROUP BY order_id) tb1";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            rs = ps.executeQuery();
            while (rs.next()){
                count = rs.getInt(1);
            }
            return count;
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
        return count;
    }

    public List<OrderInfo> getAllOrder(String userTel){
        List<OrderInfo> orderInfoList = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select DISTINCT order_id,shop_id,order_state,order_createtime,shop_info.shop_name " +
                "from order_info,shop_info " +
                "WHERE userTel = ? and order_info.shop_id = shop_info.id " +
                "ORDER BY order_info.order_createtime DESC";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            rs = ps.executeQuery();
            orderInfoList = new ArrayList<>();
            while (rs.next()){
                OrderInfo orderInfo = new OrderInfo();
                int order_id = rs.getInt(1);
                orderInfo.setOrder_id(order_id);
                orderInfo.setShop_id(rs.getInt(2));
                orderInfo.setOrder_state(rs.getString(3));
                orderInfo.setOrder_createTime(rs.getTimestamp(4));
                orderInfo.setOrder_price(getOrderPrice(order_id));
                orderInfo.setShopName(rs.getString(5));
                orderInfoList.add(orderInfo);
            }
            return orderInfoList;
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
        return orderInfoList;
    }

    public List<OrderInfo> getAllOrders(String userTel,String content){
        List<OrderInfo> orderInfoList = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select DISTINCT order_id,shop_id,order_state,order_createtime,shop_info.shop_name " +
                "from order_info,shop_info WHERE userTel = ? " +
                "and order_info.shop_id = shop_info.id " +
                "and shop_name like '%" + content +"%' " +
                "ORDER BY order_info.order_createtime DESC";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            rs = ps.executeQuery();
            orderInfoList = new ArrayList<>();
            while (rs.next()){
                OrderInfo orderInfo = new OrderInfo();
                int order_id = rs.getInt(1);
                orderInfo.setOrder_id(order_id);
                orderInfo.setShop_id(rs.getInt(2));
                orderInfo.setOrder_state(rs.getString(3));
                orderInfo.setOrder_createTime(rs.getTimestamp(4));
                orderInfo.setOrder_price(getOrderPrice(order_id));
                orderInfo.setShopName(rs.getString(5));
                orderInfoList.add(orderInfo);
            }
            return orderInfoList;
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
        return orderInfoList;
    }

    public double getOrderPrice(int order_id){
        double order_price = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select sum(order_price) from order_info where order_id = ?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, order_id);
            rs = ps.executeQuery();
            while (rs.next()){
                order_price = rs.getDouble(1);
            }
            return order_price;
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
        return order_price;
    }

    public List<CommodityInfo> getOrderDetail(int orderid){
        List<CommodityInfo> commodityInfoList = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select commodity_info.commodity_img,commodity_info.commodity_name " +
                "from commodity_info,order_info " +
                "where order_info.order_id = ? " +
                "and commodity_info.commodity_id = order_info.commodity_id ";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1,orderid);
            rs = ps.executeQuery();
            commodityInfoList = new ArrayList<>();
            while (rs.next()){
                CommodityInfo commodityInfo = new CommodityInfo();
                commodityInfo.setCommodity_img(rs.getString(1));
                commodityInfo.setCommodity_name(rs.getString(2));
                commodityInfoList.add(commodityInfo);
            }
            return commodityInfoList;
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
        return commodityInfoList;
    }

    public List<CommodityInfo> getOrderCommodities(int orderid){
        List<CommodityInfo> commodityInfoList = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT shop_info.id,shop_info.shop_name,order_id,commodity_info.commodity_id,commodity_name,commodity_img,commodity_price,commodity_count " +
                "FROM order_info,commodity_info,shop_info " +
                "where order_id = ? " +
                "and commodity_info.commodity_id = order_info.commodity_id " +
                "and commodity_info.id = shop_info.id";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1,orderid);
            rs = ps.executeQuery();
            commodityInfoList = new ArrayList<>();
            while (rs.next()){
                CommodityInfo commodityInfo = new CommodityInfo();
                commodityInfo.setId(rs.getInt(1));
                commodityInfo.setShop_name(rs.getString(2));
                commodityInfo.setOrder_id(rs.getInt(3));
                commodityInfo.setCommodity_id(rs.getInt(4));
                commodityInfo.setCommodity_name(rs.getString(5));
                commodityInfo.setCommodity_img(rs.getString(6));
                commodityInfo.setCommodity_price(rs.getDouble(7));
                commodityInfo.setCommodity_count(rs.getInt(8));

                commodityInfoList.add(commodityInfo);
            }
            return commodityInfoList;
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
        return commodityInfoList;
    }

    public double getOrderSumPrice(int orderId){
        String sql = "select sum(commodity_price * commodity_count) " +
                "from " +
                "(select commodity_price,commodity_count from commodity_info,order_info " +
                "where order_id = ? and commodity_info.commodity_id = order_info.commodity_id) tb1";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        double result = -1;
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1,orderId);
            rs = ps.executeQuery();
            while (rs.next()){
                result = rs.getDouble(1);
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

    public String getOrderState(int orderId) {
        String sql = "select DISTINCT order_state from order_info where order_id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String result = null;
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1,orderId);
            rs = ps.executeQuery();
            while (rs.next()){
                result = rs.getString(1);
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

    public List<OrderInfo> getUnPayOrder(String userTel){
        List<OrderInfo> orderInfoList = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select DISTINCT order_id,shop_id,order_state,order_createtime,shop_info.shop_name " +
                "from order_info,shop_info " +
                "WHERE userTel = ? and order_info.shop_id = shop_info.id and order_state = '未支付' " +
                "ORDER BY order_info.order_createtime DESC";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            rs = ps.executeQuery();
            orderInfoList = new ArrayList<>();
            while (rs.next()){
                OrderInfo orderInfo = new OrderInfo();
                int order_id = rs.getInt(1);
                orderInfo.setOrder_id(order_id);
                orderInfo.setShop_id(rs.getInt(2));
                orderInfo.setOrder_state(rs.getString(3));
                orderInfo.setOrder_createTime(rs.getTimestamp(4));
                orderInfo.setOrder_price(getOrderPrice(order_id));
                orderInfo.setShopName(rs.getString(5));
                orderInfoList.add(orderInfo);
            }
            return orderInfoList;
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
        return orderInfoList;
    }

    public void updateOrderCondition(int order_id){
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "UPDATE order_info SET order_state='待使用',order_createtime = LOCALTIME() WHERE order_id = ?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, order_id);
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

    public void completeOrderCondition(int order_id){
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "UPDATE order_info SET order_state='已完成' WHERE order_id = ?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, order_id);
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

    public int judgeTime(int order_id){
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from order_info where TIMESTAMPDIFF(MINUTE,order_createtime,LOCALTIME()) > 4320 and order_id = ?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, order_id);
            rs = ps.executeQuery();
            while (rs.next()){
                return 0;
            }
            return 1;
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
        return 1;
    }

    public void orderWaitToBackMoney(int orderId){
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "UPDATE order_info SET order_state='待退款' WHERE order_id = ?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
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

    public int getAllOrderCountWithKey(String userTel,int key){
        int count = -1;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        if(key == 1){
            sql = "select count(*) from " +
                    "(SELECT COUNT(order_id) FROM order_info " +
                    "where userTel = ? AND order_state='待付款' " +
                    "GROUP BY order_id) tb1";
        } else if (key == 2){
            sql = "select count(*) from " +
                    "(SELECT COUNT(order_id) FROM order_info " +
                    "where userTel = ? AND order_state='待使用' " +
                    "GROUP BY order_id) tb1";
        } else if (key == 3){
            sql = "select count(*) from " +
                    "(SELECT COUNT(order_id) FROM order_info " +
                    "where userTel = ? AND order_state='待退款' " +
                    "GROUP BY order_id) tb1";
        } else if (key == 4){
            sql = "select count(*) from " +
                    "(SELECT COUNT(order_id) FROM order_info " +
                    "where userTel = ? AND order_evaluate = 0 " +
                    "GROUP BY order_id) tb1";
        }
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            rs = ps.executeQuery();
            while (rs.next()){
                count = rs.getInt(1);
            }
            return count;
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
        return count;
    }

    public List<OrderInfo> getAllOrderWithKey(String userTel,int key){
        List<OrderInfo> orderInfoList = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        if(key == 1){
            sql = "select DISTINCT order_id,shop_id,order_state,order_createtime,shop_info.shop_name " +
                    "from order_info,shop_info " +
                    "WHERE userTel = ? " +
                    "and order_state='待付款' " +
                    "and order_info.shop_id = shop_info.id " +
                    "ORDER BY order_info.order_createtime DESC";
        } else if(key == 2){
            sql = "select DISTINCT order_id,shop_id,order_state,order_createtime,shop_info.shop_name " +
                    "from order_info,shop_info " +
                    "WHERE userTel = ? " +
                    "and order_state='待使用' " +
                    "and order_info.shop_id = shop_info.id " +
                    "ORDER BY order_info.order_createtime DESC";
        } else if(key == 3){
            sql = "select DISTINCT order_id,shop_id,order_state,order_createtime,shop_info.shop_name " +
                    "from order_info,shop_info " +
                    "WHERE userTel = ? " +
                    "and order_state='待退款' " +
                    "and order_info.shop_id = shop_info.id " +
                    "ORDER BY order_info.order_createtime DESC";
        } else if(key == 4){
            sql = "select DISTINCT order_id,shop_id,order_state,order_createtime,shop_info.shop_name " +
                    "from order_info,shop_info " +
                    "WHERE userTel = ? " +
                    "and order_evaluate = 0 " +
                    "and order_info.shop_id = shop_info.id " +
                    "ORDER BY order_info.order_createtime DESC";
        }
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            rs = ps.executeQuery();
            orderInfoList = new ArrayList<>();
            while (rs.next()){
                OrderInfo orderInfo = new OrderInfo();
                int order_id = rs.getInt(1);
                orderInfo.setOrder_id(order_id);
                orderInfo.setShop_id(rs.getInt(2));
                orderInfo.setOrder_state(rs.getString(3));
                orderInfo.setOrder_createTime(rs.getTimestamp(4));
                orderInfo.setOrder_price(getOrderPrice(order_id));
                orderInfo.setShopName(rs.getString(5));
                orderInfoList.add(orderInfo);
            }
            return orderInfoList;
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
        return orderInfoList;
    }

    public void updateShopPoint(double point, int shopId,int orderId) {
        List<String> pointAndSales = pointAndSales(shopId);
        double prePoint = Double.parseDouble(pointAndSales.get(0));
        double monthly_sales = Double.parseDouble(pointAndSales.get(1));
        double endPoint;
        if(point >= prePoint){
            endPoint = (1 / (monthly_sales + 1)) * point + prePoint;
        } else {
            endPoint = prePoint - (1 / (monthly_sales + 1)) * point;
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        updateEvaluate(orderId);
        String sql = "update shop_info set star_size = ? where id = ?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, String.valueOf(endPoint));
            ps.setInt(2, shopId);
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

    public List<String> pointAndSales(int shopId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select star_size,monthly_sales from shop_info where id = ? ";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, shopId);
            rs = ps.executeQuery();
            while (rs.next()){
                List<String> stringList = new ArrayList<>();
                stringList.add(rs.getString(1));
                stringList.add(String.valueOf(rs.getInt(2)));
                return stringList;
            }
            return null;
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
        return null;
    }

    public void updateEvaluate(int orderId){
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "update order_info set order_evaluate = 1 where order_id = ?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
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

    public int judgePoint(int orderId){
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from order_info where order_evaluate = 0 and order_id = ?";
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            while (rs.next()){
                return 0;
            }
            return 1;
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
        return 1;
    }

    public void insertOrder(int shopId,int[] commodityId,String userTel){
        List<CarCommodities> commodityList = new ShopCarDao().getOrderByShopIdAndCommodityId(shopId, commodityId, userTel);
        PreparedStatement ps = null;
        ResultSet rs = null;
        Log.e("return", "AA");
        String sql = "insert into order_info VALUES(?,?,?,?,LOCALTIME(),?,?,?,?)";
        int orderId = getNextId();
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            ps.setInt(2,shopId);
            ps.setString(6,"待付款");
            ps.setString(7,userTel);
            ps.setInt(8, 0);
            for(int i = 0;i < commodityList.size();i++){
                ps.setInt(3,commodityList.get(i).getCommodity_id());
                ps.setInt(4,commodityList.get(i).getCommodity_num());
                ps.setDouble(5,commodityList.get(i).getCommodity_price() * commodityList.get(i).getCommodity_num());
                ps.executeUpdate();
            }
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

    public int getNextId(){
        String sql = "SELECT max(order_id) from order_info ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                return (rs.getInt(1)+1);
            }
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
        return 0;
    }
}
