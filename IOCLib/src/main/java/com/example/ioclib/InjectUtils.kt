package com.example.ioclib

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
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
                val eventBus = it.annotationClass.java.getAnnotation(EventBus::class.java)
                if (eventBus != null) {
                    val listenerSetter = eventBus.listenerSetter
                    val listenerClazz = eventBus.listenerType.java
                    val methodName = eventBus.methodName

                    val value = it.annotationClass.java.getDeclaredMethod("value")
                    val viewId = value.invoke(it) as IntArray
                    Log.i("zzzzzzzzzzzzzzz","${viewId.size}")
                    viewId.forEach { id ->
                        val findViewById = javaClass.getMethod("findViewById", Int::class.java)
                        val view = findViewById.invoke(context, id) as View

                        val listener = Proxy.newProxyInstance(listenerClazz.classLoader,
                            Array(1){listenerClazz},ListenerInvocationHandler(context,method))

                        Log.i("zzzzzzzzzz","$view ,,, $listenerSetter ,,, $listenerClazz")
                        val setListener = view.javaClass.getMethod(listenerSetter, listenerClazz)
                        setListener.invoke(view,listener)
                    }

                }
            }
        }
    }

    fun inject(fragment: Fragment) {
        fragment.javaClass.declaredFields.forEach {
            val views = it.getAnnotation(ViewInject::class.java)
            if (views != null) {
                val id = views.value
                val declaredMethod =
                    fragment.view?.javaClass?.getDeclaredMethod("findViewById", Int::class.java)
                val view = declaredMethod?.invoke(fragment.view, id)
                it.isAccessible = true
                it.set(fragment,view)
            }
        }
        val javaClass = fragment.javaClass
        javaClass.declaredMethods.forEach {method ->
            method.annotations.forEach {
                val eventBus = it.annotationClass.java.getAnnotation(EventBus::class.java)
                if (eventBus != null) {
                    val listenerSetter = eventBus.listenerSetter
                    val listenerClazz = eventBus.listenerType.java
                    val methodName = eventBus.methodName

                    val value = it.annotationClass.java.getDeclaredMethod("value")
                    val viewId = value.invoke(it) as IntArray
                    Log.i("zzzzzzzzzzzzzzz","${viewId.size}")
                    viewId.forEach { id ->
                        val fv = fragment.view?.javaClass
                        val findViewById = fv?.getMethod("findViewById", Int::class.java)
                        val view = findViewById?.invoke(fragment.view, id) as View

                        val listener = Proxy.newProxyInstance(listenerClazz.classLoader,
                            Array(1){listenerClazz},ListenerInvocationHandler(fragment,method))

                        Log.i("zzzzzzzzzz","$view ,,, $listenerSetter ,,, $listenerClazz")
                        val setListener = view.javaClass.getMethod(listenerSetter, listenerClazz)
                        setListener.invoke(view,listener)
                    }

                }
            }
        }
    }

}