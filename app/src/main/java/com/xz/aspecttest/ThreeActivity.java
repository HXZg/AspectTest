package com.xz.aspecttest;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ioclib.InjectUtils;
import com.example.ioclib.annotation.ContentView;
import com.example.ioclib.annotation.OnClick;
import com.example.ioclib.annotation.ViewInject;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.aspecttest  AspectTest
 * @Des ThreeActivity
 * @DATE 2020/8/3  8:59 星期一
 */
@ContentView(R.layout.activity_three)
public class ThreeActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_three)
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtils.INSTANCE.inject(this);
    }

    @OnClick(R.id.btn_three)
    public void onClick(View view) {
        textView.setText("btn");
    }
}
