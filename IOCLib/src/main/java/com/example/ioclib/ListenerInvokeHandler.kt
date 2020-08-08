package com.example.ioclib

import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class ListenerInvokeHandler(val obj: Any,val eventMethod: Method) : InvocationHandler {
    override fun invoke(p0: Any?, p1: Method?, p2: Array<out Any>?): Any? {
        return eventMethod.invoke(obj, *(p2 ?: emptyArray()))
    }
}