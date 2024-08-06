package com.example.gutfoodwit.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gutfoodwit.Dao.RecommendDao;
import com.example.gutfoodwit.Dao.ShopDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.bean.ShopInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendResultActivity extends AppCompatActivity implements View.OnClickListener {
    private String search;//要查询的关键字
    List<ShopInfo> allSearch=new ArrayList<>();//查询的所有结果

    LinearLayout ll_all;

    LinearLayout ll_order1;
    LinearLayout ll_order2;
    LinearLayout ll_order3;

    private ListView searchListView=null;
    private List<Map<String,Object>> searchItems = null;
    private Map<String,Object> map = null;;
    private SimpleAdapter mAdapter = null;

    private LinearLayout ll_search;
    EditText et_search;//拿到ll_search的EditView
    ImageButton ib_search;//ll_search的ib_search

    private String s;
    private String key;
    private String temp;

    private String userTel;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        checkPermission(this);
//        设置透明状态栏
        setStatusBar();
        setContentView(R.layout.activity_recommend_result);

        ll_all=findViewById(R.id.ll_select);

        ll_order1=findViewById(R.id.ll_order1);
        ll_order2=findViewById(R.id.ll_order2);
        ll_order3=findViewById(R.id.ll_order3);
        ll_order1.setOnClickListener(this);
        ll_order2.setOnClickListener(this);
        ll_order3.setOnClickListener(this);


        userTel = getIntent().getExtras().getString("userTel");
        searchListView=findViewById(R.id.lv_searchlistview);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView shopId=view.findViewById(R.id.recommend_shopId);
                Intent intent = new Intent(RecommendResultActivity.this, ShopDetailActivity.class);
                intent.putExtra("id", Integer.parseInt(shopId.getText().toString()));
                intent.putExtra("userTel", userTel);
                startActivity(intent);
            }
        });



        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        s = getIntent().getExtras().getString("BigAndSmall");
        key = getIntent().getExtras().getString("key");
        temp = "";
        initSearch(temp);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    searchListView.setAdapter(null);
                    initSearch(temp);
                    break;
                case 0:
                    et_search = (EditText) ll_search.getChildAt(1);//拿到EditView的值
                    ib_search=(ImageButton)ll_search.getChildAt(2);//按钮键
                    et_search.setOnClickListener(RecommendResultActivity.this);
                    ib_search.setOnClickListener(RecommendResultActivity.this);
                    break;
                case 1:
                    mAdapter = new SimpleAdapter(RecommendResultActivity.this, (List<Map<String,Object>>) msg.obj,R.layout.recommendserach_item,
                            new String[]{"recommend_shop_Icon", "recommend_shopId", "recommend_shopName", "recommend_shopStar", "recommend_shopAverage", "recommend_shopTypeF", "recommend_shopTypeS","recommend_shopSales"},
                            new int[]{R.id.recommend_shop_Icon, R.id.recommend_shopId, R.id.recommend_shopName, R.id.recommend_shopStar, R.id.recommend_shopAverage, R.id.recommend_shopTypeF, R.id.recommend_shopTypeS,R.id.recommend_shopSales});
                    searchListView.setAdapter(mAdapter);
                    break;
                case 2:
                    findViewById(R.id.allOrders_notice).setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_order).setVisibility(View.GONE);
            }
        }
    };

    protected void initSearch(String temp){
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
//                Message message=handler.obtainMessage();
//                message.what=0;
//                handler.sendMessage(message);

                allSearch = searchInfoList(temp);
                if(allSearch.size() == 0){
                    Message message = handler.obtainMessage();
                    message.what = 2;
                    handler.sendMessage(message);
                    return ;
                }

                Log.i("查询个数", String.valueOf(allSearch.size()));

                int size = allSearch.size();
                int[] recommend_shop_Icon = new int[size];
                int[] recommend_shopId = new int[size];
                String[] recommend_shopName = new String[size];
                String[] recommend_shopStar = new String[size];
                int[] recommend_shopAverage = new int[size];
                int[] recommend_shopSales = new int[size];
                String[] recommend_shopTypeF = new String[size];
                String[] recommend_shopTypeS = new String[size];

                for (int i=0;i<size;i++){
                    recommend_shop_Icon[i] = getImageID(allSearch.get(i).getShop_icon());
                    recommend_shopId[i] = allSearch.get(i).getId();
                    recommend_shopName[i] = allSearch.get(i).getShop_name();
                    recommend_shopStar[i] = allSearch.get(i).getStar_size();
                    recommend_shopAverage[i] = allSearch.get(i).getAverage_price();
                    recommend_shopSales[i] = allSearch.get(i).getMonthly_sales();
                    recommend_shopTypeF[i] = allSearch.get(i).getShop_type();
                    recommend_shopTypeS[i] = allSearch.get(i).getShop_detail_type();
                }

                searchItems=new ArrayList<>();
                for(int i = 0;i < size;i++){
                    map = new HashMap<>();
                    map.put("recommend_shop_Icon", recommend_shop_Icon[i]);
                    map.put("recommend_shopId", String.valueOf(recommend_shopId[i]));
                    map.put("recommend_shopName", recommend_shopName[i]);
                    map.put("recommend_shopStar", recommend_shopStar[i].substring(0,3));
                    map.put("recommend_shopAverage", recommend_shopAverage[i]);
                    map.put("recommend_shopSales", recommend_shopSales[i]);
                    map.put("recommend_shopTypeF", recommend_shopTypeF[i]);
                    map.put("recommend_shopTypeS", recommend_shopTypeS[i]);
                    searchItems.add(map);
                }
                Message message1=handler.obtainMessage();
                message1.what=1;
                message1.obj=searchItems;
                handler.sendMessage(message1);
            }
        }).start();

    }

    protected List<ShopInfo> searchInfoList(String temp){
        Log.e("s", s);
        Log.e("temp", temp);
        if(s.equals("Big")){
            if("".equals(temp))
                return new RecommendDao().getTypeShopsBig(key);
            else if("a".equals(temp))
                return new RecommendDao().getTypeShopsBigOrderByStar(key);
            else if("b".equals(temp))
                return new RecommendDao().getTypeShopsBigOrderBySales(key);
            else if("c".equals(temp))
                return new RecommendDao().getTypeShopsBigOrderByAvg(key);
        } else if(s.equals("Small")){
            if("".equals(temp))
                return new RecommendDao().getTypeShopsSmall(key);
            else if("a".equals(temp))
                return new RecommendDao().getTypeShopsSmallOrderByStar(key);
            else if("b".equals(temp))
                return new RecommendDao().getTypeShopsSmallOrderBySales(key);
            else if("c".equals(temp))
                return new RecommendDao().getTypeShopsSmallOrderByAvg(key);
        }
        return null;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ll_order1){
            temp="a";
            Message message=handler.obtainMessage();
            message.what=100;
            handler.sendMessage(message);
            Toast.makeText(this,"已按评分排序！",Toast.LENGTH_LONG).show();
        }else if(v.getId()==R.id.ll_order2){
            temp="b";
            Message message=handler.obtainMessage();
            message.what=100;
            handler.sendMessage(message);
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            Toast.makeText(this,"已按销量排序！",Toast.LENGTH_LONG).show();
        }else if(v.getId()==R.id.ll_order3){
            temp="c";
            Message message=handler.obtainMessage();
            message.what=100;
            handler.sendMessage(message);
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            Toast.makeText(this,"已按人均排序！",Toast.LENGTH_LONG).show();
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
            int permission = ActivityCompat.checkSelfPermission(RecommendResultActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(RecommendResultActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if(ContextCompat.checkSelfPermission(RecommendResultActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(RecommendResultActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
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
            Log.i("图片name",name);
            Field field = R.drawable.class.getDeclaredField(name);
            id = field.getInt(field.getName());
        } catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }
}