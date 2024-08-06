package com.example.gutfoodwit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gutfoodwit.R;

public class WaitForLoginForgetActivity extends AppCompatActivity implements View.OnClickListener{
    EditText et_thephone;
    Button tb_yes;
    private String userTel; // 手机号码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_login_forget);

        et_thephone=findViewById(R.id.et_thephone);
        tb_yes=findViewById(R.id.tb_yes);
        // 从上一个页面获取要修改密码的手机号码
        userTel = getIntent().getStringExtra("userTel");
        et_thephone.setText(userTel);
        tb_yes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tb_yes){
            Intent intent = new Intent(this, WaitForLoginForgetActivity1.class);
            // 栈中存在待跳转的活动实例时，则重新创建该活动的实例，并清除原实例上方的所有实例
            intent.putExtra("userTel", userTel);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 设置启动标志
            startActivity(intent); // 跳转到意图对象指定的活动页面
        }
    }
}