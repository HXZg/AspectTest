package com.xz.aspecttest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.aspecttest  AspectTest
 * @Des TwoActivity
 * @DATE 2020/7/30  14:12 星期四
 */
public class TwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aop();
    }

    public void aop() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(TwoActivity.this,ThreeActivity.class));
            }
        },5000);
    }
}
