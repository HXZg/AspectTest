package com.xz.mmapfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xz.mmapfile.ws.UploadChunkTask;
import com.xz.mmapfile.ws.UploadTask;
import com.xz.mmapfile.ws.UserWeb;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.ExecutionException;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class MainActivity extends AppCompatActivity {

    UserWeb userWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MmapFileUtils utils = new MmapFileUtils();
//
//        utils.write();

//        userWeb = new UserWeb();

        /*try {
            userWeb.connect(getAssets().open("hxz.cer"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/

//        getSupportFragmentManager().beginTransaction().add()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
//
//        web.connect();
    }


    public void open(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent,"select file"),0);

//        userWeb.downloadFile();
        ImageView iv = findViewById(R.id.iv_pic);

        RequestManager manager = Glide.with(this);  // 加入fragment 中 的 manager
        RequestBuilder<Drawable> builder = manager.asDrawable();  // 构建一个请求build
        RequestBuilder<Drawable> load = builder.load("");  // 设置 model 参数
        ViewTarget<ImageView, Drawable> into = load.into(iv);

//        RoundedCorners
//        Glide.with(this).load("").into(iv);
        // 请求发送到哪去了  怎么被维护的  怎么被处理的
        RequestOptions options = RequestOptions.circleCropTransform()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .skipMemoryCache(true)
                .centerCrop()
                .override(100,100)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this)
//                .applyDefaultRequestOptions(options)
                .asBitmap()  // 转bitmap 存储显
                .load("http://192.168.3.124:1111/fileDir/test.jpg")  // 设置url
//                .dontTransform()  // 取消图片变换
//                .centerCrop()
//                .fitCenter()
//                .transform(new CircleCrop())  // 添加图片变换
//                .placeholder(R.drawable.ic_launcher_background)  // 设置占位图片
//                .error(R.drawable.ic_launcher_background)  // 设置 错误图片
//                .diskCacheStrategy(DiskCacheStrategy.NONE)  // 设置磁盘缓存策略为不缓存  默认为缓存转换过后的图片
//                .override(100,100) // 设置图片大小  默认用imageView 的 宽高
//                .skipMemoryCache(true)  // 跳过内存缓存
//                .apply(options)
//                .format(DecodeFormat.PREFER_ARGB_8888)
//                .into(iv);
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        // 加载失败  返回false 事件没有被处理，继续向下传递
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .preload();  // 预加载 缓存图片  内部维护一个空target实现


        FutureTarget<File> submit = Glide.with(this).download("").submit();// 仅下载图片
        FutureTarget<File> target = Glide.with(this.getApplicationContext()).load("")
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);  // 仅下载图片
        try {
            File file = target.get();  // 阻塞线程
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private SimpleTarget target = new SimpleTarget<Drawable>() {
        @Override
        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition transition) {

        }
    };

    private ViewTarget viewTarget = new ViewTarget<LinearLayout,Drawable>(new LinearLayout(this)) {
        @Override
        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition transition) {

        }
    };

        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            Uri uri = geturi(data);
            String path = null;
            String scheme = null;
            if (uri != null) {
                scheme = uri.getScheme();
            }
            if (scheme != null && scheme.equalsIgnoreCase("content")) {
                String[] project = {"_data"};
                Cursor cursor = getContentResolver().query(uri, project, null, null, null);
                int index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    path = cursor.getString(index);
                }
                if (path == null) {
                    path = queryImage(uri.getPath());
                }
            } else if (scheme != null && scheme.equalsIgnoreCase("file")) {
                path = uri.getPath();
            }

            Log.i("zzzzzzzzzzzz",path + ":::" + scheme);

            if (!TextUtils.isEmpty(path)) {
//                new UploadChunkTask().execute(path);
//                userWeb.uploadFile(path);

                // huffman
//                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                new MmapFileUtils().compress(bitmap,getExternalCacheDir().getAbsolutePath() + "/test.png");

                Luban.with(this)
                        .load(path)
                        .ignoreBy(100)
                        .setTargetDir(getExternalCacheDir().getAbsolutePath())
                        .filter(new CompressionPredicate() {
                            @Override
                            public boolean apply(String path) {
                                return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                            }
                        })
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                // TODO 压缩开始前调用，可以在方法内启动 loading UI
                            }

                            @Override
                            public void onSuccess(File file) {
                                // TODO 压缩成功后调用，返回压缩后的图片文件
                            }

                            @Override
                            public void onError(Throwable e) {
                                // TODO 当压缩过程出现问题时调用
                            }
                        }).launch();
            }
        }
    }

    /** * 解决小米手机上获取图片路径为null的情况 * @param intent * @return */
    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/*"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    private String queryImage(String data) {
        String _path="_data";
        String _album="bucket_display_name";
        int idx = data.lastIndexOf(":");
        String id = null;
        if (idx != -1) {
            id = data.substring(idx + 1);
        }
        Log.d("onActivityResult","id:" + id);
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_id=" + id, null, null);
        if (cursor.moveToFirst()) {
            String path=cursor.getString(cursor.getColumnIndex(_path));
            String album=cursor.getString(cursor.getColumnIndex(_album));
            Log.d("onActivityResult","path:" + path + "," + album);

            cursor.close();
            return path;
        }
        return null;
    }
}
