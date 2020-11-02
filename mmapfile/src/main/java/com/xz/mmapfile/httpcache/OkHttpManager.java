package com.xz.mmapfile.httpcache;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.mmapfile.httpcache  AspectTest
 * @Des OkHttpManager
 * @DATE 2020/10/17  9:27 星期六
 */
public class OkHttpManager {

    private OkHttpClient client;

    public OkHttpManager() {
        this(new OkHttpClient.Builder().build());
    }

    public OkHttpManager(OkHttpClient client) {
        this.client = client;
    }

    public Response execute(Request request) {
        Response response = null;
        try {
            response =  client.newCall(request).execute();
            //cache response
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public <T> void enqueue(Request request,CacheCallback<T> callback,Class<T> clz) {
        String header = request.header("");
        if (header != null && !header.isEmpty()) {

        }
        // get cache  callback
        client.newCall(request).enqueue(new CacheCall<T>(callback));
    }
}
