package com.sjtu.ExcelApp;

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
import android.widget.EditText;
import android.widget.Toast;

import com.sjtu.ExcelApp.Customize.FontIconView;
import com.sjtu.ExcelApp.Util.Constants;
import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;

import java.io.IOException;
import java.util.Properties;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private String user;
    private String pwd;
    private SharedPreferences spf;
    private String PREFIX = "[LoginActivity]";
    private FontIconView passwordV;
    private boolean visible;

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
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        login = findViewById(R.id.login);
        // View line1 = findViewById(R.id.line1);
        // View line2 = findViewById(R.id.line2);
        passwordV = findViewById(R.id.password_visible);
        visible = false;
        passwordV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(PREFIX, "click passwordv");
                if(visible) {
                    passwordV.setText(R.string.mimabukejian);
                    visible = false;
                    // password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
                Log.e(PREFIX + "user = ", user);
                Log.e(PREFIX + "pwd = ", pwd);
                
                SharedPreferenceUtil.putString(spf, "user", user);
                SharedPreferenceUtil.putString(spf, "pwd", pwd);
                String requestUrl = Constants.url + Constants.getAccount;
                Log.e(PREFIX + "requestUrl = ", requestUrl);
                OkHttpUtil.post(requestUrl, user, pwd, new FormBody.Builder().build(), new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String setCookie = response.header("Set-Cookie");

                        String sessionId = OkHttpUtil.getSessionId(setCookie);
                        Log.e(PREFIX + "sessionId = ", sessionId);
                        SharedPreferenceUtil.putString(spf, "sessionId", sessionId);
                        int code = response.code();
                        if(code == OkHttpUtil.SUCCESS_CODE) {
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
                                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
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
