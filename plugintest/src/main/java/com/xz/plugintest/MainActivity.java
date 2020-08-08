package com.xz.plugintest;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.xz.lib.ActivityDestination;

import java.io.IOException;
import java.io.InputStream;


@ActivityDestination(id = 0)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Test().test();

        try {
            InputStream open = getAssets().open("destination.json");
            Log.i("zzzzzzzzzzz","" + open);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
