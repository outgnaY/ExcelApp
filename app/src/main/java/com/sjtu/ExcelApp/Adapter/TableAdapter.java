package com.sjtu.ExcelApp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sjtu.ExcelApp.Model.TableItem;
import com.sjtu.ExcelApp.R;

import java.util.List;

public class TableAdapter extends ArrayAdapter<TableItem> {
    private int resourceId;
    public TableAdapter(Context context, int resourceId, List<TableItem> list) {
        super(context, resourceId, list);
        this.resourceId = resourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TableItem item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView project = (TextView) view.findViewById(R.id.table_item_project);
        TextView approval = (TextView) view.findViewById(R.id.table_item_approval);
        TextView subsidy = (TextView) view.findViewById(R.id.table_item_subsidy);
        project.setText(item.getProject());
        approval.setText(item.getApproval());
        subsidy.setText(item.getSubsidy());
        if(position % 2 == 1) {
            project.setBackgroundColor(Color.rgb(242, 242, 242));
            approval.setBackgroundColor(Color.rgb(242, 242, 242));
            subsidy.setBackgroundColor(Color.rgb(242, 242, 242));
        }
        return view;
    }
}