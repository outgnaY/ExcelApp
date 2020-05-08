package com.sjtu.ExcelApp.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtu.ExcelApp.R;

public class HistoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);
        new AlertDialog.Builder(getActivity())
                .setTitle("提示")
                .setMessage("历史数据功能暂未开放")
                .setPositiveButton("确定", null)
                .show();
        return view;
    }
}
