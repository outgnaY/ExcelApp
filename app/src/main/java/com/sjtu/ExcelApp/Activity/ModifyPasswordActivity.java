package com.sjtu.ExcelApp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private Button btn;
    private TextView accountTextView;
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
        btn = findViewById(R.id.mod_pwd_btn);
        spf = super.getSharedPreferences("login", MODE_PRIVATE);
        String account = SharedPreferenceUtil.getString(spf, "user", "");
        accountTextView = findViewById(R.id.account_password);
        accountTextView.setText(account);
        oldPassword = findViewById(R.id.old_pwd_edit);
        newPassword = findViewById(R.id.new_pwd_edit);
        newPasswordConfirm = findViewById(R.id.new_pwd_confirm_edit);
        oldPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPasswordConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setAccountUrl = Constants.url + Constants.setAccount;
                String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
                String user = SharedPreferenceUtil.getString(spf, "user", "");
                // String pwd = SharedPreferenceUtil.getString(spf, "pwd", "");
                String newPwd = newPassword.getText().toString();
                String oldPwd = oldPassword.getText().toString();
                String newPwdConfirm = newPasswordConfirm.getText().toString();
                if(TextUtils.isEmpty(oldPwd)) {
                    new AlertDialog.Builder(ModifyPasswordActivity.this).setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定触发的事件
                                }
                            }).setMessage("旧密码不能为空").show();
                    return;
                }
                if(newPwd.length() < 6) {
                    new AlertDialog.Builder(ModifyPasswordActivity.this).setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定触发的事件
                                }
                            }).setMessage("密码长度至少为六位").show();
                    return;
                }
                if(!newPwd.equals(newPwdConfirm)) {
                    new AlertDialog.Builder(ModifyPasswordActivity.this).setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定触发的事件
                                }
                            }).setMessage("两次输入的新密码不一致，请检查后重试").show();
                    return;
                }
                Log.e(PREFIX, "requestUrl = " + setAccountUrl);
                FormBody.Builder builder = new FormBody.Builder();
                // TODO setName api
                if(user.contains("@")) {
                    // email
                    builder.add("email", user);
                    builder.add("passwd", oldPwd);
                }
                else {
                    // phone
                    builder.add("phone", user);
                    builder.add("passwd", oldPwd);
                }
                builder.add("new_passwd", newPwd);
                OkHttpUtil.post(setAccountUrl, builder.build(), sessionId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(ModifyPasswordActivity.this).setTitle("提示")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //点击确定触发的事件
                                                Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                // finish();
                                            }
                                        }).setMessage("修改失败，网络错误，无法连接到服务器").show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int code = response.code();
                        Log.e(PREFIX, "code = " + String.valueOf(code));
                        String responseText = response.body().string();
                        Log.e(PREFIX, responseText);
                        if(code == OkHttpUtil.SUCCESS_CODE) {
                            JSONObject json = JSONObject.parseObject(responseText);
                            int retCode = json.getIntValue("Code");
                            Log.e(PREFIX, "retCode = " + retCode);
                            if(retCode == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(ModifyPasswordActivity.this).setTitle("提示")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //点击确定触发的事件
                                                        finish();
                                                    }
                                                }).setMessage("修改成功").show();
                                    }
                                });
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(ModifyPasswordActivity.this).setTitle("提示")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //点击确定触发的事件
                                                        // Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
                                                        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        // startActivity(intent);
                                                    }
                                                }).setMessage("修改失败，密码错误").show();
                                    }
                                });
                            }
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(ModifyPasswordActivity.this).setTitle("提示")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //点击确定触发的事件
                                                    Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            }).setMessage("修改失败，登录过期，请重新登录").show();

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
