package com.sjtu.ExcelApp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sjtu.ExcelApp.Activity.LoginActivity;
import com.sjtu.ExcelApp.Activity.MainActivity;
import com.sjtu.ExcelApp.Adapter.TableAdapter;
import com.sjtu.ExcelApp.Customize.CircleProgress;
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

public class DepartmentFragment extends Fragment {
    private String PREFIX = "[DepartmentFragment]";
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<TableItem> list = new ArrayList<>();
    private List<Boolean> status = new ArrayList<>();
    private String departmentName;
    private TextView departmentHeader;

    private Typeface scMedium;
    private Typeface scRegular;
    private Typeface numMedium;
    private Typeface numRegular;

    private TextView projectStatusTitle;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.department, container, false);
        Bundle bundle = this.getArguments();
        scMedium = Typeface.createFromAsset(getContext().getAssets(), "NotoSansSC-Medium.ttf");
        scRegular = Typeface.createFromAsset(getContext().getAssets(), "NotoSansSC-Regular.ttf");
        numMedium = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Medium.ttf");
        numRegular = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Regular.ttf");
        // limit = mainActivity.findViewById(R.id.frag_upper_limit);
        swipeRefreshLayout = view.findViewById(R.id.refresh_department);
        swipeRefreshLayout.setColorSchemeResources(R.color.departmentBackground);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                init();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        approvedItems = view.findViewById(R.id.frag_project_num);
        approvedItems.setTypeface(numMedium);
        executed = view.findViewById(R.id.frag_executed);
        executed.setTypeface(numMedium);
        circleProgress = view.findViewById(R.id.frag_department_circle);
        approvedItemsTitle = view.findViewById(R.id.frag_project_num_title);
        approvedItemsTitle.setTypeface(scRegular);
        executedTitle = view.findViewById(R.id.frag_executed_title);
        executedTitle.setTypeface(scRegular);
        projectStatusTitle = view.findViewById(R.id.frag_project_status_title);
        projectStatusTitle.setTypeface(scMedium);
        tableItemProjectTitle = view.findViewById(R.id.frag_table_item_project_title);
        tableItemProjectTitle.setTypeface(scRegular);
        tableItemApprovalTitle = view.findViewById(R.id.frag_table_item_approval_title);
        tableItemApprovalTitle.setTypeface(scRegular);
        tableItemSubsidyTitle = view.findViewById(R.id.frag_table_item_subsidy_title);
        tableItemSubsidyTitle.setTypeface(scRegular);
        departmentName = bundle.getString("key");
        departmentHeader = view.findViewById(R.id.frag_department_header);
        departmentHeader.setText(departmentName);
        departmentHeader.setTypeface(scMedium);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }
    private void init() {
        final MainActivity mainActivity = (MainActivity) getActivity();
        SharedPreferences spf = mainActivity.getSharedPreferences("login", Context.MODE_PRIVATE);
        String deptProjectsInfoUrl = Constants.url + Constants.getDeptProjectsInfo;
        String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
        Log.e(PREFIX, "sessionId = " + sessionId);
        OkHttpUtil.post(deptProjectsInfoUrl, new FormBody.Builder().build(), sessionId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(mainActivity).setTitle("提示")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //点击确定触发的事件
                                        Intent intent = new Intent(mainActivity, LoginActivity.class);
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
                    Log.e(PREFIX, "responseBody = " + responseBody);
                    String responseText = responseBody.string();
                    if(responseText.equals("null")) {
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(mainActivity).setTitle("提示")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //点击确定触发的事件
                                                Intent intent = new Intent(mainActivity, LoginActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }).setMessage("您的账号权限类型为 Normal，且所属部门没有权限访问 APP 数据。请联系管理员升级为 Supervisor 权限或更换绑定部门后访问").show();
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
                            mainActivity.runOnUiThread(new Runnable() {
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
                                        circleProgress.setBottom2Text(String.format("%.2f", finalLimitSum / 10000));
                                        circleProgress.setBottom1Text("计划额度(亿)");
                                    }
                                    else {
                                        circleProgress.setBottom2Text(String.format("%.2f", finalLimitSum));
                                        circleProgress.setBottom1Text("计划额度(万)");
                                    }
                                    // circleProgress.setBottom2Text(String.format("%.2f", finalLimitSum));
                                    approvedItems.setText(String.format("%d", finalApprovedItemsSum));
                                    executed.setText(String.format("%.2f", finalExecutedSum));

                                    TableAdapter adapter = new TableAdapter(mainActivity, R.layout.table_item, list, status);
                                    final ListView listView = (ListView) mainActivity.findViewById(R.id.frag_list_view);
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
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(mainActivity).setTitle("提示")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //点击确定触发的事件
                                                    Intent intent = new Intent(mainActivity, LoginActivity.class);
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
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(mainActivity).setTitle("提示")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //点击确定触发的事件
                                            Intent intent = new Intent(mainActivity, LoginActivity.class);
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
}
