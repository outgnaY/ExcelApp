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

import com.sjtu.ExcelApp.Customize.CircleProgress;
import com.sjtu.ExcelApp.Customize.LinearProgress;
import com.sjtu.ExcelApp.Model.TableItem;
import com.sjtu.ExcelApp.R;

import org.w3c.dom.Text;

import java.util.List;

public class TableAdapter extends ArrayAdapter<TableItem> {
    private int resourceId;
    private Typeface scMedium;
    private Typeface scRegular;
    private Typeface numMedium;
    private Typeface numRegular;
    public TableAdapter(Context context, int resourceId, List<TableItem> list) {
        super(context, resourceId, list);
        this.resourceId = resourceId;
        scMedium = Typeface.createFromAsset(getContext().getAssets(), "NotoSansSC-Medium.ttf");
        scRegular = Typeface.createFromAsset(getContext().getAssets(), "NotoSansSC-Regular.ttf");
        numMedium = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Medium.ttf");
        numRegular = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Regular.ttf");
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
        TextView quotaProp = (TextView) view.findViewById(R.id.quota_prop);
        TextView quotaVal = (TextView) view.findViewById(R.id.quota_val);
        TextView executedProp = (TextView) view.findViewById(R.id.executed_prop);
        TextView quotaPropTitle = (TextView) view.findViewById(R.id.quota_prop_title);
        TextView quotaValTitle = (TextView) view.findViewById(R.id.quota_val_title);
        TextView executedPropTitle = (TextView) view.findViewById(R.id.executed_prop_title);
        // CircleProgress quotaPropCircle = view.findViewById(R.id.quota_prop_circle);
        // CircleProgress fundingPropCircle = view.findViewById(R.id.funding_prop_circle);
        LinearProgress executedPropLinear = view.findViewById(R.id.executed_prop_linear);
        // quotaPropCircle.setProgress(item.);
        // quotaPropCircle.setProgress((float) item.getQuotaProp());
        // fundingPropCircle.setProgress((float) item.getFundingProp());
        executedPropLinear.setProgress((float) item.getExecutedProp());

        project.setText(item.getProject());
        project.setTypeface(scRegular);
        approval.setText(String.format("%d", item.getApproval()));
        approval.setTypeface(numRegular);
        subsidy.setText(String.format("%.2f", item.getSubsidy()));
        subsidy.setTypeface(numRegular);

        quotaProp.setText(String.format("%d%%", (int)item.getQuotaProp()));
        quotaProp.setTypeface(numMedium);
        quotaPropTitle.setTypeface(scRegular);
        quotaVal.setText(String.format("%.2f", item.getQuota()));
        quotaVal.setTypeface(numMedium);
        quotaValTitle.setTypeface(scRegular);
        executedProp.setText(String.format("%d%%", (int)item.getExecutedProp()));
        executedProp.setTypeface(numMedium);
        executedPropTitle.setTypeface(scRegular);
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
