package com.example.gutfoodwit.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gutfoodwit.Dao.CommodityDao;
import com.example.gutfoodwit.Dao.CommodityDetailDao;
import com.example.gutfoodwit.Dao.ShopCarDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.bean.CarCommodities;

import java.lang.reflect.Field;
import java.util.List;

public class CommodityDetailActivity extends AppCompatActivity {

    private int commodity_id;
    private String userTel;

    private List<String> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(this);
//        设置透明状态栏
        setStatusBar();
        setContentView(R.layout.activity_commoditydetail);
        getShopIdAndUserTel();
        initComponent();
    }

    /**
     * 获得商品ID和用户电话号码
     * */
    private void getShopIdAndUserTel(){
        Bundle extras = getIntent().getExtras();
        commodity_id = extras.getInt("commodity_id");
//        commodity_id = 11;
        userTel = extras.getString("userTel");
//        userTel = "111";
        if(userTel == null){
            Toast.makeText(CommodityDetailActivity.this, "您尚未登录，请先登录！", Toast.LENGTH_SHORT);
//            没有userTel，未登录，跳转至登录界面
            startActivity(new Intent(this,Lottery.class));
            overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        else{
            getData();
        }
    }

    /**
     * 改变界面UI
     * */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(@NonNull Message msg) {
        switch (msg.what){
            case 0:
//                设置商品图片
                ImageView img = findViewById(R.id.view_commodityImg);
                img.setImageResource(getImageID(resultList.get(6)));
//                设置商品名字
                TextView tv_commodityName = findViewById(R.id.tv_commodityName);
                tv_commodityName.setText(resultList.get(3));
//                设置属性
                if (resultList.get(8) == null || resultList.get(8).equals("")){
//                    说明没有属性
                    TextView tv_commodityProperty1 = findViewById(R.id.tv_commodityProperty1);
                    tv_commodityProperty1.setText("暂无商品属性");
                    findViewById(R.id.tv_commodityProperty2).setVisibility(View.INVISIBLE);
                }
                else {
//                    说明存在属性
                    TextView tv_commodityProperty1 = findViewById(R.id.tv_commodityProperty1);
                    tv_commodityProperty1.setText(resultList.get(8));
                    if(resultList.get(9) == null || resultList.get(9).equals("")){
//                        说明第二个属性不存在
                        findViewById(R.id.tv_commodityProperty2).setVisibility(View.INVISIBLE);
                    }
                    else {
//                        说明第二个属性存在
                        TextView tv_commodityProperty2 = findViewById(R.id.tv_commodityProperty2);
                        tv_commodityProperty2.setText(resultList.get(9));
                    }
                }
//                设置头衔
                if(resultList.get(10) == null || resultList.get(10).equals("")){
//                    说明暂无头衔
                    TextView tv_commodityTitle = findViewById(R.id.tv_commodityTitle);
                    tv_commodityTitle.setText("暂无商品头衔");
                }
                else {
//                    说明有头衔
                    TextView tv_commodityTitle = findViewById(R.id.tv_commodityTitle);
                    tv_commodityTitle.setText(resultList.get(10));
                }
//                设置月售
                TextView tv_commoditySales = findViewById(R.id.tv_commoditySales);
                tv_commoditySales.setText("月售：" + resultList.get(5));
//                设置价格
                TextView tv_commodidtyPrice = findViewById(R.id.tv_commodidtyPrice);
                tv_commodidtyPrice.setText(resultList.get(4));
//                设置介绍
                TextView tv_commodityContent = findViewById(R.id.tv_commodityContent);
                tv_commodityContent.setText(resultList.get(7));
                break;
        }
        }
    };

    /**
     * 获得商品数据
     * */
    private void getData(){
        new Thread(() -> {
            resultList = new CommodityDetailDao().getCommodityDetailById(commodity_id);
            Message msg = handler.obtainMessage();
            msg.what = 0;
            handler.sendMessage(msg);
        }).start();
    }

    /**
     * 初始化组件
     * */
    private void initComponent(){
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.btn_addToCart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> {
//                    获得当前shopId
                    int shopId = new CommodityDao().getShopIdByCommodityId(commodity_id);
                    int count = new ShopCarDao().getCount(userTel, shopId, commodity_id);
                    if(count == 0){
//                        说明并未加购此项，插入一个新的
                        boolean flag1=ShopCarDao.judgeCarShops(userTel,shopId);//1.看店铺库里有没有本店
                        if(flag1){
                            if (shopId == 0) {
                                Looper.prepare();
                                Toast.makeText(CommodityDetailActivity.this, "加购失败！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else {
                                ShopCarDao.addCommodity(userTel, shopId, commodity_id, 1);
                                Looper.prepare();
                                Toast.makeText(CommodityDetailActivity.this, "加购成功！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                        } else {
                            if (shopId == 0) {
                                Looper.prepare();
                                Toast.makeText(CommodityDetailActivity.this, "加购失败！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            } else {
                                ShopCarDao.addShop(userTel, shopId);
                                ShopCarDao.addCommodity(userTel, shopId, commodity_id, 1);
                                Looper.prepare();
                                Toast.makeText(CommodityDetailActivity.this, "加购成功！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        }


                    } else {
//                        说明已经加购，只需要在基础上加一（update操作)
                        if (shopId == 0) {
                            Looper.prepare();
                            Toast.makeText(CommodityDetailActivity.this, "加购失败！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        } else {
                            ShopCarDao.updataNum(userTel, shopId, commodity_id, ++count);
                            Looper.prepare();
                            Toast.makeText(CommodityDetailActivity.this, "加购成功！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                    }
                }).start();
            }
        });
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
            int permission = ActivityCompat.checkSelfPermission(CommodityDetailActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(CommodityDetailActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if(ContextCompat.checkSelfPermission(CommodityDetailActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(CommodityDetailActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
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
