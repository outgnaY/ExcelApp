package com.sjtu.ExcelApp.Util;

import com.sjtu.ExcelApp.R;

import java.util.HashMap;

public class Constants {
    public static String url = "https://192.168.0.7:8088";
    // public static String url = "https://yshenlab.mynetgear.com:15691";
    public static String getAccount = "/api/getAccount";
    public static String setAccount = "/api/setAccount";
    public static String logout = "/api/logout";
    public static String getOverallInfo = "/api/getOverallInfo";
    public static String getDeptProjectsInfo = "/api/getDeptProjectsInfo";
    public static String getProjectsInfo = "/api/getProjectsInfo";
    public static String getPdf = "/2020plan";
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

    public static final String AUTH_NORMAL = "Normal";
    public static final String AUTH_ADMIN = "Supervisor";

    public static HashMap<Integer, String> map;
    public static HashMap<Integer, String> departmentNameMap;
    public static HashMap<Integer, Integer[]> srcMap;

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

        departmentNameMap = new HashMap<>();
        departmentNameMap.put(MATHS, "数学物理科学部");
        departmentNameMap.put(CHEM, "化学科学部");
        departmentNameMap.put(LIFE, "生命科学部");
        departmentNameMap.put(GLOBE, "地球科学部");
        departmentNameMap.put(MATERIAL, "工程与材料科学部");
        departmentNameMap.put(INFO, "信息科学部");
        departmentNameMap.put(MANAGE, "管理科学部");
        departmentNameMap.put(MEDICAL, "医学科学部");

        srcMap = new HashMap<>();
        srcMap.put(MATHS, new Integer[]{R.drawable.sl1, R.drawable.sl2, R.id.maths_src});
        srcMap.put(CHEM, new Integer[]{R.drawable.hx1, R.drawable.hx2, R.id.chem_src});
        srcMap.put(LIFE, new Integer[]{R.drawable.sm1, R.drawable.sm2, R.id.life_src});
        srcMap.put(GLOBE, new Integer[]{R.drawable.dq1, R.drawable.dq2, R.id.globe_src});
        srcMap.put(MATERIAL, new Integer[]{R.drawable.gc1, R.drawable.gc2, R.id.material_src});
        srcMap.put(INFO, new Integer[]{R.drawable.xx1, R.drawable.xx2, R.id.info_src});
        srcMap.put(MANAGE, new Integer[]{R.drawable.gl1, R.drawable.gl2, R.id.manage_src});
        srcMap.put(MEDICAL, new Integer[]{R.drawable.yx1, R.drawable.yx2, R.id.medical_src});

    }

}
