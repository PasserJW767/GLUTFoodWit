package com.example.gutfoodwit.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.gutfoodwit.Dao.UserDao;
import com.example.gutfoodwit.R;


public class WaitForLoginForgetActivity1 extends AppCompatActivity {
    SwipeCaptchaView mSwipeCaptchaView;
    SeekBar mSeekBar;//托动条
    EditText et_idnumber;
    EditText et_newpwd;
    EditText et_againpwd;
    Button bt_ensure;
    private String userTel;
    int pwdCount=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_login_forget1);
        userTel=getIntent().getExtras().getString("userTel");

        mSwipeCaptchaView = (SwipeCaptchaView) findViewById(R.id.swipeCaptchaView);
        mSeekBar = (SeekBar) findViewById(R.id.dragBar);

        et_idnumber=findViewById(R.id.et_idnumber);
        et_newpwd=findViewById(R.id.et_newpwd);
        et_againpwd=findViewById(R.id.et_againpwd);

        bt_ensure=findViewById(R.id.bt_ensure);
        bt_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (UserDao.findUserIdnumber(et_idnumber.getText().toString(), userTel)) {
                            if (et_newpwd.getText().toString().equals(et_againpwd.getText().toString())) {
                                UserDao.updateUserPwd(et_newpwd.getText().toString(), userTel);

                                Intent intent = new Intent(WaitForLoginForgetActivity1.this, HomePageActivity.class);
                                // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置启动标志
                                intent.putExtra("userTel", userTel);
                                startActivity(intent); // 跳转到意图对象指定的活动页面
                            } else {
                                Looper.prepare();
                                Toast.makeText(WaitForLoginForgetActivity1.this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } else {
                            Looper.prepare();
                            Toast.makeText(WaitForLoginForgetActivity1.this, "你还有" + (pwdCount - 1) + "次输入身份证的机会", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }).start();
            }
        });


        mSwipeCaptchaView.setOnCaptchaMatchCallback(new SwipeCaptchaView.OnCaptchaMatchCallback() {
            @Override
            public void matchSuccess(SwipeCaptchaView swipeCaptchaView) {
                Toast.makeText(WaitForLoginForgetActivity1.this, "恭喜你啊 验证成功 可以搞事情了", Toast.LENGTH_SHORT).show();
                //swipeCaptcha.createCaptcha();
                mSeekBar.setEnabled(false);
            }

            @Override
            public void matchFailed(SwipeCaptchaView swipeCaptchaView) {
                Log.d("zxt", "matchFailed() called with: swipeCaptchaView = [" + swipeCaptchaView + "]");
                Toast.makeText(WaitForLoginForgetActivity1.this, "你有80%的可能是机器人，现在走还来得及", Toast.LENGTH_SHORT).show();
                swipeCaptchaView.resetCaptcha();
                mSeekBar.setProgress(0);
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSwipeCaptchaView.setCurrentSwipeValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //随便放这里是因为控件
                mSeekBar.setMax(mSwipeCaptchaView.getMaxSwipeValue());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("zxt", "onStopTrackingTouch() called with: seekBar = [" + seekBar + "]");
                mSwipeCaptchaView.matchCaptcha();
            }
        });

        //测试从网络加载图片是否ok
        Glide.with(this)
                .load("http://www.investide.cn/data/edata/image/20151201/20151201180507_281.jpg")
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mSwipeCaptchaView.setImageBitmap(resource);
                        mSwipeCaptchaView.createCaptcha();
                    }
                });
    }
}
