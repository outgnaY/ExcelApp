package com.sjtu.ExcelApp;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjtu.ExcelApp.Customize.FontIconView;


public class MainActivity extends AppCompatActivity {
    private LinearLayout homepage;
    private LinearLayout history;
    private LinearLayout my;
    private FontIconView homepageIcon;
    private FontIconView historyIcon;
    private FontIconView myIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void replaceView(View oldView, View newView) {
        ViewGroup par = (ViewGroup) oldView.getParent();
        if(par == null) return;
        int oldIndex = par.indexOfChild(oldView);
        par.removeViewAt(oldIndex);
        par.addView(newView, oldIndex);
    }
    private void init() {
        homepage = findViewById(R.id.homepage);
        history = findViewById(R.id.history);
        my = findViewById(R.id.my);
        homepageIcon = findViewById(R.id.homepage_icon);
        historyIcon = findViewById(R.id.history_icon);
        myIcon = findViewById(R.id.my_icon);
        // init
        replaceFragment(new HomePageFragment());
        homepageIcon.setText(R.string.shouyexuanzhong);

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new HomePageFragment());
                homepageIcon.setText(R.string.shouyexuanzhong);
                historyIcon.setText(R.string.liebiao);
                myIcon.setText(R.string.wode);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new HistoryFragment());
                homepageIcon.setText(R.string.zhuye);
                historyIcon.setText(R.string.liebiaoxuanzhong);
                myIcon.setText(R.string.wode);

            }
        });
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new MyFragment());
                homepageIcon.setText(R.string.zhuye);
                historyIcon.setText(R.string.liebiao);
                myIcon.setText(R.string.wodexuanzhong);
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
