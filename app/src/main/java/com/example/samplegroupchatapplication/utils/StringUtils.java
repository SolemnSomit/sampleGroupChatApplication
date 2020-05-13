package com.example.samplegroupchatapplication.utils;

import android.util.Patterns;

public class StringUtils {
    /**
     * Utility to check if a string is empty
     * @param testString string to test
     * @return true if testString is empty else false
     */
    public static boolean isEmpty(String testString)
    {
        return testString.equals("");
    }

    /**
     * Function to check if the string is a valid email id
     * @param testString string to test
     * @return true if valid email else false
     */
    public static boolean isValidEmailID(String testString)
    {
        return !Patterns.EMAIL_ADDRESS.matcher(testString.trim()).matches();
    }
}
