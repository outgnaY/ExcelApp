package com.sjtu.ExcelApp.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sjtu.ExcelApp.Customize.FontIconView;
import com.sjtu.ExcelApp.Fragment.DepartmentFragment;
import com.sjtu.ExcelApp.Fragment.HistoryFragment;
import com.sjtu.ExcelApp.Fragment.HomePageFragment;
import com.sjtu.ExcelApp.Fragment.MyFragment;
import com.sjtu.ExcelApp.R;
import com.sjtu.ExcelApp.Util.Constants;
import com.sjtu.ExcelApp.Util.OkHttpUtil;
import com.sjtu.ExcelApp.Util.SharedPreferenceUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private LinearLayout homepage;
    private LinearLayout history;
    private LinearLayout my;
    private FontIconView homepageIcon;
    private FontIconView historyIcon;
    private FontIconView myIcon;
    private SharedPreferences spf;
    private String PREFIX = "[MainActivity]";
    private String authority;

    // private String sessionId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spf = super.getSharedPreferences("login", MODE_PRIVATE);
        String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
        Log.e(PREFIX, "sessionId = " + sessionId);
        if(sessionId.length() == 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            String requestUrl = Constants.url + Constants.getAccount;
            Log.e(PREFIX, "requestUrl = " + requestUrl);

            OkHttpUtil.post(requestUrl, new FormBody.Builder().build(), sessionId, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int code = response.code();
                    Log.e(PREFIX, "code = " + String.valueOf(code));
                    String responseText = response.body().string();
                    if(code == OkHttpUtil.SUCCESS_CODE) {
                        JSONObject json = JSONObject.parseObject(responseText);
                        JSONObject objT = json.getJSONObject("ObjT");
                        Log.e(PREFIX, objT.getString("Role"));
                        int retCode = json.getIntValue("Code");
                        if(retCode == 0) {
                            authority = objT.getString("Role");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    init();
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
                @Override
                public void onFailure(Call call, final IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(PREFIX, e.toString());
                            Toast.makeText(MainActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

            });
        }
    }
    private void replaceView(View oldView, View newView) {
        ViewGroup par = (ViewGroup) oldView.getParent();
        if(par == null) return;
        int oldIndex = par.indexOfChild(oldView);
        par.removeViewAt(oldIndex);
        par.addView(newView, oldIndex);
    }
    private void init() {
        // init authority level
        // authority = Constants.AUTH_ADMIN;
        homepage = findViewById(R.id.homepage);
        history = findViewById(R.id.history);
        my = findViewById(R.id.my);
        homepageIcon = findViewById(R.id.homepage_icon);
        historyIcon = findViewById(R.id.history_icon);
        myIcon = findViewById(R.id.my_icon);
        // init
        if(authority.equals(Constants.AUTH_ADMIN)) {
            replaceFragment(new HomePageFragment());
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putInt("key", Constants.MATHS);
            DepartmentFragment departmentFragment = new DepartmentFragment();
            departmentFragment.setArguments(bundle);
            replaceFragment(departmentFragment);
        }
        homepageIcon.setText(R.string.shouyexuanzhong);
        final String getAccountUrl = Constants.url + Constants.getAccount;
        SharedPreferences spf = getSharedPreferences("login", MODE_PRIVATE);
        final String sessionId = SharedPreferenceUtil.getString(spf, "sessionId", "");
        Log.e(PREFIX, "sessionId = " + sessionId);
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtil.post(getAccountUrl, new FormBody.Builder().build(), sessionId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int code = response.code();
                        Log.e(PREFIX, "code = " + String.valueOf(code));
                        if(code == OkHttpUtil.SUCCESS_CODE) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(authority.equals(Constants.AUTH_ADMIN)) {
                                        replaceFragment(new HomePageFragment());
                                    }
                                    else {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("key", Constants.MATHS);
                                        DepartmentFragment departmentFragment = new DepartmentFragment();
                                        departmentFragment.setArguments(bundle);
                                        replaceFragment(departmentFragment);
                                    }
                                    homepageIcon.setText(R.string.shouyexuanzhong);
                                    historyIcon.setText(R.string.liebiao);
                                    myIcon.setText(R.string.wode);
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                    }
                });


            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtil.post(getAccountUrl, new FormBody.Builder().build(), sessionId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int code = response.code();
                        Log.e(PREFIX, "code = " + String.valueOf(code));
                        if(code == OkHttpUtil.SUCCESS_CODE) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    replaceFragment(new HistoryFragment());
                                    homepageIcon.setText(R.string.zhuye);
                                    historyIcon.setText(R.string.liebiaoxuanzhong);
                                    myIcon.setText(R.string.wode);
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                    }
                });
            }
        });
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpUtil.post(getAccountUrl, new FormBody.Builder().build(), sessionId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int code = response.code();
                        Log.e(PREFIX, "code = " + String.valueOf(code));
                        if(code == OkHttpUtil.SUCCESS_CODE) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyFragment myFragment = new MyFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", "沈耀");
                                    bundle.putString("office", "基金委");
                                    myFragment.setArguments(bundle);
                                    replaceFragment(myFragment);
                                    homepageIcon.setText(R.string.zhuye);
                                    historyIcon.setText(R.string.liebiao);
                                    myIcon.setText(R.string.wodexuanzhong);
                                }
                            });
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                    }
                });


            }
        });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_content, fragment);
        transaction.commit();
    }

}
