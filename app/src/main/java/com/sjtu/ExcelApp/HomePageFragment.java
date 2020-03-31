package com.sjtu.ExcelApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.Utils;
import com.sjtu.ExcelApp.Customize.LinearProgress;

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
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MainActivity mainActivity = (MainActivity) getActivity();
        init(view);
        initBars(view, mainActivity);
        // init
        maths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, DepartmentActivity.class);
                intent.putExtra("maths", MATHS);
                startActivity(intent);
            }
        });
        chem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, DepartmentActivity.class);
                intent.putExtra("chem", CHEM);
                startActivity(intent);
            }
        });
        life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, DepartmentActivity.class);
                intent.putExtra("life", LIFE);
                startActivity(intent);
            }
        });
        globe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, DepartmentActivity.class);
                intent.putExtra("globe", GLOBE);
                startActivity(intent);
            }
        });
        material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, DepartmentActivity.class);
                intent.putExtra("material", MATERIAL);
                startActivity(intent);
            }
        });
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, DepartmentActivity.class);
                intent.putExtra("info", INFO);
                startActivity(intent);
            }
        });
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, DepartmentActivity.class);
                intent.putExtra("manage", MANAGE);
                startActivity(intent);
            }
        });
        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, DepartmentActivity.class);
                intent.putExtra("medical", MEDICAL);
                startActivity(intent);
            }
        });
        coop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, DepartmentActivity.class);
                intent.putExtra("coop", COOP);
                startActivity(intent);
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, DepartmentActivity.class);
                intent.putExtra("more", MORE);
                startActivity(intent);
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

    }
}
