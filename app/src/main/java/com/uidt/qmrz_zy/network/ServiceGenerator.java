package com.uidt.qmrz_zy.network;

import com.uidt.qmrz_zy.BuildConfig;
import com.uidt.qmrz_zy.app.AppAplication;
import com.uidt.qmrz_zy.utils.ConstantsUtils;
import com.uidt.qmrz_zy.utils.PreferenceUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/12/12.
 *
 * @author yijixin
 */

public class ServiceGenerator {

    private final static OkHttpClient.Builder httpClient = getHttpClient();

    //优惠券
    private final static Retrofit.Builder builderCoupon = new Retrofit.Builder()
            .baseUrl(ConstantsUtils.IP_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createServiceCoupon(Class<S> serviceClass) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        OkHttpClient client = httpClient
                .build();
        Retrofit retrofit = builderCoupon.client(client).build();
        return retrofit.create(serviceClass);
    }

    private static Interceptor interceptor = null;

    public static OkHttpClient.Builder getHttpClient() {
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = client.newBuilder();

        //添加cookie验证
        builder.cookieJar(new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                PreferenceUtil.putString(AppAplication.getAppContext(), ConstantsUtils.SESSION_ID, list.get(0).value());
                cookieStore.put(httpUrl.host(), list);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                List<Cookie> cookies = cookieStore.get(httpUrl.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });
        //设置debug
        if (BuildConfig.DEBUG) {
            //log信息拦截
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor);
        }
        File file = new File(AppAplication.getAppContext().getExternalCacheDir(), "CacheFile");
        //设置缓存 50M缓存
        Cache cache = new Cache(file, 1024 * 1024 * 50);

        try {
            SSLContext sslContext = null;
            SSLSocketFactoryImp ssl = null;
            HostnameVerifier hostnameVerifier = null;
            if (!ConstantsUtils.IP_URL.substring(0, 5).equals("https")) {

            } else if (!ConstantsUtils.IP_URL.equals("https://guas.kingflying.cn:8442")) {
                //本地测试用
                InputStream in = AppAplication.getAppContext().getAssets().open("tomcat.cer");
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                Certificate cer = certificateFactory.generateCertificate(in);
                System.out.println("ca=" + ((X509Certificate) cer).getSubjectDN());

                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                keystore.load(null);
                keystore.setCertificateEntry("ca", cer);

                String algorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(algorithm);
                trustManagerFactory.init(keystore);

                sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, getTrustManager(), new SecureRandom());
            } else {

                //正式环境用
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                ssl = new SSLSocketFactoryImp(KeyStore.getInstance(KeyStore.getDefaultType()));

                hostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        boolean verify = false;
                        if (hostname.equals(ConstantsUtils.BASE_URL)) {
                            verify = HttpsURLConnection.getDefaultHostnameVerifier().verify(ConstantsUtils.BASE_URL, session);
                        }

                        return verify;
                    }
                };
            }

            builder.cache(cache)
                    //连接时间
                    .connectTimeout(5, TimeUnit.SECONDS)
                    //读取时间
                    .readTimeout(10, TimeUnit.SECONDS)
                    //超时
                    .writeTimeout(10, TimeUnit.SECONDS)
                    //设置错误重连
                    .retryOnConnectionFailure(true);
            if (!ConstantsUtils.IP_URL.substring(0, 5).equals("https")) {
                builder.build();
            } else if (!ConstantsUtils.IP_URL.equals("https://guas.kingflying.cn:8442")) {
                //本地测试用
                builder.sslSocketFactory(sslContext.getSocketFactory())
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        })
                        .build();
            } else {
                //正式环境用
                builder.sslSocketFactory(ssl.getSSLContext().getSocketFactory(), ssl.getTrustManager())
                        .hostnameVerifier(hostnameVerifier).build();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder;
    }

    private static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
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
        }};
        return trustAllCerts;
    }

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

    public static class SSLSocketFactoryImp extends SSLSocketFactory {
        private SSLContext sslContext = SSLContext.getInstance("TLS");
        private TrustManager trustManager = null;


        public SSLContext getSSLContext() {
            return sslContext;
        }

        public X509TrustManager getTrustManager() {
            return (X509TrustManager) trustManager;
        }

        public SSLSocketFactoryImp(KeyStore keyStore) throws NoSuchAlgorithmException, KeyManagementException {
            trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] x509Certificates = new X509Certificate[0];
                    return x509Certificates;
                }
            };

            sslContext.init(null, new TrustManager[]{trustManager}, null);
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return new String[0];
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return new String[0];
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }

        @Override
        public Socket createSocket(Socket socket, String host, int post, boolean autoClose) throws IOException {
            return sslContext.getSocketFactory().createSocket(socket, host, post, autoClose);
        }

        @Override
        public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
            return null;
        }

        @Override
        public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException, UnknownHostException {
            return null;
        }

        @Override
        public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
            return null;
        }

        @Override
        public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
            return null;
        }
    }

}
