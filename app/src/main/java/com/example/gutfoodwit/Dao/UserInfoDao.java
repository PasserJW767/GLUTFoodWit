package com.example.gutfoodwit.Dao;


import com.example.gutfoodwit.bean.UserInfo;
import com.example.gutfoodwit.utlis.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoDao {

    public UserInfo getUserInfo(String userTel) {
        UserInfo userInfo = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "select * from user_info where phone = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, userTel);
            rs = ps.executeQuery();
            while (rs.next()){
                userInfo = new UserInfo();
                userInfo.setXuhao(rs.getInt(1));
                userInfo.setName(rs.getString(2));
                userInfo.setPassword(rs.getString(3));
                userInfo.setPhone(userTel);
                userInfo.setUser_icon(rs.getString(5));
                userInfo.setId_number(rs.getString(6));
                userInfo.setGender(rs.getString(7));
            }
            return userInfo;
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
        return userInfo;
    }

    public boolean insertUserHead(String userTel,String path) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "UPDATE user_info set user_icon=? WHERE phone=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,path);
            ps.setString(2,userTel);
            int i = ps.executeUpdate();
            if (i == 1){
                return true;
            }
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
        return false;
    }

    public boolean updateUserGender(String userTel,String gender){
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "UPDATE user_info set gender=? WHERE phone=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,gender);
            ps.setString(2,userTel);
            int i = ps.executeUpdate();
            if (i == 1){
                return true;
            }
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
        return false;
    }

    public boolean updateUserName(String userTel,String userName){
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "UPDATE user_info set user_name=? WHERE phone=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,userName);
            ps.setString(2,userTel);
            int i = ps.executeUpdate();
            if (i == 1){
                return true;
            }
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
        return false;
    }

    public boolean updateUserIdentifyCard(String userTel,String identifyCard){
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "UPDATE user_info set id_number=? WHERE phone=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,identifyCard);
            ps.setString(2,userTel);
            int i = ps.executeUpdate();
            if (i == 1){
                return true;
            }
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
        return false;
    }

    public boolean checkOldPwd(String userTel,String pwd){
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "SELECT * FROM user_info where phone = ? AND password = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,userTel);
            ps.setString(2,pwd);
            rs = ps.executeQuery();
            while (rs.next()){
                return true;
            }
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
        return false;
    }

    public boolean updateUserPwd(String userTel,String pwd){
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = DBUtils.getConn("gutfoodwitdatabase");
        String sql = "UPDATE user_info set password=? WHERE phone=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,pwd);
            ps.setString(2,userTel);
            int i = ps.executeUpdate();
            if (i == 1){
                return true;
            }
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
        return false;
    }
}
