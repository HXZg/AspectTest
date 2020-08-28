package com.xz.plugintest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.xz.plugintest.receiver.MyPackageParse;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String path = getExternalCacheDir().getAbsolutePath() + "/input.apk";

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
        },3000);
    }

    public void sendBroad() {
        Log.i("zzzzzzzzzzzz","send broad cast receiver");
        Intent intent = new Intent();
        intent.setAction("com.xz.skin.receiver");
        sendBroadcast(intent);
    }


}
