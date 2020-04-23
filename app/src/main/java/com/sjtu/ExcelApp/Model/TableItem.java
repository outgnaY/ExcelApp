package com.sjtu.ExcelApp.Model;

public class TableItem {
    private String project;
    private int approval;
    private double subsidy;
    private double quotaProp;
    private double fundingProp;
    private double executedProp;

    public TableItem(String project, int approval, double subsidy, double quotaProp, double fundingProp, double executedProp) {
        this.project = project;
        this.approval = approval;
        this.subsidy = subsidy;
        this.quotaProp = quotaProp;
        this.fundingProp = fundingProp;
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
    public double getFundingProp() {
        return fundingProp;
    }
    public void setFundingProp(double fundingProp) {
        this.fundingProp = fundingProp;
    }
    public double getExecutedProp() {
        return executedProp;
    }
    public void setExecutedProp(double executedProp) {
        this.executedProp = executedProp;
    }

}
