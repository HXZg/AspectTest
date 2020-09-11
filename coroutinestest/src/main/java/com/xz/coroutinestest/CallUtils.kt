package com.xz.coroutinestest

import java.lang.IllegalStateException

/**
 * @title com.xz.coroutinestest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des CallUtils
 * @DATE 2020/9/11  14:28 星期五
 */
class CallUtils<T> {

    companion object{
        suspend fun test() {
            val any = CallUtils<Any>().await()
        }
    }

    fun equeue(callback: Callback<T>) {
        callback.onFail(IllegalStateException())
    }
}