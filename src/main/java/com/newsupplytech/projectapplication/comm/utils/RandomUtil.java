package com.newsupplytech.projectapplication.comm.utils;



import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
    public static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public RandomUtil() {
    }

    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }
    public static String randomString(String baseString, int length) {
        StringBuilder sb = new StringBuilder();
        if (length < 1) {
            length = 1;
        }

        int baseLength = baseString.length();

        for(int i = 0; i < length; ++i) {
            int number = getRandom().nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(randomString(BASE_CHAR,3));
    }
}
