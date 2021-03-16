package com.xz.qrcodedemo;

import android.os.Message;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.qrcodedemo  AspectTest
 * @Des Test
 * @DATE 2020/9/23  18:08 星期三
 */
public class Test {




    public void ttt() {
        new TestOne().test();
    }

    public static class  TestOne{

        public int i = 0;

        public void test() {
            Message message = new Message();
        }

    }

    public class TestTwo {

        int i = 0;
        void tt() {
            ttt();
        }
    }

    private ITxCall call;

    public void setCall(ITxCall call) {
        this.call = call;
    }

    public void removeCall() {
        this.call = null;
    }

    public void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                call.onText("1111");
            }
        }).start();
    }
}
