package com.xz.qrcodedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.qrcodedemo  AspectTest
 * @Des MyService
 * @DATE 2020/9/25  9:47 星期五
 */
public class MyService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        new IMyAidlInterface.Stub() {
            @Override
            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
                double v = anInt + aLong + aFloat + aDouble;
                String s = v + aString;
                if (aBoolean) {
                    s = s;
                }else {
                    s = aString;
                }

            }
        };
        return new MyBinder();
    }

    class MyBinder extends Binder {
        public void getData() {
            Log.i("zzzzzzzzzz","on BINDER service" + Thread.currentThread().getId() + "::" + Process.myPid());

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
