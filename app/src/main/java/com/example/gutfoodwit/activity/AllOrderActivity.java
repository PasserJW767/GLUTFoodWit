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
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gutfoodwit.Dao.OrderInfoDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.bean.CommodityInfo;
import com.example.gutfoodwit.bean.OrderInfo;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllOrderActivity extends AppCompatActivity {

    private ListView listView;


    private SimpleAdapter adapter;
    private List<Map<String,Object>> listItems = null;
    private Map<String,Object> map = null;

    private String userTel;

    private int allOrderCount;

    private int[] shopId = null;
    private int[] orderId = null;
    private String[] shopName = null;
    private String[] orderState = null;
    private String[] orderDate = null;
    private Double[] orderPrice = null;
    private String[] pressButton = null;

    private int[] imageView1 = null;
    private int[] imageView2 = null;
    private int[] imageView3 = null;
    private int[] imageView4 = null;
    private int[] imageView5 = null;

    private String[] textView1 = null;
    private String[] textView2 = null;
    private String[] textView3 = null;
    private String[] textView4 = null;
    private String[] textView5 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(this);
//        设置透明状态栏
        setStatusBar();
        setContentView(R.layout.activity_allorders);
        listView = findViewById(R.id.lv_listview);
        addListener();
        getInfo();
        getData();
    }

    @Override
    protected void onResume(){
        super.onResume();
        getData();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    List<OrderInfo> orderInfoList = (List<OrderInfo>) msg.obj;
                    if(orderInfoList.size() > 0){
                        shopId = new int[allOrderCount];
                        orderId = new int[allOrderCount];
                        shopName = new String[allOrderCount];
                        orderState = new String[allOrderCount];
                        orderDate = new String[allOrderCount];
                        orderPrice = new Double[allOrderCount];
                        pressButton = new String[allOrderCount];
                        initAll(allOrderCount);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        for(int i = 0;i < allOrderCount;i++){
                            shopId[i] = orderInfoList.get(i).getShop_id();
                            orderId[i] = orderInfoList.get(i).getOrder_id();
                            Log.e("aaa", String.valueOf(orderInfoList.get(i).getOrder_id()));
                            shopName[i] = orderInfoList.get(i).getShopName();
                            orderState[i] = orderInfoList.get(i).getOrder_state();
                            Date date = orderInfoList.get(i).getOrder_createTime();
                            orderDate[i] = sdf.format(date);
                            orderPrice[i] = orderInfoList.get(i).getOrder_price();
                            if(orderState[i].equals("已完成")){
                                pressButton[i] = "再来一单";
                            } else if(orderState[i].equals("待付款")){
                                pressButton[i] = "去支付";
                            } else if(orderState[i].equals("待使用")){
                                pressButton[i] = "使用完成";
                            } else if(orderState[i].equals("待评价")){
                                pressButton[i] = "去评价";
                            } else if(orderState[i].equals("待退款")){
                                pressButton[i] = "再来一单";
                            }
                            initImageAndText(i,orderInfoList.get(i).getOrder_id());
                        }
                    } else{
                        findViewById(R.id.allOrders_notice).setVisibility(View.VISIBLE);
                        findViewById(R.id.lv_listview).setVisibility(View.GONE);
                    }
                    break;
                case 1:
                    List<CommodityInfo> commodityInfoList = (List<CommodityInfo>) msg.obj;
                    int i = msg.arg1;
                    if (msg.arg2 == 1){
                        imageView1[i] = getImageID(commodityInfoList.get(0).getCommodity_img());
                        textView1[i] = commodityInfoList.get(0).getCommodity_name();
                    } else if(msg.arg2 == 2){
                        imageView1[i] = getImageID(commodityInfoList.get(0).getCommodity_img());
                        textView1[i] = commodityInfoList.get(0).getCommodity_name();
                        imageView2[i] = getImageID(commodityInfoList.get(1).getCommodity_img());
                        textView2[i] = commodityInfoList.get(1).getCommodity_name();
                    } else if(msg.arg2 == 3){
                        imageView1[i] = getImageID(commodityInfoList.get(0).getCommodity_img());
                        textView1[i] = commodityInfoList.get(0).getCommodity_name();
                        imageView2[i] = getImageID(commodityInfoList.get(1).getCommodity_img());
                        textView2[i] = commodityInfoList.get(1).getCommodity_name();
                        imageView3[i] = getImageID(commodityInfoList.get(2).getCommodity_img());
                        textView3[i] = commodityInfoList.get(2).getCommodity_name();
                    } else if(msg.arg2 == 4){
                        imageView1[i] = getImageID(commodityInfoList.get(0).getCommodity_img());
                        textView1[i] = commodityInfoList.get(0).getCommodity_name();
                        imageView2[i] = getImageID(commodityInfoList.get(1).getCommodity_img());
                        textView2[i] = commodityInfoList.get(1).getCommodity_name();
                        imageView3[i] = getImageID(commodityInfoList.get(2).getCommodity_img());
                        textView3[i] = commodityInfoList.get(2).getCommodity_name();
                        imageView4[i] = getImageID(commodityInfoList.get(3).getCommodity_img());
                        textView4[i] = commodityInfoList.get(3).getCommodity_name();
                    }else if(msg.arg2 == 5){
                        imageView1[i] = getImageID(commodityInfoList.get(0).getCommodity_img());
                        textView1[i] = commodityInfoList.get(0).getCommodity_name();
                        imageView2[i] = getImageID(commodityInfoList.get(1).getCommodity_img());
                        textView2[i] = commodityInfoList.get(1).getCommodity_name();
                        imageView3[i] = getImageID(commodityInfoList.get(2).getCommodity_img());
                        textView3[i] = commodityInfoList.get(2).getCommodity_name();
                        imageView4[i] = getImageID(commodityInfoList.get(3).getCommodity_img());
                        textView4[i] = commodityInfoList.get(3).getCommodity_name();
                        imageView5[i] = getImageID(commodityInfoList.get(4).getCommodity_img());
                        textView5[i] = commodityInfoList.get(4).getCommodity_name();
                    }
                    break;
                case 2:
                    listItems = new ArrayList<>();
                    for(int j = 0;j < allOrderCount;j++){
                        map = new HashMap<>();
                        map.put("order_shopId",shopId[j]);
                        map.put("order_Id", orderId[j]);
                        map.put("shopName", shopName[j]);
                        map.put("orderState",orderState[j]);
                        map.put("orderCreateTime",orderDate[j]);
                        map.put("orderPrice","￥" + orderPrice[j]);
                        map.put("tv_btnState",pressButton[j]);
                        map.put("imageView1",imageView1[j]);
                        map.put("imageView2",imageView2[j]);
                        map.put("imageView3",imageView3[j]);
                        map.put("imageView4",imageView4[j]);
                        map.put("imageView5",imageView5[j]);
                        map.put("textView1", textView1[j]);
                        map.put("textView2", textView2[j]);
                        map.put("textView3", textView3[j]);
                        map.put("textView4", textView4[j]);
                        map.put("textView5", textView5[j]);
                        listItems.add(map);
                    }

                    adapter = new SimpleAdapter(AllOrderActivity.this,listItems,R.layout.order_item,
                            new String[]{"order_shopId","order_Id","shopName","orderState","orderCreateTime","orderPrice","tv_btnState",
                                    "imageView1","imageView2","imageView3","imageView4","imageView5",
                                    "textView1","textView2","textView3","textView4","textView5"},
                            new int[]{R.id.order_shopId,R.id.order_Id,R.id.shopName,R.id.orderState,R.id.orderCreateTime,R.id.orderPrice,R.id.tv_btnState,
                                    R.id.imageView1,R.id.imageView2,R.id.imageView3,R.id.imageView4,R.id.imageView5,
                                    R.id.textView1,R.id.textView2,R.id.textView3,R.id.textView4,R.id.textView5});
                    listView.setAdapter(adapter);
                    break;
            }
        }
    };

    private void init(){
        new Thread(() -> {
            Message msg = handler.obtainMessage();
            msg.what = 2;
            handler.sendMessage(msg);
        }).start();
    }

    private void initImageAndText(int i,int order_id){
        new Thread(() -> {
            List<CommodityInfo> commodityInfoList = new OrderInfoDao().getOrderDetail(order_id);
            Message msg = handler.obtainMessage();
            msg.what = 1;
            msg.arg1 = i;
            msg.arg2 = commodityInfoList.size();
            msg.obj = commodityInfoList;
            handler.sendMessage(msg);
            init();
        }).start();

    }

    private void getDataByContent(String content){
        new Thread(() -> {
            allOrderCount = new OrderInfoDao().getAllOrderCounts(userTel,content);
            List<OrderInfo> orderInfoList = new OrderInfoDao().getAllOrders(userTel,content);
            Message setAllOrderInfo = handler.obtainMessage();
            setAllOrderInfo.what = 0;
            setAllOrderInfo.obj = orderInfoList;
            handler.sendMessage(setAllOrderInfo);
        }).start();
    }

    private void getData(){
        new Thread(() -> {
            allOrderCount = new OrderInfoDao().getAllOrderCount(userTel);
            List<OrderInfo> orderInfoList = new OrderInfoDao().getAllOrder(userTel);
            Message setAllOrderInfo = handler.obtainMessage();
            setAllOrderInfo.what = 0;
            setAllOrderInfo.obj = orderInfoList;
            handler.sendMessage(setAllOrderInfo);
        }).start();
    }

    private void getInfo(){
        userTel = getIntent().getExtras().getString("userTel");
//        userTel = "14795572282";
    }

    private void addListener(){
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btn_clickToSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv_content = findViewById(R.id.edit_search);
                String content = tv_content.getText().toString();
                if(content == null){
                    Toast.makeText(AllOrderActivity.this, "请输入搜索内容~", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(content == "" || content == " "){
                        getData();
                        addListener();
                    }
                    else
                        getDataByContent(content);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv2 = view.findViewById(R.id.order_Id);
                Intent intent = new Intent(AllOrderActivity.this, OrderDetailActivity.class);
                intent.putExtra("orderId", Integer.valueOf(tv2.getText().toString()));
                startActivity(intent);
            }
        });
    }

    private void initAll(int i){
        imageView1 = new int[i];
        imageView2 = new int[i];
        imageView3 = new int[i];
        imageView4 = new int[i];
        imageView5 = new int[i];

        textView1 = new String[i];
        textView2 = new String[i];
        textView3 = new String[i];
        textView4 = new String[i];
        textView5 = new String[i];
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
            int permission = ActivityCompat.checkSelfPermission(AllOrderActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(AllOrderActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if(ContextCompat.checkSelfPermission(AllOrderActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(AllOrderActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
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
