package com.xz.plugintest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mmkv.MMKV;
import com.xz.plugintest.ams.AmsHook;
import com.xz.plugintest.receiver.MyPackageParse;

import java.io.File;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*String path = getExternalCacheDir().getAbsolutePath() + "/input.apk";

        MyPackageParse parse = new MyPackageParse();

        try {
            parse.parsePackage(this,new File(path));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("zzzzzzz",e.getMessage());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendBroad();
            }
        },3000);*/

        tvTime = findViewById(R.id.tv_time);

        String initialize = MMKV.initialize(this);
        Log.i("zzzzzzzzzzzzzzzz",initialize);

        try {
            new AmsHook().hook();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("zzzzzzzzzzz",e.getMessage());
        }
    }

    public void sendBroad() {
        Log.i("zzzzzzzzzzzz","send broad cast receiver");
        Intent intent = new Intent();
        intent.setAction("com.xz.skin.receiver");
        sendBroadcast(intent);
    }


    public void sp(View view) {
        /*long startTime = System.currentTimeMillis();
        SharedPreferences.Editor editor = getSharedPreferences("main", Context.MODE_PRIVATE).edit();
        for (int i = 0;i < 1000;i++) {
            editor.putString("count" + i,"index::" + i);
            editor.commit();
        }
        tvTime.setText("" + (System.currentTimeMillis() - startTime));*/

        startActivity(new Intent(this,SettingsActivity.class));
    }

    public void mv(View view) {
        long startTime = System.currentTimeMillis();
        MMKV mmkv = MMKV.defaultMMKV();
        for (int i = 0;i< 1000;i++) {
            mmkv.encode("count" + i,"index::" + i);
        }
        tvTime.setText("" + (System.currentTimeMillis() - startTime));
    }
}
