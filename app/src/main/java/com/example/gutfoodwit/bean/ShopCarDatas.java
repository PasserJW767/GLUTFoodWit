package com.example.gutfoodwit.bean;
import java.util.List;
public class ShopCarDatas implements Cloneable{//cloneable是一个标记接口,只有实现这个接口后,然后在类中重写Object中的clone方法,然后通过类调用clone方法才能克隆成功

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private List<ShopCarDatas.DatasBean> datas;

    public List<ShopCarDatas.DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<ShopCarDatas.DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean implements Cloneable{//cloneable是一个标记接口，只有实现这个接口后，然后在类中重写Object中的clone方法，然后通过类调用clone方法才能克隆成功

        //店铺
        private int id;//店id
        private String shop_name;//店名
        private boolean isSelect_shop;      //店铺是否在购物车中被选中
        private List<ShopCarDatas.DatasBean.CommodityBean> commodities;

        public ShopCarDatas.DatasBean clone() {
            ShopCarDatas.DatasBean o = null;
            try {
                o = (ShopCarDatas.DatasBean) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return o;
        }

        public void setIdShopName(int id,String shop_name){
            this.id=id;
            this.shop_name=shop_name;
        }


        public boolean getIsSelect_shop() {
            return isSelect_shop;
        }

        public void setIsSelect_shop(boolean select_shop) {
            isSelect_shop = select_shop;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public List<ShopCarDatas.DatasBean.CommodityBean> getCommodities() {
            return commodities;
        }

        public void setCommodities(List<ShopCarDatas.DatasBean.CommodityBean> commodities) {
            this.commodities = commodities;
        }

        public static class CommodityBean {
            //商品
            private int commodity_id;//商品id
            private String commodity_img;//商品图片路径
            private String commodity_name;//商品名
            private int commodity_num;//选购数量
            private Double commodity_price;//商品单价
            private boolean isSelect;        //商品是否在购物车中被选中

            public boolean getIsSelect() {
                return isSelect;
            }

            public void setIsSelect(boolean isSelect) {
                this.isSelect = isSelect;
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

            public Double getCommodity_price() {
                return commodity_price;
            }

            public void setCommodity_price(Double commodity_price) {
                this.commodity_price = commodity_price;
            }
        }
    }
}
