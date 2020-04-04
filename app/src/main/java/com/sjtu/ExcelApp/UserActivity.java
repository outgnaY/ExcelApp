package com.sjtu.ExcelApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sjtu.ExcelApp.Util.Constants;
import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class UserActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String PREFIX = "[UserActivity]";
    private View userName;
    private View userOffice;
    private View userEmail;
    private View userPhone;
    private View userRole;
    private String name;
    private String office;
    private String email;
    private String phone;
    private String role;
    private TextView nameShow;
    private TextView officeShow;
    private TextView emailShow;
    private TextView phoneShow;
    private TextView roleShow;

    private final int NAME = 1;
    private final int OFFICE = 2;
    private final int EMAIL = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();

    }

    private void setOnClickListener(View view, final int type) {
        final String getAccountUrl = Constants.url + Constants.getAccount;
        SharedPreferences spf = getSharedPreferences("login", MODE_PRIVATE);
        final String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
        Log.e(PREFIX + "sessionId = ", sessionId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpUtil.post(getAccountUrl, new FormBody.Builder().build(), sessionId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UserActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                // mainActivity.finish();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int code = response.code();
                        Log.e(PREFIX + "code = ", String.valueOf(code));
                        if(code == OkHttpUtil.SUCCESS_CODE) {
                            switch(type) {
                                case NAME: {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(UserActivity.this, ModifyNameActivity.class);
                                            intent.putExtra("name", name);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                }
                                case OFFICE: {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(UserActivity.this, ModifyOfficeActivity.class);
                                            intent.putExtra("office", office);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                }
                                case EMAIL: {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(UserActivity.this, ModifyEmailActivity.class);
                                            intent.putExtra("email", email);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                }
                                default: {
                                    Log.e(PREFIX, "Wrong page type");
                                }
                            }
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    // mainActivity.finish();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    private void init() {
        toolbar = findViewById(R.id.user_settings);

        userName = findViewById(R.id.user_name);
        userOffice = findViewById(R.id.user_office);
        userEmail = findViewById(R.id.user_email);
        userPhone = findViewById(R.id.user_phone);
        userRole = findViewById(R.id.user_role);

        nameShow = findViewById(R.id.name_show);
        officeShow = findViewById(R.id.office_show);
        emailShow = findViewById(R.id.email_show);
        phoneShow = findViewById(R.id.phone_show);
        roleShow = findViewById(R.id.role_show);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(PREFIX, "click toolbar");
                finish();//返回
            }
        });
        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        Log.e(PREFIX, "name = " + name);
        office = intent.getStringExtra("office");
        Log.e(PREFIX, "office = " + office);
        email = intent.getStringExtra("email");
        Log.e(PREFIX, "email = " + email);
        phone = intent.getStringExtra("phone");
        Log.e(PREFIX, "phone = " + phone);
        role = intent.getStringExtra("role");
        Log.e(PREFIX, "role = " + role);
        nameShow.setText(name);
        officeShow.setText(office);
        emailShow.setText(email);
        phoneShow.setText(phone);
        roleShow.setText(role);

        setOnClickListener(userName, NAME);
        setOnClickListener(userOffice, OFFICE);
        setOnClickListener(userEmail, EMAIL);
    }
}
