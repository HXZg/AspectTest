package com.xz.mmapfile.httpcache;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.mmapfile.httpcache  AspectTest
 * @Des CacheCall
 * @DATE 2020/10/17  9:23 星期六
 */
public class CacheCall<T> implements Callback {

    public CacheCall(CacheCallback<T> callback) {

    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Response newResponse = response.newBuilder().build();
        String s = newResponse.body().string();

        // cache response

    }
}
