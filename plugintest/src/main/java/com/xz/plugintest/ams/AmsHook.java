package com.xz.plugintest.ams;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import com.xz.plugintest.ui.login.LoginActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.plugintest.ams  AspectTest
 * @Des AmsHook
 * @DATE 2020/9/4  10:27 星期五
 */
public class AmsHook {

    // Android 9.0
    public void hook() throws Exception {
        // app 跳转activity 最终 都会调用到 ActivityManagerService 的 startActivity 方法
        // app 进程 都是 通过 ActivityManager.getService() 方法 拿到 AMS  (Android 28)
        // 适配 其它 Android 版本 拿到Singleton  以后的逻辑相同  其它版本 需要从不同的地方拿 Singleton
        // 例如 ： (29/10.0) ActivityTaskManager  拿   IActivityTaskManagerSingleton
        // ActivityManager
        // IActivityManagerSingleton
        // mInstance
        Class<?> activityManager = Class.forName("android.app.ActivityManager");
        Field singletons = activityManager.getDeclaredField("IActivityManagerSingleton");
        singletons.setAccessible(true);
        Object singleton = singletons.get(null);  // 静态变量  可以直接获取

        Field ams = Class.forName("android.util.Singleton").getDeclaredField("mInstance");
        ams.setAccessible(true);
        Object amsObject = ams.get(singleton);  // 需要动态代理的对象

        Class<?> iam = Class.forName("android.app.IActivityManager");
        Object o = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{iam}, new AmsInvocationHandler(amsObject));

        ams.set(singleton,o);  // 重新设置 ams 对象
    }

    class AmsInvocationHandler implements InvocationHandler {
        private Object ams;

        public AmsInvocationHandler(Object ams) {
            this.ams = ams;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("startActivity")) {
                Intent intent = null;
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg != null) {
                        if (arg.getClass().getSimpleName().equals("Intent")) {
                            intent = (Intent) arg;
                            break;
                        }
                    }
                }
                // 可以内部维护 变量  判断是否需要 集中式登录操作
                if (intent != null) { // 把原来的 跳转 逻辑  改到  LoginActivity
                    intent.setComponent(new ComponentName("com.xz.plugintest",LoginActivity.class.getName()));
                }
            }
            return method.invoke(ams,args);  // 最终执行的方法  依然是 ActivityManagerService
        }
    }
}
