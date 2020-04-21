package com.sjtu.ExcelApp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sjtu.ExcelApp.Model.TableItem;
import com.sjtu.ExcelApp.R;

import org.w3c.dom.Text;

import java.util.List;

public class TableAdapter extends ArrayAdapter<TableItem> {
    private int resourceId;
    private Typeface typeface;
    public TableAdapter(Context context, int resourceId, List<TableItem> list) {
        super(context, resourceId, list);
        this.resourceId = resourceId;
        this.typeface = Typeface.createFromAsset(context.getAssets(), "DINAlternateBold.ttf");
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TableItem item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        RelativeLayout projectWrapper = (RelativeLayout) view.findViewById(R.id.project_wrapper);
        RelativeLayout approvalWrapper = (RelativeLayout) view.findViewById(R.id.approval_wrapper);
        RelativeLayout subsidyWrapper = (RelativeLayout) view.findViewById(R.id.subsidy_wrapper);
        // RelativeLayout limitWrapper = (RelativeLayout) view.findViewById(R.id.limit_wrapper);

        TextView project = (TextView) view.findViewById(R.id.table_item_project);
        TextView approval = (TextView) view.findViewById(R.id.table_item_approval);
        TextView subsidy = (TextView) view.findViewById(R.id.table_item_subsidy);
        // TextView limit = (TextView) view.findViewById(R.id.table_item_limit);
        project.setText(item.getProject());
        project.setTypeface(typeface);
        approval.setText(item.getApproval());
        approval.setTypeface(typeface);
        subsidy.setText(item.getSubsidy());
        subsidy.setTypeface(typeface);
        // limit.setText(item.getLimit());
        /*
        if(position % 2 == 1) {
            projectWrapper.setBackgroundColor(Color.rgb(242, 242, 242));
            approvalWrapper.setBackgroundColor(Color.rgb(242, 242, 242));
            subsidyWrapper.setBackgroundColor(Color.rgb(242, 242, 242));
            // limitWrapper.setBackgroundColor(Color.rgb(242, 242, 242));
        }
        */
        return view;
    }
}
