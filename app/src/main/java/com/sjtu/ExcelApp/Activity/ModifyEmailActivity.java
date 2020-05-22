package com.sjtu.ExcelApp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class ModifyEmailActivity extends BaseActivity {
    private String PREFIX = "[ModifyEmailActivity]";
    private EditText emailEdit;
    private String email;
    private EditText pwdEdit;
    private TextView accountTextView;
    private SharedPreferences spf;
    private Button btn;
    final String regexEmail = "^[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyemail);
        init();
    }
    private void init() {
        emailEdit = findViewById(R.id.email_edit);
        pwdEdit = findViewById(R.id.email_pwd_edit);
        pwdEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btn = findViewById(R.id.mod_email_btn);
        spf = super.getSharedPreferences("login", MODE_PRIVATE);
        String account = SharedPreferenceUtil.getString(spf, "user", "");
        accountTextView = findViewById(R.id.account_email);
        accountTextView.setText(account);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        emailEdit.setText(email);
        emailEdit.setSelection(email.length());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setAccountUrl = Constants.url + Constants.setAccount;
                String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
                String user = SharedPreferenceUtil.getString(spf, "user", "");
                Log.e(PREFIX, "user = " + user);

                // String pwd = SharedPreferenceUtil.getString(spf, "pwd", "");
                String pwd = pwdEdit.getText().toString();
                Log.e(PREFIX, "pwd = " + pwd);
                final String newEmail = emailEdit.getText().toString();
                Log.e(PREFIX, "requestUrl = " + setAccountUrl);
                FormBody.Builder builder = new FormBody.Builder();
                if(TextUtils.isEmpty(pwd)) {
                    new AlertDialog.Builder(ModifyEmailActivity.this).setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定触发的事件
                                }
                            }).setMessage("密码不能为空").show();
                    return;
                }
                if(TextUtils.isEmpty(newEmail)) {
                    new AlertDialog.Builder(ModifyEmailActivity.this).setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定触发的事件
                                }
                            }).setMessage("新邮箱不能为空").show();
                    return;
                }
                if(Pattern.matches(regexEmail, newEmail)) {
                    Log.e(PREFIX, "matched");
                }
                else {
                    new android.app.AlertDialog.Builder(ModifyEmailActivity.this).setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定触发的事件
                                }
                            }).setMessage("新邮箱不符合格式").show();
                    Log.e(PREFIX, "not matched");
                    return;
                }
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
                                new AlertDialog.Builder(ModifyEmailActivity.this).setTitle("提示")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //点击确定触发的事件
                                                Intent intent = new Intent(ModifyEmailActivity.this, LoginActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }).setMessage("网络错误，无法连接到服务器").show();
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
                            Log.e(PREFIX, "responseText = " + responseText);
                            if(retCode == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(ModifyEmailActivity.this).setTitle("提示")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //点击确定触发的事件
                                                        Intent intent = new Intent();
                                                        intent.putExtra("email", newEmail);
                                                        setResult(Constants.MOD_OK, intent);
                                                        SharedPreferenceUtil.putString(spf, "user", newEmail);
                                                        finish();
                                                    }
                                                }).setMessage("绑定邮箱修改成功").show();
                                    }
                                });
                            }
                            else {
                                String objT = json.getString("ObjT");
                                if(objT.equals("Cannot match a user.")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertDialog.Builder(ModifyEmailActivity.this).setTitle("提示")
                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //点击确定触发的事件
                                                        }
                                                    }).setMessage("修改失败，密码错误").show();
                                        }
                                    });
                                }
                                else if(objT.equals("duplicated phone!")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertDialog.Builder(ModifyEmailActivity.this).setTitle("提示")
                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //点击确定触发的事件
                                                        }
                                                    }).setMessage("修改失败，新邮箱已经被绑定").show();
                                        }
                                    });
                                }
                                else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertDialog.Builder(ModifyEmailActivity.this).setTitle("提示")
                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //点击确定触发的事件
                                                            Intent intent = new Intent(ModifyEmailActivity.this, LoginActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                        }
                                                    }).setMessage("修改失败，发生了未知错误，请联系管理员").show();
                                        }
                                    });
                                }
                            }
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(ModifyEmailActivity.this).setTitle("提示")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //点击确定触发的事件
                                                    Intent intent = new Intent(ModifyEmailActivity.this, LoginActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            }).setMessage("登录过期，请重新登录").show();
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
