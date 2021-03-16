package com.example.ioclib.annotation;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.example.ioclib.annotation  AspectTest
 * @Des TestObject
 * @DATE 2020/8/6  14:14 星期四
 */
public class TestObject implements Cloneable{

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        ArrayList list = new ArrayList();
        list.clone();
        return super.clone();
    }
}
