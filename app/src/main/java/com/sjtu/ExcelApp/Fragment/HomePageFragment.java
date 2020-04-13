package com.sjtu.ExcelApp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.sjtu.ExcelApp.Customize.LinearProgress;
import com.sjtu.ExcelApp.R;
import com.sjtu.ExcelApp.Util.Constants;
import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class HomePageFragment extends Fragment {

    private LinearLayout maths;
    private LinearLayout chem;
    private LinearLayout life;
    private LinearLayout globe;
    private LinearLayout material;
    private LinearLayout info;
    private LinearLayout manage;
    private LinearLayout medical;
    private LinearLayout coop;
    private LinearLayout more;
    private View view;

    private TextView budgetData;
    private TextView planData;
    private TextView executeData;
    private TextView rateData;

    // private String getAccountUrl;
    // private String sessionId;
    private String PREFIX = "[HomePageFragment]";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage, container, false);
        this.view = view;
        return view;
    }

    public void initBars(View view, Context context, JSONObject objT) {
        JSONArray array = objT.getJSONArray("ProjectInfoList");

        Log.e(PREFIX, String.valueOf(array.size()));
        /*
        String[] title = {
                            "面上项目",
                            "重点项目",
                            "重大项目",
                            "重大研究计划项目",
                            "国际(地区)合作研究项目",
                            "青年科学基金项目",
                            "优秀青年科学基金项目",
                            "国家杰出青年科学项目基金",
                            "创新研究群体项目",
                            "地区科学基金项目",
                            "联合基金项目(委内出资额)",
                            "国家重大科研仪器研制项目",
                            "基础科学中心项目",
                            "专项项目",
                            "数学田园基金项目",
                            "外国青年学者研究基金项目",
                            "国际(地区)合作交流项目"
        };
        */
        LinearLayout layoutParent = view.findViewById(R.id.projects);
        double maxRate = 0;
        for(int i = 0; i < array.size(); i++) {
            maxRate = Math.max(maxRate, array.getJSONObject(i).getDouble("ExeRate"));
        }
        for(int i = 0; i < array.size(); i++) {
            JSONObject o = array.getJSONObject(i);
            double rate = o.getDouble("ExeRate");
            String projectName = o.getString("Name");


            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(params);

            LinearLayout.LayoutParams textViewLp = new LinearLayout.LayoutParams(0, (int) Utils.dp2px(getResources(), 50), 4);
            // textViewLp.setMargins(0, (int) Utils.dp2px(getResources(), 15), 0, (int) Utils.dp2px(getResources(), 15));
            TextView textView = new TextView(context);
            textView.setLayoutParams(textViewLp);
            textView.setText(projectName);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setGravity(Gravity.CENTER);


            LinearLayout.LayoutParams relativeLayoutLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 7);
            relativeLayoutLp.setMargins(0, 0, (int) Utils.dp2px(getResources(), 10), 0);
            RelativeLayout relativeLayout = new RelativeLayout(context);
            relativeLayout.setLayoutParams(relativeLayoutLp);

            RelativeLayout.LayoutParams linearProgressLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            LinearProgress linearProgress = new LinearProgress(context);
            linearProgress.setLayoutParams(linearProgressLp);
            linearProgress.setProgressBackgroundColor(Color.rgb(184, 184, 184));
            linearProgress.setProgress((float) (rate * 100 / maxRate));
            linearProgress.setProgressColor(Color.rgb(27, 130, 209));
            linearProgress.setTextColor(Color.rgb(0, 0, 0));
            linearProgress.setText(String.format("%.2f%%", rate * 100));
            relativeLayout.addView(linearProgress);
            // textView.set
            layout.addView(textView);
            layout.addView(relativeLayout);
            layoutParent.addView(layout);
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
                // FormBody.Builder builder = new FormBody.Builder();
                // builder.add("deptId", String.valueOf(extraValue));
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
                            // Log.e(PREFIX, response.body().string());
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
                    }
                });

            }
        });
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MainActivity mainActivity = (MainActivity) getActivity();
        init(view);
        // initBars(view, mainActivity);
        // init
        setOnClickListener(maths, "key", Constants.MATHS);
        setOnClickListener(chem, "key", Constants.CHEM);
        setOnClickListener(life, "key", Constants.LIFE);
        setOnClickListener(globe, "key", Constants.GLOBE);
        setOnClickListener(material, "key", Constants.MATERIAL);
        setOnClickListener(info, "key", Constants.INFO);
        setOnClickListener(manage, "key", Constants.MANAGE);
        setOnClickListener(medical, "key", Constants.MEDICAL);
        // setOnClickListener(coop, "key", Constants.COOP);
        // setOnClickListener(more, "key", MORE);
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
                            final double totalOfPlan = objT.getDouble("TotalOfPlan");
                            Log.e(PREFIX, "TotalOfPlan = " + totalOfPlan);
                            final double exeQuota = objT.getDouble("ExeQuota");
                            Log.e(PREFIX, "ExeQuota = " + exeQuota);
                            final double exeRate = objT.getDouble("ExeRate");
                            Log.e(PREFIX, "ExeRate = " + exeRate);
                            mainActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    budgetData.setText(String.format("%.2f", budget));
                                    planData.setText(String.format("%.2f", totalOfPlan));
                                    executeData.setText(String.format("%.2f", exeQuota));
                                    rateData.setText(String.format("%.2f%%", exeRate * 100));

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
            }
        });
    }
    private void init(View view) {
        // get views
        maths = view.findViewById(R.id.maths);
        chem = view.findViewById(R.id.chem);
        life = view.findViewById(R.id.life);
        globe = view.findViewById(R.id.globe);
        material = view.findViewById(R.id.material);
        info = view.findViewById(R.id.info);
        manage = view.findViewById(R.id.manage);
        medical = view.findViewById(R.id.medical);
        coop = view.findViewById(R.id.coop);
        more = view.findViewById(R.id.more);

        budgetData = view.findViewById(R.id.budget_data);
        planData = view.findViewById(R.id.plan_data);
        executeData = view.findViewById(R.id.execute_data);
        rateData = view.findViewById(R.id.rate_data);

    }
}
