package com.sjtu.ExcelApp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.circleprogress.Utils;
import com.sjtu.ExcelApp.Activity.DepartmentActivity;
import com.sjtu.ExcelApp.Activity.LoginActivity;
import com.sjtu.ExcelApp.Activity.MainActivity;
import com.sjtu.ExcelApp.Adapter.ViewPagerAdapter;
import com.sjtu.ExcelApp.Customize.DotView;
import com.sjtu.ExcelApp.Customize.LinearProgress;
import com.sjtu.ExcelApp.Customize.SemiCircleProgress;
import com.sjtu.ExcelApp.R;
import com.sjtu.ExcelApp.Util.ComputeUtil;
import com.sjtu.ExcelApp.Util.Constants;
import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class HomePageFragment extends Fragment {
    private List<View> pages;
    private ViewPager viewPager;

    private LinearLayout maths;
    private LinearLayout chem;
    private LinearLayout life;
    private LinearLayout globe;
    private LinearLayout material;
    private LinearLayout info;
    private LinearLayout manage;
    private LinearLayout medical;
    private View view;
    private TextView budgetData;
    private TextView planData;
    private TextView executeData;
    private TextView rateData;
    private DotView dot1;
    private DotView dot2;

    private String PREFIX = "[HomePageFragment]";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage, container, false);
        this.view = view;
        init(view);
        // init
        setOnClickListener(maths, "key", Constants.MATHS);
        setOnClickListener(chem, "key", Constants.CHEM);
        setOnClickListener(life, "key", Constants.LIFE);
        setOnClickListener(globe, "key", Constants.GLOBE);
        setOnClickListener(material, "key", Constants.MATERIAL);
        setOnClickListener(info, "key", Constants.INFO);
        setOnClickListener(manage, "key", Constants.MANAGE);
        setOnClickListener(medical, "key", Constants.MEDICAL);
        return view;
    }

    public void initBars(View view, Context context, JSONObject objT) {
        JSONArray array = objT.getJSONArray("ProjectInfoList");
        Log.e(PREFIX, String.valueOf(array.toJSONString()));
        LinearLayout layoutParent = view.findViewById(R.id.projects);
        double maxRate = 0;
        for(int i = 0; i < array.size(); i++) {
            maxRate = Math.max(maxRate, array.getJSONObject(i).getDouble("ExeRate"));
        }
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        for(int i = 0; i < array.size(); i++) {
            JSONObject o = array.getJSONObject(i);
            int items = o.getIntValue("Items");
            String name = o.getString("Name");
            double exeQuota = o.getDoubleValue("ExeQuota");
            double exeRate = o.getDoubleValue("ExeRate");
            int totalOfPlan = o.getIntValue("TotalOfPlan");
            View progressItem = inflater.inflate(R.layout.progress_item, null);
            TextView itemsText = progressItem.findViewById(R.id.items);
            TextView exeQuotaText = progressItem.findViewById(R.id.exe_quota);
            TextView exeRateText = progressItem.findViewById(R.id.exe_rate);
            TextView totalOfPlanText = progressItem.findViewById(R.id.total_of_plan);
            TextView projectNameText = progressItem.findViewById(R.id.project_name);
            LinearProgress progress = progressItem.findViewById(R.id.progress);

            itemsText.setText(String.format("项目数：%d", items));
            exeQuotaText.setText(String.format("执行资金：%.2f", exeQuota));
            exeRateText.setText(String.format("执行率：%.2f%%", exeRate * 100));
            totalOfPlanText.setText(String.format("计划额度：%d", totalOfPlan));
            if(exeRate * 100 >= 100) {
                progress.setProgress(100);
            }
            else {
                progress.setProgress((float) (exeRate * 100));
            }
            projectNameText.setText(name);
            layoutParent.addView(progressItem);
        }
    }
    private void setOnClickListener(View view, final String extraKey, final int extraValue) {
        
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MainActivity mainActivity = (MainActivity) getActivity();
                final String getAccountUrl = Constants.url + Constants.getAccount;
                SharedPreferences spf = mainActivity.getSharedPreferences("login", mainActivity.MODE_PRIVATE);
                final String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
                Log.e(PREFIX, "sessionId = " + sessionId);
                OkHttpUtil.post(getAccountUrl, new FormBody.Builder().build(), sessionId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mainActivity, "服务器出错", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mainActivity, LoginActivity.class);
                                startActivity(intent);
                                mainActivity.finish();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int code = response.code();
                        Log.e(PREFIX, "code = " + String.valueOf(code));
                        if(code == OkHttpUtil.SUCCESS_CODE) {
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(mainActivity, DepartmentActivity.class);
                                    intent.putExtra(extraKey, extraValue);
                                    startActivity(intent);
                                }
                            });
                        }
                        else {
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(mainActivity, LoginActivity.class);
                                    startActivity(intent);
                                    mainActivity.finish();
                                }
                            });
                        }
                        response.close();
                    }
                });

            }
        });
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MainActivity mainActivity = (MainActivity) getActivity();
        LayoutInflater layoutInflater = mainActivity.getLayoutInflater();
        View pager1 = layoutInflater.inflate(R.layout.pager1, null);
        View pager2 = layoutInflater.inflate(R.layout.pager2, null);
        pages = new ArrayList<>();
        pages.add(pager1);
        pages.add(pager2);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(pages, viewPager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                Log.e(PREFIX, "Select page on position " + position);
                if(position == 0) {
                    dot1.setColor(Color.rgb(159, 195, 247));
                    dot1.setLength(ComputeUtil.dp2px(getResources(), 8));
                    dot2.setColor(Color.rgb(60, 109, 185));
                    dot2.setLength(ComputeUtil.dp2px(getResources(), (float) 0.01));
                }
                else {
                    dot2.setColor(Color.rgb(159, 195, 247));
                    dot2.setLength(ComputeUtil.dp2px(getResources(), 8));
                    dot1.setColor(Color.rgb(60, 109, 185));
                    dot1.setLength(ComputeUtil.dp2px(getResources(), (float) 0.01));
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // send http requests
        getOverallInfo();
        getProjectsInfo();

    }
    private void getOverallInfo() {
        final MainActivity mainActivity = (MainActivity) getActivity();
        final String getOverallInfoUrl = Constants.url + Constants.getOverallInfo;
        SharedPreferences spf = mainActivity.getSharedPreferences("login", mainActivity.MODE_PRIVATE);
        final String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
        OkHttpUtil.post(getOverallInfoUrl, new FormBody.Builder().build(), sessionId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainActivity, "服务器出错", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mainActivity, LoginActivity.class);
                        startActivity(intent);
                        mainActivity.finish();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                Log.e(PREFIX, "code = " + String.valueOf(code));
                if(code == OkHttpUtil.SUCCESS_CODE) {
                    String responseText = response.body().string();
                    if(responseText == null) {
                        // auth error
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
                    else {
                        JSONObject json = JSONObject.parseObject(responseText);
                        int retCode = json.getIntValue("Code");
                        if(retCode == 0) {
                            JSONObject objT = json.getJSONObject("ObjT");
                            final double budget = objT.getDouble("Budget");
                            Log.e(PREFIX, "Budget = " + budget);
                            final int totalOfPlan = objT.getIntValue("TotalOfPlan");
                            Log.e(PREFIX, "TotalOfPlan = " + totalOfPlan);
                            final double exeQuota = objT.getDouble("ExeQuota");
                            Log.e(PREFIX, "ExeQuota = " + exeQuota);
                            final double exeRate = objT.getDouble("ExeRate");
                            Log.e(PREFIX, "ExeRate = " + exeRate);
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    View page1 = pages.get(0);
                                    View page2 = pages.get(1);
                                    SemiCircleProgress semiCircleProgress1 = page1.findViewById(R.id.pager_circle1);
                                    SemiCircleProgress semiCircleProgress2 = page2.findViewById(R.id.pager_circle2);
                                    semiCircleProgress1.setMidText(String.format("%.2f", budget / 10000));
                                    semiCircleProgress1.setBottom2Text(String.format("%.2f", exeQuota / 10000));
                                    if(budget < exeQuota) {
                                        semiCircleProgress1.setProgress((float) (budget * 100 / exeQuota));
                                    }
                                    else {
                                        semiCircleProgress1.setProgress(100);
                                    }

                                    semiCircleProgress2.setMidText(String.format("%.2f", exeRate * 100));
                                    semiCircleProgress2.setBottom2Text(String.format("%d", totalOfPlan));
                                    if(exeRate < 1) {
                                        semiCircleProgress2.setProgress((float) (exeRate * 100));
                                    }
                                    else {
                                        semiCircleProgress2.setProgress(100);
                                    }
                                    /*
                                    budgetData.setText(String.format("%.2f", budget));
                                    planData.setText(String.format("%.2f", totalOfPlan));
                                    executeData.setText(String.format("%.2f", exeQuota));
                                    rateData.setText(String.format("%.2f%%", exeRate * 100));
                                    */
                                }
                            });
                        }
                        else {
                            // should not be here
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mainActivity, "服务器出错", Toast.LENGTH_SHORT).show();
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
                            startActivity(intent);
                            mainActivity.finish();
                        }
                    });
                }
                response.close();
            }
        });
    }
    private void getProjectsInfo() {
        final MainActivity mainActivity = (MainActivity) getActivity();
        final String getProjectsInfoUrl = Constants.url + Constants.getProjectsInfo;
        SharedPreferences spf = mainActivity.getSharedPreferences("login", mainActivity.MODE_PRIVATE);
        final String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
        OkHttpUtil.post(getProjectsInfoUrl, new FormBody.Builder().build(), sessionId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainActivity, "服务器出错", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mainActivity, LoginActivity.class);
                        startActivity(intent);
                        mainActivity.finish();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                Log.e(PREFIX, "code = " + String.valueOf(code));
                if(code == OkHttpUtil.SUCCESS_CODE) {
                    String responseText = response.body().string();
                    if(responseText == null) {
                        // auth error
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
                    else {
                        JSONObject json = JSONObject.parseObject(responseText);
                        int retCode = json.getIntValue("Code");
                        if(retCode == 0) {
                            final JSONObject objT = json.getJSONObject("ObjT");
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initBars(view, mainActivity, objT);
                                }
                            });
                        }
                        else {
                            // should not be here
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mainActivity, "服务器出错", Toast.LENGTH_SHORT).show();
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
                            startActivity(intent);
                            mainActivity.finish();
                        }
                    });
                }
                response.close();
            }
        });
    }
    private void init(View view) {
        // get views
        viewPager = view.findViewById(R.id.pager);
        dot1 = view.findViewById(R.id.dot1);
        dot2 = view.findViewById(R.id.dot2);

        maths = view.findViewById(R.id.maths);
        chem = view.findViewById(R.id.chem);
        life = view.findViewById(R.id.life);
        globe = view.findViewById(R.id.globe);
        material = view.findViewById(R.id.material);
        info = view.findViewById(R.id.info);
        manage = view.findViewById(R.id.manage);
        medical = view.findViewById(R.id.medical);
        /*
        budgetData = view.findViewById(R.id.budget_data);
        planData = view.findViewById(R.id.plan_data);
        executeData = view.findViewById(R.id.execute_data);
        rateData = view.findViewById(R.id.rate_data);
        */
    }
}
