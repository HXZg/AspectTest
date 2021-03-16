package com.xz.qrcodedemo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.qrcodedemo  AspectTest
 * @Des MainViewModel
 * @DATE 2020/10/13  17:55 星期二  生命周期绑定
 */
public class MainViewModel extends ViewModel {

    LiveData<String> liveData = new MutableLiveData<>();

    MainModel model;

    public MainModel getModel() {
//        String s = model.getInfo();
        return model;
    }
}
