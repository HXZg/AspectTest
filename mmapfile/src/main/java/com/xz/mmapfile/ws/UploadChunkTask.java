package com.xz.mmapfile.ws;

import android.os.AsyncTask;
import android.util.Log;

import com.xz.mmapfile.SSLSocketClient;
import com.xz.mmapfile.UploadInterceptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.mmapfile.ws  AspectTest
 * @Des UploadTask
 * @DATE 2020/9/10  9:56 星期四
 */
public class UploadChunkTask extends AsyncTask<String,Integer,Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... strings) {
        int blockLength = 2048 * 2048;
        String path = strings[0];
        File file = new File(path);
        long size = file.length();
        long chunks = 0;
        if (size % blockLength == 0) {
            chunks = size / blockLength;
        } else {
            chunks = size / blockLength + 1;
        }
        long chunk = 0;
        try {
            getChunk("file.apk",chunks);
            while (chunk < chunks) {
                Log.i("zzzzzzzzzzzzzz",chunk + "::::" + chunks);
                Response response = uploadFile(file, chunk,chunks);
                if (response.isSuccessful()) {
                    chunk += 1;
                } else  {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private void getChunk(String name,long chunks) throws IOException {
        FormBody build = new FormBody.Builder()
                .add("name", name)
                .add("chunks", chunks + "")
                .build();
        Request request = new Request.Builder().url("https://192.168.3.124:1111/big/chunks").post(build).build();

        Response response = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .addInterceptor(new UploadInterceptor())
                .build().newCall(request).execute();
        String string = response.body().string();

        Log.i("zzzzzzzzzzzzzz",string);
    }

    public Response uploadFile(File file,long chunk,long chunks) throws IOException {
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), getBlock(chunk,file,2048));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("chunks","" + chunks)
                .addFormDataPart("chunk","" + chunk)
                .addFormDataPart("file","file.apk",fileBody)
                .build();
        Request request = new Request.Builder().url("https://192.168.3.124:1111/big/file").post(requestBody).build();

        return new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .addInterceptor(new UploadInterceptor())
                .build().newCall(request).execute();
    }

    private byte[] getBlock(long chunk,File file,int blockSize) {
        byte[] bytes = new byte[blockSize * blockSize];
        RandomAccessFile raf = null;
        try {
            long seek = (chunk) * (blockSize * blockSize);
            raf = new RandomAccessFile(file, "rwd");
            raf.seek(seek);
            int len = raf.read(bytes);
            if (len == -1) {
                return null;
            } else if (len == blockSize) {
                return bytes;
            } else {  // 最后一帧 不足 blockSize 大小
                Log.i("zzzzzzzzzzzzzzz",len + "");
                byte[] tempByte = new byte[len];
                System.arraycopy(bytes,0,tempByte,0,len);
                return tempByte;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
