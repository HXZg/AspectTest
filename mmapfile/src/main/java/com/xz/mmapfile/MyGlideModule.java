package com.xz.mmapfile;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.Excludes;
//import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule;
//import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.module.LibraryGlideModule;

import java.io.InputStream;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.mmapfile  AspectTest
 * @Des MyGlideModule
 * @DATE 2020/10/12  14:58 星期一
 */
public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        // app
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        // library
        glide.getRegistry().replace(GlideUrl.class, InputStream.class,new OKHttpGlideUrlLoder.Factory());
    }

    public class NewGlideModule extends LibraryGlideModule{
        @Override
        public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
            super.registerComponents(context, glide, registry);
        }
    }

    @com.bumptech.glide.annotation.GlideModule
    @Excludes({MyGlideModule.class})
    public class AppGlideModule extends com.bumptech.glide.module.AppGlideModule {
        @Override
        public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
            super.applyOptions(context, builder);
        }

        @Override
        public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
            super.registerComponents(context, glide, registry);
        }

        @Override
        public boolean isManifestParsingEnabled() {
            return super.isManifestParsingEnabled();
        }
    }
}
