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
import com.xz.aspectlib.utils.ActivityTimeUtils
import com.xz.aspectlib.utils.AppExecutor
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import org.aspectj.lang.reflect.MethodSignature
import java.lang.reflect.Method

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

    // 同时会匹配父类的方法  指定android.app.Activity 也没用
    @Pointcut("@target(com.xz.aspectlib.annotation.ActivityTime) && execution(void *.onCreate(android.os.Bundle))")
    fun onCreate() {}

    @Pointcut("@target(com.xz.aspectlib.annotation.ActivityTime) && execution(void *.onDestroy())")
    fun onDestroy() {}

    @Before("onCreate()")
    fun getTime(joinPoint: JoinPoint) {
        val name = joinPoint.`this`.toString()
        ActivityTimeUtils.onCreate(name)
        Log.i("zzzzzzzzzzzz","onCreate  $name")
    }

    @After("onDestroy()")
    fun calculateActivityTime(joinPoint: JoinPoint) {
        val name = joinPoint.`this`.toString()
        val time = ActivityTimeUtils.onDestroy(name)
        if (time == 0L) {

        }
        Log.i("zzzzzzzzzzz","$time ,,,, $name")
    }

    private fun getMethodAnnotation(joinPoint: ProceedingJoinPoint) {
        // 获取到执行的方法
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        // 获取到方法上的所有注解
        val annotations = method.annotations

        // 获取到 方法所有 属性的注解
        getMethodParamByAnnotation(method)

        annotations.forEach {
            // 获取到 该注解上的注解
            val annotation = it.javaClass.getAnnotation(Intercept::class.java)
        }

        // 获取到 该方法切入点的所有属性值
        val args = joinPoint.args
    }

    private fun getMethodParamByAnnotation(method: Method) {
        // 获取 方法所有属性的注解
        val parameterAnnotations = method.parameterAnnotations
        parameterAnnotations.forEachIndexed { index, arrayOfAnnotations ->
            // 每个属性上的所有注解
            arrayOfAnnotations.forEach {
                // 判断是否为想要的注解
            }
        }
    }
}