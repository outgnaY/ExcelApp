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

import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;

public class ModifyNameActivity extends AppCompatActivity {
    private String PREFIX = "[ModifyNameActivity]";
    private EditText nameEdit;
    private String name;
    private Toolbar toolbar;
    private SharedPreferences spf;
    private String user;
    private String pwd;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyname);
        init();
    }
    private void init() {
        spf = super.getSharedPreferences("login", MODE_PRIVATE);
        user = SharedPreferenceUtil.getString(spf, "user", "");
        pwd = SharedPreferenceUtil.getString(spf, "pwd", "");
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
                // OkHttpUtil.post();
            }
        });
        nameEdit.setText(name);
        nameEdit.setSelection(name.length());
    }
}
