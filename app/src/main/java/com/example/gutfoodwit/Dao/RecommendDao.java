package com.example.gutfoodwit.Dao;

import com.example.gutfoodwit.bean.CommodityInfo;
import com.example.gutfoodwit.bean.ShopInfo;
import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecommendDao {

    public List<CommodityInfo> getLoveShopsInfo(String userTel){
        List<CommodityInfo> lovingShopList = new ArrayList<>();
        String sql = "SELECT commodity_info.*,shop_info.shop_name,shop_info.shop_icon,shop_info.star_size " +
                "FROM commodity_info,shop_info " +
                "WHERE commodity_id = ? and shop_info.id = commodity_info.id";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            int i;
            List<Integer> list = getLoveShops(userTel);
            for(i = 0;i < list.size();i++){
                ps = conn.prepareStatement(sql);
                ps.setInt(1,list.get(i));
                rs = ps.executeQuery();
                while (rs.next()){
                    CommodityInfo commodityInfo = new CommodityInfo();
                    commodityInfo.setId(rs.getInt(1));
                    commodityInfo.setCommodity_id(rs.getInt(2));
                    commodityInfo.setCommodity_name(rs.getString(4));
                    commodityInfo.setCommodity_price(rs.getDouble(5));
                    commodityInfo.setCommodity_sales(rs.getInt(6));
                    commodityInfo.setCommodity_img(rs.getString(7));
                    commodityInfo.setShop_name(rs.getString(9));
                    commodityInfo.setShop_img(rs.getString(10));
                    commodityInfo.setShop_starSize(rs.getString(11));
                    lovingShopList.add(commodityInfo);
                }
            }
            return lovingShopList;
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

    private List<Integer> getLoveShops(String userTel){
        List<Integer> lovingShopIdList = new ArrayList<>();
        String sql = "SELECT commodity_id,COUNT(commodity_id) " +
                "FROM (SELECT DISTINCT order_id,commodity_id FROM order_info WHERE userTel = ?) tb1 " +
                "GROUP BY commodity_id " +
                "ORDER BY COUNT(commodity_id) DESC ";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            rs = ps.executeQuery();
            while (rs.next() && lovingShopIdList.size() < 5){
                lovingShopIdList.add(rs.getInt(1));
            }
            return lovingShopIdList;
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

    public List<ShopInfo> getRecommendAccrodingTimeShop(String time){
        List<ShopInfo> lovingShopList = new ArrayList<>();
        String sql = "SELECT shop_info.id,shop_info.shop_icon,shop_info.shop_name " +
                "FROM shop_recommend,shop_info " +
                "WHERE shop_time = ? and shop_recommend.id = shop_info.id " +
                "ORDER BY shop_info.monthly_sales DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1,time);
            rs = ps.executeQuery();
            while (rs.next()){
                ShopInfo shopInfo = new ShopInfo();
                shopInfo.setId(rs.getInt(1));
                shopInfo.setShop_icon(rs.getString(2));
                shopInfo.setShop_name(rs.getString(3));
                lovingShopList.add(shopInfo);
            }
            return lovingShopList;
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

    public List<ShopInfo> getTypeShopsBig(String s){
        List<ShopInfo> shopInfoList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "SELECT shop_info.id,shop_info.shop_name,shop_info.star_size,shop_info.average_price,shop_recommend.shop_type,shop_recommend.shop_detail_type,shop_info.shop_icon,shop_info.monthly_sales " +
                "FROM shop_info,shop_recommend " +
                "WHERE shop_info.id = shop_recommend.id and shop_type = ?";
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, s);
            rs = ps.executeQuery();
            while (rs.next()){
                ShopInfo shopInfo = new ShopInfo();
                shopInfo.setId(rs.getInt(1));
                shopInfo.setShop_name(rs.getString(2));
                shopInfo.setStar_size(rs.getString(3));
                shopInfo.setAverage_price(rs.getInt(4));
                shopInfo.setShop_type(rs.getString(5));
                shopInfo.setShop_detail_type(rs.getString(6));
                shopInfo.setShop_icon(rs.getString(7));
                shopInfo.setMonthly_sales(rs.getInt(8));
                shopInfoList.add(shopInfo);
            }
            return shopInfoList;
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

    public List<ShopInfo> getTypeShopsBigOrderByStar(String s){
        List<ShopInfo> shopInfoList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "SELECT shop_info.id,shop_info.shop_name,shop_info.star_size,shop_info.average_price,shop_recommend.shop_type,shop_recommend.shop_detail_type,shop_info.shop_icon,shop_info.monthly_sales " +
                "FROM shop_info,shop_recommend " +
                "WHERE shop_info.id = shop_recommend.id and shop_type = ? " +
                "ORDER BY shop_info.star_size DESC";
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, s);
            rs = ps.executeQuery();
            while (rs.next()){
                ShopInfo shopInfo = new ShopInfo();
                shopInfo.setId(rs.getInt(1));
                shopInfo.setShop_name(rs.getString(2));
                shopInfo.setStar_size(rs.getString(3));
                shopInfo.setAverage_price(rs.getInt(4));
                shopInfo.setShop_type(rs.getString(5));
                shopInfo.setShop_detail_type(rs.getString(6));
                shopInfo.setShop_icon(rs.getString(7));
                shopInfo.setMonthly_sales(rs.getInt(8));
                shopInfoList.add(shopInfo);
            }
            return shopInfoList;
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

    public List<ShopInfo> getTypeShopsBigOrderBySales(String s){
        List<ShopInfo> shopInfoList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "SELECT shop_info.id,shop_info.shop_name,shop_info.star_size,shop_info.average_price,shop_recommend.shop_type,shop_recommend.shop_detail_type,shop_info.shop_icon,shop_info.monthly_sales " +
                "FROM shop_info,shop_recommend " +
                "WHERE shop_info.id = shop_recommend.id and shop_type = ? " +
                "ORDER BY shop_info.monthly_sales DESC";
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, s);
            rs = ps.executeQuery();
            while (rs.next()){
                ShopInfo shopInfo = new ShopInfo();
                shopInfo.setId(rs.getInt(1));
                shopInfo.setShop_name(rs.getString(2));
                shopInfo.setStar_size(rs.getString(3));
                shopInfo.setAverage_price(rs.getInt(4));
                shopInfo.setShop_type(rs.getString(5));
                shopInfo.setShop_detail_type(rs.getString(6));
                shopInfo.setShop_icon(rs.getString(7));
                shopInfo.setMonthly_sales(rs.getInt(8));
                shopInfoList.add(shopInfo);
            }
            return shopInfoList;
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

    public List<ShopInfo> getTypeShopsBigOrderByAvg(String s){
        List<ShopInfo> shopInfoList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "SELECT shop_info.id,shop_info.shop_name,shop_info.star_size,shop_info.average_price,shop_recommend.shop_type,shop_recommend.shop_detail_type,shop_info.shop_icon,shop_info.monthly_sales " +
                "FROM shop_info,shop_recommend " +
                "WHERE shop_info.id = shop_recommend.id and shop_type = ? " +
                "ORDER BY shop_info.average_price DESC";
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, s);
            rs = ps.executeQuery();
            while (rs.next()){
                ShopInfo shopInfo = new ShopInfo();
                shopInfo.setId(rs.getInt(1));
                shopInfo.setShop_name(rs.getString(2));
                shopInfo.setStar_size(rs.getString(3));
                shopInfo.setAverage_price(rs.getInt(4));
                shopInfo.setShop_type(rs.getString(5));
                shopInfo.setShop_detail_type(rs.getString(6));
                shopInfo.setShop_icon(rs.getString(7));
                shopInfo.setMonthly_sales(rs.getInt(8));
                shopInfoList.add(shopInfo);
            }
            return shopInfoList;
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

    public List<ShopInfo> getTypeShopsSmall(String s){
        List<ShopInfo> shopInfoList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "SELECT shop_info.id,shop_info.shop_name,shop_info.star_size,shop_info.average_price,shop_recommend.shop_type,shop_recommend.shop_detail_type,shop_info.shop_icon,shop_info.monthly_sales " +
                "FROM shop_info,shop_recommend " +
                "WHERE shop_info.id = shop_recommend.id and shop_detail_type = ?";
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, s);
            rs = ps.executeQuery();
            while (rs.next()){
                ShopInfo shopInfo = new ShopInfo();
                shopInfo.setId(rs.getInt(1));
                shopInfo.setShop_name(rs.getString(2));
                shopInfo.setStar_size(rs.getString(3));
                shopInfo.setAverage_price(rs.getInt(4));
                shopInfo.setShop_type(rs.getString(5));
                shopInfo.setShop_detail_type(rs.getString(6));
                shopInfo.setShop_icon(rs.getString(7));
                shopInfo.setMonthly_sales(rs.getInt(8));
                shopInfoList.add(shopInfo);
            }
            return shopInfoList;
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

    public List<ShopInfo> getTypeShopsSmallOrderByStar(String s){
        List<ShopInfo> shopInfoList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "SELECT shop_info.id,shop_info.shop_name,shop_info.star_size,shop_info.average_price,shop_recommend.shop_type,shop_recommend.shop_detail_type,shop_info.shop_icon,shop_info.monthly_sales " +
                "FROM shop_info,shop_recommend " +
                "WHERE shop_info.id = shop_recommend.id and shop_detail_type = ? " +
                "ORDER BY shop_info.star_size DESC";
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, s);
            rs = ps.executeQuery();
            while (rs.next()){
                ShopInfo shopInfo = new ShopInfo();
                shopInfo.setId(rs.getInt(1));
                shopInfo.setShop_name(rs.getString(2));
                shopInfo.setStar_size(rs.getString(3));
                shopInfo.setAverage_price(rs.getInt(4));
                shopInfo.setShop_type(rs.getString(5));
                shopInfo.setShop_detail_type(rs.getString(6));
                shopInfo.setShop_icon(rs.getString(7));
                shopInfo.setMonthly_sales(rs.getInt(8));
                shopInfoList.add(shopInfo);
            }
            return shopInfoList;
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

    public List<ShopInfo> getTypeShopsSmallOrderBySales(String s){
        List<ShopInfo> shopInfoList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "SELECT shop_info.id,shop_info.shop_name,shop_info.star_size,shop_info.average_price,shop_recommend.shop_type,shop_recommend.shop_detail_type,shop_info.shop_icon,shop_info.monthly_sales " +
                "FROM shop_info,shop_recommend " +
                "WHERE shop_info.id = shop_recommend.id and shop_detail_type = ? " +
                "ORDER BY shop_info.monthly_sales DESC";
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, s);
            rs = ps.executeQuery();
            while (rs.next()){
                ShopInfo shopInfo = new ShopInfo();
                shopInfo.setId(rs.getInt(1));
                shopInfo.setShop_name(rs.getString(2));
                shopInfo.setStar_size(rs.getString(3));
                shopInfo.setAverage_price(rs.getInt(4));
                shopInfo.setShop_type(rs.getString(5));
                shopInfo.setShop_detail_type(rs.getString(6));
                shopInfo.setShop_icon(rs.getString(7));
                shopInfo.setMonthly_sales(rs.getInt(8));
                shopInfoList.add(shopInfo);
            }
            return shopInfoList;
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

    public List<ShopInfo> getTypeShopsSmallOrderByAvg(String s){
        List<ShopInfo> shopInfoList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "SELECT shop_info.id,shop_info.shop_name,shop_info.star_size,shop_info.average_price,shop_recommend.shop_type,shop_recommend.shop_detail_type,shop_info.shop_icon,shop_info.monthly_sales " +
                "FROM shop_info,shop_recommend " +
                "WHERE shop_info.id = shop_recommend.id and shop_detail_type = ? " +
                "ORDER BY shop_info.average_price DESC";
        try{
            ps = conn.prepareStatement(sql);
            ps.setString(1, s);
            rs = ps.executeQuery();
            while (rs.next()){
                ShopInfo shopInfo = new ShopInfo();
                shopInfo.setId(rs.getInt(1));
                shopInfo.setShop_name(rs.getString(2));
                shopInfo.setStar_size(rs.getString(3));
                shopInfo.setAverage_price(rs.getInt(4));
                shopInfo.setShop_type(rs.getString(5));
                shopInfo.setShop_detail_type(rs.getString(6));
                shopInfo.setShop_icon(rs.getString(7));
                shopInfo.setMonthly_sales(rs.getInt(8));
                shopInfoList.add(shopInfo);
            }
            return shopInfoList;
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
}
