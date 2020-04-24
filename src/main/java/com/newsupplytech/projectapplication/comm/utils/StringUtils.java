package com.newsupplytech.projectapplication.comm.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 下划线转驼峰 ,大写自动转小写<br />
     * 例如：USER_NAME->userName<br />
     * USERNAME->username<br />
     * USER_NAME->userName<br />
     * fParentNoLeader->fParentNoLeader
     *
     */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = Pattern.compile("_(\\w)").matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线,大写自动转小写<br />
     * 例如： USER_NAME->user_name USERNAME->userName<br />
     * f_parent_no_leader->f_parent_no_leader
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(humpToLine("USERNAME"));// userName
        System.out.println(lineToHump("user_name"));// userName
        System.out.println(humpToLine("userName"));// userName
    }

    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        } else {
            if (o instanceof String) {
                if (o.toString().trim().equals("")) {
                    return true;
                }
            } else if (o instanceof List) {
                if (((List)o).size() == 0) {
                    return true;
                }
            } else if (o instanceof Map) {
                if (((Map)o).size() == 0) {
                    return true;
                }
            } else if (o instanceof Set) {
                if (((Set)o).size() == 0) {
                    return true;
                }
            } else if (o instanceof Object[]) {
                if (((Object[])((Object[])o)).length == 0) {
                    return true;
                }
            } else if (o instanceof int[]) {
                if (((int[])((int[])o)).length == 0) {
                    return true;
                }
            } else if (o instanceof long[] && ((long[])((long[])o)).length == 0) {
                return true;
            }

            return false;
        }
    }
}
