package com.sjtu.ExcelApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sjtu.ExcelApp.Util.Constants;
import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class ModifyNameActivity extends AppCompatActivity {
    private String PREFIX = "[ModifyNameActivity]";
    private EditText nameEdit;
    private String name;
    private Toolbar toolbar;
    private SharedPreferences spf;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyname);
        init();
    }
    private void init() {
        spf = super.getSharedPreferences("login", MODE_PRIVATE);
        nameEdit = findViewById(R.id.name_edit);
        btn = findViewById(R.id.mod_name_btn);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        toolbar = findViewById(R.id.user_modify_name);

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

                OkHttpUtil.post(setAccountUrl, builder.build(), sessionId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ModifyNameActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ModifyNameActivity.this, LoginActivity.class);
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
                        Log.e(PREFIX + "code = ", String.valueOf(code));
                        if(code == OkHttpUtil.SUCCESS_CODE) {
                            // TODO JsonParse
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent();
                                    intent.putExtra("name", "name");
                                    setResult(Constants.MOD_OK, intent);
                                    finish();
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(ModifyNameActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
            }
        });
        nameEdit.setText(name);
        nameEdit.setSelection(name.length());
    }
}
