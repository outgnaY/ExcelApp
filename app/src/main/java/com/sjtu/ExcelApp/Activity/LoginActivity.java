package com.sjtu.ExcelApp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.sjtu.ExcelApp.Customize.FontIconView;
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

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private CheckBox rememberPass;
    private String user;
    private String pwd;
    private SharedPreferences spf;
    private String PREFIX = "[LoginActivity]";
    private FontIconView passwordV;
    private boolean visible;
    final String regexDX = "^1(33|49|53|73|74|77|80|81|89|99)\\d{8}$";
    final String regexLT = "^1(30|31|32|45|46|55|56|66|71|75|76|85|86)\\d{8}";
    final String regexYD = "^1(34|35|36|37|38|39|47|48|50|51|52|57|58|59|72|78|82|83|84|87|88|98)\\d{8}";
    final String regexEmail = "^[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {


        spf = super.getSharedPreferences("login", MODE_PRIVATE);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        rememberPass = findViewById(R.id.remember_pass);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        login = findViewById(R.id.login);
        passwordV = findViewById(R.id.password_visible);
        visible = false;

        passwordV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(PREFIX, "click passwordv");
                if(visible) {
                    passwordV.setText(R.string.mimabukejian);
                    visible = false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.getText().length());
                }
                else {
                    passwordV.setText(R.string.mimakejian);
                    visible = true;
                    // password.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password.setSelection(password.getText().length());
                }

            }
        });

        boolean isRemember = SharedPreferenceUtil.getBoolean(spf, "isRemember", false);
        if(isRemember) {
            String user = SharedPreferenceUtil.getString(spf, "lastLogin", "");
            String pwd = SharedPreferenceUtil.getString(spf, "pwd", "");
            username.setText(user);
            password.setText(pwd);
        }
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(PREFIX, "click username");
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(PREFIX, "click password");
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pwd = password.getText().toString();
                if(TextUtils.isEmpty(user)) {
                    new AlertDialog.Builder(LoginActivity.this).setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定触发的事件
                                }
                            }).setMessage("账号不能为空").show();
                    return;
                }
                if(TextUtils.isEmpty(pwd)) {
                    new AlertDialog.Builder(LoginActivity.this).setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定触发的事件
                                }
                            }).setMessage("密码不能为空").show();
                    return;
                }
                Log.e(PREFIX, "user = " + user);
                Log.e(PREFIX, "pwd = " + pwd);
                if(Pattern.matches(regexLT, user) || Pattern.matches(regexYD, user) || Pattern.matches(regexDX, user) || Pattern.matches(regexEmail, user)) {
                    Log.e(PREFIX, "matched");
                }
                else {
                    new AlertDialog.Builder(LoginActivity.this).setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定触发的事件
                                }
                            }).setMessage("输入的账号不符合格式，请输入正确的手机号或邮箱").show();
                    Log.e(PREFIX, "not matched");
                    return;
                }
                if(rememberPass.isChecked()) {
                    SharedPreferenceUtil.putBoolean(spf, "isRemember", true);
                }
                else {
                    SharedPreferenceUtil.putBoolean(spf, "isRemember", false);
                }
                SharedPreferenceUtil.putString(spf, "user", user);
                SharedPreferenceUtil.putString(spf, "pwd", pwd);
                SharedPreferenceUtil.putString(spf, "lastLogin", user);
                String requestUrl = Constants.url + Constants.getAccount;
                Log.e(PREFIX, "requestUrl = " + requestUrl);
                OkHttpUtil.post(requestUrl, user, pwd, new FormBody.Builder().build(), new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String setCookie = response.header("Set-Cookie");

                        String sessionId = OkHttpUtil.getSessionId(setCookie);
                        Log.e(PREFIX, "sessionId = " + sessionId);
                        SharedPreferenceUtil.putString(spf, "sessionId", sessionId);
                        int code = response.code();
                        String responseText = response.body().string();
                        if(code == OkHttpUtil.SUCCESS_CODE) {
                            if(responseText.equals("null")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(LoginActivity.this).setTitle("提示")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //点击确定触发的事件
                                                    }
                                                }).setMessage("您的账号权限类型为 Admin ，不能用于登录 APP ，请联系管理员修改权限").show();
                                    }
                                });
                            }
                            else {
                                JSONObject json = JSONObject.parseObject(responseText);
                                int retCode = json.getIntValue("Code");
                                if(retCode == 0) {
                                    JSONObject objT = json.getJSONObject("ObjT");
                                    Log.e(PREFIX, objT.getString("Role"));
                                    SharedPreferenceUtil.putString(spf, "auth", objT.getString("Role"));
                                    SharedPreferenceUtil.putString(spf, "email", objT.getString("Email"));
                                    SharedPreferenceUtil.putString(spf, "phone", objT.getString("Phone"));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                                else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertDialog.Builder(LoginActivity.this).setTitle("提示")
                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //点击确定触发的事件
                                                        }
                                                    }).setMessage("服务器出错").show();
                                        }
                                    });
                                }
                            }

                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(LoginActivity.this).setTitle("提示")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //点击确定触发的事件
                                                }
                                            }).setMessage("用户名或密码错误").show();
                                }
                            });
                        }
                        response.close();
                    }
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(LoginActivity.this).setTitle("提示")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //点击确定触发的事件
                                            }
                                        }).setMessage("网络错误，无法连接到服务器").show();
                            }
                        });
                    }
                });
            }
        });

    }
}
