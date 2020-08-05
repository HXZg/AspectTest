package com.example.ioclib

import android.view.View
import com.example.ioclib.annotation.ContentView
import com.example.ioclib.annotation.EventBus
import com.example.ioclib.annotation.ViewInject
import java.lang.reflect.Proxy

object InjectUtils {

    fun inject(context: Any) {
        injectLayout(context)
        injectView(context)
        injectClick(context)
    }

    private fun injectLayout(context: Any) {
        val javaClass = context.javaClass
        val contentView = javaClass.getAnnotation(ContentView::class.java) ?: return
        val layoutId = contentView.value

        val method = javaClass.getMethod("setContentView", Int::class.java)
        method.invoke(context,layoutId)
    }

    private fun injectView(context: Any) {

        val javaClass = context.javaClass
        javaClass.declaredFields.forEach {
            val viewInject = it.getAnnotation(ViewInject::class.java) ?: return@forEach
            val value = viewInject.value
            val method = javaClass.getMethod("findViewById", Int::class.java)
            val invoke = method.invoke(context, value)
            it.isAccessible = true
            it.set(context,invoke)
        }
    }

    private fun injectClick(context: Any) {
        val javaClass = context.javaClass
        javaClass.declaredMethods.forEach {method ->
            method.annotations.forEach {
                val eventBus = it.javaClass.getAnnotation(EventBus::class.java)
                if (eventBus != null) {
                    val listenerSetter = eventBus.listenerSetter
                    val listenerClazz = eventBus.listenerType.java
                    val methodName = eventBus.methodName

                    val value = it.javaClass.getDeclaredField("value")
                    val viewId = value.get(it) as IntArray
                    viewId.forEach { id ->
                        val findViewById = javaClass.getMethod("findViewById", Int::class.java)
                        val view = findViewById.invoke(context, value) as View

                        val listener = Proxy.newProxyInstance(listenerClazz.classLoader,
                            Array(1){listenerClazz},ListenerInvokeHandler(context,method))

                        val declaredMethod =
                            view.javaClass.getDeclaredMethod(listenerSetter, listenerClazz)
                        declaredMethod.invoke(view,listener)
                    }

                }
            }
        }
    }

}