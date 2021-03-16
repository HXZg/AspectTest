package com.xz.skin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SecondActivity.this,MainActivity.class));
            }
        },3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("zzzzzzzzzzzzzzzzzz","second destroy");
    }
}
