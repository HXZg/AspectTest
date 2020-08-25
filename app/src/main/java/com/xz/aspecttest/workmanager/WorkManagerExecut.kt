package com.xz.aspecttest.workmanager

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @title com.xz.aspecttest.workmanager  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des WorkManagerExecut
 * @DATE 2020/8/14  9:31 星期五
 */
object WorkManagerExecut {

    inline fun<reified T: Worker> queueWork(context: Context) : UUID {

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        // >= 15 min
        val build = PeriodicWorkRequest.Builder(T::class.java, 20, TimeUnit.SECONDS).build()

        val request = OneTimeWorkRequest.Builder(T::class.java).addTag("my worker")
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueue(request)

        return request.id
    }
}