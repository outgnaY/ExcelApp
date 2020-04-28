package com.sjtu.ExcelApp.Model;

public class TableItem {
    private String project;
    private int approval;
    private double subsidy;
    private double quotaProp;
    private double quota;
    private double executedProp;

    public TableItem(String project, int approval, double subsidy, double quotaProp, double quota, double executedProp) {
        this.project = project;
        this.approval = approval;
        this.subsidy = subsidy;
        this.quotaProp = quotaProp;
        this.quota = quota;
        this.executedProp = executedProp;
    }
    public String getProject() {
        return project;
    }
    public int getApproval() {
        return approval;
    }
    public double getSubsidy() {
        return subsidy;
    }
    public double getQuotaProp() {
        return quotaProp;
    }
    public void setQuotaProp(double quotaProp) {
        this.quotaProp = quotaProp;
    }
    public double getQuota() {
        return quota;
    }
    public void setQuota(double quota) {
        this.quota = quota;
    }
    public double getExecutedProp() {
        return executedProp;
    }
    public void setExecutedProp(double executedProp) {
        this.executedProp = executedProp;
    }

}
