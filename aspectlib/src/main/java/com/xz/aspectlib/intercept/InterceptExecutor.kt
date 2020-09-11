package com.xz.aspectlib.intercept

/**
 * @title com.xz.aspectlib.intercept  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des InterceptExecutor
 * @DATE 2020/7/31  15:00 星期五
 */
interface InterceptExecutor {

    /**
     * 返回 false  则执行对应的方法，否则拦截 不执行
     */
    fun intercept(type: Int) : Boolean
}