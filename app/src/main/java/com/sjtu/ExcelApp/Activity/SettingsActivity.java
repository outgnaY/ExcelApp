package com.sjtu.ExcelApp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sjtu.ExcelApp.R;
import com.sjtu.ExcelApp.Util.Constants;
import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String PREFIX = "[SettingsActivity]";
    private TextView logout;
    private SharedPreferences spf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }
    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        logout = (TextView) findViewById(R.id.logout);
        spf = super.getSharedPreferences("login", MODE_PRIVATE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(PREFIX, "click toolbar");
                finish();//返回
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(PREFIX, "click logout");
                String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
                String requestUrl = Constants.url + Constants.logout;
                Log.e(PREFIX, "requestUrl = " + requestUrl);
                OkHttpUtil.post(requestUrl, new FormBody.Builder().build(), sessionId, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        int code = response.code();
                        Log.e(PREFIX, "code = " + String.valueOf(code));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                // delete sessionId from local storage
                                SharedPreferenceUtil.putString(spf, "sessionId", "");
                                startActivity(intent);
                                // finish();
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SettingsActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                // finish();
                            }
                        });
                    }
                });
            }
        });
    }
}
