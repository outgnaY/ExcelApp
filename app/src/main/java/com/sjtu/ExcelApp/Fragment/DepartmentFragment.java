package com.sjtu.ExcelApp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sjtu.ExcelApp.Activity.LoginActivity;
import com.sjtu.ExcelApp.Activity.MainActivity;
import com.sjtu.ExcelApp.Activity.UserActivity;
import com.sjtu.ExcelApp.Adapter.TableAdapter;
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
    private List<TableItem> list = new ArrayList<>();
    private String departmentName;
    private TextView departmentHeader;
    private TextView limit;
    private TextView approvedItems;
    private TextView executed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.department, container, false);
        Bundle bundle = this.getArguments();
        departmentName = bundle.getString("key");
        departmentHeader = view.findViewById(R.id.department_header);
        departmentHeader.setText(departmentName);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }
    private void init() {

        final MainActivity mainActivity = (MainActivity) getActivity();
        limit = mainActivity.findViewById(R.id.frag_upper_limit);
        approvedItems = mainActivity.findViewById(R.id.frag_project_num);
        executed = mainActivity.findViewById(R.id.frag_executed);
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
                        Toast.makeText(mainActivity, "服务器出错", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mainActivity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        // mainActivity.finish();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                Log.e(PREFIX, "code = " + String.valueOf(code));
                if(code == OkHttpUtil.SUCCESS_CODE) {
                    ResponseBody responseBody = response.body();
                    if(responseBody == null) {
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mainActivity, "请重新登录！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mainActivity, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    }
                    else {
                        String responseText = responseBody.string();
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
                                JSONObject o = array.getJSONObject(i);
                                limitSum += o.getDouble("Limit");
                                approvedItemsSum += o.getIntValue("ApprovedItems");
                                executedSum += o.getDoubleValue("Funding");
                                list.add(new TableItem(o.getString("Name"), String.format("%d", o.getIntValue("ApprovedItems")), String.format("%.2f", o.getDoubleValue("Funding")), String.format("%.2f", o.getDoubleValue("Limit"))));
                            }

                            final double finalLimitSum = limitSum;
                            final int finalApprovedItemsSum = approvedItemsSum;
                            final double finalExecutedSum = executedSum;
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    limit.setText(String.format("%.2f", finalLimitSum));
                                    approvedItems.setText(String.format("%d", finalApprovedItemsSum));
                                    executed.setText(String.format("%.2f", finalExecutedSum));
                                    TableAdapter adapter = new TableAdapter(mainActivity, R.layout.table_item, list);
                                    ListView listView = (ListView) mainActivity.findViewById(R.id.frag_list_view);
                                    listView.setAdapter(adapter);
                                }
                            });
                        }
                        else {
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mainActivity, "服务器出错", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(mainActivity, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                }
                else {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(mainActivity, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            // mainActivity.finish();
                        }
                    });
                }
                response.close();
            }
        });
    }
}
