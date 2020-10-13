package com.xz.mmapfile;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.mmapfile  AspectTest
 * @Des ProgressResponseBody
 * @DATE 2020/10/12  16:21 星期一
 */
public class ProgressResponseBody extends ResponseBody {

    private BufferedSource source;

    private ResponseBody responseBody;

    private ProgressInterceptor.ProgressListener listener;

    public ProgressResponseBody(ProgressInterceptor.ProgressListener listener, ResponseBody body) {
        this.responseBody = body;
        // 获取 listener
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (source == null) {
            source = Okio.buffer(new ProgressSource(responseBody.source()));
        }
        return source;
    }

    private class ProgressSource extends ForwardingSource {

        long totalBytesRead = 0;

        int currentProgress;

        public ProgressSource(Source delegate) {
            super(delegate);
        }

        @Override
        public long read(Buffer sink, long byteCount) throws IOException {
            long bytesRead = super.read(sink, byteCount);
            long fullLength = responseBody.contentLength();
            if (bytesRead == -1) {
                totalBytesRead = fullLength;
            } else {
                totalBytesRead += bytesRead;
            }
            int progress = (int) (100f * totalBytesRead / fullLength);
            if (listener != null && currentProgress != progress) {
                listener.onProgress(progress);
            }
            if (listener != null && totalBytesRead == fullLength) {
                listener = null;
            }
            currentProgress = progress;
            return bytesRead;
        }
    }
}
