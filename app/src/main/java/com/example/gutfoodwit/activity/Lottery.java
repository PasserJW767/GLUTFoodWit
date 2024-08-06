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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gutfoodwit.Dao.UserLotteryDao;
import com.example.gutfoodwit.R;


public class Lottery extends AppCompatActivity implements View.OnClickListener{

    private LuckyView luckyView;
    private boolean flag;
    private ImageView view_back;

    private String userTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(this);
//        设置透明状态栏
        setStatusBar();
        setContentView(R.layout.activity_lottery);
        userTel = getIntent().getExtras().getString("userTel");
        getData();
        initComponent();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    findViewById(R.id.lucky_view).setVisibility(View.VISIBLE);
                    break;
                case 1:
                    TextView showNotice = findViewById(R.id.showNotice);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
                    param.setMargins(250,50,0,0);
                    showNotice.setLayoutParams(param);
                    showNotice.setText("您今天已经抽过奖了");
                    showNotice.setTextSize(25);
                    break;
            }
        }
    };

    private void getData() {
        new Thread(() -> {
            boolean flag = new UserLotteryDao().lotteryOrNot(userTel);
            /*Log.e("flag", String.valueOf(flag));*/
            if(flag == false){
                Message showMsg = handler.obtainMessage();
                showMsg.what = 0;
                handler.sendMessage(showMsg);
                initView();
                initListener();
            }
            else {
                Message nothingMsg = handler.obtainMessage();
                nothingMsg.what = 1;
                handler.sendMessage(nothingMsg);
            }
        }).start();
    }

    /**
     * 初始化组件
     * */
    private void initComponent(){
        view_back = findViewById(R.id.btn_back);
        view_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void initView() {
        luckyView = findViewById(R.id.lucky_view);
    }

    private void initListener() {
        luckyView.setLuckAnimationEndListener(new LuckyView.OnLuckAnimationEndListener() {
            @Override
            public void onLuckAnimationEnd(int pos, String msg) {
                insertRecord();
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void insertRecord(){
        new Thread(() -> {
            new UserLotteryDao().insertIntoTable(userTel);
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
            int permission = ActivityCompat.checkSelfPermission(Lottery.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(Lottery.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if(ContextCompat.checkSelfPermission(Lottery.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Lottery.this,new String[]{Manifest.permission.CALL_PHONE},1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
