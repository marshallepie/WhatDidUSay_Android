package com.marshallepie.root.whatdidusay.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Created by dottechnologies on 2/9/15.
 * This class is to store,retrieve values for timer  .
 */

public class Prefrences {

    public static String KEY_PREFRENCE_INIT = "KEY_PREFRENCE_INIT";
    public static String KEY_RECORD_DURATION = "KEY_RECORD_DURATION";
    public static String KEY_RECORD_ID = "KEY_RECORD_ID";
    public static String KEY_IN_APP = "KEY_IN_APP";
    public static String KEY_IN_APP_50 = "KEY_IN_APP_50";
    public static String KEY_IN_APP_200 = "KEY_IN_APP_200";
    public static String KEY_IN_APP_UNLIMITED = "KEY_IN_APP_UNLIMITED";
    public static String KEY_FIRST_TIME_DILAOG = "KEY_FIRST_TIME";

    public static String ACCESS_KEY_NAME = "ACCESS_KEY";
    public static String ACCESS_SECRET_NAME = "ACCESS_SECRET";

    private SharedPreferences prefs;
    private Editor edit;

    public Prefrences(Context context) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public void setStringPrefs(String key, String value) {
        edit = prefs.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public void setIntPrefs(String key, int value) {
        edit = prefs.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public void setBooleanPrefs(String key, Boolean value) {
        edit = prefs.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public String getString(String key) {

        return prefs.getString(key, "");
    }

    public int getInt(String key) {

        return prefs.getInt(key, 0);
    }

    public Boolean getBoolean(String key) {

        return prefs.getBoolean(key, true);
    }
    public Boolean getBooleanDefaultFalse(String key) {

        return prefs.getBoolean(key, false);
    }

    public Boolean getContains(String key) {

        if (prefs.contains(key)) {
            return true;
        }

        return false;
    }
}
