package com.xz.coroutinestest

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

/**
 * @title com.xz.coroutinestest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des MainActivity
 * @DATE 2020/9/11  9:32 星期五
 */
class MainActivity : AppCompatActivity() {

    private val mainVM by lazy {
        ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainVM.createCoroutines()

        Log.i("zzzzzzzzzzzz","1111111111")
    }


}