package com.xz.aspectlib.intercept

/**
 * @title com.xz.aspectlib.intercept  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des InterceptUtils
 * @DATE 2020/7/31  14:59 星期五
 */
object InterceptUtils {

    private var interceptExecutor : InterceptExecutor? = null

    fun setIntercept(executor: InterceptExecutor) {
        interceptExecutor = executor
    }

    fun getExecutor() = interceptExecutor
}