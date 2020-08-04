package com.example.ioclib

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class ListenerInvokeHandler(val context: Any,val method: Method) : InvocationHandler {
    override fun invoke(p0: Any?, p1: Method?, p2: Array<out Any>?): Any? {
        return method.invoke(context, p2)
    }
}