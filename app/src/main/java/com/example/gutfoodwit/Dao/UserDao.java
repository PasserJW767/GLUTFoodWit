package com.example.gutfoodwit.Dao;

import android.util.Log;

import com.example.gutfoodwit.bean.UserInfo;
import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public static boolean loginUser(String et_phone,String et_password) {
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据user_info表的phone和password字段来查询
            String sql = "select * from user_info where phone=? and password=? ";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("loginUser_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                // 设置上面的sql语句中的？的值
                ps.setString(1, et_phone);
                ps.setString(2,et_password);
                // 执行sql查询语句并返回结果集
                rs = ps.executeQuery();
                while (rs.next()) {
                    return true;
                }
            } else return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
            return false;
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
        return false;
    }


    public static String findPwdByPhone(String et_phone) {
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据user_info表的phone字段来查询
            String sql = "select password from user_info where phone=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("findPwdByPhone_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                // 设置上面的sql语句中的？的值
                ps.setString(1, et_phone);
                // 执行sql查询语句并返回结果集
                rs = ps.executeQuery();
                while (rs.next()) {
                    return rs.getString(1);
                }
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
        return null;
    }

    public static boolean findUserByPhone(String et_phone) {
        /*UserInfo userInfo=new UserInfo();*/
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据user_info表的phone字段来查询
            String sql = "select * from user_info where phone=? ";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("findUserByPhone_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                // 设置上面的sql语句中的？的值
                ps.setString(1, et_phone);
                // 执行sql查询语句并返回结果集
                rs = ps.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        return true;
                    }
                } return false;
            } else return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
            return false;
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


    public static void addUser(UserInfo userInfo) {
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int id=0;
        String user_name=userInfo.getName();
        String phone=userInfo.getPhone();
        String password=userInfo.getPassword();
        String sql = "insert into user_info VALUES(?,?,?,?,'','','')";
        String sql1="select COUNT(*) from user_info";
        try {
            ps = conn.prepareStatement(sql1);
            rs=ps.executeQuery();
            while (rs.next()){
                id=rs.getInt(1)+1;
                break;
            }

            ps=conn.prepareStatement(sql);
            ps.setInt(1,id);
            ps.setString(2,user_name);
            ps.setString(3,password);
            ps.setString(4,phone);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
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
        }
    }

    public static boolean findUserIdnumber(String id_number,String userTel) {
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // mysql简单的查询语句。这里是根据user_info表的phone字段来查询
            String sql = "select * from user_info where id_number=? and phone=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("findUserIdnumber_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                // 设置上面的sql语句中的？的值
                // 执行sql查询语句并返回结果集
                ps.setString(1, id_number);
                ps.setString(2, userTel);
                rs = ps.executeQuery();
                while (rs.next()) {
                    return true;
                }
            } else return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
            return false;
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
        return false;
    }

    public static void updateUserPwd(String password,String userTel) {
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        try {
            String sql="update user_info set password=? where phone=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                ps = connection.prepareStatement(sql);
                ps.setString(1, password);
                ps.setString(2,userTel);
                ps.executeUpdate();
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
        }
    }

    public static String getUserPwdByPhone(String userTel) {
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select rememberPwd from user_rememberpwd where phone=? ";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getUserPwdByPhone_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                ps.setString(1, userTel);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String uPassword=rs.getString(1);
                    return uPassword;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
            return null;
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
        return null;
    }

    public static void addRememberPwd(String phone,String password){
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        try {
            String sql = "insert INTO user_rememberpwd VALUES(?,?)";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getUserPwdByPhone_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                ps.setString(1, phone);
                ps.setString(2,password);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
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
        }
    }

    public static boolean findRememberPwd(String phone,String password){
        Connection connection = DBUtils.getConn("gutfoodwitdatabase");
        PreparedStatement ps = null;
        ResultSet rs=null;
        try {
            String sql = "select * from user_rememberpwd where phone=? and rememberPwd=?";
            if (connection != null) {// connection不为null表示与数据库建立了连接
                Log.e("getUserPwdByPhone_conn", "不为NULL");
                ps = connection.prepareStatement(sql);
                ps.setString(1, phone);
                ps.setString(2,password);
                rs=ps.executeQuery();
                while (rs.next())
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DBUtils", "异常：" + e.getMessage());
            return false;
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
        }
        return false;
    }

}
