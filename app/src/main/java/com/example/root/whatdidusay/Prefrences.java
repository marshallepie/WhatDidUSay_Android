package com.example.root.whatdidusay;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Prefrences {

	public static String KEY_PREFRENCE_INIT = "KEY_PREFRENCE_INIT";
	public static String KEY_RECORD_DURATION = "KEY_RECORD_DURATION";

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

		return prefs.getInt(key,0);
	}
	public Boolean getBoolean(String key) {

		return prefs.getBoolean(key, true);
	}

	public Boolean getContains(String key) {

		if (prefs.contains(key)) {
			return true;
		}

		return false;
	}
}
