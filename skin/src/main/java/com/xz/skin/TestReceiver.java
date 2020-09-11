package com.xz.skin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.skin  AspectTest
 * @Des TestReceiver
 * @DATE 2020/8/28  9:43 星期五
 */
public class TestReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new Test().test(context);
    }
}
