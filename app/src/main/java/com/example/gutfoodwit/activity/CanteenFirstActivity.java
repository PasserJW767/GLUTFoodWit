package com.example.gutfoodwit.activity;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Message;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gutfoodwit.Dao.ShopDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.bean.ShopInfo;


import java.lang.reflect.Field;
import java.util.List;

public class CanteenFirstActivity extends AppCompatActivity implements View.OnClickListener{
    int canten_id=1;
    LinearLayout layout;//选择部分
    LinearLayout ll_all;//整个页面线性布局
    ScrollView sv_all;//整个页面的滚动视图
    LinearLayout ll_item;//每个店铺视图
    LinearLayout[] allChildLinearLayout = null;//存放各个店铺视图

    LinearLayout ll_order1;
    LinearLayout ll_order2;
    LinearLayout ll_order3;

    String userTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_first);
        userTel=getIntent().getExtras().getString("userTel");

        initContent();//初始化店铺布局
        showShops();

        layout=findViewById(R.id.ll_first);
        layout.addView(ll_all);

        ll_order1=findViewById(R.id.ll_order1);
        ll_order2=findViewById(R.id.ll_order2);
        ll_order3=findViewById(R.id.ll_order3);
        ll_order1.setOnClickListener(this);
        ll_order2.setOnClickListener(this);
        ll_order3.setOnClickListener(this);

    }

    private void initContent(){
        //整个页面线性布局
        ll_all=new LinearLayout(CanteenFirstActivity.this);
        LayoutParams llParaAll=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1500);
        ll_all.setOrientation(LinearLayout.VERTICAL);

        //整个页面的滚动视图
        sv_all=new ScrollView(CanteenFirstActivity.this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                1110
        );
        sv_all.setLayoutParams(layoutParams);

//                创建每个店铺布局
        ll_item = new LinearLayout(CanteenFirstActivity.this);
        LayoutParams lpPara = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        lpPara.setMargins(5,0,0,5);
        ll_item.setOrientation(LinearLayout.VERTICAL);


        sv_all.setBackground(getResources().getDrawable(R.drawable.canteen_acticity_background));
        sv_all.addView(ll_item);
        ll_all.addView(sv_all);
        ll_all.setLayoutParams(llParaAll);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    ll_item.removeAllViews();
                    showShops();
                    break;
                case 0:
                    //初始化所有的子布局
                    int arrLength = Integer.parseInt(String.valueOf(msg.obj));
                    LayoutParams allPara = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300);
                    allPara.setMargins(30,10,30,10);
                    allChildLinearLayout = new LinearLayout[arrLength];
                    for(int i = 0;i < arrLength;i++){
                        allChildLinearLayout[i] = new LinearLayout(CanteenFirstActivity.this);
                        allChildLinearLayout[i].setOrientation(LinearLayout.HORIZONTAL);
                        allChildLinearLayout[i].setBackground(getResources().getDrawable(R.drawable.shop_background));
                        allChildLinearLayout[i].setLayoutParams(allPara);
                        int j=i;
                        allChildLinearLayout[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TextView tv1=(TextView)allChildLinearLayout[j].getChildAt(1);
                                TextView tv=(TextView)allChildLinearLayout[j].getChildAt(6);
                               /* Log.i("店铺",(String) tv1.getText());
                                Log.i("id",(String) tv.getText());*/

                                // 创建一个意图对象，准备跳到指定的活动页面
                                Intent intent = new Intent(CanteenFirstActivity.this, ShopDetailActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("id",Integer.parseInt(tv.getText().toString()));
                                intent.putExtra("userTel", userTel);
                                startActivity(intent); // 跳转到意图对象指定的活动页面
                            }
                        });
                    }
                    break;
                case 1:
