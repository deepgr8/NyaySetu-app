package com.example.nyaysetu;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private static final String user_info = "User_Info";

    public static SharedPreferences getSharedstore(Context context) {
        return context.getSharedPreferences(user_info, Context.MODE_PRIVATE);
    }

    // Method to save a boolean value to SharedPreferences
    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = getSharedstore(context).edit();
        editor.putBoolean(key, value);
        editor.apply();
        editor.commit();
    }

    public static void saveStringInfo(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedstore(context).edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    // Method to retrieve a boolean value from SharedPreferences
    public static boolean getBoolean(Context context, String key, boolean s1) {
        return getSharedstore(context).getBoolean(key, s1);
    }
    public static String getString(Context context, String key,String s1) {
        return getSharedstore(context).getString(key,s1);
    }
}
