package com.mcds.app.android.estar.provider;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class PersistenceProvider {
	public static final String RUN_FIRST = "run_first";
	public static final String USER_NAME = "username";
	public static final String PASSWORD = "password";

	private static SharedPreferences preferences;

	public static void init(Application app) {
		preferences = app.getSharedPreferences("estar", Context.MODE_PRIVATE);
	}

	public static void put(String key, Object value) {
		SharedPreferences.Editor editor = preferences.edit();

		if (value.getClass() == String.class) {
			editor.putString(key, (String) value);
		} else if (value.getClass() == Integer.class) {
			editor.putInt(key, ((Integer) value).intValue());
		} else if (value.getClass() == Float.class) {
			editor.putFloat(key, ((Float) value).floatValue());
		} else if (value.getClass() == Long.class) {
			editor.putLong(key, ((Long) value).longValue());
		} else if (value.getClass() == Boolean.class) {
			editor.putBoolean(key, (Boolean) value);
		}

		editor.commit();
	}

	public static String getString(String key, String defValue) {
		return preferences.getString(key, defValue);
	}

	public static int getInt(String key, int defValue) {
		return preferences.getInt(key, defValue);
	}

	public static float getFloat(String key, float defValue) {
		return preferences.getFloat(key, defValue);
	}

	public static long getLong(String key, long defValue) {
		return preferences.getLong(key, defValue);
	}

	public static boolean getBoolean(String key, boolean defValue) {
		return preferences.getBoolean(key, defValue);
	}
}
