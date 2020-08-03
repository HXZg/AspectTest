package com.xz.aspecttest;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xz.aspectlib.annotation.ActivityTime;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.aspecttest  AspectTest
 * @Des ThreeActivity
 * @DATE 2020/8/3  8:59 星期一
 */
@ActivityTime
public class ThreeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
