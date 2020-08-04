package com.example.ioclib.annotation

import android.view.View

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@EventBus(listenerSetter = "setOnClickListener",listenerType = View.OnClickListener::class,methodName = "onClick")
annotation class OnClick(val value: IntArray)