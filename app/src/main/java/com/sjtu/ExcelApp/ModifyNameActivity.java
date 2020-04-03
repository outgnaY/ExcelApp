package com.sjtu.ExcelApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ModifyNameActivity extends AppCompatActivity {
    private String PREFIX = "[ModifyNameActivity]";
    private EditText nameEdit;
    private String name;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyname);
        init();
    }
    private void init() {
        nameEdit = findViewById(R.id.name_edit);
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
        nameEdit.setText(name);
        nameEdit.setSelection(name.length());
    }
}
