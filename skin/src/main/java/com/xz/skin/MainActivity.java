package com.xz.skin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.skin  AspectTest
 * @Des MainActivity
 * @DATE 2020/8/27  15:48 星期四
 */
public class MainActivity extends Activity {

    private GLSurfaceView glSurfaceView;

    Handler h = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glSurfaceView = findViewById(R.id.gl_surface);

        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new TriangleRender());

        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // 被动绘制模式

        Collections.sort(new ArrayList<Integer>(), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return 0;
            }
        });

        try {
            new FutureTask<String>(new MyCallable()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("zzzzzzzzzzzzzzzzzzz","main destroy");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
