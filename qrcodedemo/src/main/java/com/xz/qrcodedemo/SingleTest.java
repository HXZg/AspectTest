package com.xz.qrcodedemo;

import androidx.lifecycle.LiveData;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.qrcodedemo  AspectTest
 * @Des SingleTest
 * @DATE 2020/9/23  11:37 星期三
 */
public class SingleTest {

    private static Object object = new Object();
    private volatile static SingleTest test;

    public static SingleTest getInstance() {
        if (test == null) {
            synchronized (object) {
                if (test == null) {
                    test = new SingleTest();
                }
            }
        }
        return test;
    }
}
