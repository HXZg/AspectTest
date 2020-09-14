package com.xz.coroutinestest

/**
 * @title com.xz.coroutinestest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des Callback
 * @DATE 2020/9/11  14:17 星期五
 */
interface Callback<T> {

    fun onSuccess(t: T)

    fun onFail(e: Exception)
}