package com.example.gutfoodwit.bean;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.gutfoodwit.Dao.ShopCarDao;

public class ShopCarInstance {
    public static volatile List<ShopCarDatas.DatasBean> datasBeans= new ArrayList<>();

    public void init(String code) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<CarShops> carshops= new ArrayList<>();
                List<CarCommodities> carCommodities= new ArrayList<>();
                carshops = ShopCarDao.getCarShopsByCode(code);
                carCommodities = ShopCarDao.getCarCommoditiesByCode(code);
                List<ShopCarDatas.DatasBean> datasBeans= new ArrayList<>();
                if (carCommodities.size()!=0) {
                    for (int i = 0; i < carshops.size(); i++) {
                        CarShops theShop = carshops.get(i);
                        ShopCarDatas.DatasBean data=new ShopCarDatas.DatasBean();
                        data.setId(theShop.getShop_id());
                        data.setShop_name(theShop.getShop_name());

                        int theShopId = theShop.getShop_id();
                        List<ShopCarDatas.DatasBean.CommodityBean> commodityBeans= new ArrayList<>();
                        List<CarCommodities> theCommodities = ShopCarDao.getCarCommoditiesByCodeShopId(theShopId, code);
                        for (int j = 0; j < theCommodities.size(); j++) {
                            ShopCarDatas.DatasBean.CommodityBean commodities =new ShopCarDatas.DatasBean.CommodityBean();
                            commodities.setCommodity_num(theCommodities.get(j).getCommodity_num());
                            commodities.setCommodity_id(theCommodities.get(j).getCommodity_id());
                            commodities.setCommodity_img(theCommodities.get(j).getCommodity_img());
                            commodities.setCommodity_name(theCommodities.get(j).getCommodity_name());
                            commodities.setCommodity_price(theCommodities.get(j).getCommodity_price());
                            commodityBeans.add(commodities);
                            data.setCommodities(commodityBeans);
                        }
                        datasBeans.add(data);
                    }
                    Message message=handler.obtainMessage();
                    message.what=1;
                    message.obj=datasBeans;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    datasBeans= (List<ShopCarDatas.DatasBean>) msg.obj;
                    break;
                /*case 2:
                    datasBeans= (List<ShopCarDatas.DatasBean>) msg.obj;
                    break;
                case 3:
                    datasBeans= (List<ShopCarDatas.DatasBean>) msg.obj;
                    break;*/
            }
        }
    };

    public List<ShopCarDatas.DatasBean> getDatasBeans(){
        return datasBeans;
    }

    public void deleteByShopId(String code,int shop_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShopCarDao.deleteCarByShopId(code,shop_id);
                List<CarShops> carshops= new ArrayList<>();
                List<CarCommodities> carCommodities= new ArrayList<>();
                carshops = ShopCarDao.getCarShopsByCode(code);
                carCommodities = ShopCarDao.getCarCommoditiesByCode(code);
                List<ShopCarDatas.DatasBean> datasBeans= new ArrayList<>();
                if (carCommodities.size()!=0) {
                    for (int i = 0; i < carshops.size(); i++) {
                        CarShops theShop = carshops.get(i);
                        ShopCarDatas.DatasBean data=new ShopCarDatas.DatasBean();
                        data.setId(theShop.getShop_id());
                        data.setShop_name(theShop.getShop_name());

                        int theShopId = theShop.getShop_id();
                        List<ShopCarDatas.DatasBean.CommodityBean> commodityBeans= new ArrayList<>();
                        List<CarCommodities> theCommodities = ShopCarDao.getCarCommoditiesByCodeShopId(theShopId, code);
                        for (int j = 0; j < theCommodities.size(); j++) {
                            ShopCarDatas.DatasBean.CommodityBean commodities =new ShopCarDatas.DatasBean.CommodityBean();
                            commodities.setCommodity_num(theCommodities.get(j).getCommodity_num());
                            commodities.setCommodity_id(theCommodities.get(j).getCommodity_id());
                            commodities.setCommodity_img(theCommodities.get(j).getCommodity_img());
                            commodities.setCommodity_name(theCommodities.get(j).getCommodity_name());
                            commodities.setCommodity_price(theCommodities.get(j).getCommodity_price());
                            commodityBeans.add(commodities);
                            data.setCommodities(commodityBeans);
                        }
                        datasBeans.add(data);
                    }
                    Message message=handler.obtainMessage();
                    message.what=1;
                    message.obj=datasBeans;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    public void deleteByCommodityId(String code,int commodity_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShopCarDao.deleteCarByCommodityId(code,commodity_id);
                List<CarShops> carshops= new ArrayList<>();
                List<CarCommodities> carCommodities= new ArrayList<>();
                carshops = ShopCarDao.getCarShopsByCode(code);
                carCommodities = ShopCarDao.getCarCommoditiesByCode(code);
                List<ShopCarDatas.DatasBean> datasBeans= new ArrayList<>();
                if (carCommodities.size()!=0) {
                    for (int i = 0; i < carshops.size(); i++) {
                        CarShops theShop = carshops.get(i);
                        ShopCarDatas.DatasBean data=new ShopCarDatas.DatasBean();
                        data.setId(theShop.getShop_id());
                        data.setShop_name(theShop.getShop_name());

                        int theShopId = theShop.getShop_id();
                        List<ShopCarDatas.DatasBean.CommodityBean> commodityBeans= new ArrayList<>();
                        List<CarCommodities> theCommodities = ShopCarDao.getCarCommoditiesByCodeShopId(theShopId, code);
                        for (int j = 0; j < theCommodities.size(); j++) {
                            ShopCarDatas.DatasBean.CommodityBean commodities =new ShopCarDatas.DatasBean.CommodityBean();
                            commodities.setCommodity_num(theCommodities.get(j).getCommodity_num());
                            commodities.setCommodity_id(theCommodities.get(j).getCommodity_id());
                            commodities.setCommodity_img(theCommodities.get(j).getCommodity_img());
                            commodities.setCommodity_name(theCommodities.get(j).getCommodity_name());
                            commodities.setCommodity_price(theCommodities.get(j).getCommodity_price());
                            commodityBeans.add(commodities);
                            data.setCommodities(commodityBeans);
                        }
                        datasBeans.add(data);
                    }
                    Message message=handler.obtainMessage();
                    message.what=1;
                    message.obj=datasBeans;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }


    public void updataNum(String code,int shop_p,int commodity_id,int num) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ShopCarDao.updataNum(code,shop_p,commodity_id,num);
                List<CarShops> carshops= new ArrayList<>();
                List<CarCommodities> carCommodities= new ArrayList<>();
                carshops = ShopCarDao.getCarShopsByCode(code);
                carCommodities = ShopCarDao.getCarCommoditiesByCode(code);
                List<ShopCarDatas.DatasBean> datasBeans= new ArrayList<>();
                if (carCommodities.size()!=0) {
                    for (int i = 0; i < carshops.size(); i++) {
                        CarShops theShop = carshops.get(i);
                        ShopCarDatas.DatasBean data=new ShopCarDatas.DatasBean();
                        data.setId(theShop.getShop_id());
                        data.setShop_name(theShop.getShop_name());

                        int theShopId = theShop.getShop_id();
                        List<ShopCarDatas.DatasBean.CommodityBean> commodityBeans= new ArrayList<>();
                        List<CarCommodities> theCommodities = ShopCarDao.getCarCommoditiesByCodeShopId(theShopId, code);
                        for (int j = 0; j < theCommodities.size(); j++) {
                            ShopCarDatas.DatasBean.CommodityBean commodities =new ShopCarDatas.DatasBean.CommodityBean();
                            commodities.setCommodity_num(theCommodities.get(j).getCommodity_num());
                            commodities.setCommodity_id(theCommodities.get(j).getCommodity_id());
                            commodities.setCommodity_img(theCommodities.get(j).getCommodity_img());
                            commodities.setCommodity_name(theCommodities.get(j).getCommodity_name());
                            commodities.setCommodity_price(theCommodities.get(j).getCommodity_price());
                            commodityBeans.add(commodities);
                            data.setCommodities(commodityBeans);
                        }
                        datasBeans.add(data);
                    }
                    Message message=handler.obtainMessage();
                    message.what=1;
                    message.obj=datasBeans;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

}
