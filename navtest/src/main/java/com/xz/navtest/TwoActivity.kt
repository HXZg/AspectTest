package com.xz.navtest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xz.lib.ActivityDestination

/**
 * @title com.xz.navtest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des TwoActivity
 * @DATE 2020/8/8  10:55 星期六
 */
@ActivityDestination(id = 23,control = "home")
class TwoActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)
    }
}