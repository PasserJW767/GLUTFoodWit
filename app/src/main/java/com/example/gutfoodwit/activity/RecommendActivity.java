package com.example.gutfoodwit.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gutfoodwit.Dao.RecommendDao;
import com.example.gutfoodwit.Dao.UserInfoDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.bean.CommodityInfo;
import com.example.gutfoodwit.bean.ShopInfo;
import com.example.gutfoodwit.bean.UserInfo;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class RecommendActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_navigation_home = null;
    private Button btn_navigation_recommend = null;
    private Button btn_navigation_me = null;

    private String userTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(this);
//        设置透明状态栏
        setStatusBar();
        setContentView(R.layout.activity_recommend);
//        初始化导航栏
        initNavigationRadioButton();
        getInfo();
        getRecommendByTime();
        getLoveData();
        addListener();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    List<CommodityInfo> commodityInfoList = (List<CommodityInfo>) msg.obj;
                    initLovingPlate(commodityInfoList);
                    break;
                case 1:
                    List<ShopInfo> shopInfoList = (List<ShopInfo>) msg.obj;
                    initRecommendPlate(shopInfoList);
                    break;
                case 2:
                    TextView tv_welcomeInfo = findViewById(R.id.tv_welcomeInfo);
                    tv_welcomeInfo.setText(String.valueOf(msg.obj));
                    break;
                case 3:
                    updateTitle(String.valueOf(msg.obj));
                    break;
            }
        }
    };

    private void getRecommendByTime(){
        new Thread(() -> {

            UserInfo userInfo = new UserInfoDao().getUserInfo(userTel);
            String userName = userInfo.getName();
            String welcomeInfo = "亲爱的" + userName + ",";

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            String strHours = sdf.format(date);
            int hour = -1;
            if (strHours.equals("00")) {
                hour = 0;
            } else if(strHours.equals("01")) { hour = 1;}
            else if (strHours.equals("02")) {hour = 2;}
            else if (strHours.equals("03")) {hour = 3;}
            else if (strHours.equals("04")) {hour = 4;}
            else if (strHours.equals("05")) {hour = 5;}
            else if (strHours.equals("06")) {hour = 6;}
            else if (strHours.equals("07")) {hour = 7;}
            else if (strHours.equals("08")) {hour = 8;}
            else if (strHours.equals("09")) {hour = 9;}
            else {
                hour = Integer.valueOf(strHours);
            }
            Log.e("aaa", String.valueOf(hour));

            List<ShopInfo> shopInfoList = null;

            String tempStr = null;
//            夜宵时间段
            if((hour >= 0 && hour < 6) || (hour > 21)){
                tempStr = "夜宵";
                shopInfoList = new RecommendDao().getRecommendAccrodingTimeShop("夜宵");
                welcomeInfo += "夜深了";
            }
//            早餐时间段
            else if(hour >= 6 && hour < 11){
                tempStr = "早餐" ;
                shopInfoList = new RecommendDao().getRecommendAccrodingTimeShop("早餐");
                welcomeInfo += "早上好";
            }
//            午餐时间段
            else if(hour >= 11 && hour < 14){
                tempStr = "午餐";
                shopInfoList = new RecommendDao().getRecommendAccrodingTimeShop("午餐");
                welcomeInfo += "中午好";
            }
//            下午茶时间段
            else if(hour >= 14 && hour <= 16){
                tempStr = "下午茶";
                shopInfoList = new RecommendDao().getRecommendAccrodingTimeShop("下午茶");
                welcomeInfo += "下午好";
            }
//            晚餐时间段
            else if(hour > 16 && hour <= 21){
                tempStr = "晚餐";
                shopInfoList = new RecommendDao().getRecommendAccrodingTimeShop("晚餐");
                welcomeInfo += "晚上好";
            }
            Message msg = handler.obtainMessage();
            msg.what = 1;
            msg.obj = shopInfoList;
            handler.sendMessage(msg);

            Message welcomeMsg = handler.obtainMessage();
            welcomeMsg.what = 2;
            welcomeMsg.obj = welcomeInfo;
            handler.sendMessage(welcomeMsg);

            Message setMsg = handler.obtainMessage();
            setMsg.what = 3;
            setMsg.obj = tempStr;
            handler.sendMessage(setMsg);
        }).start();
    }

    private void getLoveData(){
        new Thread(() -> {

            List<CommodityInfo> commodityInfoList =  new RecommendDao().getLoveShopsInfo(userTel);
            Log.e("commodityInfoListLength", String.valueOf(commodityInfoList.size()));
            Message msg = handler.obtainMessage();
            msg.obj = commodityInfoList;
            msg.what = 0;
            handler.sendMessage(msg);

        }).start();
    }

    private void getInfo(){
        userTel = getIntent().getExtras().getString("userTel");
//        userTel = "14795572282";
    }


    @Override
    protected void onResume(){
        super.onResume();
    }


    @Override
    public void onClick(View v) {
//
        if(v.getId() == R.id.btn_navigation_me){
//            页面跳转
            // 创建一个意图对象，准备跳到指定的活动页面
            Intent intent = new Intent(RecommendActivity.this, My.class);
            // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
            intent.putExtra("userTel", userTel);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // 设置启动标志
            startActivity(intent); // 跳转到意图对象指定的活动页面
        }
        else if(v.getId() == R.id.btn_navigation_home){
//            页面跳转
            // 创建一个意图对象，准备跳到指定的活动页面
            Intent intent = new Intent(RecommendActivity.this, HomePageActivity.class);
            // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
            intent.putExtra("userTel", userTel);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // 设置启动标志
            startActivity(intent); // 跳转到意图对象指定的活动页面
        }
        else if(v.getId() == R.id.deliciousFood){
            Intent intent = new Intent(RecommendActivity.this, RecommendResultActivity.class);
            intent.putExtra("BigAndSmall", "Big");
            intent.putExtra("key", "美食");
            intent.putExtra("userTel", userTel);
            startActivity(intent);
        }
        else if(v.getId() == R.id.desertDrink){
            Intent intent = new Intent(RecommendActivity.this, RecommendResultActivity.class);
            intent.putExtra("BigAndSmall", "Big");
            intent.putExtra("key", "甜点饮品");
            intent.putExtra("userTel", userTel);
            startActivity(intent);
        }
        else if(v.getId() == R.id.superMarket){
            Intent intent = new Intent(RecommendActivity.this, RecommendResultActivity.class);
            intent.putExtra("BigAndSmall", "Big");
            intent.putExtra("key", "超市便利");
            intent.putExtra("userTel", userTel);
            startActivity(intent);
        }
        else if(v.getId() == R.id.fruitVegetable){
            Intent intent = new Intent(RecommendActivity.this, RecommendResultActivity.class);
            intent.putExtra("BigAndSmall", "Big");
            intent.putExtra("key", "蔬菜水果");
            intent.putExtra("userTel", userTel);
            startActivity(intent);
        }
        else if(v.getId() == R.id.medicine){
            Intent intent = new Intent(RecommendActivity.this, RecommendResultActivity.class);
            intent.putExtra("BigAndSmall", "Big");
            intent.putExtra("key", "买药");
            intent.putExtra("userTel", userTel);
            startActivity(intent);
        }
        else if(v.getId() == R.id.hamburgerPizza){
            Intent intent = new Intent(RecommendActivity.this, RecommendResultActivity.class);
            intent.putExtra("BigAndSmall", "Small");
            intent.putExtra("key", "汉堡披萨");
            intent.putExtra("userTel", userTel);
            startActivity(intent);
        }
        else if(v.getId() == R.id.homeVegetable){
            Intent intent = new Intent(RecommendActivity.this, RecommendResultActivity.class);
            intent.putExtra("BigAndSmall", "Small");
            intent.putExtra("key", "家常菜");
            intent.putExtra("userTel", userTel);
            startActivity(intent);
        }
        else if(v.getId() == R.id.snacks){
            Intent intent = new Intent(RecommendActivity.this, RecommendResultActivity.class);
            intent.putExtra("BigAndSmall", "Small");
            intent.putExtra("key", "小吃馆");
            intent.putExtra("userTel", userTel);
            startActivity(intent);
        }
        else if(v.getId() == R.id.fruits){
            Intent intent = new Intent(RecommendActivity.this, RecommendResultActivity.class);
            intent.putExtra("BigAndSmall", "Small");
            intent.putExtra("key", "果捞果切");
            intent.putExtra("userTel", userTel);
            startActivity(intent);
        }
        else if(v.getId() == R.id.fastFood){
            Intent intent = new Intent(RecommendActivity.this, RecommendResultActivity.class);
            intent.putExtra("BigAndSmall", "Small");
            intent.putExtra("key", "快餐便当");
            intent.putExtra("userTel", userTel);
            startActivity(intent);
        }
    }

    private void updateTitle(String s){
        TextView tv_goodShopTitle = findViewById(R.id.tv_goodShopTitle);
        tv_goodShopTitle.setText(s + "好店");
    }

    private void initRecommendPlate(List<ShopInfo> shopInfoList){
        ImageView img = findViewById(R.id.img_recommendF);
        img.setImageResource(getImageID(shopInfoList.get(0).getShop_icon()));
        img = findViewById(R.id.img_recommendS);
        img.setImageResource(getImageID(shopInfoList.get(1).getShop_icon()));
        img = findViewById(R.id.img_recommendT);
        img.setImageResource(getImageID(shopInfoList.get(2).getShop_icon()));
        img = findViewById(R.id.img_recommendFo);
        img.setImageResource(getImageID(shopInfoList.get(3).getShop_icon()));

        TextView tv = findViewById(R.id.tv_shopIdrecommendF);
        tv.setText(String.valueOf(shopInfoList.get(0).getId()));
        tv = findViewById(R.id.tv_shopIdrecommendS);
        tv.setText(String.valueOf(shopInfoList.get(1).getId()));
        tv = findViewById(R.id.tv_shopIdrecommendT);
        tv.setText(String.valueOf(shopInfoList.get(2).getId()));
        tv = findViewById(R.id.tv_shopIdrecommendFo);
        tv.setText(String.valueOf(shopInfoList.get(3).getId()));

        tv = findViewById(R.id.tv_shopNamerecommendF);
        tv.setText(shopInfoList.get(0).getShop_name());
        tv = findViewById(R.id.tv_shopNamerecommendS);
        tv.setText(shopInfoList.get(1).getShop_name());
        tv = findViewById(R.id.tv_shopNamerecommendT);
        tv.setText(shopInfoList.get(2).getShop_name());
        tv = findViewById(R.id.tv_shopNamerecommendFo);
        tv.setText(shopInfoList.get(3).getShop_name());

        findViewById(R.id.recommend_shopF).setVisibility(View.VISIBLE);
        findViewById(R.id.recommend_shopS).setVisibility(View.VISIBLE);
        findViewById(R.id.recommend_shopT).setVisibility(View.VISIBLE);
        findViewById(R.id.recommend_shopFo).setVisibility(View.VISIBLE);
    }

    private void initLovingPlate(List<CommodityInfo> commodityInfoList){
        int size = commodityInfoList.size();
        switch (size){
            case 5:
                ImageView img = findViewById(R.id.img_lovingF);
                img.setImageResource(getImageID(commodityInfoList.get(0).getCommodity_img()));
                TextView tv = findViewById(R.id.tv_name_lovingF);
                tv.setText(commodityInfoList.get(0).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingF);
                tv.setText("月售 " + commodityInfoList.get(0).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingF);
                tv.setText("价格 ￥" + commodityInfoList.get(0).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingF);
                img.setImageResource(getImageID(commodityInfoList.get(0).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingF);
                tv.setText(String.valueOf(commodityInfoList.get(0).getId()));
                tv = findViewById(R.id.tv_shopName_lovingF);
                tv.setText(commodityInfoList.get(0).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingF);
                tv.setText(commodityInfoList.get(0).getShop_starSize());

                img = findViewById(R.id.img_lovingS);
                img.setImageResource(getImageID(commodityInfoList.get(1).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingS);
                tv.setText(commodityInfoList.get(1).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingS);
                tv.setText("月售 " + commodityInfoList.get(1).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingS);
                tv.setText("价格 ￥" + commodityInfoList.get(1).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingS);
                img.setImageResource(getImageID(commodityInfoList.get(1).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingS);
                tv.setText(String.valueOf(commodityInfoList.get(1).getId()));
                tv = findViewById(R.id.tv_shopName_lovingS);
                tv.setText(commodityInfoList.get(1).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingS);
                tv.setText(commodityInfoList.get(1).getShop_starSize());

                img = findViewById(R.id.img_lovingT);
                img.setImageResource(getImageID(commodityInfoList.get(2).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingT);
                tv.setText(commodityInfoList.get(2).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingT);
                tv.setText("月售 " + commodityInfoList.get(2).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingT);
                tv.setText("价格 ￥" + commodityInfoList.get(2).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingT);
                img.setImageResource(getImageID(commodityInfoList.get(2).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingT);
                tv.setText(String.valueOf(commodityInfoList.get(2).getId()));
                tv = findViewById(R.id.tv_shopName_lovingT);
                tv.setText(commodityInfoList.get(2).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingT);
                tv.setText(commodityInfoList.get(2).getShop_starSize());

                img = findViewById(R.id.img_lovingFo);
                img.setImageResource(getImageID(commodityInfoList.get(3).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingFo);
                tv.setText(commodityInfoList.get(3).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingFo);
                tv.setText("月售 " + commodityInfoList.get(3).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingFo);
                tv.setText("价格 ￥" + commodityInfoList.get(3).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingFo);
                img.setImageResource(getImageID(commodityInfoList.get(3).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingFo);
                tv.setText(String.valueOf(commodityInfoList.get(3).getId()));
                tv = findViewById(R.id.tv_shopName_lovingFo);
                tv.setText(commodityInfoList.get(3).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingFo);
                tv.setText(commodityInfoList.get(3).getShop_starSize());

                img = findViewById(R.id.img_lovingFi);
                img.setImageResource(getImageID(commodityInfoList.get(4).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingFi);
                tv.setText(commodityInfoList.get(4).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingFi);
                tv.setText("月售 " + commodityInfoList.get(4).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingFi);
                tv.setText("价格 ￥" + commodityInfoList.get(4).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingFi);
                img.setImageResource(getImageID(commodityInfoList.get(4).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingFi);
                tv.setText(String.valueOf(commodityInfoList.get(4).getId()));
                tv = findViewById(R.id.tv_shopName_lovingFi);
                tv.setText(commodityInfoList.get(4).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingFi);
                tv.setText(commodityInfoList.get(4).getShop_starSize());
                break;
            case 4:
                img = findViewById(R.id.img_lovingF);
                img.setImageResource(getImageID(commodityInfoList.get(0).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingF);
                tv.setText(commodityInfoList.get(0).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingF);
                tv.setText("月售 " + commodityInfoList.get(0).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingF);
                tv.setText("价格 ￥" + commodityInfoList.get(0).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingF);
                img.setImageResource(getImageID(commodityInfoList.get(0).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingF);
                tv.setText(String.valueOf(commodityInfoList.get(0).getId()));
                tv = findViewById(R.id.tv_shopName_lovingF);
                tv.setText(commodityInfoList.get(0).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingF);
                tv.setText(commodityInfoList.get(0).getShop_starSize());

                img = findViewById(R.id.img_lovingS);
                img.setImageResource(getImageID(commodityInfoList.get(1).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingS);
                tv.setText(commodityInfoList.get(1).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingS);
                tv.setText("月售 " + commodityInfoList.get(1).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingS);
                tv.setText("价格 ￥" + commodityInfoList.get(1).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingS);
                img.setImageResource(getImageID(commodityInfoList.get(1).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingS);
                tv.setText(String.valueOf(commodityInfoList.get(1).getId()));
                tv = findViewById(R.id.tv_shopName_lovingS);
                tv.setText(commodityInfoList.get(1).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingS);
                tv.setText(commodityInfoList.get(1).getShop_starSize());

                img = findViewById(R.id.img_lovingT);
                img.setImageResource(getImageID(commodityInfoList.get(2).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingT);
                tv.setText(commodityInfoList.get(2).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingT);
                tv.setText("月售 " + commodityInfoList.get(2).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingT);
                tv.setText("价格 ￥" + commodityInfoList.get(2).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingT);
                img.setImageResource(getImageID(commodityInfoList.get(2).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingT);
                tv.setText(String.valueOf(commodityInfoList.get(2).getId()));
                tv = findViewById(R.id.tv_shopName_lovingT);
                tv.setText(commodityInfoList.get(2).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingT);
                tv.setText(commodityInfoList.get(2).getShop_starSize());

                img = findViewById(R.id.img_lovingFo);
                img.setImageResource(getImageID(commodityInfoList.get(3).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingFo);
                tv.setText(commodityInfoList.get(3).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingFo);
                tv.setText("月售 " + commodityInfoList.get(3).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingFo);
                tv.setText("价格 ￥" + commodityInfoList.get(3).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingFo);
                img.setImageResource(getImageID(commodityInfoList.get(3).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingFo);
                tv.setText(String.valueOf(commodityInfoList.get(3).getId()));
                tv = findViewById(R.id.tv_shopName_lovingFo);
                tv.setText(commodityInfoList.get(3).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingFo);
                tv.setText(commodityInfoList.get(3).getShop_starSize());

                findViewById(R.id.linear_plate5).setVisibility(View.GONE);
                break;
            case 3:
                img = findViewById(R.id.img_lovingF);
                img.setImageResource(getImageID(commodityInfoList.get(0).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingF);
                tv.setText(commodityInfoList.get(0).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingF);
                tv.setText("月售 " + commodityInfoList.get(0).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingF);
                tv.setText("价格 ￥" + commodityInfoList.get(0).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingF);
                img.setImageResource(getImageID(commodityInfoList.get(0).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingF);
                tv.setText(String.valueOf(commodityInfoList.get(0).getId()));
                tv = findViewById(R.id.tv_shopName_lovingF);
                tv.setText(commodityInfoList.get(0).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingF);
                tv.setText(commodityInfoList.get(0).getShop_starSize());

                img = findViewById(R.id.img_lovingS);
                img.setImageResource(getImageID(commodityInfoList.get(1).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingS);
                tv.setText(commodityInfoList.get(1).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingS);
                tv.setText("月售 " + commodityInfoList.get(1).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingS);
                tv.setText("价格 ￥" + commodityInfoList.get(1).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingS);
                img.setImageResource(getImageID(commodityInfoList.get(1).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingS);
                tv.setText(String.valueOf(commodityInfoList.get(1).getId()));
                tv = findViewById(R.id.tv_shopName_lovingS);
                tv.setText(commodityInfoList.get(1).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingS);
                tv.setText(commodityInfoList.get(1).getShop_starSize());

                img = findViewById(R.id.img_lovingT);
                img.setImageResource(getImageID(commodityInfoList.get(2).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingT);
                tv.setText(commodityInfoList.get(2).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingT);
                tv.setText("月售 " + commodityInfoList.get(2).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingT);
                tv.setText("价格 ￥" + commodityInfoList.get(2).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingT);
                img.setImageResource(getImageID(commodityInfoList.get(2).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingT);
                tv.setText(String.valueOf(commodityInfoList.get(2).getId()));
                tv = findViewById(R.id.tv_shopName_lovingT);
                tv.setText(commodityInfoList.get(2).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingT);
                tv.setText(commodityInfoList.get(2).getShop_starSize());

                findViewById(R.id.linear_plate4).setVisibility(View.GONE);
                findViewById(R.id.linear_plate5).setVisibility(View.GONE);
                break;
            case 2:
                img = findViewById(R.id.img_lovingF);
                img.setImageResource(getImageID(commodityInfoList.get(0).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingF);
                tv.setText(commodityInfoList.get(0).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingF);
                tv.setText("月售 " + commodityInfoList.get(0).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingF);
                tv.setText("价格 ￥" + commodityInfoList.get(0).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingF);
                img.setImageResource(getImageID(commodityInfoList.get(0).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingF);
                tv.setText(String.valueOf(commodityInfoList.get(0).getId()));
                tv = findViewById(R.id.tv_shopName_lovingF);
                tv.setText(commodityInfoList.get(0).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingF);
                tv.setText(commodityInfoList.get(0).getShop_starSize());

                img = findViewById(R.id.img_lovingS);
                img.setImageResource(getImageID(commodityInfoList.get(1).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingS);
                tv.setText(commodityInfoList.get(1).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingS);
                tv.setText("月售 " + commodityInfoList.get(1).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingS);
                tv.setText("价格 ￥" + commodityInfoList.get(1).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingS);
                img.setImageResource(getImageID(commodityInfoList.get(1).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingS);
                tv.setText(String.valueOf(commodityInfoList.get(1).getId()));
                tv = findViewById(R.id.tv_shopName_lovingS);
                tv.setText(commodityInfoList.get(1).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingS);
                tv.setText(commodityInfoList.get(1).getShop_starSize());

                findViewById(R.id.linear_plate3).setVisibility(View.GONE);
                findViewById(R.id.linear_plate4).setVisibility(View.GONE);
                findViewById(R.id.linear_plate5).setVisibility(View.GONE);
                break;
            case 1:
                img = findViewById(R.id.img_lovingF);
                img.setImageResource(getImageID(commodityInfoList.get(0).getCommodity_img()));
                tv = findViewById(R.id.tv_name_lovingF);
                tv.setText(commodityInfoList.get(0).getCommodity_name());
                tv = findViewById(R.id.tv_sale_lovingF);
                tv.setText("月售 " + commodityInfoList.get(0).getCommodity_sales());
                tv = findViewById(R.id.tv_price_lovingF);
                tv.setText("价格 ￥" + commodityInfoList.get(0).getCommodity_price());
                img = findViewById(R.id.img_shopIcon_lovingF);
                img.setImageResource(getImageID(commodityInfoList.get(0).getShop_img()));
                tv = findViewById(R.id.tv_shopId_lovingF);
                tv.setText(String.valueOf(commodityInfoList.get(0).getId()));
                tv = findViewById(R.id.tv_shopName_lovingF);
                tv.setText(commodityInfoList.get(0).getShop_name());
                tv = findViewById(R.id.tv_shopStar_lovingF);
                tv.setText(commodityInfoList.get(0).getShop_starSize());

                findViewById(R.id.linear_plate2).setVisibility(View.GONE);
                findViewById(R.id.linear_plate3).setVisibility(View.GONE);
                findViewById(R.id.linear_plate4).setVisibility(View.GONE);
                findViewById(R.id.linear_plate5).setVisibility(View.GONE);
                break;
            case 0:
                findViewById(R.id.linear_plate1).setVisibility(View.GONE);
                findViewById(R.id.linear_plate2).setVisibility(View.GONE);
                findViewById(R.id.linear_plate3).setVisibility(View.GONE);
                findViewById(R.id.linear_plate4).setVisibility(View.GONE);
                findViewById(R.id.linear_plate5).setVisibility(View.GONE);

                findViewById(R.id.allOrders_notice).setVisibility(View.VISIBLE);
                break;
        }
    }

    private void addListener(){
        findViewById(R.id.recommend_shopF).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.tv_shopIdrecommendF);
                Intent intent = new Intent(RecommendActivity.this, ShopDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(tv.getText().toString()));
                intent.putExtra("userTel", userTel);
                startActivity(intent);
            }
        });
        findViewById(R.id.recommend_shopS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.tv_shopIdrecommendS);
                Intent intent = new Intent(RecommendActivity.this, ShopDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(tv.getText().toString()));
                intent.putExtra("userTel", userTel);
                startActivity(intent);
            }
        });
        findViewById(R.id.recommend_shopT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.tv_shopIdrecommendT);
                Intent intent = new Intent(RecommendActivity.this, ShopDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(tv.getText().toString()));
                intent.putExtra("userTel", userTel);
                startActivity(intent);
            }
        });
        findViewById(R.id.recommend_shopFo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.tv_shopIdrecommendFo);
                Intent intent = new Intent(RecommendActivity.this, ShopDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(tv.getText().toString()));
                intent.putExtra("userTel", userTel);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_plate1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.tv_shopId_lovingF);
                Intent intent = new Intent(RecommendActivity.this, ShopDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(tv.getText().toString()));
                intent.putExtra("userTel", userTel);
                startActivity(intent);
            }
        });
        findViewById(R.id.linear_plate2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.tv_shopId_lovingS);
                Intent intent = new Intent(RecommendActivity.this, ShopDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(tv.getText().toString()));
                intent.putExtra("userTel", userTel);
                startActivity(intent);
            }
        });
        findViewById(R.id.linear_plate3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.tv_shopId_lovingT);
                Intent intent = new Intent(RecommendActivity.this, ShopDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(tv.getText().toString()));
                intent.putExtra("userTel", userTel);
                startActivity(intent);
            }
        });
        findViewById(R.id.linear_plate4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.tv_shopId_lovingFo);
                Intent intent = new Intent(RecommendActivity.this, ShopDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(tv.getText().toString()));
                intent.putExtra("userTel", userTel);
                startActivity(intent);
            }
        });
        findViewById(R.id.linear_plate5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.tv_shopId_lovingFi);
                Intent intent = new Intent(RecommendActivity.this, ShopDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(tv.getText().toString()));
                intent.putExtra("userTel", userTel);
                startActivity(intent);
            }
        });

        findViewById(R.id.deliciousFood).setOnClickListener(this);
        findViewById(R.id.desertDrink).setOnClickListener(this);
        findViewById(R.id.superMarket).setOnClickListener(this);
        findViewById(R.id.fruitVegetable).setOnClickListener(this);
        findViewById(R.id.medicine).setOnClickListener(this);
        findViewById(R.id.hamburgerPizza).setOnClickListener(this);
        findViewById(R.id.homeVegetable).setOnClickListener(this);
        findViewById(R.id.snacks).setOnClickListener(this);
        findViewById(R.id.fruits).setOnClickListener(this);
        findViewById(R.id.fastFood).setOnClickListener(this);
    }

    /**
     * 初始化按钮组，设置按钮组的初始状态
     * */
    private void initNavigationRadioButton(){
        btn_navigation_home = findViewById(R.id.btn_navigation_home);
        btn_navigation_home.setOnClickListener(this);
        btn_navigation_recommend = findViewById(R.id.btn_navigation_recommend);
        btn_navigation_recommend.setOnClickListener(this);
        btn_navigation_me = findViewById(R.id.btn_navigation_me);
        btn_navigation_me.setOnClickListener(this);
//        设置“首页”
        Drawable drawable = getResources().getDrawable(R.drawable.navigation_home_normal);
        drawable.setBounds(0,0,65,65);
        RadioButton radioButton = findViewById(R.id.btn_navigation_home);
        radioButton.setCompoundDrawables(null,drawable,null,null);
//        设置“推荐”
        drawable = getResources().getDrawable(R.drawable.navigation_recommend_selector);
        drawable.setBounds(0,0,65,65);
        radioButton = findViewById(R.id.btn_navigation_recommend);
        radioButton.setCompoundDrawables(null,drawable,null,null);
//        设置“我的”
        drawable = getResources().getDrawable(R.drawable.navigation_me_normal);
        drawable.setBounds(0,0,65,65);
        radioButton = findViewById(R.id.btn_navigation_me);
        radioButton.setCompoundDrawables(null,drawable,null,null);
    }

    /**
     *  开启本地读写权限
     *  */
    private void checkPermission(Activity activity) {//开启本地的照片读取与写入权限
        // Storage Permissions
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,//读内存权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE};//写内存权限
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(RecommendActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(RecommendActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if(ContextCompat.checkSelfPermission(RecommendActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(RecommendActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置透明状态栏
     * */
    private void setStatusBar(){
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private int getImageID(String name){
        int id = -1;
        try{
            Log.i("name",name);
            Field field = R.drawable.class.getDeclaredField(name);
            id = field.getInt(field.getName());
        } catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

}
