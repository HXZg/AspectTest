package com.xz.mmapfile;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.mmapfile  AspectTest
 * @Des OkHttpFetcher
 * @DATE 2020/10/12  15:27 星期一
 */
public class OkHttpFetcher implements DataFetcher<InputStream> {

    private OkHttpClient okHttpClient;
    private GlideUrl url;
    private InputStream inputStream;
    private ResponseBody body;
    private volatile boolean isCancelled;

    public OkHttpFetcher(OkHttpClient client,GlideUrl url) {
        this.okHttpClient = client;
        this.url = url;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        Request.Builder builder = new Request.Builder().url(url.toStringUrl());
        for (Map.Entry<String, String> stringStringEntry : url.getHeaders().entrySet()) {
            String key = stringStringEntry.getKey();
            builder.addHeader(key,stringStringEntry.getValue());
        }

        builder.addHeader("httplib", "OkHttp");
        Request request = builder.build();
        if (isCancelled) {
            return;
        }
        try {
            Response response = okHttpClient.newCall(request).execute();
            body = response.body();
            if (!response.isSuccessful() || body == null) {
                throw new IOException("");
            }
            inputStream = ContentLengthInputStream.obtain(body.byteStream(),
                    body.contentLength());
            callback.onDataReady(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void cleanup() {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (body != null) {
                    body.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
