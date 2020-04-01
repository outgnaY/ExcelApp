package com.sjtu.ExcelApp.Util;

import android.content.SharedPreferences;

public class SharedPreferenceUtil {
    public static void putString(SharedPreferences spf, String key, String value) {
        SharedPreferences.Editor editor = spf.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getString(SharedPreferences spf, String key, String defaultValue) {
        String info = spf.getString(key, defaultValue);
        return info;
    }
}
