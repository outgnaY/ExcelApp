package com.sjtu.ExcelApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sjtu.ExcelApp.Util.Constants;
import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MyFragment extends Fragment {
    private View userWrapper;
    private View passwordWrapper;
    private View settingsWrapper;
    private TextView nameText;
    private TextView officeText;
    private View view;
    private String PREFIX = "[MyFragment]";
    private String name;
    private String office;
    private de.hdodenhof.circleimageview.CircleImageView profile;
    private final int USER = 1;
    private final int PASSWORD = 2;
    private final int SETTINGS = 3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my, container, false);
        Bundle bundle = this.getArguments();
        this.name = bundle.getString("name");
        this.office = bundle.getString("office");
        Log.e(PREFIX + "name", name);
        Log.e(PREFIX + "office", office);
        this.view = view;
        init(view);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MainActivity mainActivity = (MainActivity) getActivity();
        // for performance
        profile = mainActivity.findViewById(R.id.header);
        profile.setImageResource(R.drawable.profile);

        setOnClickListener(userWrapper, USER);
        setOnClickListener(passwordWrapper, PASSWORD);
        setOnClickListener(settingsWrapper, SETTINGS);
    }
    private void setOnClickListener(View view, final int type) {
        
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MainActivity mainActivity = (MainActivity) getActivity();
                final String getAccountUrl = Constants.url + Constants.getAccount;
                SharedPreferences spf = mainActivity.getSharedPreferences("login", mainActivity.MODE_PRIVATE);
                final String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
                Log.e(PREFIX + "sessionId = ", sessionId);
                OkHttpUtil.post(getAccountUrl, new FormBody.Builder().build(), sessionId, new Callback() {
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
                        final String responseText = response.body().string();
                        Log.e(PREFIX, "responseText = " +responseText);
                        Log.e(PREFIX + "code = ", String.valueOf(code));
                        if(code == OkHttpUtil.SUCCESS_CODE) {
                            switch(type) {
                                case USER: {
                                    mainActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(mainActivity, UserActivity.class);
                                            // TODO: Json Parse
                                            JSONObject json = JSONObject.parseObject(responseText);
                                            intent.putExtra("name", json.getString("Name"));
                                            intent.putExtra("office", json.getString("Department"));
                                            intent.putExtra("role", json.getString("Role"));
                                            intent.putExtra("email", json.getString("Email"));
                                            intent.putExtra("phone", json.getString("Phone"));
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                }
                                case PASSWORD: {
                                    break;
                                }
                                case SETTINGS: {
                                    mainActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(mainActivity, SettingsActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                }
                                default: {
                                    Log.e(PREFIX, "Wrong page type");
                                    break;
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
                    }
                });
            }
        });
    }
    private void init(View view) {
        userWrapper = view.findViewById(R.id.user_wrapper);
        passwordWrapper = view.findViewById(R.id.password_wrapper);
        settingsWrapper = view.findViewById(R.id.settings_wrapper);
        nameText = view.findViewById(R.id.name_text);
        officeText = view.findViewById(R.id.office_text);
        nameText.setText(name);
        officeText.setText(office);

    }
}
