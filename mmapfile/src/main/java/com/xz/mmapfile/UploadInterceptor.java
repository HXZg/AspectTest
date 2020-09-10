package com.xz.mmapfile;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.mmapfile  AspectTest
 * @Des UploadInterceptor
 * @DATE 2020/9/9  14:44 星期三
 */
public class UploadInterceptor implements Interceptor {

    public static final String UPLOAD_TAG = "UPLOAD_TAG";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.i(UPLOAD_TAG,request.method());
        for (int i = 0;i < request.headers().size(); i++) {
            String name = request.headers().name(i);
            String value = request.headers().get(name);
            Log.i(UPLOAD_TAG,"request::" + name + "::" + value);
        }
        Response response = chain.proceed(request);
        for (int i = 0;i < response.headers().size();i++) {
            String name = response.headers().name(i);
            String value = response.headers().get(name);
            Log.i(UPLOAD_TAG,name + "::" + value);
        }
        return response;
    }
}
