package com.sjtu.ExcelApp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ModifyPasswordActivity extends AppCompatActivity {
    private String PREFIX = "[ModifyPasswordActivity]";
    private Toolbar toolbar;
    private Button btn;
    private EditText oldPassword;
    private EditText newPassword;
    private EditText newPasswordConfirm;
    private SharedPreferences spf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypwd);
        init();
    }
    private void init() {
        toolbar = findViewById(R.id.user_modify_pwd);
        btn = findViewById(R.id.mod_pwd_btn);
        spf = super.getSharedPreferences("login", MODE_PRIVATE);
        oldPassword = findViewById(R.id.old_pwd_edit);
        newPassword = findViewById(R.id.new_pwd_edit);
        newPasswordConfirm = findViewById(R.id.new_pwd_confirm__edit);
        oldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPasswordConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(PREFIX, "click toolbar");
                finish();//返回
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setAccountUrl = Constants.url + Constants.setAccount;
                String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
                String user = SharedPreferenceUtil.getString(spf, "user", "");
                String pwd = SharedPreferenceUtil.getString(spf, "pwd", "");
                String newPwd = newPassword.getText().toString();
                String oldPwd = oldPassword.getText().toString();
                String newPwdConfirm = newPasswordConfirm.getText().toString();
                if(!oldPwd.equals(pwd)) {
                    Toast.makeText(ModifyPasswordActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newPwd.length() == 0) {
                    Toast.makeText(ModifyPasswordActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newPwd.equals(newPwdConfirm)) {
                    Toast.makeText(ModifyPasswordActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                builder.add("new_passwd", newPwd);
                OkHttpUtil.post(setAccountUrl, builder.build(), sessionId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ModifyPasswordActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
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
                            JSONObject json = JSONObject.parseObject(responseText);
                            int retCode = json.getIntValue("Code");
                            Log.e(PREFIX, "retCode = " + retCode);
                            if(retCode == 0) {
                                // SharedPreferenceUtil.putString(spf, "sessionId", "");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                });
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ModifyPasswordActivity.this, "未知错误，请重新登录", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
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
                                    Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
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
