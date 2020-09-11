package com.xz.aspectlib.annotation

/**
 * @title com.xz.aspectlib.annotation  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des Permission
 * @DATE 2020/7/31  10:29 星期五
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Permission(val value: Array<String>,val requestCode: Int)