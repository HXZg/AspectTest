package com.example.ioclib;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.example.ioclib  AspectTest
 * @Des ListenerInvocationHandler
 * @DATE 2020/8/6  14:11 星期四
 */
public class ListenerInvocationHandler implements InvocationHandler {

    private Object context;
    private Method eventMethod;

    public ListenerInvocationHandler(Object context,Method method) {
        this.context = context;
        this.eventMethod = method;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return eventMethod.invoke(context, objects);
    }
}
