package com.elcom.utils;

import java.time.Year;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class StringUtil {

    public StringUtil() {
    }

    public static String printException(Exception ex) {
        return ex.getCause() != null ? ex.getCause().toString() : ex.toString();
    }

    public static boolean isNumberic(String sNumber) {
        if (sNumber != null && !"".equals(sNumber)) {
            char ch_max = 57;
            char ch_min = 48;

            for(int i = 0; i < sNumber.length(); ++i) {
                char ch = sNumber.charAt(i);
                if (ch < ch_min || ch > ch_max) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    public static boolean isUUID(String string) {
        if (isNullOrEmpty(string)) {
            return false;
        } else {
            try {
                UUID.fromString(string);
                return true;
            } catch (Exception var2) {
                return false;
            }
        }
    }

    public static String generateMapString(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        Iterator iterator = map.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)iterator.next();
            builder.append((String)entry.getKey()).append("=").append((String)entry.getValue()).append("&");
        }

        String result = builder.toString();
        return result != null && result.endsWith("&") ? result.substring(0, result.length() - 1) : result;
    }

    public static String randomUUID(){
        Random random = new Random();
        int r = 100000 + random.nextInt(900000);
        int year = Year.now().getValue();
        return year + "" + r;
    }
}
