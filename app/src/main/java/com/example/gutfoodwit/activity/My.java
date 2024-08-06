package com.example.gutfoodwit.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gutfoodwit.Dao.UserInfoDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.activity.AboutUs;
import com.example.gutfoodwit.activity.CollectActivity;
import com.example.gutfoodwit.activity.Lottery;
import com.example.gutfoodwit.activity.MyInfo;
import com.example.gutfoodwit.activity.Voucher;
import com.example.gutfoodwit.bean.UserInfo;
import com.example.gutfoodwit.utlis.FileUtil;
import com.example.gutfoodwit.utlis.FileUtils;

import java.io.File;
import java.util.List;

public class My extends AppCompatActivity implements View.OnClickListener{

    private Button btn_navigation_home = null;
    private Button btn_navigation_recommend = null;
    private Button btn_navigation_me = null;
    private ImageView iv_shopcar;

    private ImageView iv_head;

    private String userTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(this);
//        设置透明状态栏
        setStatusBar();
        setContentView(R.layout.activity_me);

        userTel=getIntent().getExtras().getString("userTel");

        iv_shopcar=findViewById(R.id.iv_shopcar);
        iv_shopcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建一个意图对象，准备跳到指定的活动页面
                Intent intent=new Intent(My.this,ShopCarActivity.class);
                intent.putExtra("userTel",userTel);//
                // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置启动标志
                startActivity(intent); // 跳转到意图对象指定的活动页面
            }
        });

//        初始化导航栏
        initNavigationRadioButton();
