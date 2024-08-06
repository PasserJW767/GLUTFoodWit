package com.example.gutfoodwit.bean;

import java.util.Date;
import java.util.List;

public class OrderInfo {
    int order_id;
    int shop_id;
    int commodityId;
    int commodityCount;
    Date order_createTime;
    double order_price;
    String order_state;
    String userTel;

    String shopName;
    int order_evaluate;

    public OrderInfo() {
    }

    public OrderInfo(int order_id, int shop_id, int commodityId, int commodityCount, Date order_createTime, double order_price, String order_state, String userTel) {
        this.order_id = order_id;
        this.shop_id = shop_id;
        this.commodityId = commodityId;
        this.commodityCount = commodityCount;
        this.order_createTime = order_createTime;
        this.order_price = order_price;
        this.order_state = order_state;
        this.userTel = userTel;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public int getCommodityCount() {
        return commodityCount;
    }

    public void setCommodityCount(int commodityCount) {
        this.commodityCount = commodityCount;
    }

    public Date getOrder_createTime() {
        return order_createTime;
    }

    public void setOrder_createTime(Date order_createTime) {
        this.order_createTime = order_createTime;
    }

    public double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(double order_price) {
        this.order_price = order_price;
    }

    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getOrder_evaluate() {
        return order_evaluate;
    }

    public void setOrder_evaluate(int order_evaluate) {
        this.order_evaluate = order_evaluate;
    }
}
