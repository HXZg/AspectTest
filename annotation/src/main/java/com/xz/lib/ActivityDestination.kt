package com.xz.lib

/**
 * @title com.xz.lib  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des FragmentDestination
 * @DATE 2020/8/7  14:05 星期五
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class ActivityDestination(val id: Int, val isStart: Boolean = false,
                                     val control: String = "", val pageUrl: String = "", val intercept: String = "")