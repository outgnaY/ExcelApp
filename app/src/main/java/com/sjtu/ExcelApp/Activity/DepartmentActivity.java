package com.sjtu.ExcelApp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.sjtu.ExcelApp.Adapter.TableAdapter;
import com.sjtu.ExcelApp.Model.TableItem;
import com.sjtu.ExcelApp.R;
import com.sjtu.ExcelApp.Util.Constants;

import java.util.ArrayList;
import java.util.List;


public class DepartmentActivity extends AppCompatActivity {
    private String PREFIX = "[DepartmentActivity]";
    private List<TableItem> list = new ArrayList<>();
    private Toolbar toolbar;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        initItems();
        TableAdapter adapter = new TableAdapter(this, R.layout.table_item, list);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }
    private void initItems() {
        toolbar = findViewById(R.id.department_detailed);
        title = Constants.map.get(getIntent().getIntExtra("key", 0));
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(PREFIX, "click toolbar");
                finish();//返回
            }
        });
        list.add(new TableItem("面上项目", "223", "160000"));
        list.add(new TableItem("重点项目", "223", "16555"));
        list.add(new TableItem("重大项目", "223", "160000"));
        list.add(new TableItem("重大研究计划项目", "2253", "1600000000"));
        list.add(new TableItem("国际（地区）合作研究项目", "223", "165555"));
        list.add(new TableItem("青年科学基金项目", "223", "16456"));
        list.add(new TableItem("优秀青年科学基金项目", "223", "1655"));
        list.add(new TableItem("国家杰出青年科学基金项目", "223", "1655"));
        list.add(new TableItem("创新研究群体项目", "2234", "1655"));
        list.add(new TableItem("海外及港澳学者合作研究基金项目", "223", "1655"));
        list.add(new TableItem("地区科学基金项目", "2233", "1655"));
        list.add(new TableItem("联合基金项目（委内经费）", "223", "1655"));
        list.add(new TableItem("国家重大科研仪器研制项目", "223", "1655"));
        list.add(new TableItem("基础科学中心项目", "223", "1655"));
        list.add(new TableItem("专项项目", "223", "1655"));
        list.add(new TableItem("数学天元基金项目", "223", "1655"));
        list.add(new TableItem("外国青年学者研究基金项目", "223", "1655"));
        list.add(new TableItem("国际（地区）合作交流项目", "223", "1655"));
    }

}
