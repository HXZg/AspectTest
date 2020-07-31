package com.xz.aspectlib.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

/**
 * @title com.xz.aspectlib.utils  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des MainExecutor
 * @DATE 2020/7/31  14:20 星期五
 */
class MainExecutor : Executor {
    private val mHandler by lazy { Handler(Looper.getMainLooper()) }

    override fun execute(p0: Runnable?) {
        if (p0 != null) mHandler.post(p0)
    }

    fun isMainThread() : Boolean {
        return Looper.getMainLooper() == Looper.myLooper()
    }
}