package com.xz.qrcodedemo;

import java.net.HttpURLConnection;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.qrcodedemo  AspectTest
 * @Des MainModel
 * @DATE 2020/10/13  17:55 星期二
 */
public class MainModel {

    // http  请求数据

    public void getInfo() {
//        HttpURLConnection
    }

    class Persent{
        Listener listener;  // 内存泄漏

        MainModel model;

        public void setListener(Listener listener) {
            this.listener = listener;
        }

        public void test()
        {
            model.getInfo();
            listener.getInfo("");
        }
    }

    interface Listener{
        void getInfo(String s);
    }
}
