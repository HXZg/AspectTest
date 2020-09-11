package com.xz.mmapfile;

import android.util.Log;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class MyX509TrustManager implements X509TrustManager {

    private InputStream is;

    public MyX509TrustManager(InputStream is) {
        this.is = is;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (chain == null) {
            throw new CertificateException("checkServerTrusted: X509Certificate array is null");
        }
        if (chain.length < 1) {
            throw new CertificateException("checkServerTrusted: X50+Certificate is empty");
        }
        if (!(authType != null && authType.equals("ECDHE_RSA"))) {
            throw new CertificateException("checkServerTrusted: AuthType is not ECDHE_RSA");
        }

        // 检查所有证书  hxz证书 不安全，无法检查所有证书
        /*try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance("X509");
            factory.init((KeyStore) null);
            for (TrustManager trustManager : factory.getTrustManagers()) {
                Log.i("zzzzzzzzzzzzz",trustManager.getClass().getName());
                ((X509TrustManager)trustManager).checkServerTrusted(chain,authType);
            }
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }*/

        // 获取本地证书中的信息
        String clientEncoded = "";
        String clientSubject = "";
        String clientIssuser = "";
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate clientCertificate = (X509Certificate) certificateFactory.generateCertificate(is);
        clientEncoded = new BigInteger(1,clientCertificate.getPublicKey().getEncoded()).toString(16);
        clientSubject = clientCertificate.getSubjectDN().getName();
        clientIssuser = clientCertificate.getIssuerDN().getName();

        // 获取网络中的证书信息
        X509Certificate certificate = chain[0];
        PublicKey publicKey = certificate.getPublicKey();
        String serverEncoded = new BigInteger(1,publicKey.getEncoded()).toString(16);
        // 如果 与 服务器证书不一样的话 抛出异常 校验不通过
        if (!clientEncoded.equals(serverEncoded)) {
            throw new CertificateException("server publickey is not equals to client publickey");
        }
        String subject = certificate.getSubjectDN().getName();
        if (!clientSubject.equals(subject)) {
            throw new CertificateException("server's subject is not equals to client's subject");
        }
        String issuser = certificate.getIssuerDN().getName();
        if (!clientIssuser.equals(issuser)) {
            throw new CertificateException("server's issuser is not equals to client's issuser");
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
}
