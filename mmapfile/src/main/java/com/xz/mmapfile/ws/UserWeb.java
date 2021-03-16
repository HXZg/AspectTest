package com.xz.mmapfile.ws;

import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import com.xz.mmapfile.MyX509TrustManager;
import com.xz.mmapfile.R;
import com.xz.mmapfile.SSLHxzSocket;
import com.xz.mmapfile.SSLSocketClient;
import com.xz.mmapfile.UploadInterceptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.mmapfile.ws  AspectTest
 * @Des UserWeb
 * @DATE 2020/9/7  10:00 星期一
 */
public class UserWeb {

    public long getFileSize() {
        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hxz" + "/test.apk";
        File file = new File(savePath);
        if (file.exists()) {
            return file.length();  // 文件已下载的大小
        } else {
            return 0;
        }
    }

    public void downloadFile() {
        final long size = getFileSize();
        Request request = new Request.Builder().url("https://192.168.3.124:1111/get/txt/file.apk")
                .addHeader("range","bytes= " + size + "-")
                .build();
        new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .addInterceptor(new UploadInterceptor())
                .build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zzzzzzzzzzzz","fail::" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("zzzzzzzzzzzzzz",new Date().toString());
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                RandomAccessFile raf = null;
                String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hxz";
                File mkFile = new File(savePath);
                if (!mkFile.exists()) {
                    mkFile.mkdirs();
                }
                try{
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath,"test.apk");
                    raf = new RandomAccessFile(file, "rwd");
                    if (response.code() == 206) {
                        raf.seek(size);
                    }
                    long sum = 0;
                    while((len = is.read(buf)) != -1) {
                        raf.write(buf,0,len);
                        sum += len;
                        Log.i("zzzzzzzzzzzz",sum + "::" + total);
                    }
                    raf.close();
                    Log.i("zzzzzzzzzzz","success");
                }catch (Exception e) {
                    Log.i("zzzzzzzzzzzz",e.getMessage());
                }finally {
                    if (is != null) is.close();
                    if (raf != null) raf.close();
                }
            }
        });
    }

    public void uploadFile(String path) {
        RequestBody fileBody = RequestBody.create(MediaType.parse(""), new File(path));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file","file.apk",fileBody)
                .build();
        Request request = new Request.Builder().url("https://192.168.3.124:1111/upload").post(requestBody).build();

        new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .addInterceptor(new UploadInterceptor())
                .build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("zzzzzzzzzzzzzz",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("zzzzzzzzzzzzz",response.body().string());
            }
        });
    }

    public void connect(InputStream is) {
        MyX509TrustManager trustManager = new MyX509TrustManager(is);
        SSLHxzSocket socket = new SSLHxzSocket();
        SSLSocketFactory factory = socket.getSSLFacotry(trustManager);
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(factory,trustManager)
//                .sslSocketFactory(SSLSocketClient.sslSocketFactory(),SSLSocketClient.x509TrustManager)
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .pingInterval(40, TimeUnit.SECONDS).build();

//        RequestBody.create(MediaType.parse(""),new File(""));
        String url = "wss://192.168.3.124:1111/user/web";
        Request request = new Request.Builder().header("token","123").url(url).build();

        client.newWebSocket(request,new WebListener());
    }

    private void log(String head,String message) {
        Log.i("zzzzzzzzzzzzzz",head + ":::" + message);
    }

    class WebListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            try {
                log("on open",webSocket.toString() + ":::" + response.body().string());
                webSocket.send("open success");
                Thread.sleep(5000);
                webSocket.send("sleep 5000");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            log("on closed" , webSocket.toString() + "::" + code + "::" + reason);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            log("on closing" , webSocket.toString() + "::" + code + "::" + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            try {
                log("on failure",webSocket.toString() + "::" + t.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            log("on message" , webSocket.toString() + "::" + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            log("on message byte" , webSocket.toString() + "::" + bytes.string(Charset.forName("UTF-8")));
        }
    }
}
