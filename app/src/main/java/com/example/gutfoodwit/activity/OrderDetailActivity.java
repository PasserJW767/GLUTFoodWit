package com.example.gutfoodwit.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gutfoodwit.Dao.OrderInfoDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.bean.CommodityInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> listItems = null;
    private Map<String, Object> map = null;

    private int orderId;
    private String userTel;
    private int shopId;

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
        setContentView(R.layout.activity_orderdetail);
        listView = findViewById(R.id.lv_listview);
        initOrderId();
        getData();
        initListener();
    }

    private void initListener(){
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.orderdetail_shopName).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.orderdetail_shopName:
                TextView tv_orderdetail_shopId = findViewById(R.id.orderdetail_shopId);
                String shopId = tv_orderdetail_shopId.getText().toString();
                Intent intent = new Intent(OrderDetailActivity.this, ShopDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(shopId));
                startActivity(intent);
                break;
            case R.id.btn_goToPay:
                String[] chooseArr = new String[]{"否","是"};
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setTitle("是否完成支付？");
                builder.setSingleChoiceItems(chooseArr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(chooseArr[which].equals("是")){
                            TextView tv_orderState = findViewById(R.id.orderdetail_orderState);
                            tv_orderState.setText("订单待使用");
                            findViewById(R.id.btn_goToPay).setVisibility(View.GONE);
                            findViewById(R.id.btn_useComplete).setVisibility(View.VISIBLE);
                            findViewById(R.id.btn_useComplete).setOnClickListener(OrderDetailActivity.this);
                            updateOrderCondition();
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.btn_useComplete:
                chooseArr = new String[]{"否","是"};
                builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setTitle("是否已经拿到餐？");
                builder.setSingleChoiceItems(chooseArr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(chooseArr[which].equals("是")){
                            TextView tv_orderState = findViewById(R.id.orderdetail_orderState);
                            tv_orderState.setText("订单已完成");
                            findViewById(R.id.btn_useComplete).setVisibility(View.GONE);
                            findViewById(R.id.btn_orderAgain).setVisibility(View.VISIBLE);
                            findViewById(R.id.btn_orderAgain).setOnClickListener(OrderDetailActivity.this);
                            findViewById(R.id.btn_evaluate).setVisibility(View.VISIBLE);
                            findViewById(R.id.btn_evaluate).setOnClickListener(OrderDetailActivity.this);
                            findViewById(R.id.btn_backMoney).setVisibility(View.VISIBLE);
                            findViewById(R.id.btn_backMoney).setOnClickListener(OrderDetailActivity.this);
                            completeOrderCondition();
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.btn_orderAgain:
                break;
            case R.id.btn_backMoney:
                chooseArr = new String[]{"否","是"};
                builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setTitle("是否申请退款？");
                builder.setSingleChoiceItems(chooseArr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(chooseArr[which].equals("是")){
                            TextView tv_orderState = findViewById(R.id.orderdetail_orderState);
                            tv_orderState.setText("订单待退款");
                            findViewById(R.id.btn_backMoney).setVisibility(View.GONE);
                            orderWaitToBackMoney();
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.btn_evaluate:
                chooseArr = new String[]{"5","4","3","2","1"};
                builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setTitle("请给这家店铺打分");
                builder.setSingleChoiceItems(chooseArr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(chooseArr[which].equals("5")){
                            decidePoint(Double.parseDouble("5"));
                            Toast.makeText(OrderDetailActivity.this, "评分完成，您的评分为5分",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            detailPoint(Integer.valueOf(chooseArr[which]));
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
    }

    private void detailPoint(int point){
        switch (point){
            case 1:
                String[] chooseArr = new String[]{"1.0","1.1","1.2","1.3","1.4","1.5","1.6","1.7","1.8","1.9"};
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setTitle("选择一个更详细的分数吧");
                builder.setSingleChoiceItems(chooseArr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        decidePoint(Double.parseDouble(chooseArr[which]));
                        findViewById(R.id.btn_evaluate).setVisibility(View.GONE);
                        Toast.makeText(OrderDetailActivity.this, "评分完成，您的评分为" + chooseArr[which] + "分",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case 2:
                chooseArr = new String[]{"2.0","2.1","2.2","2.3","2.4","2.5","2.6","2.7","2.8","2.9"};
                builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setTitle("选择一个更详细的分数吧");
                builder.setSingleChoiceItems(chooseArr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        decidePoint(Double.parseDouble(chooseArr[which]));
                        findViewById(R.id.btn_evaluate).setVisibility(View.GONE);
                        Toast.makeText(OrderDetailActivity.this, "评分完成，您的评分为" + chooseArr[which] + "分",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case 3:
                chooseArr = new String[]{"3.0","3.1","3.2","3.3","3.4","3.5","3.6","3.7","3.8","3.9"};
                builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setTitle("选择一个更详细的分数吧");
                builder.setSingleChoiceItems(chooseArr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        decidePoint(Double.parseDouble(chooseArr[which]));
                        findViewById(R.id.btn_evaluate).setVisibility(View.GONE);
                        Toast.makeText(OrderDetailActivity.this, "评分完成，您的评分为" + chooseArr[which] + "分",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case 4:
                chooseArr = new String[]{"4.0","4.1","4.2","4.3","4.4","4.5","4.6","4.7","4.8","4.9"};
                builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setTitle("选择一个更详细的分数吧");
                builder.setSingleChoiceItems(chooseArr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        decidePoint(Double.parseDouble(chooseArr[which]));
                        findViewById(R.id.btn_evaluate).setVisibility(View.GONE);
                        Toast.makeText(OrderDetailActivity.this, "评分完成，您的评分为" + chooseArr[which] + "分",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
        }
    }

    private void decidePoint(double point){
        new Thread(() -> {
            new OrderInfoDao().updateShopPoint(point, shopId, orderId);
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    List<CommodityInfo> commodityInfoList = (List<CommodityInfo>) msg.obj;
                    int size = commodityInfoList.size();
                    commodityImg = new int[size];
                    commodityName = new String[size];
                    commodityCount = new int[size];
                    commodityPrice = new double[size];
                    for(int i = 0;i < size;i++){
                        commodityImg[i] = getImageID(commodityInfoList.get(i).getCommodity_img());
                        commodityName[i] = commodityInfoList.get(i).getCommodity_name();
                        commodityCount[i] = commodityInfoList.get(i).getCommodity_count();
                        commodityPrice[i] = commodityInfoList.get(i).getCommodity_count() * commodityInfoList.get(i).getCommodity_price();
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
                    adapter = new SimpleAdapter(OrderDetailActivity.this,listItems,R.layout.orderdetail_item,
                            new String[]{"orderdetail_commodityImg","orderdetail_commodityName","orderdetail_commodityCount","orderdetail_commodityPrice"},
                            new int[]{R.id.orderdetail_commodityImg,R.id.orderdetail_commodityName,R.id.orderdetail_commodityCount,R.id.orderdetail_commodityPrice});
                    listView.setAdapter(adapter);
                    break;
                case 1:
                    TextView tv_shopName = findViewById(R.id.orderdetail_shopName);
                    tv_shopName.setText(String.valueOf(msg.obj) + ">");
                    TextView tv_shopId = findViewById(R.id.orderdetail_shopId);
                    tv_shopId.setText(String.valueOf(msg.arg1));
                    break;
                case 2:
                    TextView tv_sumPrice = findViewById(R.id.orderdetail_sumPrice);
                    tv_sumPrice.setText(String.valueOf(msg.obj));
                    break;
                case 3:
                    TextView tv_orderdetail_orderState = findViewById(R.id.orderdetail_orderState);
                    tv_orderdetail_orderState.setText("订单" + String.valueOf(msg.obj));
                    if ("待付款".equals(String.valueOf(msg.obj))){
                        findViewById(R.id.btn_goToPay).setVisibility(View.VISIBLE);
                        findViewById(R.id.btn_goToPay).setOnClickListener(OrderDetailActivity.this);
                    } else if("待使用".equals(String.valueOf(msg.obj))){
                        findViewById(R.id.btn_useComplete).setVisibility(View.VISIBLE);
                        findViewById(R.id.btn_useComplete).setOnClickListener(OrderDetailActivity.this);
                    } else if ("已完成".equals(String.valueOf(msg.obj))) {
                        if(msg.arg1 == 1){
                            findViewById(R.id.btn_backMoney).setVisibility(View.VISIBLE);
                            findViewById(R.id.btn_backMoney).setOnClickListener(OrderDetailActivity.this);
                        }
                        if(msg.arg2 == 0){
                            findViewById(R.id.btn_evaluate).setVisibility(View.VISIBLE);
                            findViewById(R.id.btn_evaluate).setOnClickListener(OrderDetailActivity.this);
                        }
                        findViewById(R.id.btn_orderAgain).setVisibility(View.VISIBLE);
                        findViewById(R.id.btn_orderAgain).setOnClickListener(OrderDetailActivity.this);
                    } else if ("待退款".equals(String.valueOf(msg.obj))) {
                        findViewById(R.id.btn_orderAgain).setVisibility(View.VISIBLE);
                        findViewById(R.id.btn_orderAgain).setOnClickListener(OrderDetailActivity.this);
                    }
                    break;
            }
        }
    };

    private void orderWaitToBackMoney(){
        new Thread(() -> {
            new OrderInfoDao().orderWaitToBackMoney(orderId);
        }).start();
    }

    private void completeOrderCondition(){
        new Thread(() -> {
            new OrderInfoDao().completeOrderCondition(orderId);
        }).start();
    }

    private void updateOrderCondition(){
        new Thread(() -> {
            new OrderInfoDao().updateOrderCondition(orderId);
        }).start();
    }

    private void getData() {
        new Thread(() -> {
            List<CommodityInfo> commodityInfoList = new OrderInfoDao().getOrderCommodities(orderId);
            Message msg = handler.obtainMessage();
            msg.what = 0;
            msg.obj = commodityInfoList;
            handler.sendMessage(msg);

            Message shopMessage = handler.obtainMessage();
            shopMessage.what = 1;
            shopMessage.obj = commodityInfoList.get(0).getShop_name();
            shopMessage.arg1 = commodityInfoList.get(0).getId();
            shopId = shopMessage.arg1;
            handler.sendMessage(shopMessage);

            double sumPrice = new OrderInfoDao().getOrderSumPrice(orderId);
            Message sumPriceMessage = handler.obtainMessage();
            sumPriceMessage.what = 2;
            sumPriceMessage.obj = sumPrice;
            handler.sendMessage(sumPriceMessage);

            String orderState = new OrderInfoDao().getOrderState(orderId);
            Message orderStateMessage = handler.obtainMessage();
            orderStateMessage.what = 3;
            orderStateMessage.obj = orderState;
            orderStateMessage.arg1 = new OrderInfoDao().judgeTime(orderId);
            orderStateMessage.arg2 = new OrderInfoDao().judgePoint(orderId);
            handler.sendMessage(orderStateMessage);



        }).start();
    }

    private void initOrderId() {
        userTel = getIntent().getExtras().getString("userTel");
        orderId = getIntent().getExtras().getInt("orderId");
        Log.e("orderId", String.valueOf(orderId));
//        orderId = 1;
    }


    /**
     * 开启本地读写权限
     */
    private void checkPermission(Activity activity) {//开启本地的照片读取与写入权限
        // Storage Permissions
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,//读内存权限
                Manifest.permission.WRITE_EXTERNAL_STORAGE};//写内存权限
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(OrderDetailActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(OrderDetailActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置透明状态栏
     */
    private void setStatusBar() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
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
