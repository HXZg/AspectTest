package com.xz.coroutinestest

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.Exception
import java.lang.IllegalStateException
import kotlin.concurrent.thread
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.measureTimeMillis

/**
 * @title com.xz.coroutinestest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des MainViewModel
 * @DATE 2020/9/11  9:34 星期五
 *
 * 创建的协程 执行逻辑不会阻塞当前线程
 *
 * 父协程手动调用cancel 或者 异常结束 会立即取消它的所有子协程
 * 父协程必须等待所有子协程完成（处于完成或取消状态） 才能完成
 * 子协程抛出未捕获的异常时 默认情况下会取消其父协程  抛出 CancellationException 会被当做正常的协程结束不会取消其父协程
 */
class MainViewModel : ViewModel() {

    fun test() {
        // viewModelScope : 内部创建一个 CloseableCoroutineScope  在clear 方法中将未执行完的协程取消掉,并缓存在 mBagOfTags 中
        // 指定 CoroutineDispatcher 为 主线程  + SupervisorJob  子 job 的失败 不会影响其它 job 的执行
        // 在 Android 中 异常没有被处理的话 最终都会被抛出，导致应用崩溃
    }

    fun createCoroutines() {

        /**
         * @param context: 协程上下文  主要包括Job 和 CoroutineDispatcher 元素  EmptyCoroutineContext 空的协程上下文
         * 重载了 plus 方法 可以直接用+ 号 连接 context
         * CoroutineDispatcher : 协程调度器 决定协程所在的线程或线程池  Dispatchers
         * Job : 任务 封装了协程中需要执行的代码逻辑  Deferred : 有返回值的任务
         *
         *
         * 创建一个新的协程 默认一个空的上下文 即不指定 job 以及 调度器  默认调度器 指定的线程 为共有的线程池
         */
        GlobalScope.launch {

        }
        Log.i("zzzzzzzzzzzz",Thread.currentThread().toString())
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        coroutineScope.launch {
//            Thread.sleep(5000)  // 会阻塞当前 线程 导致activity 显示 过慢
            delay(500)  // Thread.sleep()  会阻塞当前 线程    ，用delay 函数 会挂起当前协程
            Log.i("zzzzzzzzzzzz",Thread.currentThread().toString())

            val time = measureTimeMillis {
                val def = async() {
                    delay(5000)
                    11
                }  // 创建一个有返回值的新协程 挂起当前协程 直到返回

                delay(6000)
                val i = def.await()  // 等待拿到结果
            }

            Log.i("zzzzzzzzzzzzzz","$time")  // 6005

            launch {  // 创建一个没有返回值的新协程
                repeat(2) {
//                    delay(1000)
                    Log.i("zzzzzzzzzzzzzzzzzzzzz","12222221212121")
//                    yield()  // 挂起当前协程  可以让其它协程继续执行  直至 其它协程 执行完 或 也放弃使用权（yield）
                }
            }

            launch {
                repeat(2) {
//                    delay(1000)
                    Log.i("zzzzzzzzzzzzzzzzzzzzz","13333333333333333444444444")
//                    yield()  // 挂起当前协程  可以让其它协程继续执行  直至 其它协程 执行完 或 也放弃使用权（yield）
                }
            }

            withContext(this.coroutineContext) {
                // 在 指定协程上运行挂起代码块，并挂起该协程 直至 代码块运行完成  阻塞当前协程  知道拿到返回结果
                Log.i("zzzzzzzzzzzz11",Thread.currentThread().toString())
//                delay(5000)
                Log.i("zzzzzzzzzzzz","33333333333")
                111  // 最后一行 为 返回值
            }

            Log.i("zzzzzzzzzzzz","222222222")
        }

        Log.i("zzzzzzzzzzzz","1111111111")
    }

    fun runBlocking() {
        runBlocking(SupervisorJob()) {
            // 阻塞 当前线程 直到协程结束
        }
    }

    companion object{
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        @JvmStatic
        fun main(args: Array<String>) {
            val coroutineHandler = CoroutineExceptionHandler{context,e ->
                println(e.message)  //  + "${e.suppressed.last()}"
            }
            // SupervisorJob  让 各个子job 的工作 互不影响  coroutineHandler  统一处理异常
            /*val coroutineScope = CoroutineScope(SupervisorJob() + coroutineHandler)
            coroutineScope.launch {
                throw IllegalStateException("xxxx")
            }
            coroutineScope.launch {
                delay(1000)
                println("111111")
            }
            val dfe = coroutineScope.async {
                println("2")
                throw IllegalStateException("zzzzz") // 不会被打印  等待 await 的调用 才抛出
            }
            coroutineScope.launch {
                dfe.await()
            }
            Thread.sleep(2000)*/

            // supervisorScope  coroutineScope
            /*GlobalScope.launch {
                *//*supervisorScope {
                    launch() {
                        print("222")
                        throw IllegalStateException("111")  // 与 SupervisorJob 相似  可以设置 handler
                    }

                    launch {
                        delay(1000)
                        print("333")
                    }
                }*//*

                coroutineScope {

                    *//*actor<Any> {

                    }*//*
                    launch() {
                        print("222")
                        throw IllegalStateException("111")  // 设置的handler 无法起作用  同时 父协程 会被取消
                    }

                    launch {
                        delay(1000)
                        print("333")
                    }
                }
            }*/

            // 当 子协程 执行完后 才会处理原始异常
            // 异常聚合  一般取第一个异常
            /*GlobalScope.launch(coroutineHandler) {
                launch {
                    try {
                        delay(200)
                    }finally {
                        *//*println("222")
                        delay(100)
                        println("444")*//*
                        throw IllegalStateException("555")
                    }
                }
                launch {
                    delay(100)
                    println("111111")
                    throw IllegalStateException("33333")
                }
            }*/

            // 并发
            /*val mutex = Mutex()  // 相当于 java 中的 锁 只是不会阻塞线程  会挂起协程
            var counter = 0
            runBlocking<Unit> {
                repeat(100) {
                    GlobalScope.launch {
                        mutex.withLock { ++counter }
//                        ++counter
                    }
                }
                *//*repeat(5) {
                    launch { mutex.withLock { ++counter } }
                }*//*
            }
            Thread.sleep(500)
            println("$counter")*/

            // ThreadLocal  协程局部数据
            runBlocking {
                val local = ThreadLocal<String>().apply { set("Init") }

                printInLocal(local)

                val job = GlobalScope.launch(local.asContextElement("Launcher")) {
                    printInLocal(local)
                    local.set("change")
                    printInLocal(local)
                    delay(100)
//                    yield()  // 此处 协程 挂起后  会 设置local 中的值 为 null
                    // 恢复 协程 后  会重新 设置 为 launcher
                    printInLocal(local)
                }
                job.join()
                printInLocal(local)
            }

        }

        fun printInLocal(local: ThreadLocal<String>) {
            println("${Thread.currentThread()}     ${local.get()}")
        }
    }
}