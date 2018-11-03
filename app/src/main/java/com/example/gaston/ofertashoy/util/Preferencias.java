package com.example.gaston.ofertashoy.util;

import android.content.Context;
import android.preference.PreferenceManager;

public class Preferencias {
    private static final String KEY_USER = "user";

    public static String getKeyUser() {
        return KEY_USER;
    }
    public static String getString(Context context, final String key) {
        android.content.SharedPreferences shaPref = PreferenceManager.getDefaultSharedPreferences(context);
        return shaPref.getString(key, "");
    }

    public static void setString(Context context, final String key, final String value) {
        android.content.SharedPreferences shaPref = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = shaPref.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
