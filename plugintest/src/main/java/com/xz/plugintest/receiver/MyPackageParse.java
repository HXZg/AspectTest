package com.xz.plugintest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.plugintest.receiver  AspectTest
 * @Des MyPackageParse
 * @DATE 2020/8/28  9:27 星期五
 */
public class MyPackageParse {

    public void parsePackage(Context context,File path) throws Exception {

        Class<?> parse = Class.forName("android.content.pm.PackageParser");
        Object packageParse = parse.newInstance();

        Method parsePackageMethod = parse.getDeclaredMethod("parsePackage", File.class, int.class);

        Object pkg = parsePackageMethod.invoke(packageParse, path, PackageManager.GET_RECEIVERS);

        Field receivers = pkg.getClass().getDeclaredField("receivers");
        receivers.setAccessible(true);
        List list = (List) receivers.get(pkg);

        DexClassLoader classLoader = new DexClassLoader(path.getAbsolutePath(),
                context.getCacheDir().getAbsolutePath() + "/plugin",null,context.getClassLoader());
        for (Object o : list) {
            Field className = o.getClass().getField("className");
            className.setAccessible(true);
            Object name = className.get(o);
            Class<?> receive = classLoader.loadClass(name.toString());

            Field intents = o.getClass().getField("intents");
            List<IntentFilter> filter = (List<IntentFilter>) intents.get(o);

            for (IntentFilter intentFilter : filter) {
                context.registerReceiver((BroadcastReceiver) receive.newInstance(),intentFilter);
            }
        }
    }
}
