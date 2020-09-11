package com.xz.mmapfile;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 *  信任所有证书
 */
public class SSLSocketClient {
 
    //获取这个SSLSocketFactory
    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
 
    //获取TrustManager
    public static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }
 
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }
 
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
        return trustAllCerts;
    }

    public static SSLSocketFactory sslSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static X509TrustManager x509TrustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
            // 可以再此处校验 本地证书 是否 受信任
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    };

    //获取HostnameVerifier
    public static HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        return hostnameVerifier;
    }

    /**
     * TrustManagerFactory trustManagerFactory = null;
     *                 trustManagerFactory = TrustManagerFactory.getInstance(
     *                         TrustManagerFactory.getDefaultAlgorithm());
     *                 trustManagerFactory.init((KeyStore) null);
     *                 TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
     *                 if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
     *                     throw new IllegalStateException("Unexpected default trust managers:"
     *                             + Arrays.toString(trustManagers));
     *                 }
     *                 X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
     *                 SSLContext sslContext = SSLContext.getInstance("TLS");
     *                 sslContext.init(null, new TrustManager[]{trustManager}, null);
     *                 SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
     */
}