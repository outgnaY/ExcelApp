package com.sjtu.ExcelApp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sjtu.ExcelApp.R;
import com.sjtu.ExcelApp.Util.Constants;
import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class ModifyEmailActivity extends AppCompatActivity {
    private String PREFIX = "[ModifyEmailActivity]";
    private EditText emailEdit;
    private String email;
    private Toolbar toolbar;
    private TextView accountTextView;
    private SharedPreferences spf;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyemail);
        init();
    }
    private void init() {
        emailEdit = findViewById(R.id.email_edit);
        btn = findViewById(R.id.mod_email_btn);
        spf = super.getSharedPreferences("login", MODE_PRIVATE);
        String account = SharedPreferenceUtil.getString(spf, "user", "");
        accountTextView = findViewById(R.id.account_email);
        accountTextView.setText(account);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        /*
        toolbar = findViewById(R.id.user_modify_email);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(PREFIX, "click toolbar");
                finish();//返回
            }
        });
        */
        emailEdit.setText(email);
        emailEdit.setSelection(email.length());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setAccountUrl = Constants.url + Constants.setAccount;
                String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
                String user = SharedPreferenceUtil.getString(spf, "user", "");
                Log.e(PREFIX, "user = " + user);

                String pwd = SharedPreferenceUtil.getString(spf, "pwd", "");
                Log.e(PREFIX, "pwd = " + pwd);
                final String newEmail = emailEdit.getText().toString();
                Log.e(PREFIX, "requestUrl = " + setAccountUrl);
                FormBody.Builder builder = new FormBody.Builder();
                // TODO setName api
                if(user.contains("@")) {
                    // email
                    builder.add("email", user);
                    builder.add("passwd", pwd);
                }
                else {
                    // phone
                    builder.add("phone", user);
                    builder.add("passwd", pwd);
                }
                builder.add("new_email", newEmail);
                OkHttpUtil.post(setAccountUrl, builder.build(), sessionId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ModifyEmailActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ModifyEmailActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                // finish();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int code = response.code();
                        Log.e(PREFIX, "code = " + String.valueOf(code));
                        if(code == OkHttpUtil.SUCCESS_CODE) {
                            String responseText = response.body().string();
                            // Log.e(PREFIX, "103 " + responseText);
                            JSONObject json = JSONObject.parseObject(responseText);
                            // Log.e(PREFIX, "json = " + json);
                            int retCode = json.getIntValue("Code");
                            Log.e(PREFIX, "retCode = " + retCode);
                            if(retCode == 0) {
                                // SharedPreferenceUtil.putString(spf, "sessionId", "");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ModifyEmailActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.putExtra("email", newEmail);
                                        setResult(Constants.MOD_OK, intent);
                                        SharedPreferenceUtil.putString(spf, "user", newEmail);
                                        // SharedPreferenceUtil.putString(spf, "email", newEmail);
                                        finish();
                                    }
                                });
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ModifyEmailActivity.this, "未知错误，请重新登录", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ModifyEmailActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });
                            }
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ModifyEmailActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                        }
                        response.close();
                    }
                });
            }
        });
    }
}
