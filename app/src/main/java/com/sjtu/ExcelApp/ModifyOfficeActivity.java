package com.sjtu.ExcelApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ModifyOfficeActivity extends AppCompatActivity {
    private String PREFIX = "[ModifyOfficeActivity]";
    private EditText officeEdit;
    private String office;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyoffice);
        init();
    }
    private void init() {
        officeEdit = findViewById(R.id.office_edit);
        Intent intent = getIntent();
        office = intent.getStringExtra("office");
        toolbar = findViewById(R.id.user_modify_office);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(PREFIX, "click toolbar");
                finish();//返回
            }
        });
        officeEdit.setText(office);
        officeEdit.setSelection(office.length());
    }
}
