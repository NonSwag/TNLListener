package net.nonswag.tnl.listener.utils;

import java.util.Random;

public class StringUtil {

    private static String getValidChars() {
        return "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
    }

    public static String random(int length) {
        StringBuilder randomString = new StringBuilder();
        Random randomInt = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = getValidChars().charAt(randomInt.nextInt(getValidChars().length()));
        }
        for (char c : text) {
            randomString.append(c);
        }
        return randomString.toString();
    }
}
