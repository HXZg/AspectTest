package com.xz.aspectlib.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.xz.aspectlib.annotation.PermissionCancel
import com.xz.aspectlib.annotation.PermissionDenied

/**
 * @title com.xz.aspectlib.permission  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des PermissionUtils
 * @DATE 2020/7/31  10:44 星期五
 */
typealias PermissionResult = (type: Int) -> Unit
object PermissionUtils {

    var callResult: PermissionResult? = null
    private set

    fun init(context: Context,permission: Array<String>,requestCode: Int,callBack: PermissionResult) {
        callResult = callBack
        if (isPermissionGranted(context,permission)) {  // 有权限需要被请求
            PermissionActivity.launcherPermission(context, permission, requestCode)
        }
    }

    private fun isPermissionGranted(context: Context,permission: Array<String>) : Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callResult?.invoke(0)
            return false
        } else {
            permission.forEach {
                if (ActivityCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_DENIED) {
                    return true
                }
            }
            callResult?.invoke(0)
            return false
        }
    }

    fun exeFailMethod(any: Any,requestCode: Int) {
        val clazz = any::class.java
        clazz.declaredMethods.forEach {
            val denied = it.getAnnotation(PermissionDenied::class.java)
            if (denied != null) {
                it.invoke(any,requestCode)
                return@forEach
            }
        }
    }

    fun exeCancelMethod(any: Any,requestCode: Int) {
        val clazz = any::class.java
        clazz.declaredMethods.forEach {
            val denied = it.getAnnotation(PermissionCancel::class.java)
            if (denied != null) {
                it.invoke(any,requestCode)
                return@forEach
            }
        }
    }


}