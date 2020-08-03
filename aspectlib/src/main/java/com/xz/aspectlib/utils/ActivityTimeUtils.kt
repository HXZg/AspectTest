package com.xz.aspectlib.utils

import com.xz.aspectlib.BuildConfig

/**
 * 统计  activity 存在时长
 */
object ActivityTimeUtils {

    val packageName = BuildConfig.APPLICATION_ID
    private val map = hashMapOf<String,Long>()

    fun onCreate(key: String) {
        val startTime = System.currentTimeMillis()
        if (!map.containsKey(key)) {
            map[key] = startTime
        }
    }

    fun onDestroy(key: String) : Long {
        val startTime = map[key]
        if (startTime != null) {
            map.remove(key)
            return System.currentTimeMillis() - startTime
        }
        return 0
    }
}