package com.example.gutfoodwit.bean;

public class CarShops {
    private String code;//唯一标识码(userId)
    private String shop_name;
    private int shop_id;

    public CarShops() {
    }

    public CarShops(String code, String shop_name, int shop_id) {
        this.code = code;
        this.shop_name = shop_name;
        this.shop_id = shop_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }
}
