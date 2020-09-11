package com.xz.aspectlib.utils

import java.util.concurrent.Executors

/**
 * @title com.xz.aspectlib.utils  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des AppExecutor
 * @DATE 2020/7/31  14:20 星期五
 */
object AppExecutor {

    private val singIO by lazy {
        Executors.newSingleThreadExecutor()
    }

    private val poolIO by lazy {
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    }

    private val mainExecutor by lazy { MainExecutor() }

    fun executionRunnable(type: ThreadEnum,runnable: Runnable) {
        if (type == ThreadEnum.MAIN && mainExecutor.isMainThread()) {
            // 当前就在主线程
            runnable.run()
            return
        }
        if (type != ThreadEnum.MAIN && !mainExecutor.isMainThread()) {
            runnable.run()
            return
        }
        when(type) {
            ThreadEnum.FIXED -> poolIO.execute(runnable)
            ThreadEnum.SINGLE -> singIO.execute(runnable)
            ThreadEnum.MAIN -> mainExecutor.execute(runnable)
        }
    }
}