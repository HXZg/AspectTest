package com.xz.skinmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @title com.xz.skinmanager  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des BaseActivity
 * @DATE 2020/8/27  10:26 星期四
 */
open class BaseActivity : AppCompatActivity() {

    private val factory by lazy { SkinFactory(delegate) }

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutInflater.factory2 = factory
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        factory.apply()
    }

    fun testChange(path: String) {
        SkinManager.loadPath(path)
        factory.apply()
    }
}