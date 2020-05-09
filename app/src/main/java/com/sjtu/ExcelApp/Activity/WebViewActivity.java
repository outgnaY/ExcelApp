package com.sjtu.ExcelApp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.sjtu.ExcelApp.Customize.CustomToolbar;
import com.sjtu.ExcelApp.R;
import com.sjtu.ExcelApp.Util.Constants;
import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WebViewActivity extends AppCompatActivity {
    private String PREFIX = "[WebViewActivity]";
    private WebView webView;
    private CustomToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        init();
    }
    public interface OnDownloadListener {
        void onDownloadSuccess(String path);
        void onDownloading(int progress);
        void onDownloadFailed(String msg);
    }
    private void init() {
        Log.e(PREFIX, "init");
        toolbar = findViewById(R.id.webview_toolbar);
        toolbar.setTitle("2020年度科学基金资助计划\n（国科金发【2020】X号）");
        webView = findViewById(R.id.webview_pdf);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);
        download(getCacheDir() + "/temp.pdf", new OnDownloadListener() {
            @Override
            public void onDownloadSuccess(String path) {
                Log.e(PREFIX, "onDownloadSuccess: " + path);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        preView(getCacheDir() + "/temp.pdf");
                    }
                });
            }
            @Override
            public void onDownloading(int progress) {
                Log.e(PREFIX, "onDownloadSuccess: " + progress);
            }

            @Override
            public void onDownloadFailed(String msg) {
                Log.e(PREFIX, "onDownloadSuccess: " + msg);
                Toast.makeText(WebViewActivity.this, "获取文件出错", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    private void preView(String pdfUrl) {
        webView.loadUrl("file:///android_asset/index.html?" + pdfUrl);
    }
    public void download(final String saveFile, final OnDownloadListener listener) {
        SharedPreferences spf = getSharedPreferences("login", Context.MODE_PRIVATE);
        String pdfUrl = Constants.url + Constants.getPdf;
        Log.e(PREFIX, "pdfUrl = " + pdfUrl);
        String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");

        OkHttpUtil.get(pdfUrl, sessionId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WebViewActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(WebViewActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    // long total = response.body().contentLength();
                    // Log.e(PREFIX, "total length = " + total);
                    File file = new File(saveFile);
                    fos = new FileOutputStream(file);
                    // long sum = 0;
                    while((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        // sum += len;
                    }
                    fos.flush();
                    listener.onDownloadSuccess(file.getAbsolutePath());
                } catch(Exception e) {
                    listener.onDownloadFailed(e.getMessage());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
