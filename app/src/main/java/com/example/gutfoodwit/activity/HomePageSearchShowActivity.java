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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gutfoodwit.Dao.SearchDao;
import com.example.gutfoodwit.Dao.ShopCarDao;
import com.example.gutfoodwit.Dao.ShopDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.bean.SearchInfo;
import com.example.gutfoodwit.bean.ShopInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageSearchShowActivity extends AppCompatActivity implements View.OnClickListener {
    private String search;//要查询的关键字
    List<SearchInfo> allSearch=new ArrayList<>();//查询的所有结果

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

    private TextView tv_count;// 显示购物车图标中的商品数量
    private ImageView iv_cart;//购物车图标*/

    String userTel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        checkPermission(this);
//        设置透明状态栏
        setStatusBar();
        setContentView(R.layout.activity_homa_page_search_show);

        ll_search=findViewById(R.id.ll_search);
        ll_all=findViewById(R.id.ll_select);

        ll_order1=findViewById(R.id.ll_order1);
        ll_order2=findViewById(R.id.ll_order2);
        ll_order3=findViewById(R.id.ll_order3);
        ll_order1.setOnClickListener(this);
        ll_order2.setOnClickListener(this);
        ll_order3.setOnClickListener(this);

        userTel=getIntent().getExtras().getString("userTel");
        tv_count=findViewById(R.id.tv_count);
        iv_cart=findViewById(R.id.iv_cart);
        iv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建一个意图对象，准备跳到指定的活动页面
                Intent intent=new Intent(HomePageSearchShowActivity.this,ShopCarActivity.class);
                intent.putExtra("userTel",userTel);//
                // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置启动标志
                startActivity(intent); // 跳转到意图对象指定的活动页面
            }
        });
        showCount(); // 显示购物车的商品数量

        searchListView=findViewById(R.id.lv_searchlistview);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position", String.valueOf(position));
                TextView shopName=view.findViewById(R.id.tv_shop_name);
                String shop_name=shopName.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int id= ShopDao.getShopIdByShopname(shop_name);
                        Log.e("点击的是",shop_name);
                        Log.e("点击的ID", String.valueOf(id));
                        Intent intent = new Intent(HomePageSearchShowActivity.this, ShopDetailActivity.class);
                        intent.putExtra("userTel",userTel);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                }).start();
            }
        });

        search=getIntent().getStringExtra("search");
        initSearch(search);
    }

    @Override
    protected void onResume(){
        super.onResume();
        showCount();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    searchListView.setAdapter(null);
                    initSearch(search);
                    break;
                case 0:
                    et_search = (EditText) ll_search.getChildAt(1);//拿到EditView的值
                    ib_search=(ImageButton)ll_search.getChildAt(2);//按钮键
                    et_search.setOnClickListener(HomePageSearchShowActivity.this);
                    ib_search.setOnClickListener(HomePageSearchShowActivity.this);
                    break;
                case 1:
                    mAdapter = new SimpleAdapter(HomePageSearchShowActivity.this, (List<Map<String,Object>>) msg.obj,R.layout.search_item,
                            new String[]{"iv_commodity_img", "tv_commodity_name", "tv_commodity_price", "tv_commodity_sales", "iv_shop_icon", "tv_shop_name", "tv_star_size", "tv_monthly_sales", "tv_average_price"},
                            new int[]{R.id.iv_commodity_img, R.id.tv_commodity_name, R.id.tv_commodity_price, R.id.tv_commodity_sales, R.id.iv_shop_icon, R.id.tv_shop_name, R.id.tv_star_size, R.id.tv_monthly_sales, R.id.tv_average_price});
                    searchListView.setAdapter(mAdapter);
                    break;
                case 2:
                    tv_count.setText(msg.obj + "");
                    break;
            }
        }
    };

    static String s="";
    protected void initSearch(String search){
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=handler.obtainMessage();
                message.what=0;
                handler.sendMessage(message);

                allSearch=searchInfoList(search,s);
                Log.i("查询个数", String.valueOf(allSearch.size()));

                int size =allSearch.size();
                int[] commodity_img=new int[size];
                String[] commodity_name=new String[size];
                double[] commodity_price=new double[size];
                int[] commodity_sales=new int[size];
                int[] shop_icon=new int[size];
                String[] shop_name=new String[size];
                String[] star_size=new String[size];
                int[] monthly_sales=new int[size];
                int[] average_price=new int[size];
                for (int i=0;i<size;i++){
                    commodity_img[i]=getImageID(allSearch.get(i).getCommodity_img());
                    commodity_name[i]=allSearch.get(i).getCommodity_name();
                    commodity_price[i]=allSearch.get(i).getCommodity_price();
                    commodity_sales[i]=allSearch.get(i).getCommodity_sales();
                    shop_icon[i]=getImageID(allSearch.get(i).getShop_icon());
                    shop_name[i]=allSearch.get(i).getShop_name();
                    star_size[i]=allSearch.get(i).getStar_size();
                    monthly_sales[i]=allSearch.get(i).getMonthly_sales();
                    average_price[i]=allSearch.get(i).getAverage_price();
                }

                searchItems=new ArrayList<>();
                for(int i = 0;i < size;i++){
                    map = new HashMap<>();
                    map.put("iv_commodity_img",commodity_img[i]);
                    map.put("tv_commodity_name", commodity_name[i]);
                    map.put("tv_commodity_price", commodity_price[i]);
                    map.put("tv_commodity_sales",commodity_sales[i]);
                    map.put("iv_shop_icon",shop_icon[i]);
                    map.put("tv_shop_name",shop_name[i]);
                    map.put("tv_star_size", star_size[i]);
                    map.put("tv_monthly_sales",monthly_sales[i]);
                    map.put("tv_average_price",average_price[i]);
                    searchItems.add(map);
                }
                Message message1=handler.obtainMessage();
                message1.what=1;
                message1.obj=searchItems;
                handler.sendMessage(message1);
            }
        }).start();

    }

    protected List<SearchInfo> searchInfoList(String search, String s){
        if("".equals(s))
            return SearchDao.getAllSearchByCommodityName(search);
        else if("a".equals(s))
            return SearchDao.getAllSearchByCommodityNameOrderByStar(search);
        else if("b".equals(s))
            return SearchDao.getAllSearchByCommodityNameOrderBySales(search);
        else if("c".equals(s))
            return SearchDao.getAllSearchByCommodityNameOrderByAvg(search);
        else return null;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.ib_search){
            Intent intent=new Intent(this,HomePageSearchShowActivity.class);
            intent.putExtra("search",et_search.getText().toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if(v.getId()==R.id.ll_order1){
            s="a";
            Message message=handler.obtainMessage();
            message.what=100;
            handler.sendMessage(message);
            Toast.makeText(this,"已按评分排序！",Toast.LENGTH_LONG).show();
        }else if(v.getId()==R.id.ll_order2){
            s="b";
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
            s="c";
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

    // 显示购物车图标中的商品数量
    public void showCount() {
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                int countCar= ShopCarDao.getCommiditiesCount(userTel);

                Message ms=handler.obtainMessage();
                ms.what=2;
                ms.obj=countCar;
                handler.sendMessage(ms);
            }
        }).start();

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
            int permission = ActivityCompat.checkSelfPermission(HomePageSearchShowActivity.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(HomePageSearchShowActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if(ContextCompat.checkSelfPermission(HomePageSearchShowActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(HomePageSearchShowActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}