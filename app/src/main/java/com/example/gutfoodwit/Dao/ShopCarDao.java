package com.example.gutfoodwit.Dao;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.gutfoodwit.bean.CarCommodities;
import com.example.gutfoodwit.bean.CarShops;
import com.example.gutfoodwit.bean.CommodityInfo;
import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopCarDao {
    public static List<CarShops> getCarShopsByCode(String code){
        List<CarShops> shops=new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据shop_info表的canteen_id字段来查询
            String sql = "SELECT * FROM car_shops where code="+code;
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getCarShopsByCode_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    CarShops shop=new CarShops();
                    shop.setCode(rs.getString(1));
                    shop.setShop_name(rs.getString(2));
                    shop.setShop_id(rs.getInt(3));
                    shops.add(shop);
                }
                return shops;
            }
            return null;
        }catch (Exception e){
            System.err.println("购物车店铺获取失败"+e);
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
        return null;
    }

    @SuppressLint("LongLogTag")
    public static List<CarCommodities> getCarCommoditiesByCode(String code){
        List<CarCommodities> commodities=new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据shop_info表的canteen_id字段来查询
            String sql = "SELECT * FROM car_commodities where code="+code;
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getCarCommoditiesByCode_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    CarCommodities commodity=new CarCommodities();
                    commodity.setCode(rs.getString(1));
                    commodity.setShop_id(rs.getInt(2));
                    commodity.setCommodity_id(rs.getInt(3));
                    commodity.setCommodity_img(rs.getString(4));
                    commodity.setCommodity_name(rs.getString(5));
                    commodity.setCommodity_num(rs.getInt(6));
                    commodity.setCommodity_price(rs.getDouble(7));
                    commodities.add(commodity);
                }
                return commodities;
            }
            return null;
        }catch (Exception e){
            System.err.println("购物车商品获取失败"+e);
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
        return null;
    }

    @SuppressLint("LongLogTag")
    public static List<CarCommodities> getCarCommoditiesByCodeShopId(int shop_id,String code){
        List<CarCommodities> commodities=new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据shop_info表的canteen_id字段来查询
            String sql = "SELECT * FROM car_commodities where code="+code+" and shop_id= "+shop_id;
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getCarCommoditiesByCode_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    CarCommodities commodity=new CarCommodities();
                    commodity.setCode(rs.getString(1));
                    commodity.setShop_id(rs.getInt(2));
                    commodity.setCommodity_id(rs.getInt(3));
                    commodity.setCommodity_img(rs.getString(4));
                    commodity.setCommodity_name(rs.getString(5));
                    commodity.setCommodity_num(rs.getInt(6));
                    commodity.setCommodity_price(rs.getDouble(7));
                    commodities.add(commodity);
                }
                return commodities;
            }
            return null;
        }catch (Exception e){
            System.err.println("购物车商品获取失败"+e);
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
        return null;
    }

    @SuppressLint("LongLogTag")
    public static void deleteCarByShopId(String code, int shop_id){
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM car_shops WHERE CODE=? and shop_id=?";
            String sql2="delete from car_commodities WHERE CODE=? and shop_id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("deleteCarCommodities_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                ps.setString(1,code);
                ps.setInt(2,shop_id);
                ps.executeUpdate();

                ps=connection.prepareStatement(sql2);
                ps.setString(1,code);
                ps.setInt(2,shop_id);
                ps.executeUpdate();
            }
        }catch (Exception e){
            System.err.println("购物车商品删除失败"+e);
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
        }
    }


    @SuppressLint("LongLogTag")
    public static void deleteCarByCommodityId(String code,int commodity_id){
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        try {
            String sql="delete from car_commodities WHERE CODE=? and commodity_id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("deleteCarCommodities_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                ps.setString(1,code);
                ps.setInt(2,commodity_id);
                ps.executeUpdate();
            }
        }catch (Exception e){
            System.err.println("购物车商品删除失败"+e);
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
        }
    }


    @SuppressLint("LongLogTag")
    public static void updataNum(String code,int shop_p,int commodity_id,int num){
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        try {
            String sql="UPDATE car_commodities set commodity_num=? where CODE=? and commodity_id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("updataNum_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                ps.setInt(1,num);
                ps.setString(2,code);
                ps.setInt(3,commodity_id);
                ps.executeUpdate();
            }
        }catch (Exception e){
            System.err.println("购物车商品数量更新失败"+e);
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
        }
    }


    @SuppressLint("LongLogTag")
    public static CarCommodities getCarCommoditiesByCodeShopIdCommodityId(String code,int shop_id,int commodity_id){
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据shop_info表的canteen_id字段来查询
            String sql = "SELECT * FROM car_commodities where code=? and shop_id=? and commodity_id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getCarCommoditiesByCodeShopIdCommodityId_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                ps.setString(1,code);
                ps.setInt(2,shop_id);
                ps.setInt(3,commodity_id);
                rs = ps.executeQuery();
                while (rs.next()) {
                    CarCommodities commodity=new CarCommodities();
                    commodity.setCode(rs.getString(1));
                    commodity.setShop_id(rs.getInt(2));
                    commodity.setCommodity_id(rs.getInt(3));
                    commodity.setCommodity_img(rs.getString(4));
                    commodity.setCommodity_name(rs.getString(5));
                    commodity.setCommodity_num(rs.getInt(6));
                    commodity.setCommodity_price(rs.getDouble(7));
                    return commodity;
                }
                return null;
            }
            return null;
        }catch (Exception e){
            System.err.println("购物车商品获取失败"+e);
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
        return null;
    }

    @SuppressLint("LongLogTag")
    public static void addCommodity(String code,int shop_id,int commodity_id,int num){
        String commodity_img=null;
        String commodity_name=null;
        double commodity_price=0;

        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs=null;
        try {
            String sql1="SELECT commodity_img from commodity_info where commodity_id=?";
            String sql2="SELECT commodity_name from commodity_info where commodity_id=?";
            String sql3="SELECT commodity_price from commodity_info where commodity_id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("addCommodity_conn", "不为NULL");
                ps = connection.prepareStatement(sql1);
                ps.setInt(1,commodity_id);
                rs=ps.executeQuery();
                while (rs.next()){
                    commodity_img=rs.getString(1);
                    break;
                }
                ps = connection.prepareStatement(sql2);
                ps.setInt(1,commodity_id);
                rs=ps.executeQuery();
                while (rs.next()){
                    commodity_name=rs.getString(1);
                    break;
                }
                ps = connection.prepareStatement(sql3);
                ps.setInt(1,commodity_id);
                rs=ps.executeQuery();
                while (rs.next()){
                    commodity_price=rs.getDouble(1);
                    break;
                }

                String sql="INSERT into car_commodities VALUES(?,?,?,?,?,?,?)";
                ps = connection.prepareStatement(sql);
                ps.setString(1,code);
                ps.setInt(2,shop_id);
                ps.setInt(3,commodity_id);
                ps.setString(4,commodity_img);
                ps.setString(5,commodity_name);
                ps.setInt(6,num);
                ps.setDouble(7,commodity_price);
                ps.executeUpdate();
                Log.e("插入商品", commodity_name+"成功！");
            }

        }catch (Exception e){
            System.err.println("购物车商品数量更新失败"+e);
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

    @SuppressLint("LongLogTag")
    public static void addShop(String code,int shop_id) {
        if (!ShopCarDao.judgeCarShops(code, shop_id)) {
            String shop_name = null;
            Connection connection = DBUtils.getConn("gutfoodwitdatabase");
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                String sql1 = "SELECT shop_name from shop_info where id=?";
                if (connection != null) {// connection不为null表示与数据库建立了连接
                    Log.e("addCommodity_conn", "不为NULL");
                    ps = connection.prepareStatement(sql1);
                    ps.setInt(1, shop_id);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        shop_name = rs.getString(1);
                        break;
                    }

                    String sql = "INSERT into car_shops VALUES(?,?,?)";
                    ps = connection.prepareStatement(sql);
                    ps.setString(1, code);
                    ps.setString(2, shop_name);
                    ps.setInt(3, shop_id);
                    ps.executeUpdate();
                }

            } catch (Exception e) {
                System.err.println("购物车店铺添加失败" + e);
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        }
    }

    public static boolean judgeCarShops(String code,int shop_id){
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据shop_info表的canteen_id字段来查询
            String sql = "SELECT * FROM car_shops where code=? and shop_id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("judgeCarShops_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                ps.setString(1,code);
                ps.setInt(2,shop_id);
                rs = ps.executeQuery();
                while (rs.next()) {
                    return true;
                }
                return false;
            }
            return false;
        }catch (Exception e){
            System.err.println("购物车店铺获取失败"+e);
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
        return false;
    }

    @SuppressLint("LongLogTag")
    public static void deleteShop(String code,int shop_id){
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        try {
            String sql = "DELETE FROM car_shops WHERE CODE=? and shop_id=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("deleteShop_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                ps.setString(1,code);
                ps.setInt(2,shop_id);
                ps.executeUpdate();
            }
        }catch (Exception e){
            System.err.println("购物车店铺删除失败"+e);
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
        }
    }

    @SuppressLint("LongLogTag")
    public static int getCommiditiesCount(String code){
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs=null;
        try {
            String sql = "select sum(commodity_num) from car_commodities where code=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getCommiditiesCount_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                ps.setString(1,code);
                rs=ps.executeQuery();
                while (rs.next()){
                    int answer = rs.getInt(1);
                    return answer;
                }
            }
        }catch (Exception e){
            System.err.println("购物车店铺删除失败"+e);
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
        }
        return 0;
    }

    public int getCount(String userTel,int shopId,int commodityId){
        String sql = "SELECT commodity_num FROM car_commodities WHERE code = ? and shop_id = ? and commodity_id = ?";
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs=null;
        try {
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                ps.setString(1,userTel);
                ps.setInt(2,shopId);
                ps.setInt(3, commodityId);
                rs=ps.executeQuery();
                while (rs.next()){
                    return rs.getInt(1);
                }
            }
        }catch (Exception e){
            System.err.println("数据查询失败"+e);
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
        }
        return 0;
    }

    public List<CarCommodities> getOrderByShopIdAndCommodityId(int shopId, int[] commodityId,String userTel) {
        String sql = "SELECT * FROM car_commodities WHERE shop_id = ? AND commodity_id = ? AND code = ?";
        List<CarCommodities> commoditiesList = new ArrayList<>();
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs=null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, shopId);
            ps.setString(3,userTel);
            for(int i = 0;i < commodityId.length;i++){
                ps.setInt(2, commodityId[i]);
                Log.e("shopId", String.valueOf(shopId));
                Log.e("commodityId", String.valueOf(commodityId[i]));
                rs = ps.executeQuery();
                while (rs.next()){
                    CarCommodities carCommodities = new CarCommodities();
                    carCommodities.setCommodity_id(rs.getInt(3));
                    carCommodities.setCommodity_img(rs.getString(4));
                    carCommodities.setCommodity_name(rs.getString(5));
                    carCommodities.setCommodity_num(rs.getInt(6));
                    carCommodities.setCommodity_price(rs.getDouble(7));
                    commoditiesList.add(carCommodities);
                }
            }
            return commoditiesList;
        }catch (Exception e){
            System.err.println("数据查询失败"+e);
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
        }
        return null;
    }

    public double getSumPrice(int shopId,int[] commodities,String userTel){
        double sumPrice = 0;
        String sql = "SELECT commodity_num * commodity_price from car_commodities where shop_id = ? and commodity_id = ? and code = ?";
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs=null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, shopId);
            ps.setString(3,userTel);
            for(int i = 0;i < commodities.length;i++){
                ps.setInt(2, commodities[i]);
                rs = ps.executeQuery();
                while (rs.next()){
                    sumPrice += rs.getDouble(1);
                }
            }
            return sumPrice;
        }catch (Exception e){
            System.err.println("数据查询失败"+e);
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
        }
        return 0;
    }

    public boolean judgeShopHaveCommodities(int shopId,String userTel){
        String sql = "SELECT * from car_commodities where shop_id = ? and code = ?";
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs=null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, shopId);
            ps.setString(2, userTel);
            rs = ps.executeQuery();
            while (rs.next()){
                Log.e("判断","a");
                return true;
            }
        }catch (Exception e){
            System.err.println("数据查询失败"+e);
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
        }
        return false;
    }
}
