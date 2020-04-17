package com.sjtu.ExcelApp.Model;

public class TableItem {
    private String project;
    private String approval;
    private String subsidy;
    private String limit;
    public TableItem(String project, String approval, String subsidy, String limit) {
        this.project = project;
        this.approval = approval;
        this.subsidy = subsidy;
        this.limit = limit;
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
    public String getLimit() {
        return limit;
    }
}
