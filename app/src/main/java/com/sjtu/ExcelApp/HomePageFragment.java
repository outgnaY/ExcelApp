package com.sjtu.ExcelApp;

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

import com.github.lzyzsd.circleprogress.Utils;
import com.sjtu.ExcelApp.Customize.LinearProgress;
import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.PropertiesUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;

import java.io.IOException;
import java.util.Properties;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class HomePageFragment extends Fragment {
    private static final int MATHS = 1;
    private static final int CHEM = 2;
    private static final int LIFE = 3;
    private static final int GLOBE = 4;
    private static final int MATERIAL = 5;
    private static final int INFO = 6;
    private static final int MANAGE = 7;
    private static final int MEDICAL = 8;
    private static final int COOP = 9;
    private static final int MORE = 10;
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
    // private String getAccountUrl;
    // private String sessionId;
    private String PREFIX = "[HomePageFragment]";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage, container, false);
        this.view = view;
        return view;
    }
    public void initBars(View view, Context context) {
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
        LinearLayout layoutParent = view.findViewById(R.id.projects);
        for(int i = 0; i < title.length; i++) {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setLayoutParams(params);

            LinearLayout.LayoutParams textViewLp = new LinearLayout.LayoutParams(0, (int) Utils.dp2px(getResources(), 50), 4);
            // textViewLp.setMargins(0, (int) Utils.dp2px(getResources(), 15), 0, (int) Utils.dp2px(getResources(), 15));
            TextView textView = new TextView(context);
            textView.setLayoutParams(textViewLp);
            textView.setText(title[i]);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setGravity(Gravity.CENTER);
            // textView.setBackgroundColor(Color.rgb(2*i, 2*i+1, 2*i+2));


            LinearLayout.LayoutParams relativeLayoutLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 7);
            relativeLayoutLp.setMargins(0, 0, (int) Utils.dp2px(getResources(), 10), 0);
            RelativeLayout relativeLayout = new RelativeLayout(context);
            relativeLayout.setLayoutParams(relativeLayoutLp);

            RelativeLayout.LayoutParams linearProgressLp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            LinearProgress linearProgress = new LinearProgress(context);
            linearProgress.setLayoutParams(linearProgressLp);
            linearProgress.setProgressBackgroundColor(Color.rgb(184, 184, 184));
            linearProgress.setProgress(40);
            linearProgress.setProgressColor(Color.rgb(1, 2, 3));
            linearProgress.setTextColor(Color.rgb(0, 0, 0));
            linearProgress.setText("40%");
            relativeLayout.addView(linearProgress);
            // textView.set
            layout.addView(textView);
            layout.addView(relativeLayout);
            layoutParent.addView(layout);
        }

    }
    private void setOnClickListener(View view, final String extraKey, final int extraValue) {
        final MainActivity mainActivity = (MainActivity) getActivity();
        Properties properties = PropertiesUtil.getProperties();
        String url = properties.getProperty("url");
        String port = properties.getProperty("port");
        final String getAccountUrl = url + ":" + port + "/api/getAccount";
        SharedPreferences spf = mainActivity.getSharedPreferences("login", mainActivity.MODE_PRIVATE);
        final String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
        Log.e(PREFIX + "sessionId = ", sessionId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Log.e(PREFIX + "code = ", String.valueOf(code));
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
        initBars(view, mainActivity);

        // init
        setOnClickListener(maths, "maths", MATHS);
        setOnClickListener(chem, "chem", CHEM);
        setOnClickListener(life, "life", LIFE);
        setOnClickListener(globe, "globe", GLOBE);
        setOnClickListener(material, "material", MATERIAL);
        setOnClickListener(info, "info", INFO);
        setOnClickListener(manage, "manage", MANAGE);
        setOnClickListener(medical, "medical", MEDICAL);
        setOnClickListener(coop, "coop", COOP);
        setOnClickListener(more, "more", MORE);

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

    }
}
