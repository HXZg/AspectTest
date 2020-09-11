package com.xz.lib

/**
 * @title com.xz.lib  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des FragmentDestination
 * @DATE 2020/8/7  14:05 星期五
 * @param id: ID作为key  也可以用它跳转
 * @param isStart:是否是开始的目的地
 * @param control:所属控制器
 * @param pageUrl:深度连接
 * @param intercept:经过的拦截器名
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class FixFragmentDestination(val id: Int, val isStart: Boolean = false,
                                        val control: String = "", val pageUrl: String = "", val intercept: String = "")