package com.sjtu.ExcelApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ModifyEmailActivity extends AppCompatActivity {
    private String PREFIX = "[ModifyEmailActivity]";
    private EditText emailEdit;
    private String email;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyemail);
        init();
    }
    private void init() {
        emailEdit = findViewById(R.id.email_edit);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        toolbar = findViewById(R.id.user_modify_email);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(PREFIX, "click toolbar");
                finish();//返回
            }
        });
        emailEdit.setText(email);
        emailEdit.setSelection(email.length());
    }
}
