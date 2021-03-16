package com.xz.aspecttest

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ioclib.InjectUtils
import com.example.ioclib.annotation.ContentView
import com.example.ioclib.annotation.OnClick
import com.example.ioclib.annotation.ViewInject

/**
 * @title com.xz.aspecttest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des TestThreeActivity
 * @DATE 2020/8/6  14:25 星期四
 */
@ContentView(R.layout.activity_three)
class TestThreeActivity : AppCompatActivity() {

    @ViewInject(R.id.tv_three)
    lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        InjectUtils.inject(this)
    }

    @OnClick([R.id.btn_three])
    fun onClick(view: View) {
        text.text = "btn"
    }
}