package com.xz.aspectlib.annotation

/**
 * @title com.xz.aspectlib.annotation  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des Intercept
 * @DATE 2020/7/31  14:57 星期五  type 为数组 表示可以组合执行一些拦截的方法
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Intercept(val type: IntArray)