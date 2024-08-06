package com.example.gutfoodwit.bean;

public class CarCommodities {
    private String code;
    private int shop_id;
    private int commodity_id;
    private String commodity_img;
    private String commodity_name;
    private int commodity_num;
    private double commodity_price;

    public CarCommodities() {
    }

    public CarCommodities(String code, int shop_id, int commodity_id, String commodity_img, String commodity_name, int commodity_num, double commodity_price) {
        this.code = code;
        this.shop_id = shop_id;
        this.commodity_id = commodity_id;
        this.commodity_img = commodity_img;
        this.commodity_name = commodity_name;
        this.commodity_num = commodity_num;
        this.commodity_price = commodity_price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(int commodity_id) {
        this.commodity_id = commodity_id;
    }

    public String getCommodity_img() {
        return commodity_img;
    }

    public void setCommodity_img(String commodity_img) {
        this.commodity_img = commodity_img;
    }

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public int getCommodity_num() {
        return commodity_num;
    }

    public void setCommodity_num(int commodity_num) {
        this.commodity_num = commodity_num;
    }

    public double getCommodity_price() {
        return commodity_price;
    }

    public void setCommodity_price(double commodity_price) {
        this.commodity_price = commodity_price;
    }
}
