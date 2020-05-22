package com.sjtu.ExcelApp.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sjtu.ExcelApp.Adapter.TableAdapter;
import com.sjtu.ExcelApp.Customize.CircleProgress;
import com.sjtu.ExcelApp.Customize.CustomToolbar;
import com.sjtu.ExcelApp.Customize.FontIconView;
import com.sjtu.ExcelApp.Model.TableItem;
import com.sjtu.ExcelApp.R;
import com.sjtu.ExcelApp.Util.Constants;
import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class DepartmentActivity extends BaseActivity {
    private String PREFIX = "[DepartmentActivity]";
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<TableItem> list = new ArrayList<>();
    private List<Boolean> status = new ArrayList<>();
    private CustomToolbar toolbar;
    private String title;
    private int deptId;
    private TextView overallTitle;
    private TextView projectStatusTitle;
    private Typeface scMedium;
    private Typeface scRegular;
    private Typeface numMedium;
    private Typeface numRegular;

    // private TextView limit;
    private TextView approvedItems;
    private TextView executed;
    private TextView approvedItemsTitle;
    private TextView executedTitle;
    private CircleProgress circleProgress;
    private TextView tableItemProjectTitle;
    private TextView tableItemApprovalTitle;
    private TextView tableItemSubsidyTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        Intent intent = getIntent();
        deptId = intent.getIntExtra("key", 10);
        Log.e(PREFIX, String.valueOf(deptId));
        scMedium = Typeface.createFromAsset(getAssets(), "NotoSansSC-Medium.ttf");
        scRegular = Typeface.createFromAsset(getAssets(), "NotoSansSC-Regular.ttf");
        numMedium = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");
        numRegular = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        initItems();
        getData();
    }
    private void getData() {
        SharedPreferences spf = getSharedPreferences("login", Context.MODE_PRIVATE);
        String deptProjectsInfoUrl = Constants.url + Constants.getDeptProjectsInfo;
        String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("deptId", String.valueOf(deptId));
        OkHttpUtil.post(deptProjectsInfoUrl, builder.build(), sessionId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(DepartmentActivity.this).setTitle("提示")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //点击确定触发的事件
                                        Intent intent = new Intent(DepartmentActivity.this, LoginActivity.class);
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
                Log.e(PREFIX, "code = " + String.valueOf(code));
                if(code == OkHttpUtil.SUCCESS_CODE) {
                    ResponseBody responseBody = response.body();
                    String responseText = responseBody.string();
                    if(responseText.equals("null")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(DepartmentActivity.this).setTitle("提示")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //点击确定触发的事件
                                                Intent intent = new Intent(DepartmentActivity.this, LoginActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }).setMessage("权限错误，请重新登录").show();
                            }
                        });
                    }
                    else {
                        JSONObject json = JSONObject.parseObject(responseText);
                        int retCode = json.getIntValue("Code");
                        if(retCode == 0) {
                            JSONObject objT = json.getJSONObject("ObjT");
                            JSONArray array = objT.getJSONArray("DeptProjectInfoList");
                            Log.e(PREFIX, array.toString());
                            double limitSum = 0;
                            int approvedItemsSum = 0;
                            double executedSum = 0;
                            status = new ArrayList<>();
                            list = new ArrayList<>();
                            for(int i = 0; i < array.size(); i++) {
                                status.add(false);
                                JSONObject o = array.getJSONObject(i);
                                String nameVal = o.getString("Name");
                                int approvedItemsVal = o.getIntValue("ApprovedItems");
                                double fundingVal = o.getDoubleValue("Funding");
                                double limitVal = o.getDoubleValue("Limit");

                                limitSum += limitVal;
                                approvedItemsSum += approvedItemsVal;
                                executedSum += fundingVal;
                                list.add(new TableItem(nameVal, approvedItemsVal, fundingVal, 0, 0, 0));
                            }
                            for(int i = 0; i < array.size(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                double fundingVal = o.getDoubleValue("Funding");
                                double limitVal = o.getDoubleValue("Limit");
                                double totalLimitVal = o.getDoubleValue("TotalLimit");
                                TableItem item = list.get(i);
                                // limit / totalLimit
                                item.setQuotaProp(limitVal * 100 / totalLimitVal);
                                // funding / totalFunding
                                // item.setFundingProp(fundingVal * 100 / executedSum);
                                item.setQuota(limitVal);
                                // funding / limit
                                if(limitVal == 0) {
                                    item.setExecutedProp(0.0);
                                }
                                else {
                                    item.setExecutedProp(fundingVal * 100 / limitVal);
                                }

                            }
                            final double finalLimitSum = limitSum;
                            final int finalApprovedItemsSum = approvedItemsSum;
                            final double finalExecutedSum = executedSum;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(finalExecutedSum >= finalLimitSum) {
                                        circleProgress.setProgress(100);
                                    }
                                    else {
                                        circleProgress.setProgress((float) (100 * finalExecutedSum / finalLimitSum));
                                    }
                                    circleProgress.setMidText(String.format("%.1f", (float) (100 * finalExecutedSum / finalLimitSum)));
                                    if(finalLimitSum >= 100000) {
                                        circleProgress.setBottom2Text(String.format("%.2f", finalLimitSum / 10000) + " 亿");
                                        circleProgress.setBottom1Text("计划额度");
                                    }
                                    else {
                                        circleProgress.setBottom2Text(String.format("%.2f", finalLimitSum) + " 万");
                                        circleProgress.setBottom1Text("计划额度");
                                    }

                                    approvedItems.setText(String.format("%d", finalApprovedItemsSum));
                                    executed.setText(String.format("%.2f", finalExecutedSum));
                                    final TableAdapter adapter = new TableAdapter(DepartmentActivity.this, R.layout.table_item, list, status);
                                    final ListView listView = (ListView) DepartmentActivity.this.findViewById(R.id.list_view);

                                    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(AbsListView absListView, int i) {

                                        }
                                        @Override
                                        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                                            boolean enable = false;
                                            if (listView != null && listView.getChildCount() > 0) {
                                                // check if the first item of the list is visible
                                                boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                                                // check if the top of the first item is visible
                                                boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                                                // enabling or disabling the refresh layout
                                                enable = firstItemVisible && topOfFirstItemVisible;
                                            }
                                            swipeRefreshLayout.setEnabled(enable);
                                        }
                                    });
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            Log.e(PREFIX, "click at position: " + i);
                                            LinearLayout itemDetailed = view.findViewById(R.id.item_detailed);
                                            FontIconView icon = view.findViewById(R.id.solid_arrow);
                                            if(!status.get(i)) {
                                                itemDetailed.setVisibility(View.VISIBLE);
                                                icon.setText(R.string.solid_arrow_down);
                                                status.set(i, true);
                                            }
                                            else {
                                                itemDetailed.setVisibility(View.GONE);
                                                icon.setText(R.string.solid_arrow_right);
                                                status.set(i, false);
                                            }
                                            /*
                                            if(itemDetailed.getVisibility() == View.VISIBLE) {
                                                itemDetailed.setVisibility(View.GONE);
                                                icon.setText(R.string.solid_arrow_right);
                                            }
                                            else {
                                                itemDetailed.setVisibility(View.VISIBLE);
                                                icon.setText(R.string.solid_arrow_down);
                                            }
                                            */
                                        }
                                    });
                                    listView.setAdapter(adapter);

                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(DepartmentActivity.this).setTitle("提示")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //点击确定触发的事件
                                                    Intent intent = new Intent(DepartmentActivity.this, LoginActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            }).setMessage("获取数据失败，请重新登录").show();

                                }
                            });
                        }
                    }
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(DepartmentActivity.this).setTitle("提示")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //点击确定触发的事件
                                            Intent intent = new Intent(DepartmentActivity.this, LoginActivity.class);
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
    private void initItems() {
        swipeRefreshLayout = findViewById(R.id.refresh_activity_department);
        swipeRefreshLayout.setColorSchemeResources(R.color.departmentBackground);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        tableItemProjectTitle = findViewById(R.id.table_item_project_title);
        tableItemProjectTitle.setTypeface(scRegular);
        tableItemApprovalTitle = findViewById(R.id.table_item_approval_title);
        tableItemApprovalTitle.setTypeface(scRegular);
        tableItemSubsidyTitle = findViewById(R.id.table_item_subsidy_title);
        tableItemSubsidyTitle.setTypeface(scRegular);
        // limit = findViewById(R.id.upper_limit);
        circleProgress = findViewById(R.id.department_circle);
        approvedItems = findViewById(R.id.project_num);
        approvedItems.setTypeface(numMedium);
        executed = findViewById(R.id.executed);
        executed.setTypeface(numMedium);
        approvedItemsTitle = findViewById(R.id.project_num_title);
        approvedItemsTitle.setTypeface(scRegular);
        executedTitle = findViewById(R.id.executed_title);
        executedTitle.setTypeface(scRegular);

        // overallTitle = findViewById(R.id.overall_title);
        // overallTitle.setTypeface(typeface);
        projectStatusTitle = findViewById(R.id.project_status_title);
        projectStatusTitle.setTypeface(scMedium);

        toolbar = findViewById(R.id.department_detailed);
        title = Constants.departmentNameMap.get(getIntent().getIntExtra("key", 0));
        Log.e(PREFIX, "title = " + title);
        toolbar.setTitle(title);
    }

}
