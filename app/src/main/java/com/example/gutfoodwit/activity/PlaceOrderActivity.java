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
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gutfoodwit.Dao.OrderInfoDao;
import com.example.gutfoodwit.Dao.ShopCarDao;
import com.example.gutfoodwit.Dao.ShopDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.bean.CarCommodities;
import com.example.gutfoodwit.bean.CommodityInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceOrderActivity extends AppCompatActivity {

    private ListView listView;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> listItems = null;
    private Map<String, Object> map = null;

    private int shopId;
    private String userTel;
    private int[] commodities;
    private int orderId;

    private int[] commodityImg = null;
    private String[] commodityName = null;
    private int[] commodityCount = null;
    private double[] commodityPrice = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(this);
//        设置透明状态栏
        setStatusBar();
        setContentView(R.layout.activity_placeorder);
        getInfo();
        getData();
        initListener();
    }

    private void initListener(){
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                临时表订单插入订单表语句
                new Thread(() -> {
                    new OrderInfoDao().insertOrder(shopId,commodities,userTel);

                    for(int i=0;i<commodities.length;i++) {
                        new ShopCarDao().deleteCarByCommodityId(userTel,commodities[i]);
                    }
                    if(!new ShopCarDao().judgeShopHaveCommodities(shopId,userTel))
                        ShopCarDao.deleteShop(userTel,shopId);

                    Intent intent = new Intent(PlaceOrderActivity.this, OrderDetailActivity.class);
                    intent.putExtra("orderId", orderId);
                    startActivity(intent);
                }).start();
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                删除临时表订单语句
                finish();
            }
        });

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    List<CarCommodities> commoditiesList = (List<CarCommodities>) msg.obj;
                    int size = commoditiesList.size();
                    commodityImg = new int[size];
                    commodityName = new String[size];
                    commodityCount = new int[size];
                    commodityPrice = new double[size];
                    for(int i = 0;i < size;i++){
                        commodityImg[i] = getImageID(commoditiesList.get(i).getCommodity_img());
                        commodityName[i] = commoditiesList.get(i).getCommodity_name();
                        commodityCount[i] = commoditiesList.get(i).getCommodity_num();
                        commodityPrice[i] = commoditiesList.get(i).getCommodity_price() * commoditiesList.get(i).getCommodity_num();
                    }
                    listItems = new ArrayList<>();
                    for(int j = 0;j < size;j++){
                        map = new HashMap<>();
                        map.put("orderdetail_commodityImg", commodityImg[j]);
                        map.put("orderdetail_commodityName", commodityName[j]);
                        map.put("orderdetail_commodityCount", "×" + commodityCount[j]);
                        map.put("orderdetail_commodityPrice", "￥" + commodityPrice[j]);
                        listItems.add(map);
                    }
                    adapter = new SimpleAdapter(PlaceOrderActivity.this,listItems,R.layout.orderdetail_item,
                            new String[]{"orderdetail_commodityImg","orderdetail_commodityName","orderdetail_commodityCount","orderdetail_commodityPrice"},
                            new int[]{R.id.orderdetail_commodityImg,R.id.orderdetail_commodityName,R.id.orderdetail_commodityCount,R.id.orderdetail_commodityPrice});
                    listView.setAdapter(adapter);
                    break;
                case 1:
                    TextView tv_shopName = findViewById(R.id.shopName);
                    tv_shopName.setText(String.valueOf(msg.obj));
                    break;
                case 2:
                    TextView tv_sumprice = findViewById(R.id.tv_sumprice);
                    tv_sumprice.setText(String.valueOf(msg.obj));
                    break;
            }
        }
    };

    private void getData() {
        new Thread(() -> {
//            commodityInfoList
            List<CarCommodities> commoditiesList = new ShopCarDao().getOrderByShopIdAndCommodityId(shopId, commodities,userTel);
            Log.e("commoditiesList", String.valueOf(commoditiesList.size()));
            Message msg = handler.obtainMessage();
            msg.obj = commoditiesList;
            msg.what = 0;
            handler.sendMessage(msg);

            Message shopNameMsg = handler.obtainMessage();
            shopNameMsg.obj = new ShopDao().getShopNameById(shopId);
            shopNameMsg.what = 1;
            handler.sendMessage(shopNameMsg);

            Message sumPriceMsg = handler.obtainMessage();
            sumPriceMsg.obj = new ShopCarDao().getSumPrice(shopId, commodities, userTel);
            sumPriceMsg.what = 2;
            handler.sendMessage(sumPriceMsg);

            orderId = new OrderInfoDao().getNextId();

        }).start();
    }

    private void getInfo(){
        listView = findViewById(R.id.lv_listview);

        shopId = getIntent().getExtras().getInt("shopId");
        userTel = getIntent().getExtras().getString("userTel");
        Log.e("userTel", userTel);
        commodities = getIntent().getExtras().getIntArray("commodities");
        for (int i = 0; i < commodities.length; i++) {
            Log.e("aa", String.valueOf(commodities[i]));
        }
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
            int permission = ActivityCompat.checkSelfPermission(PlaceOrderActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(PlaceOrderActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if(ContextCompat.checkSelfPermission(PlaceOrderActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(PlaceOrderActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
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

    private int getImageID(String name) {
        int id = -1;
        try {
            Log.i("name", name);
            Field field = R.drawable.class.getDeclaredField(name);
            id = field.getInt(field.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
