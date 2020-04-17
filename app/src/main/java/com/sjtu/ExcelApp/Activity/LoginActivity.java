package com.sjtu.ExcelApp.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sjtu.ExcelApp.Customize.FontIconView;
import com.sjtu.ExcelApp.R;
import com.sjtu.ExcelApp.Util.Constants;
import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;

import java.io.IOException;

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
    // private boolean usernameFocus = false;
    // private boolean passwordFocus = false;

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
                /*
                usernameFocus = !usernameFocus;
                if(usernameFocus) {
                    KeyboardUtil.showKeyboard(username);
                }
                else {
                    KeyboardUtil.hideKeyboard(username);
                }
                */
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(PREFIX, "click password");
                /*
                passwordFocus = !passwordFocus;
                if(passwordFocus) {
                    KeyboardUtil.showKeyboard(password);
                }
                else {
                    KeyboardUtil.hideKeyboard(password);
                }
                */
                // InputMethodManager imm = (InputMethodManager) password.getContext().getSystemService(password.getContext().INPUT_METHOD_SERVICE);
                // Log.e(PREFIX, String.valueOf(imm.isActive(password)));

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pwd = password.getText().toString();
                Log.e(PREFIX, "user = " + user);
                Log.e(PREFIX, "pwd = " + pwd);
                if(rememberPass.isChecked()) {
                    SharedPreferenceUtil.putBoolean(spf, "isRemember", true);
                }
                else {
                    SharedPreferenceUtil.putBoolean(spf, "isRemember", false);
                }
                SharedPreferenceUtil.putString(spf, "user", user);
                SharedPreferenceUtil.putString(spf, "pwd", pwd);
                SharedPreferenceUtil.putString(spf, "lastLogin", user);
                /*
                if(user.contains("@")) {
                    SharedPreferenceUtil.putString(spf, "email", user);
                }
                else {
                    SharedPreferenceUtil.putString(spf, "phone", user);
                }
                */
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
                            JSONObject json = JSONObject.parseObject(responseText);
                            int retCode = json.getIntValue("Code");
                            if(retCode == 0) {
                                JSONObject objT = json.getJSONObject("ObjT");
                                Log.e(PREFIX, objT.getString("Role"));
                                SharedPreferenceUtil.putString(spf, "auth", objT.getString("Role"));
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
                                        Toast.makeText(LoginActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(LoginActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

    }
}
