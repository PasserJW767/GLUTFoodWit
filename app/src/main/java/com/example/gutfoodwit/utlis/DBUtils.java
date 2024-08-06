package com.example.gutfoodwit.utlis;

import android.util.Log;

import com.example.gutfoodwit.bean.ShopInfo;

import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库工具类：连接数据库用、获取数据库数据用
 * 相关操作数据库的方法均可写在该类
 */
public class DBUtils {

    private static String driver = "com.mysql.jdbc.Driver";// MySql驱动

//    private static String url = "jdbc:mysql://localhost:3306/map_designer_test_db";

    private static String user = "root";// 用户名

    private static String password = "12345";// 密码

    public static Connection getConn(String dbName){

        Connection connection = null;
        try{
            Class.forName(driver);// 动态加载类
            String ip = "10.105.96.132";// 写成本机地址，不能写成localhost，同时手机和电脑连接的网络必须是同一个
            Log.e("ip", ip);

            // 尝试建立到给定数据库URL的连接
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + dbName/*+"?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone = GMT"*/,
                    user, password);

        }catch (Exception e){
            e.printStackTrace();
        }

        return connection;
    }
}

