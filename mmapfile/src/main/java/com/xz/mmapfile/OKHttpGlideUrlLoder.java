package com.xz.mmapfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.mmapfile  AspectTest
 * @Des OKHttpGlideUrlLoder
 * @DATE 2020/10/12  15:38 星期一
 */
public class OKHttpGlideUrlLoder implements ModelLoader<GlideUrl, InputStream> {

    /** The default factory for {@link HttpGlideUrlLoader}s. */
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private final ModelCache<GlideUrl, GlideUrl> modelCache = new ModelCache<>(500);

        @NonNull
        @Override
        public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new OKHttpGlideUrlLoder();
        }

        @Override
        public void teardown() {
            // Do nothing.
        }
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull GlideUrl url, int width, int height, @NonNull Options options) {
        return new LoadData<>(url,new OkHttpFetcher(new OkHttpClient.Builder().build(),url));
    }

    @Override
    public boolean handles(@NonNull GlideUrl url) {
        return false;
    }
}
