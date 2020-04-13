package com.sjtu.ExcelApp.Util;

import java.util.HashMap;

public class Constants {
    public static String url = "https://192.168.0.7:8088";
    public static String getAccount = "/api/getAccount";
    public static String setAccount = "/api/setAccount";
    public static String logout = "/api/logout";
    public static String getOverallInfo = "/api/getOverallInfo";
    public static String getDeptProjectsInfo = "/api/getDeptProjectsInfo";
    public static String getProjectsInfo = "/api/getProjectsInfo";
    public static final int MOD_OK = 1;

    public static final int REQ_MOD_NAME = 1;
    public static final int REQ_MOD_OFFICE = 2;
    public static final int REQ_MOD_EMAIL = 3;
    public static final int REQ_MOD_PHONE = 4;

    public static final int MATHS = 10;
    public static final int CHEM = 11;
    public static final int LIFE = 12;
    public static final int GLOBE = 13;
    public static final int MATERIAL = 14;
    public static final int INFO =15;
    public static final int MANAGE = 16;
    public static final int MEDICAL = 17;
    public static final int COOP = 18;
    public static final int MORE = 19;

    public static final String AUTH_NORMAL = "Normal";
    public static final String AUTH_ADMIN = "Supervisor";

    public static HashMap<Integer, String> map;
    public static HashMap<Integer, String> departmentNameMap;

    static {
        map = new HashMap<>();
        map.put(MATHS, "数理");
        map.put(CHEM, "化学");
        map.put(LIFE, "生命");
        map.put(GLOBE, "地球");
        map.put(MATERIAL, "工材");
        map.put(INFO, "信息");
        map.put(MANAGE, "管理");
        map.put(MEDICAL, "医学");
        map.put(COOP, "合作局");

        departmentNameMap = new HashMap<>();
        departmentNameMap.put(MATHS, "数学物理科学部");
        departmentNameMap.put(CHEM, "化学科学部");
        departmentNameMap.put(LIFE, "生命科学部");
        departmentNameMap.put(GLOBE, "地球科学部");
        departmentNameMap.put(MATERIAL, "工程与材料科学部");
        departmentNameMap.put(INFO, "信息科学部");
        departmentNameMap.put(MANAGE, "管理科学部");
        departmentNameMap.put(MEDICAL, "医学科学部");
        departmentNameMap.put(COOP, "国际合作局");
    }

}
