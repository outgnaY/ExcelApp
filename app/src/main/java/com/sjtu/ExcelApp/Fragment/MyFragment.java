package com.sjtu.ExcelApp.Fragment;

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

import com.alibaba.fastjson.JSONObject;
import com.sjtu.ExcelApp.Activity.LoginActivity;
import com.sjtu.ExcelApp.Activity.MainActivity;
import com.sjtu.ExcelApp.Activity.ModifyEmailActivity;
import com.sjtu.ExcelApp.Activity.ModifyPasswordActivity;
import com.sjtu.ExcelApp.Activity.ModifyPhoneActivity;
import com.sjtu.ExcelApp.Activity.SettingsActivity;
import com.sjtu.ExcelApp.Activity.UserActivity;
import com.sjtu.ExcelApp.R;
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
    private View mobileWrapper;
    private View emailWrapper;
    private View passwordWrapper;
    private TextView nameText;
    private TextView officeText;
    private View view;
    private String PREFIX = "[MyFragment]";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    private String name;
    private String office;
    private String email;
    private String phone;
    private String role;

    private final int USER = 1;
    private final int MOBILE = 2;
    private final int EMAIL = 3;
    private final int PASSWORD = 4;
    @Override
    public void onResume() {
        Log.e(PREFIX, "resume");
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my, container, false);
        Bundle bundle = this.getArguments();
        this.name = bundle.getString("name");
        this.office = bundle.getString("office");
        Log.e(PREFIX, "name = " + name);
        Log.e(PREFIX, "office = " + office);
        this.view = view;
        init(view);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MainActivity mainActivity = (MainActivity) getActivity();
        // for performance
        // profile = mainActivity.findViewById(R.id.header);
        // profile.setImageResource(R.drawable.profile);

        setOnClickListener(userWrapper, USER);
        setOnClickListener(mobileWrapper, MOBILE);
        setOnClickListener(emailWrapper, EMAIL);
        setOnClickListener(passwordWrapper, PASSWORD);

    }
    private void setOnClickListener(View view, final int type) {
        
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int code = response.code();
                        final String responseText = response.body().string();
                        Log.e(PREFIX, "responseText = " +responseText);
                        Log.e(PREFIX, "code = " + String.valueOf(code));
                        JSONObject json = JSONObject.parseObject(responseText);
                        JSONObject objT = json.getJSONObject("ObjT");
                        name = objT.getString("Name");
                        office = objT.getString("Department");
                        role = objT.getString("Role");
                        email = objT.getString("Email");
                        phone = objT.getString("Phone");
                        if(code == OkHttpUtil.SUCCESS_CODE) {
                            switch(type) {
                                case USER: {
                                    mainActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(mainActivity, UserActivity.class);
                                            intent.putExtra("name", name);
                                            intent.putExtra("office", office);
                                            intent.putExtra("role", role);
                                            intent.putExtra("email", email);
                                            intent.putExtra("phone", phone);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                }
                                case PASSWORD: {
                                    mainActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(mainActivity, ModifyPasswordActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                }
                                case EMAIL: {
                                    mainActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(mainActivity, ModifyEmailActivity.class);
                                            intent.putExtra("email", email);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                }
                                case MOBILE: {
                                    mainActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(mainActivity, ModifyPhoneActivity.class);
                                            intent.putExtra("phone", phone);
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
                                }
                            });
                        }
                        response.close();
                    }
                });
            }
        });
    }

    private void init(View view) {
        userWrapper = view.findViewById(R.id.user_wrapper);
        mobileWrapper = view.findViewById(R.id.mobile_wrapper);
        emailWrapper = view.findViewById(R.id.email_wrapper);
        passwordWrapper = view.findViewById(R.id.password_wrapper);

        nameText = view.findViewById(R.id.name_text);
        officeText = view.findViewById(R.id.office_text);
        nameText.setText(name);
        officeText.setText(office);
    }
}
