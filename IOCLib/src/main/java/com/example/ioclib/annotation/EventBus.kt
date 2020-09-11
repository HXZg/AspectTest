package com.example.ioclib.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EventBus(val listenerSetter: String,val listenerType: KClass<*>,val methodName: String)