//        初始化组件
        getUserTel();
        getData();
        initComponent();
    }

    @Override
    protected void onResume(){
        super.onResume();
        getData();
    }

    private void getUserTel(){
        Bundle extras = getIntent().getExtras();
        userTel = extras.getString("userTel");
//        userTel = "18706979150";
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    initHead((String) msg.obj);
                    break;
                case 2:
                    TextView tv_userName = findViewById(R.id.tv_userName);
                    String userName = String.valueOf(msg.obj);
                    tv_userName.setText(userName);

            }
        }
    };

    private void getData(){
        new Thread(() -> {
            UserInfo userInfo = new UserInfoDao().getUserInfo(userTel);
            Message head = handler.obtainMessage();
            head.what = 1;
            head.obj = userInfo.getUser_icon();
            handler.sendMessage(head);

            Message userName = handler.obtainMessage();
            userName.what = 2;
            userName.obj = userInfo.getName();
            handler.sendMessage(userName);

        }).start();
    }

    /**
     * 初始化组件
     * */
    private void initComponent(){
        iv_head = findViewById(R.id.iv_head);
        findViewById(R.id.my_lottery).setOnClickListener(this);
        findViewById(R.id.my_aboutUs).setOnClickListener(this);
        findViewById(R.id.my_myInfo).setOnClickListener(this);
        findViewById(R.id.my_voucher).setOnClickListener(this);
        findViewById(R.id.my_order).setOnClickListener(this);
        findViewById(R.id.btn_collect).setOnClickListener(this);
        findViewById(R.id.waitToPay).setOnClickListener(this);
        findViewById(R.id.waitToUse).setOnClickListener(this);
        findViewById(R.id.waitToEvaluate).setOnClickListener(this);
        findViewById(R.id.my_refundOrAfterSale).setOnClickListener(this);
        findViewById(R.id.my_track).setOnClickListener(this);
        findViewById(R.id.shopCart).setOnClickListener(this);
    }

    private void initHead(String path){
        List<File> fileList = FileUtils.getFileList(path, new String[]{".jpeg"});
        if(fileList.size() > 0){
            String file_path = fileList.get(0).getAbsolutePath();
            Bitmap bitmap = FileUtil.openImage(file_path);
            Bitmap bitmapCircle = cut2Circular(bitmap, true);
            iv_head.setImageBitmap(bitmapCircle);
        } else {
            iv_head.setImageResource(R.drawable.my_headpicture);
        }
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
        drawable = getResources().getDrawable(R.drawable.navigation_recommend_normal);
        drawable.setBounds(0,0,65,65);
        radioButton = findViewById(R.id.btn_navigation_recommend);
        radioButton.setCompoundDrawables(null,drawable,null,null);
//        设置“我的”
        drawable = getResources().getDrawable(R.drawable.navigation_me_selector);
        drawable.setBounds(0,0,65,65);
        radioButton = findViewById(R.id.btn_navigation_me);
        radioButton.setCompoundDrawables(null,drawable,null,null);
    }

    /**
     * 设置按钮组被点击时的状态，需要跳转页面可在每个if的后面加上
     * */
    @Override
    public void onClick(View v) {
//        如果点击了每日抽奖
        if(v.getId() == R.id.my_lottery){
//            页面跳转
            Intent intent = new Intent(this, Lottery.class);
            intent.putExtra("userTel", userTel);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // 设置启动标志
            startActivity(intent); // 跳转到意图对象指定的活动页面
//            动画
            overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        else if(v.getId() == R.id.shopCart){
//            在这里写跳转代码
            // 创建一个意图对象，准备跳到指定的活动页面
            Intent intent=new Intent(My.this,ShopCarActivity.class);
            intent.putExtra("userTel",userTel);//
            // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent); // 跳转到意图对象指定的活动页面
        }
//        如果点击了收藏夹
        else if(v.getId() == R.id.btn_collect){
            // 创建一个意图对象，准备跳到指定的活动页面
            Intent intent = new Intent(this, CollectActivity.class);
            intent.putExtra("userTel", userTel);
            startActivity(intent); // 跳转到意图对象指定的活动页面
        }
        else if(v.getId() == R.id.my_track){
            Intent intent = new Intent(this, TrackActivity.class);
            intent.putExtra("userTel", userTel);
            startActivity(intent); // 跳转到意图对象指定的活动页面
        }
        else if(v.getId() == R.id.waitToPay || v.getId() == R.id.waitToUse || v.getId() == R.id.waitToEvaluate || v.getId() == R.id.my_refundOrAfterSale){
            Intent intent = new Intent(My.this, OtherOrderActivity.class);
            if(v.getId() == R.id.waitToPay){
                intent.putExtra("title", "待付款");
                intent.putExtra("userTel", userTel);
            }
            else if(v.getId() == R.id.waitToUse){
                intent.putExtra("title", "待使用");
                intent.putExtra("userTel", userTel);
            }
            else if(v.getId() == R.id.my_refundOrAfterSale){
                intent.putExtra("title", "待退款");
                intent.putExtra("userTel", userTel);
            }
            else if(v.getId() == R.id.waitToEvaluate){
                intent.putExtra("title", "待评分");
                intent.putExtra("userTel", userTel);
            }
            startActivity(intent);
        }
//        如果点击了关于我们
        else if(v.getId() == R.id.my_aboutUs){
//            页面跳转
            Intent intent = new Intent(this, AboutUs.class);
            startActivity(intent); // 跳转到意图对象指定的活动页面
        }
//        如果点击了个人信息
        else if(v.getId() == R.id.my_myInfo){
//            页面跳转
            Intent intent = new Intent(this, MyInfo.class);
            intent.putExtra("userTel", userTel);
            startActivity(intent); // 跳转到意图对象指定的活动页面
        }
//        如果点击了红包卡券
        else if(v.getId() == R.id.my_voucher){
//            页面跳转
            startActivity(new Intent(this, Voucher.class));
//            动画
            overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
//        如果点击了我的订单
        else if(v.getId() == R.id.my_order){
//            页面跳转
            Intent intent = new Intent(this, AllOrderActivity.class);
            intent.putExtra("userTel", userTel);
            startActivity(intent); // 跳转到意图对象指定的活动页面
//            动画
            overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
//        如果点击了首页
        else if(v.getId() == R.id.btn_navigation_home){
//            设置“首页”
            // 创建一个意图对象，准备跳到指定的活动页面
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.putExtra("userTel", userTel);
            // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置启动标志
            startActivity(intent); // 跳转到意图对象指定的活动页面
        }
//        点击推荐
        else if(v.getId() == R.id.btn_navigation_recommend){
//            设置“首页”
            // 创建一个意图对象，准备跳到指定的活动页面
            Intent intent = new Intent(this, RecommendActivity.class);
            intent.putExtra("userTel", userTel);
            // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置启动标志
            startActivity(intent); // 跳转到意图对象指定的活动页面
        }
//        点击我的
        else if(v.getId() == R.id.btn_navigation_me){
//            设置“首页”
            Drawable drawable = getResources().getDrawable(R.drawable.navigation_home_normal);
            drawable.setBounds(0,0,65,65);
            RadioButton radioButton = findViewById(R.id.btn_navigation_home);
            radioButton.setCompoundDrawables(null,drawable,null,null);
//            设置“推荐”
            drawable = getResources().getDrawable(R.drawable.navigation_recommend_normal);
            drawable.setBounds(0,0,65,65);
            radioButton = findViewById(R.id.btn_navigation_recommend);
            radioButton.setCompoundDrawables(null,drawable,null,null);
//            设置“我的”
            drawable = getResources().getDrawable(R.drawable.navigation_me_selector);
            drawable.setBounds(0,0,65,65);
            radioButton = findViewById(R.id.btn_navigation_me);
            radioButton.setCompoundDrawables(null,drawable,null,null);
        }

    }

    /**
     * 减圆代码
     * */
    private Bitmap cut2Circular(Bitmap source,boolean recycleSource){
        int width = source.getWidth();
        int height = source.getHeight();
        int diameter = Math.min(width, height);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap result = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888);
        if(result != null){
            Canvas canvas = new Canvas(result);
            canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(source, (diameter - width) / 2, (diameter - height) / 2, paint);
            if(recycleSource){
                source.recycle();
                source = null;
            }
        } else {
            result = source;
        }
        return result;
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
            int permission = ActivityCompat.checkSelfPermission(My.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(My.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if(ContextCompat.checkSelfPermission(My.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(My.this,new String[]{Manifest.permission.CALL_PHONE},1);
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
}
