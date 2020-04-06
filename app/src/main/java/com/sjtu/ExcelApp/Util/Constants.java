package com.sjtu.ExcelApp.Util;

import java.util.HashMap;

public class Constants {
    public static String url = "https://192.168.0.7:8088";
    public static String getAccount = "/api/getAccount";
    public static String setAccount = "/api/setAccount";
    public static String logout = "/api/logout";
    public static final int MOD_OK = 1;

    public static final int REQ_MOD_NAME = 1;
    public static final int REQ_MOD_OFFICE = 2;
    public static final int REQ_MOD_EMAIL = 3;
    public static final int REQ_MOD_PHONE = 4;

    public static final int MATHS = 1;
    public static final int CHEM = 2;
    public static final int LIFE = 3;
    public static final int GLOBE = 4;
    public static final int MATERIAL = 5;
    public static final int INFO = 6;
    public static final int MANAGE = 7;
    public static final int MEDICAL = 8;
    public static final int COOP = 9;
    public static final int MORE = 10;

    public static HashMap<Integer, String> map;

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
    }

}
