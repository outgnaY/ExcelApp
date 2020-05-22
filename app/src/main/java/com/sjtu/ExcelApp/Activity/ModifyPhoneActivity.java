package com.sjtu.ExcelApp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class ModifyPhoneActivity extends BaseActivity {
    private String PREFIX = "[ModifyPhoneActivity]";
    private EditText phoneEdit;
    private String phone;
    private EditText pwdEdit;
    private TextView accountTextView;
    private SharedPreferences spf;
    private Button btn;
    final String regexDX = "^1(33|49|53|73|74|77|80|81|89|99)\\d{8}$";
    final String regexLT = "^1(30|31|32|45|46|55|56|66|71|75|76|85|86)\\d{8}";
    final String regexYD = "^1(34|35|36|37|38|39|47|48|50|51|52|57|58|59|72|78|82|83|84|87|88|98)\\d{8}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyphone);
        init();
    }
    private void init() {
        phoneEdit = findViewById(R.id.phone_edit);
        pwdEdit = findViewById(R.id.phone_pwd_edit);
        pwdEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btn = findViewById(R.id.mod_phone_btn);
        spf = super.getSharedPreferences("login", MODE_PRIVATE);
        String account = SharedPreferenceUtil.getString(spf, "user", "");
        accountTextView = findViewById(R.id.account_phone);
        accountTextView.setText(account);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        phoneEdit.setText(phone);
        phoneEdit.setSelection(phone.length());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setAccountUrl = Constants.url + Constants.setAccount;
                String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
                String user = SharedPreferenceUtil.getString(spf, "user", "");
                // String pwd = SharedPreferenceUtil.getString(spf, "pwd", "");
                String pwd = pwdEdit.getText().toString();
                final String newPhone = phoneEdit.getText().toString();
                Log.e(PREFIX, "requestUrl = " + setAccountUrl);
                FormBody.Builder builder = new FormBody.Builder();
                if(TextUtils.isEmpty(pwd)) {
                    new AlertDialog.Builder(ModifyPhoneActivity.this).setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定触发的事件
                                }
                            }).setMessage("密码不能为空").show();
                    return;
                }
                if(TextUtils.isEmpty(newPhone)) {
                    new AlertDialog.Builder(ModifyPhoneActivity.this).setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定触发的事件
                                }
                            }).setMessage("新手机号不能为空").show();
                    return;
                }
                if(Pattern.matches(regexLT, newPhone) || Pattern.matches(regexYD, newPhone) || Pattern.matches(regexDX, newPhone)) {
                    Log.e(PREFIX, "matched");
                }
                else {
                    new android.app.AlertDialog.Builder(ModifyPhoneActivity.this).setTitle("提示")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //点击确定触发的事件
                                }
                            }).setMessage("新手机号不符合格式").show();
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
                builder.add("new_phone", newPhone);
                OkHttpUtil.post(setAccountUrl, builder.build(), sessionId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(ModifyPhoneActivity.this).setTitle("提示")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //点击确定触发的事件
                                                Intent intent = new Intent(ModifyPhoneActivity.this, LoginActivity.class);
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
                        if(code == OkHttpUtil.SUCCESS_CODE) {
                            String responseText = response.body().string();
                            JSONObject json = JSONObject.parseObject(responseText);
                            int retCode = json.getIntValue("Code");
                            Log.e(PREFIX, "responseText = " + responseText);
                            if(retCode == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AlertDialog.Builder(ModifyPhoneActivity.this).setTitle("提示")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //点击确定触发的事件
                                                        Intent intent = new Intent();
                                                        intent.putExtra("phone", newPhone);
                                                        setResult(Constants.MOD_OK, intent);
                                                        SharedPreferenceUtil.putString(spf, "user", newPhone);
                                                        finish();
                                                    }
                                                }).setMessage("绑定手机修改成功").show();
                                    }
                                });
                            }
                            else {
                                String objT = json.getString("ObjT");
                                if(objT.equals("Cannot match a user.")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertDialog.Builder(ModifyPhoneActivity.this).setTitle("提示")
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
                                            new AlertDialog.Builder(ModifyPhoneActivity.this).setTitle("提示")
                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //点击确定触发的事件
                                                        }
                                                    }).setMessage("修改失败，新手机号已经被绑定").show();
                                        }
                                    });
                                }
                                else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertDialog.Builder(ModifyPhoneActivity.this).setTitle("提示")
                                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //点击确定触发的事件
                                                            Intent intent = new Intent(ModifyPhoneActivity.this, LoginActivity.class);
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
                                    new AlertDialog.Builder(ModifyPhoneActivity.this).setTitle("提示")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //点击确定触发的事件
                                                    Intent intent = new Intent(ModifyPhoneActivity.this, LoginActivity.class);
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

