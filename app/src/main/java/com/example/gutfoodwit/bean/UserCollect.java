package com.example.gutfoodwit.bean;

public class UserCollect {
    private int id;
    private String userTel;
    private int shopId;

    private String shopName;
    private String shopIcon;
    private String shopStar;
    private int shopSale;
    private int shopAverage;

    public UserCollect() {
    }

    public UserCollect(int id, String userTel, int shopId, String shopName, String shopIcon, String shopStar, int shopSale, int shopAverage) {
        this.id = id;
        this.userTel = userTel;
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopIcon = shopIcon;
        this.shopStar = shopStar;
        this.shopSale = shopSale;
        this.shopAverage = shopAverage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopIcon() {
        return shopIcon;
    }

    public void setShopIcon(String shopIcon) {
        this.shopIcon = shopIcon;
    }

    public String getShopStar() {
        return shopStar;
    }

    public void setShopStar(String shopStar) {
        this.shopStar = shopStar;
    }

    public int getShopSale() {
        return shopSale;
    }

    public void setShopSale(int shopSale) {
        this.shopSale = shopSale;
    }

    public int getShopAverage() {
        return shopAverage;
    }

    public void setShopAverage(int shopAverage) {
        this.shopAverage = shopAverage;
    }
}
