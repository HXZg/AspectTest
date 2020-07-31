package com.xz.aspectlib

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.xz.aspectlib.annotation.IOThread
import com.xz.aspectlib.annotation.Intercept
import com.xz.aspectlib.annotation.Permission
import com.xz.aspectlib.intercept.InterceptExecutor
import com.xz.aspectlib.intercept.InterceptUtils
import com.xz.aspectlib.permission.PermissionUtils
import com.xz.aspectlib.utils.AppExecutor
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut

/**
 * @title com.xz.aspecttest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des AspectTest
 * @DATE 2020/7/30  15:41 星期四
 */
@Aspect
class AspectTest {

    @Pointcut("execution(@com.xz.aspectlib.annotation.Intercept * *(..))")
    fun intercept() {}

    @Around("intercept() && @annotation(intercept)")
    fun beforeTest(joinPoint: ProceedingJoinPoint,intercept: Intercept) {
        val types = intercept.type
        val executor = InterceptUtils.getExecutor()
        if (executor == null) {
            joinPoint.proceed()
            return
        }else {
            if (!isIntercept(types, executor)) {
                joinPoint.proceed()
            }
        }
    }

    private fun isIntercept(types: IntArray,executor: InterceptExecutor) : Boolean {
        types.forEach {
            val intercept = executor.intercept(it)
            if (intercept) {
                // 拦截事件 不再执行
                return true
            }
        }
        return false
    }

    @Pointcut("execution(@com.xz.aspectlib.annotation.IOThread * *(..))")
    fun thread() {}

    @Around("thread() && @annotation(thread)")
    fun exeThread(joinPoint: ProceedingJoinPoint,thread: IOThread) {
        val enum = thread.value
        val runnable = Runnable {
            joinPoint.proceed()
        }
        AppExecutor.executionRunnable(enum,runnable)
    }

    @Pointcut("execution(@com.xz.aspectlib.annotation.Permission * *(..))")
    fun getPermission(){}

    @Around("getPermission() && @annotation(permission)")
    fun requestPermission(joinPoint: ProceedingJoinPoint,permission: Permission) {
        val value = permission.value
        val requestCode = permission.requestCode

        var context: Context? = null
        val any = joinPoint.`this`
        if (any is Context) {
            context = any
        } else if (any is Fragment) {
            context = any.context
        }
        if (context != null) {
            PermissionUtils.init(context,value,requestCode){
                if (it == 0) {
                    joinPoint.proceed()
                } else {
                    if (it == 1) PermissionUtils.exeFailMethod(any,requestCode) else PermissionUtils.exeCancelMethod(any,requestCode)
                }
            }
        }
    }
}