package com.sjtu.ExcelApp.Util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    public static Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(PropertiesUtil.class.getResourceAsStream("/assets/appConfig"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
