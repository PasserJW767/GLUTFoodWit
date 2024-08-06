package com.example.gutfoodwit.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gutfoodwit.Dao.UserInfoDao;
import com.example.gutfoodwit.R;
import com.example.gutfoodwit.bean.UserInfo;
import com.example.gutfoodwit.utlis.BitmapUtil;
import com.example.gutfoodwit.utlis.FileUtil;
import com.example.gutfoodwit.utlis.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyInfo extends AppCompatActivity implements View.OnClickListener{

    private int CHOOSE_CODE = 3; // 只在相册挑选图片的请求码
    private ImageView btn_back;
    private ImageView iv_head;
    private LinearLayout liner_updateUserName;
    private LinearLayout liner_updateUserHead;
    private LinearLayout liner_updateUserGender;
    private LinearLayout liner_updateUserTel;
    private TextView tv_updateUserNameText;
    private TextView tv_updateUserGenderText;
    private TextView tv_userTelText;
    private Button bt_exit;

    private String userTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(this);
//        设置透明状态栏
        setStatusBar();
        setContentView(R.layout.activity_myinfo);
        getUserTel();
        initComponent();

        getData();

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("ResourceType")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    UserInfo userInfo = (UserInfo) msg.obj;
                    tv_updateUserNameText.setText(userInfo.getName());
                    tv_userTelText.setText(userInfo.getPhone());
                    TextView tv_updateUserGenderText = findViewById(R.id.tv_updateUserGenderText);
                    tv_updateUserGenderText.setText(userInfo.getGender());
                    TextView textView = findViewById(R.id.userIdentifyCardText);
                    textView.setText(userInfo.getId_number());
                    break;
                case 1:
                    initHead((String) msg.obj);
                    break;
                case 2:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyInfo.this);
                    builder.setTitle("请输入新密码");
                    builder.setIcon(R.mipmap.myinfo_userpwd);
                    EditText temp1 = new EditText(MyInfo.this);
                    builder.setView(temp1);
                    builder.setPositiveButton("确定", (dialog, which) -> {
                        String tempStr = temp1.getText().toString();
                        againPwd(tempStr);
                    });
                    builder.setNegativeButton("取消",null);
                    builder.show();
                    break;

            }
        }
    };

    private void getData(){
        new Thread(() -> {
            UserInfo userInfo = new UserInfoDao().getUserInfo(userTel);
            Message msg = handler.obtainMessage();
            msg.what = 0;
            msg.obj = userInfo;
            handler.sendMessage(msg);

            Message head = handler.obtainMessage();
            head.what = 1;
            head.obj = userInfo.getUser_icon();
            handler.sendMessage(head);

        }).start();
    }



    private void getUserTel(){
        Bundle extras = getIntent().getExtras();
        userTel = extras.getString("userTel");
//        userTel = "18706979150";
    }

    private void initComponent(){
        iv_head = findViewById(R.id.iv_head);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        liner_updateUserName = findViewById(R.id.liner_updateUserName);
        liner_updateUserName.setOnClickListener(this);
        tv_updateUserNameText = findViewById(R.id.tv_updateUserNameText);
        liner_updateUserHead = findViewById(R.id.liner_updateUserHead);
        liner_updateUserHead.setOnClickListener(this);
        liner_updateUserGender = findViewById(R.id.liner_updateUserGender);
        liner_updateUserGender.setOnClickListener(this);
        tv_updateUserGenderText = findViewById(R.id.tv_updateUserGenderText);
        liner_updateUserTel = findViewById(R.id.liner_updateUserTel);
        liner_updateUserTel.setOnClickListener(this);
        tv_userTelText = findViewById(R.id.tv_userTelText);
        bt_exit=findViewById(R.id.bt_exit);
        bt_exit.setOnClickListener(this);
        findViewById(R.id.liner_updateUserIdentifyCard).setOnClickListener(this);
        findViewById(R.id.liner_updateUserPwd).setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.liner_updateUserName:
                AlertDialog.Builder builder = new AlertDialog.Builder(MyInfo.this);
                builder.setTitle("请输入新用户名");
                builder.setIcon(R.mipmap.myinfo_username);
                EditText temp = new EditText(MyInfo.this);
                builder.setView(temp);
                builder.setPositiveButton("确定", (dialog, which) -> {
                    String tempStr = temp.getText().toString();
                    tv_updateUserNameText.setText(tempStr);
                    updateUserName(tempStr);
                });
                builder.setNegativeButton("取消",null);
                builder.show();
                break;
            case R.id.liner_updateUserHead:
                Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 是否允许多选
                albumIntent.setType("image/*"); // 类型为图像
                startActivityForResult(albumIntent, CHOOSE_CODE); // 打开系统相册
                break;
            case R.id.liner_updateUserGender:
                String[] sexArr = new String[]{"不告诉你", "女", "男"};
                builder = new AlertDialog.Builder(MyInfo.this);
                builder.setSingleChoiceItems(sexArr, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_updateUserGenderText.setText(sexArr[which]);
                        dialog.dismiss();
                        updateGender(sexArr[which]);
                    }
                });
                builder.show();
                break;
            case R.id.liner_updateUserTel:
                Toast.makeText(this, "手机号暂不允许更新~可联系工作人员：14795572282", Toast.LENGTH_SHORT).show();
                break;
            case R.id.liner_updateUserIdentifyCard:
                builder = new AlertDialog.Builder(MyInfo.this);
                builder.setTitle("请输入身份证号");
                builder.setIcon(R.mipmap.myinfo_identifycard);
                temp = new EditText(MyInfo.this);
                builder.setView(temp);
                builder.setPositiveButton("确定", (dialog, which) -> {
                    String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                            "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
                    String tempStr = temp.getText().toString();
                    boolean matches = tempStr.matches(regularExpression);
                    if(!matches){
                        Toast.makeText(MyInfo.this,"身份证号非法！",Toast.LENGTH_SHORT).show();
                    } else {
                        TextView tv = findViewById(R.id.userIdentifyCardText);
                        tv.setText(tempStr);
                        updateUserIdentifyCard(tempStr);
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
                break;
            case R.id.liner_updateUserPwd:
                builder = new AlertDialog.Builder(MyInfo.this);
                builder.setTitle("请输入旧密码");
                builder.setIcon(R.mipmap.myinfo_userpwd);
                temp = new EditText(MyInfo.this);
                builder.setView(temp);
                builder.setPositiveButton("确定", (dialog, which) -> {
                    checkOldPwd(temp.getText().toString());
                });
                builder.setNegativeButton("取消",null);
                builder.show();
                break;
            case R.id.bt_exit:
                Intent intent=new Intent(MyInfo.this,WaitToLogin.class);
                // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
                intent.setFlags(intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent); // 跳转到意图对象指定的活动页面
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == CHOOSE_CODE) { // 从相册返回
            if (intent.getData() != null) { // 从相册选择一张照片
                Uri uri = intent.getData(); // 获得已选择照片的路径对象
                // 根据指定图片的uri，获得自动缩小后的位图对象
                Bitmap bitmap = BitmapUtil.getAutoZoomImage(this, uri);
                String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
//                在这里获得用户的电话号码
                String file_path = path + userTel + ".jpeg";
                FileUtil.saveImage(file_path,bitmap);
                writeImgToDataBase(path);
                Log.e("路径", file_path);
                Intent intent1 = new Intent(MyInfo.this,MyInfo.class);
                intent1.putExtra("userTel", userTel);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
            } else if (intent.getClipData() != null) { // 从相册选择多张照片
                ClipData images = intent.getClipData(); // 获取剪切板数据
                if (images.getItemCount() > 0) { // 至少选择了一个文件
                    Uri uri = images.getItemAt(0).getUri(); // 取第一张照片
                    // 根据指定图片的uri，获得自动缩小后的位图对象
                    Bitmap bitmap = BitmapUtil.getAutoZoomImage(this, uri);
//                      在这里获得用户的电话号码
                    String userTel = "userName";
                    String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
//                在这里获得用户的电话号码
                    String file_path = path + userTel + ".jpeg";
                    FileUtil.saveImage(file_path,bitmap);
                    writeImgToDataBase(path);
                    Log.e("路径", file_path);
                    startActivity(new Intent(MyInfo.this,MyInfo.class));
                }
            }
        }
    }

    private void againPwd(String newPwd){
        if(newPwd == null || newPwd.equals("")){
            Toast.makeText(MyInfo.this, "新密码不允许为空！", Toast.LENGTH_SHORT).show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyInfo.this);
            builder.setTitle("请再输入一次新密码");
            builder.setIcon(R.mipmap.myinfo_userpwd);
            EditText temp1 = new EditText(MyInfo.this);
            builder.setView(temp1);
            builder.setPositiveButton("确定", (dialog, which) -> {
                String tempStr = temp1.getText().toString();
                if (tempStr.equals(newPwd)){
                    Toast.makeText(MyInfo.this,"修改成功!",Toast.LENGTH_SHORT).show();
                    updateUserPwd(tempStr);
                }
                else {
                    Toast.makeText(MyInfo.this,"两次输入的密码不一致!",Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("取消",null);
            builder.show();
        }
    }

    /**
     * 修改密码
     * */
    private void updateUserPwd(String pwd){
        new Thread(() -> {
            Looper.prepare();
            boolean flag = new UserInfoDao().updateUserPwd(userTel, pwd);
            if(!flag){
                Toast.makeText(MyInfo.this,"修改成功！",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MyInfo.this,"修改失败！",Toast.LENGTH_SHORT).show();
            }
            Looper.loop();
        }).start();
    }

    /**
     * 检查旧密码
     * */
    private void checkOldPwd(String oldPwd){
        new Thread(() -> {
            Looper.prepare();
            boolean flag = new UserInfoDao().checkOldPwd(userTel, oldPwd);
            if(!flag){
                Toast.makeText(MyInfo.this,"旧密码有误！",Toast.LENGTH_SHORT).show();
            } else {
                Message msg = handler.obtainMessage();
                msg.what = 2;
                handler.sendMessage(msg);
            }
            Looper.loop();
        }).start();
    }

    /**
     * 更新用户身份证
     * */
    private void updateUserIdentifyCard(String identifyCard){
        new Thread(() -> {
            Looper.prepare();
            boolean flag = new UserInfoDao().updateUserIdentifyCard(userTel, identifyCard);
            if(flag == true){
                Toast.makeText(MyInfo.this, "修改成功！", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MyInfo.this,"修改失败！",Toast.LENGTH_SHORT).show();
            }
            Looper.loop();
        }).start();
    }

    /**
     * 更新用户名
     * */
    private void updateUserName(String userName){
        new Thread(() -> {
            Looper.prepare();
            boolean flag = new UserInfoDao().updateUserName(userTel, userName);
            if(flag == true){
                Toast.makeText(MyInfo.this, "修改成功！", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MyInfo.this,"修改失败！",Toast.LENGTH_SHORT).show();
            }
            Looper.loop();
        }).start();
    }


    /**
     * 更新用户性别
     * */
    private void updateGender(String gender){
        new Thread(() -> {
            Looper.prepare();
            boolean flag = new UserInfoDao().updateUserGender(userTel, gender);
            if(flag == true){
                Toast.makeText(MyInfo.this, "修改成功！", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MyInfo.this,"修改失败！",Toast.LENGTH_SHORT).show();
            }
            Looper.loop();
        }).start();
    }


    /**
     * 将图片路径写入数据库
     *
     * */
    private void writeImgToDataBase(String filePath){
        new Thread(() -> {
            Looper.prepare();
            boolean result = new UserInfoDao().insertUserHead(userTel,filePath);
            if(result == true){
                Toast.makeText(MyInfo.this, "头像上传成功！", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(MyInfo.this, "头像上传失败！", Toast.LENGTH_SHORT).show();
            Looper.loop();

        }).start();

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
            int permission = ActivityCompat.checkSelfPermission(MyInfo.this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(MyInfo.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
            if(ContextCompat.checkSelfPermission(MyInfo.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MyInfo.this,new String[]{Manifest.permission.CALL_PHONE},1);
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
