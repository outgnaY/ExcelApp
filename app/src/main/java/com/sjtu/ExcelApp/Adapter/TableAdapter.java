package com.sjtu.ExcelApp.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sjtu.ExcelApp.Customize.FontIconView;
import com.sjtu.ExcelApp.Customize.LinearProgress;
import com.sjtu.ExcelApp.Customize.SimpleCircleProgress;
import com.sjtu.ExcelApp.Model.TableItem;
import com.sjtu.ExcelApp.R;
import java.util.List;

public class TableAdapter extends ArrayAdapter<TableItem> {
    private String PREFIX = "[TableAdapter]";
    private int resourceId;
    private Typeface scMedium;
    private Typeface scRegular;
    private Typeface numMedium;
    private Typeface numRegular;
    private List<Boolean> status;
    public TableAdapter(Context context, int resourceId, List<TableItem> list) {
        super(context, resourceId, list);
        this.resourceId = resourceId;
        scMedium = Typeface.createFromAsset(getContext().getAssets(), "NotoSansSC-Medium.ttf");
        scRegular = Typeface.createFromAsset(getContext().getAssets(), "NotoSansSC-Regular.ttf");
        numMedium = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Medium.ttf");
        numRegular = Typeface.createFromAsset(getContext().getAssets(), "Roboto-Regular.ttf");
    }
    public TableAdapter(Context context, int resourceId, List<TableItem> list, List<Boolean> status) {
        this(context, resourceId, list);
        this.status = status;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e(PREFIX, String.valueOf(position));
        TableItem item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        RelativeLayout projectWrapper = (RelativeLayout) view.findViewById(R.id.project_wrapper);
        RelativeLayout approvalWrapper = (RelativeLayout) view.findViewById(R.id.approval_wrapper);
        RelativeLayout subsidyWrapper = (RelativeLayout) view.findViewById(R.id.subsidy_wrapper);

        TextView project = (TextView) view.findViewById(R.id.table_item_project);
        TextView approval = (TextView) view.findViewById(R.id.table_item_approval);
        TextView subsidy = (TextView) view.findViewById(R.id.table_item_subsidy);
        TextView quotaProp = (TextView) view.findViewById(R.id.quota_prop);
        TextView quotaVal = (TextView) view.findViewById(R.id.quota_val);
        TextView executedProp = (TextView) view.findViewById(R.id.executed_prop);
        TextView quotaPropTitle = (TextView) view.findViewById(R.id.quota_prop_title);
        TextView quotaValTitle = (TextView) view.findViewById(R.id.quota_val_title);
        TextView executedPropTitle = (TextView) view.findViewById(R.id.executed_prop_title);
        SimpleCircleProgress simpleCircleProgress = view.findViewById(R.id.prop);
        LinearProgress executedPropLinear = view.findViewById(R.id.executed_prop_linear);
        if(item.getExecutedProp() < 100) {
            executedPropLinear.setProgress((float) item.getExecutedProp());
        }
        else {
            executedPropLinear.setProgress(100);
        }
        simpleCircleProgress.setProgress((float) item.getQuotaProp());
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
        if(this.status != null) {
            LinearLayout itemDetailed = view.findViewById(R.id.item_detailed);
            FontIconView icon = view.findViewById(R.id.solid_arrow);
            if(this.status.get(position)) {
                itemDetailed.setVisibility(View.VISIBLE);
                icon.setText(R.string.solid_arrow_down);
                status.set(position, true);
            }
            else {
                itemDetailed.setVisibility(View.GONE);
                icon.setText(R.string.solid_arrow_right);
                status.set(position, false);
            }
        }
        return view;
    }
}
