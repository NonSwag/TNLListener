package net.nonswag.tnl.listener.utils;

import java.util.Random;

/*******************************************************
 * Copyright (C) 2019-2023 NonSwag kirschnerdavid2466@gmail.com
 *
 * This file is part of TNLListener and was created at the 10/31/20
 *
 * TNLListener can not be copied and/or distributed without the express
 * permission of the owner.
 *
 *******************************************************/

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
