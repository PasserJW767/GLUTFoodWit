//package com.example.gutfoodwit.activity;
//
//import android.os.Bundle;
//
//import com.example.gutfoodwit.R;
//import com.example.gutfoodwit.utlis.DBUtils;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import java.util.HashMap;
//
//public class TestActivity extends Activity {
//
//    private Button btn_get_data;
//    private TextView tv_data;
//
//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//
//            switch (msg.what){
//                case 0x11:
//                    String s = String.valueOf( msg.obj);
//                    tv_data.setText(s);
//                    break;
//                case 0x12:
//                    String ss = String.valueOf( msg.obj);
//                    tv_data.setText(ss);
//                    break;
//            }
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
//        // 控件的初始化
//        btn_get_data = findViewById(R.id.btn_get_data);
//        tv_data = findViewById(R.id.tv_data);
//
//        setListener();
//    }
//
//
//    private void setListener() {
//        // 按钮点击事件
//        btn_get_data.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // 创建一个线程来连接数据库并获取数据库中对应表的数据
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
//                        int map = DBUtils.getInfoByName();
//                        Message message = handler.obtainMessage();
//                        if(map != 0){
//                            String s = "";
//                            for (String key : map.keySet()){
//                                s += key + ":" + map.get(key) + "\n";
//                            }
//
//                            message.what = 0x12;
//                            message.obj = map;
//                        }else {
//                            message.what = 0x11;
//                            message.obj = 0;
//                        }
//                        // 发消息通知主线程更新UI
//                        handler.sendMessage(message);
//                    }
//                }).start();
//
//            }
//        });
//
//    }
//}
//
