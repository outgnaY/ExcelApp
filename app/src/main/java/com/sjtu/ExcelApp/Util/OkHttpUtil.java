package com.sjtu.ExcelApp.Util;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtil {
    public static final int SUCCESS_CODE = 200;
    public static String PREFIX = "OkHttpUtil";
    // 单例
    private static volatile OkHttpClient client = null;
    private static OkHttpClient getHttpClient() {
        if(client == null) {
            synchronized (OkHttpUtil.class) {
                if(client == null) {
                    client = getUnsafeOkHttpClient();
                }
            }
        }
        return client;
    }
    private static int getFromIndex(String str, String modelStr, Integer count) {
        //对子字符串进行匹配
        Matcher slashMatcher = Pattern.compile(modelStr).matcher(str);
        int index = 0;
        //matcher.find();尝试查找与该模式匹配的输入序列的下一个子序列
        while(slashMatcher.find()) {
            index++;
            //当modelStr字符第count次出现的位置
            if(index == count){
                break;
            }
        }
        //matcher.start();返回以前匹配的初始索引。
        return slashMatcher.start();
    }
    public static String getSessionId(String input)
    {
        int s = getFromIndex(input, "__session=", 1);
        String str= input.substring(s);
        int e = getFromIndex(str, ";",1);
        String str2 = str.substring(0, e);
        String[] strs = str2.split("=");
        return strs[1];
    }
    public static void get(String url, Callback callback) {
        OkHttpClient client = getHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
    // send post with sessionId
    public static void post(String url, RequestBody requestBody, String sessionId, Callback callback) {
        OkHttpClient client = getHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded").addHeader("Accept", "text/plain;charset=utf-8").addHeader("Cookie", "__session="+sessionId).method("POST", requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    // send post with basic auth
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void post(String url, String user, String pwd, RequestBody requestBody, Callback callback) {
        OkHttpClient client = getHttpClient();
        Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded").addHeader("Accept", "text/plain;charset=utf-8").addHeader("Authorization", getAuthHeader(user, pwd)).method("POST", requestBody).build();
        client.newCall(request).enqueue(callback);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static String getAuthHeader(String user, String pwd) {
        // String auth = "13916629822:123456";
        String auth = user + ":" + pwd;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        Log.e(PREFIX + "authHeader = ", authHeader);
        return authHeader;
    }
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
