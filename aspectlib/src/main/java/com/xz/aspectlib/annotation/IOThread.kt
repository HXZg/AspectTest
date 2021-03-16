package com.xz.aspectlib.annotation

import com.xz.aspectlib.utils.ThreadEnum

/**
 * @title com.xz.aspectlib.annotation  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des IOThread
 * @DATE 2020/7/31  14:44 星期五
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class IOThread(val value: ThreadEnum)