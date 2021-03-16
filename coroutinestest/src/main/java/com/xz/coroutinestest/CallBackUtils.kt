package com.xz.coroutinestest

import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @title com.xz.coroutinestest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des CallBackUtils
 * @DATE 2020/9/11  14:25 星期五
 */
/**
 *  回调 的 协程 简化写法
 */
suspend fun <T> CallUtils<T>.await() : T = suspendCoroutine {
    equeue(object : Callback<T> {
        override fun onSuccess(t: T) {
            it.resume(t)
        }

        override fun onFail(e: Exception) {
            it.resumeWithException(e)
        }
    })
}

/**
 * 协程 的 取消
 */
fun main(args: Array<String>) {
    runBlocking(Dispatchers.Unconfined) {
        val job1 = launch {
            repeat(5) {
                println("job1   $it")
                delay(500)  // 会检测协程状态
            }
        }

        delay(1000)
        job1.cancel()

        val job2 = launch {
            var i = 1;
            var nextPrintTime = 0L
            while (isActive) {  //i <= 5  换成 isActive  进行检测当前协程的状态  注： 必须要指定 Dispatchers  不能设置Dispatchers.Unconfined
                val currentTime = System.currentTimeMillis()
                if (currentTime > nextPrintTime) {
                    println("job2  ${i++}")
                    nextPrintTime = currentTime + 1000
                }
            }
        }

        delay(700)
        job2.cancel()  // 协程的取消 只是状态的改变  在 任务2 中 没有检查协程状态的逻辑 所以 依然是在执行运算
    }
}