package com.xz.mmapfile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.mmapfile  AspectTest
 * @Des ProgressInterceptor
 * @DATE 2020/10/12  16:17 星期一
 */
public class ProgressInterceptor implements Interceptor {

    private Map<String, ProgressListener> listenerMap = new HashMap<>();


    public void addProgressListener(String key,ProgressListener listener) {
        listenerMap.put(key,listener);
    }

    public void removeProgressListener(String key) {
        listenerMap.remove(key);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        String s = request.url().toString();
        ResponseBody body = response.body();
        Response newResponse = response.newBuilder().body(new ProgressResponseBody(listenerMap.get(s),body)).build();
        return newResponse;
    }

    interface ProgressListener{
        void onProgress(int progress);
    }
}
