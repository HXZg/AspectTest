package com.xz.aspectlib.utils

/**
 * 统计  activity 存在时长
 */
object ActivityTimeUtils {

    private var startTime = 0L

    fun onCreate() {
        startTime = System.currentTimeMillis()
    }

    fun onDestroy() : Long {
        return System.currentTimeMillis() - startTime
    }
}