//                    初始化所有的店铺图标
                    LayoutParams iv_shopIconPara = new LayoutParams(280, 260);
                    iv_shopIconPara.setMargins(20,0,0,0);
                    ImageView iv_shopIcon = new ImageView(CanteenFirstActivity.this);
                    //  设置图标
                    iv_shopIcon.setImageResource(msg.arg2);
                    //  设置布局
                    iv_shopIcon.setLayoutParams(iv_shopIconPara);
                    //  加入到子布局中
                    allChildLinearLayout[msg.arg1].addView(iv_shopIcon);
                    break;
                case 2:
                    //初始化所有店名
                    LayoutParams tv_Para = new LayoutParams(
                            550,
                            80
                    );
                    TextView tv_shopname = new TextView(CanteenFirstActivity.this);
                    tv_shopname.setTextSize(22);
                    tv_shopname.setText(String.valueOf(msg.obj));
                    tv_Para.setMargins(20,15,0,0);
                    tv_shopname.setLayoutParams(tv_Para);
                    allChildLinearLayout[msg.arg1].addView(tv_shopname);
                    break;
                case 3:
                    //初始化所有店“星星“
                    LayoutParams iv_starPara = new LayoutParams(40, 40);
                    ImageView iv_star = new ImageView(CanteenFirstActivity.this);
                    iv_starPara.setMargins(-550,110,0,0);
                    int path = Integer.parseInt(String.valueOf(msg.obj));
                    iv_star.setImageResource(path);
                    iv_star.setLayoutParams(iv_starPara);
                    allChildLinearLayout[msg.arg1].addView(iv_star);
                    break;
                case 4:
                    //初始化所有评分
                    LayoutParams tv_starPara = new LinearLayout.LayoutParams(100,80);
                    tv_starPara.setMargins(0,90,0,0);
                    TextView tv_starsize = new TextView(CanteenFirstActivity.this);
                    tv_starsize.setText(String.valueOf(msg.obj));
                    tv_starsize.setTextSize(18);
                    tv_starsize.setTextColor(0xffff9933);
                    tv_starsize.setLayoutParams(tv_starPara);
                    allChildLinearLayout[msg.arg1].addView(tv_starsize);
                    break;
                case 5:
                    LayoutParams tv_monthlysalesPara=new LinearLayout.LayoutParams(300,80) ;
                    tv_monthlysalesPara.setMargins(70,90,0,0);
                    TextView tv_monthlysales=new TextView(CanteenFirstActivity.this);
                    tv_monthlysales.setTextSize(14);
                    tv_monthlysales.setText("月售："+msg.obj);
                    tv_monthlysales.setLayoutParams(tv_monthlysalesPara);
                    allChildLinearLayout[msg.arg1].addView(tv_monthlysales);
                    break;
                case 6:
                    LayoutParams tv_averagepricePara=new LinearLayout.LayoutParams(300,80);
                    tv_averagepricePara.setMargins(10,90,0,0);
                    TextView tv_averageprice=new TextView(CanteenFirstActivity.this);
                    tv_averageprice.setText("人均￥"+msg.obj);
                    tv_averageprice.setTextSize(14);
                    tv_averageprice.setLayoutParams(tv_averagepricePara);
                    allChildLinearLayout[msg.arg1].addView(tv_averageprice);
                    break;
                case 7:
                    TextView tv_id=new TextView(CanteenFirstActivity.this);
                    tv_id.setText(msg.obj+"");
                    tv_id.setVisibility(View.INVISIBLE);
                    allChildLinearLayout[msg.arg1].addView(tv_id);
                    break;
                case 8:
                    ll_item.addView(allChildLinearLayout[msg.arg1]);
            }
        }
    };

    static String s="";
    protected void showShops(){
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用数据库工具类DBUtils的getAllShop方法获取数据库表中数据
                List<ShopInfo> shopInfoList= shopInfoList(canten_id,s);
                Log.e("shopInfoList.size", String.valueOf(shopInfoList.size()));

//                初始化子布局数组
                Message message_createChild = handler.obtainMessage();
                String length = String.valueOf(shopInfoList.size());
                message_createChild.obj = length;
                message_createChild.what = 0;
                handler.sendMessage(message_createChild);

                for(int i=0;i<shopInfoList.size();i++){
                    Message message_shopIcon = handler.obtainMessage();
                    message_shopIcon.what = 1;
                    message_shopIcon.arg1 = i;
                    message_shopIcon.arg2 = getImageID(shopInfoList.get(i).getShop_icon());
                    handler.sendMessage(message_shopIcon);

                    Message message_shopName = handler.obtainMessage();
                    message_shopName.what = 2;
                    message_shopName.arg1=i;
                    message_shopName.obj=shopInfoList.get(i).getShop_name();
                    handler.sendMessage(message_shopName);

                    Message message_star = handler.obtainMessage();
                    message_star.what = 3;
                    message_star.arg1=i;
                    message_star.obj = R.drawable.xing;
                    handler.sendMessage(message_star);

                    Message message_starSize = handler.obtainMessage();
                    message_starSize.arg1=i;
                    message_starSize.obj = shopInfoList.get(i).getStar_size();
                    message_starSize.what = 4;
                    handler.sendMessage(message_starSize);

                    Message message_monthlysales=handler.obtainMessage();
                    message_monthlysales.arg1=i;
                    message_monthlysales.obj=shopInfoList.get(i).getMonthly_sales();
                    message_monthlysales.what=5;
                    handler.sendMessage(message_monthlysales);

                    Message message_averageprice=handler.obtainMessage();
                    message_averageprice.arg1=i;
                    message_averageprice.obj=shopInfoList.get(i).getAverage_price();
                    message_averageprice.what=6;
                    handler.sendMessage(message_averageprice);

                    Message message_id=handler.obtainMessage();
                    message_id.arg1=i;
                    message_id.obj=shopInfoList.get(i).getId();
                    message_id.what=7;
                    handler.sendMessage(message_id);

                    Message message_addChildToll = handler.obtainMessage();
                    message_addChildToll.what =8;
                    message_addChildToll.arg1 = i;
                    handler.sendMessage(message_addChildToll);
               }
             }
        }).start();
    }


    //通过得到的String得对应图片ID
    protected int getImageID(String name){
        int id = -1;
        try {
            Log.i("图片name",name);
            Field field = R.drawable.class.getDeclaredField(name);
            id = field.getInt(field.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    protected List<ShopInfo> shopInfoList(int canten_id,String s){
        if("".equals(s))
            return ShopDao.getAllShop(canten_id);
        else if("a".equals(s))
            return ShopDao.getAllShopOrderByStar(canten_id);
        else if("b".equals(s))
            return ShopDao.getAllShopOrderBySale(canten_id);
        else if("c".equals(s))
            return ShopDao.getAllShopOrderByAvg(canten_id);
        else return null;
    }

    @Override
    public void onClick(View v) {
        /*Toast.makeText(this,"排序！",Toast.LENGTH_LONG).show();*/
        if(v.getId()==R.id.ll_order1){
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
        }else if(v.getId()==R.id.ll_item){
            Log.i("ll_item",ll_item.toString());
        }
    }
}