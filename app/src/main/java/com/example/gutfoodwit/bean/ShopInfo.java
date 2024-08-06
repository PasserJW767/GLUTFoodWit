package com.example.gutfoodwit.bean;

import java.util.ArrayList;
import java.util.List;

public class ShopInfo {
    private int id;
    private String shop_name;
    private int canteen_id;
    private String shop_icon;
    private String star_size;
    private int monthly_sales;
    private int average_price;
    private String shop_describe;
    String shop_type;
    String shop_detail_type;

    private String canteenName;

    public String getCanteenName() {
        return canteenName;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }


    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCanteen_id() {
        return canteen_id;
    }

    public void setCanteen_id(int canteen_id) {
        this.canteen_id = canteen_id;
    }

    public String getShop_icon() {
        return shop_icon;
    }

    public void setShop_icon(String shop_icon) {
        this.shop_icon = shop_icon;
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

    public String getShop_describe() {
        return shop_describe;
    }

    public void setShop_describe(String shop_describe) {
        this.shop_describe = shop_describe;
    }

    public String getShop_type() {
        return shop_type;
    }

    public void setShop_type(String shop_type) {
        this.shop_type = shop_type;
    }

    public String getShop_detail_type() {
        return shop_detail_type;
    }

    public void setShop_detail_type(String shop_detail_type) {
        this.shop_detail_type = shop_detail_type;
    }

    // 声明一个手机商品的名称数组
    private static String[] mNameArray = {
            "一号食堂", "二号食堂", "三号食堂"
    };

    // 获取默认的食堂信息列表
    public static List<ShopInfo> getDefaultShopList() {
        List<ShopInfo> shopInfoList = new ArrayList<ShopInfo>();
        for (int i = 0; i < mNameArray.length; i++) {
            ShopInfo shopInfo = new ShopInfo();
            shopInfo.setCanteenName(mNameArray[i]);
            shopInfoList.add(shopInfo);
        }
        return shopInfoList;
    }
}
