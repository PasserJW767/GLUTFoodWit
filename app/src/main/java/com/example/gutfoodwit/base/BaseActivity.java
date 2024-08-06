package com.example.gutfoodwit.base;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setUpView();
        setUpData(savedInstanceState);
    }


    protected abstract void setUpView();

    protected abstract void setUpData(Bundle savedInstanceState);

}
