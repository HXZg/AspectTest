package com.xz.aspectlib.annotation

/**
 * @title com.xz.aspectlib.annotation  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des ActivityTime
 * @DATE 2020/8/3  9:31 星期一
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityTime(val value: String = "") {
}