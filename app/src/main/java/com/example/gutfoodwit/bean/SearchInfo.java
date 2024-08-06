package com.example.gutfoodwit.bean;

public class SearchInfo {
    String commodity_img;//商品图片路径
    String commodity_name;//类型名称
    int commodity_price;//价钱
    int commodity_sales;//销量

    String shop_icon;//店铺照片路径
    String shop_name;//店铺名字
    String star_size;//评分
    int monthly_sales;//月售
    int average_price;//人均花费

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

    public int getCommodity_price() {
        return commodity_price;
    }

    public void setCommodity_price(int commodity_price) {
        this.commodity_price = commodity_price;
    }

    public int getCommodity_sales() {
        return commodity_sales;
    }

    public void setCommodity_sales(int commodity_sales) {
        this.commodity_sales = commodity_sales;
    }

    public String getShop_icon() {
        return shop_icon;
    }

    public void setShop_icon(String shop_icon) {
        this.shop_icon = shop_icon;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getStar_size() {
        return star_size;
    }

    public void setStar_size(String star_size) {
        this.star_size = star_size;
    }

    public int getMonthly_sales() {
        return monthly_sales;
    }

    public void setMonthly_sales(int monthly_sales) {
        this.monthly_sales = monthly_sales;
    }

    public int getAverage_price() {
        return average_price;
    }

    public void setAverage_price(int average_price) {
        this.average_price = average_price;
    }
}
