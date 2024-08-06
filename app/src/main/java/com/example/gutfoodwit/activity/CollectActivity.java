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
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gutfoodwit.Dao.UserCollectDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.bean.UserCollect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectActivity extends AppCompatActivity {

    private ListView listView = null;
    private List<Map<String,Object>> listItems = null;
    private Map<String,Object> map = null;

    private int[] imageId = null;
    private int[] shopId = null;;
    private String[] shopName = null;
    private String[] shopStar = null;
    private int[] shopSales = null;
    private int[] shopAverage = null;
    private SimpleAdapter mAdapter = null;


    private int xing;

    private int dataLength;
    private String userTel = "14795572282";
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(this);
//        设置透明状态栏
        setStatusBar();
        setContentView(R.layout.activity_collect);
        listView = findViewById(R.id.lv_listview);
        userTel = getIntent().getExtras().getString("userTel");
        getData();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    List<UserCollect> orderList = (List<UserCollect>) msg.obj;
                    if(orderList != null){
                        imageId = new int[msg.arg1];
                        shopId = new int[msg.arg1];
                        shopName = new String[msg.arg1];
                        shopStar = new String[msg.arg1];
                        shopSales = new int[msg.arg1];
                        shopAverage = new int[msg.arg1];
                        for(int i = 0;i < dataLength;i++){
                            shopId[i] = orderList.get(i).getShopId();
                            imageId[i] = getImageID(orderList.get(i).getShopIcon());
                            shopName[i] = orderList.get(i).getShopName();
                            shopStar[i] = orderList.get(i).getShopStar();
                            shopSales[i] = orderList.get(i).getShopSale();
                            shopAverage[i] = orderList.get(i).getShopAverage();
                        }
                        xing = getImageID("xing");
                    }
                    break;
                case 2:
                    listItems = new ArrayList<>();
                    for(int i = 0;i < dataLength;i++){
                        map = new HashMap<>();
                        map.put("shopid",shopId[i]);
                        map.put("image", imageId[i]);
                        map.put("shopName", shopName[i]);
                        map.put("xing",xing);
                        map.put("shopStar",shopStar[i]);
                        map.put("shopSales","月销量" + shopSales[i]);
                        map.put("shopAverage","￥" + shopAverage[i]);
                        listItems.add(map);
                    }
                    break;
                case 3:
                    mAdapter = new SimpleAdapter(CollectActivity.this,listItems,R.layout.collect_item,
                            new String[]{"shopid","image","shopName","xing","shopStar","shopSales","shopAverage"},
                            new int[]{R.id.shopid,R.id.image,R.id.shopName,R.id.xing,R.id.shopStar,R.id.shopSales,R.id.shopAverage});
                    listView.setAdapter(mAdapter);
                    break;
                case 4:
                    getData();
                    break;
                case 5:
                    findViewById(R.id.allOrders_notice).setVisibility(View.VISIBLE);
                    findViewById(R.id.lv_listview).setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void getData(){
        new Thread(() -> {
            List<UserCollect> orderList = new UserCollectDao().getUserCollectShop(userTel);
            dataLength = orderList.size();
            if(dataLength != 0){
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = orderList;
                msg.arg1 = dataLength;
                handler.sendMessage(msg);

                Message loadInfoMsg = handler.obtainMessage();
                loadInfoMsg.what = 2;
                handler.sendMessage(loadInfoMsg);

                Message loadToListView = handler.obtainMessage();
                loadToListView.what = 3;
                handler.sendMessage(loadToListView);
            }
            else {
                Message nothingMsg = handler.obtainMessage();
                nothingMsg.what = 5;
                handler.sendMessage(nothingMsg);
            }
        }).start();
    }

    private void deleteCollectShop(String userTel,int shopId){
        new Thread(() -> {
            flag = new UserCollectDao().deleteCollectShop(userTel,shopId);
            if(flag = true){
                Looper.prepare();
                Toast.makeText(CollectActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                Message deleteMsg = handler.obtainMessage();
                deleteMsg.what = 4;
                handler.sendMessage(deleteMsg);
                Looper.loop();
            } else {
                Looper.prepare();
                Toast.makeText(CollectActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    private void init(){
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.shopid);
                int shopId = Integer.parseInt(textView.getText().toString());
                Intent intent = new Intent(CollectActivity.this, ShopDetailActivity.class);
                intent.putExtra("id", shopId);
                intent.putExtra("userTel", userTel);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CollectActivity.this);
                builder.setCancelable(true);
                builder.setTitle("删除");
                builder.setIcon(R.mipmap.collect_crush);
                TextView tv_name = view.findViewById(R.id.shopName);
                TextView tv_id = view.findViewById(R.id.shopid);
                builder.setMessage("是否要删除你收藏的店铺：" + tv_name.getText().toString() + "?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCollectShop(userTel,Integer.parseInt(tv_id.getText().toString()));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
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
            int permission = ActivityCompat.checkSelfPermission(CollectActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(CollectActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if(ContextCompat.checkSelfPermission(CollectActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(CollectActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
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
