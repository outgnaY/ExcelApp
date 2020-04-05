package com.sjtu.ExcelApp.Model;

public class TableItem {
    private String project;
    private String approval;
    private String subsidy;
    public TableItem(String project, String approval, String subsidy) {
        this.project = project;
        this.approval = approval;
        this.subsidy = subsidy;
    }
    public String getProject() {
        return project;
    }
    public String getApproval() {
        return approval;
    }
    public String getSubsidy() {
        return subsidy;
    }
}
