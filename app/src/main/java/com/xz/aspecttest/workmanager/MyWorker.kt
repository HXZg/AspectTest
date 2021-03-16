package com.xz.aspecttest.workmanager

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * @title com.xz.aspecttest.workmanager  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des MyWorker
 * @DATE 2020/8/14  9:29 星期五
 */
class MyWorker(val context: Context,params: WorkerParameters) : Worker(context,params) {

    private val mHandler = Handler(Looper.getMainLooper())

    override fun doWork(): Result {
        Log.i("zzzzzzzzzzz","do work")

        setProgressAsync(Data.Builder().putInt("progress",10).build())

        mHandler.post {
            Toast.makeText(context,"11111111",Toast.LENGTH_SHORT).show()
        }
        Log.i("zzzzzzzzzzzzzzzzzz","do work success")
        return Result.success()
    }
}