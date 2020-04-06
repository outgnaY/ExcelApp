package com.sjtu.ExcelApp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.sjtu.ExcelApp.Adapter.TableAdapter;
import com.sjtu.ExcelApp.Model.TableItem;
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
        list.add(new TableItem("张三", "223", "16"));
        list.add(new TableItem("李四", "223", "16"));
        list.add(new TableItem("王五", "22", "16000000"));
        list.add(new TableItem("张四", "2253", "1600000000"));
        list.add(new TableItem("李三", "223", "16"));
        list.add(new TableItem("王五", "223", "16"));
    }

}
