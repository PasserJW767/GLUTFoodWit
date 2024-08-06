package com.example.gutfoodwit.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gutfoodwit.Dao.UserDao;
import com.example.gutfoodwit.Dao.UserInfoDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.bean.UserInfo;
import com.mob.MobSDK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class Register extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

    Button requestCodeBtn = null;
    Button registerBtn = null;
    EditText userName = null;
    EditText userPwd = null;
    EditText userCertainPwd = null;
    EditText userTel = null;
    EditText userCode = null;

    boolean flag=false;
    UserInfo user=new UserInfo();

    int i = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(this);
        setStatusBar();
        setContentView(R.layout.activity_register);
        init();

    }

    /**
     * 初始化
     * */
    private void init(){
        userName = findViewById(R.id.edit_userName);
        userPwd = findViewById(R.id.edit_userPwd);
        userCertainPwd = findViewById(R.id.edit_userCertainPwd);
        userTel = findViewById(R.id.edit_userTel);
        userCode = findViewById(R.id.edit_vertifyCode);
        requestCodeBtn = findViewById(R.id.btn_getCode);
        registerBtn = findViewById(R.id.btn_vertifyCode);
        userName.setOnFocusChangeListener(this);
        userPwd.setOnFocusChangeListener(this);
        userCertainPwd.setOnFocusChangeListener(this);
        userTel.setOnFocusChangeListener(this);
        userCode.setOnFocusChangeListener(this);
        requestCodeBtn.setOnClickListener(this);
        findViewById(R.id.btn_vertifyCode).setOnClickListener(this);
        findViewById(R.id.btn_toLogin).setOnClickListener(this);

//        启动短信验证SDK
        MobSDK.init(this);
        SMSSDK.setAskPermisionOnReadContact(true);
//        同意隐私授权，接入接口
        MobSDK.submitPolicyGrantResult(true,null);
        MobSDK.init(this,"33462bd5e7e2a","fff17a98bdbcf57f60970dd8781ed799");

        EventHandler eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
//        注册一个事件回调监听，用于处理SMSSDK接口的请求结果
        SMSSDK.registerEventHandler(eventHandler);
    }

    /**
     * 收到Mob返回的信息处理
     * */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                requestCodeBtn.setText("重新发送(" + i + ")");
            }
            else if (msg.what == -8) {
                requestCodeBtn.setText("获取验证码");
                requestCodeBtn.setClickable(true);
                i = 30;
            }
            else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                Log.e("result", "result=" + result);
                Log.e("data","data=" + data.toString());
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                        验证码正确，在此处跳转至注册成功的界面
                        Toast.makeText(getApplicationContext(), "验证码正确",Toast.LENGTH_SHORT).show();

                        user.setName(userName.getText().toString());
                        user.setPassword(userPwd.getText().toString());
                        user.setPhone(userTel.getText().toString());
                        new Thread(() ->{
                            UserDao.addUser(user);
                            // 创建一个意图对象，准备跳到指定的活动页面
                            Intent intent = new Intent(Register.this, HomePageActivity.class);
                            // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置启动标志
                            intent.putExtra("userTel",user.getPhone());
                            startActivity(intent); // 跳转到意图对象指定的活动页面
                        }).start();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Log.e("ing","正在获取验证码");
                        Toast.makeText(getApplicationContext(), "正在获取验证码",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "验证码不正确",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    Handler handler2 = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    userTel.setBackground(getResources().getDrawable(R.drawable.edit_shape_error));
                    Toast.makeText(Register.this, "该手机号已经被注册过了", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    userTel.setBackground(getResources().getDrawable(R.drawable.edit_register_tel));
                    break;
            }
        }
    };

    /**
     * 注册OnFocus事件
     * */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            switch (v.getId()){
                case R.id.edit_userName:
                    String str = userName.getText().toString();
//                      判断非空
                    if(TextUtils.isEmpty(str)){
                        userName.setBackground(getResources().getDrawable(R.drawable.edit_shape_error));
                        Toast.makeText(this,"请输入用户名",Toast.LENGTH_SHORT).show();
                        return ;
                    }
//                      要求用户名为数字和英文
                    if(!judgeUserName(str)){
                        userName.setBackground(getResources().getDrawable(R.drawable.edit_shape_error));
                        Toast.makeText(this,"用户名仅能由英文字母和数字组成",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    userName.setBackground(getResources().getDrawable(R.drawable.edit_selector));
                    break;
                case R.id.edit_userPwd:
                    str = userPwd.getText().toString();
                    if(TextUtils.isEmpty(str)){
                        userPwd.setBackground(getResources().getDrawable(R.drawable.edit_shape_error));
                        Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    if(!judgeUserPwd(str)){
                        userPwd.setBackground(getResources().getDrawable(R.drawable.edit_shape_error));
                        Toast.makeText(this,"密码至少包含数字和英文，长度为6-20",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    userPwd.setBackground(getResources().getDrawable(R.drawable.edit_selector));
                    break;
                case R.id.edit_userCertainPwd:
                    str = userCertainPwd.getText().toString();
                    if(TextUtils.isEmpty(str)){
                        userCertainPwd.setBackground(getResources().getDrawable(R.drawable.edit_shape_error));
                        Toast.makeText(this,"请确认密码",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    if(!judgeCertainUserPwd(str)){
                        userCertainPwd.setBackground(getResources().getDrawable(R.drawable.edit_shape_error));
                        Toast.makeText(this,"确认密码与原密码不一致",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    userCertainPwd.setBackground(getResources().getDrawable(R.drawable.edit_selector));
                    break;
                case R.id.edit_userTel:
                    str = userTel.getText().toString();
                    if(TextUtils.isEmpty(str)){
                        userTel.setBackground(getResources().getDrawable(R.drawable.edit_shape_error));
                        Toast.makeText(this,"请输入电话号码",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    else if((!isMatchLength(str, 11)) || (!isMobileNO(str))){
                        userTel.setBackground(getResources().getDrawable(R.drawable.edit_shape_error));
                        Toast.makeText(this,"手机号码格式有误",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    else {
                        judgePhoneRegisterOrNot(str);
                    }
                    userTel.setBackground(getResources().getDrawable(R.drawable.edit_register_tel));
                    break;
                case R.id.edit_vertifyCode:
                    str = userCode.getText().toString();
                    if(TextUtils.isEmpty(str)){
                        userCode.setBackground(getResources().getDrawable(R.drawable.edit_shape_error));
                        Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    userCode.setBackground(getResources().getDrawable(R.drawable.edit_selector));
                    break;
            }
        }
    }


    /**
     * 注册OnClick事件
     * */
    @Override
    public void onClick (View view){
        String phoneNums = userTel.getText().toString();
        switch (view.getId()) {
            case R.id.btn_getCode:
                // 1. 通过正则规则判断手机号
                if (!judgePhoneNums(phoneNums)) {
                    return;
                } // 2. 通过sdk发送短信验证
                // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
                Toast.makeText(this,phoneNums,Toast.LENGTH_SHORT).show();
                SMSSDK.getVerificationCode("86", phoneNums);
                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                requestCodeBtn.setClickable(false);
                requestCodeBtn.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; i > 0; i--) {
                            handler.sendEmptyMessage(-9);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;

            case R.id.btn_vertifyCode:
//                判断是否有输入的内容为空,是否符合要求
                boolean flag = judgeAllInfo();
                if(flag == true){
                    //将收到的验证码和手机号提交再次核对
                    SMSSDK.submitVerificationCode("86", phoneNums, userCode.getText().toString());
                }
                break;
            case R.id.btn_toLogin:
                startActivity(new Intent(this,LoginActivity.class));
                overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    private void judgePhoneRegisterOrNot(String phone){
        new Thread(() -> {
            boolean flag = new UserDao().findUserByPhone(phone);
            if(flag){
//                说明这个号码存在
                Message msg = handler2.obtainMessage();
                msg.what = 1;
                handler2.sendMessage(msg);
            } else {
                Message msg = handler2.obtainMessage();
                msg.what = 2;
                handler2.sendMessage(msg);
            }

        }).start();
    }

    /**
     * 注册验证
     * */
    private boolean judgeAllInfo(){
        if(TextUtils.isEmpty(userName.getText().toString()) ||
                TextUtils.isEmpty(userPwd.getText().toString()) ||
                TextUtils.isEmpty(userCertainPwd.getText().toString()) ||
                TextUtils.isEmpty(userTel.getText().toString()) ){
            Toast.makeText(this,"您有信息尚未填写，请填写完毕后再注册",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(userCode.getText().toString()) ){
            Toast.makeText(this,"您尚未填写验证码，请填写完毕后再注册",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!judgeUserName(userName.getText().toString())){
            Toast.makeText(this,"用户名仅能由英文字母和数字组成",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!judgeUserPwd(userPwd.getText().toString())){
            Toast.makeText(this,"密码至少包含数字和英文，长度为6-20",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!judgeCertainUserPwd(userCertainPwd.getText().toString())){
            Toast.makeText(this,"确认密码与原密码不一致",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!judgePhoneNums(userTel.getText().toString())){
            Toast.makeText(this,"手机号码格式有误",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 判断用户名是否符合格式
     * */
    private boolean judgeUserName(String str){
        String reg = "^[A-Za-z0-9]+$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        if(!matcher.matches()){
            return false;
        }
        return true;
    }

    /**
     * 判断密码是否符合格式
     * */
    private boolean judgeUserPwd(String str){
        String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        if(!matcher.matches()){
            return false;
        }
        return true;
    }

    /**
     * 判断两次密码是否一致
     * */
    private boolean judgeCertainUserPwd(String str){
        if(!userPwd.getText().toString().equals(userCertainPwd.getText().toString())){
            return false;
        }
        return true;
    }

    /**
    * 判断手机号是否正确
    * */
    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码格式有误", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
    * 判断手机号长度
    * */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }
    /**
    * 判断手机号格式
    * */
    public static boolean isMobileNO(String mobileNums) {
        String telRegex = "[1]\\d{10}";// "[1]"代表第1位为数字1，"\\d{10}"代表后面是可以是0～9的数字，有10位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }
    /**
    * 注销监听事件，防止内存泄漏
    * */
    protected void onDestroy(){
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
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
            int permission = ActivityCompat.checkSelfPermission(Register.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(Register.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if(ContextCompat.checkSelfPermission(Register.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Register.this,new String[]{Manifest.permission.CALL_PHONE},1);
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
