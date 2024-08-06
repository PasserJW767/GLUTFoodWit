package com.example.gutfoodwit.bean;

public class CommodityDetail {

    Integer commodity_id;
    String commodity_propertyF;
    String commodity_propertyS;
    String commodity_title;

    public CommodityDetail(Integer commodity_id, String commodity_propertyF, String commodity_propertyS, String commodity_title) {
        this.commodity_id = commodity_id;
        this.commodity_propertyF = commodity_propertyF;
        this.commodity_propertyS = commodity_propertyS;
        this.commodity_title = commodity_title;
    }

    public CommodityDetail() {
    }

    public Integer getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(Integer commodity_id) {
        this.commodity_id = commodity_id;
    }

    public String getCommodity_propertyF() {
        return commodity_propertyF;
    }

    public void setCommodity_propertyF(String commodity_propertyF) {
        this.commodity_propertyF = commodity_propertyF;
    }

    public String getCommodity_propertyS() {
        return commodity_propertyS;
    }

    public void setCommodity_propertyS(String commodity_propertyS) {
        this.commodity_propertyS = commodity_propertyS;
    }

    public String getCommodity_title() {
        return commodity_title;
    }

    public void setCommodity_title(String commodity_title) {
        this.commodity_title = commodity_title;
    }
}
