package com.mcds.app.android.estar.util;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtility {
	public static String getString(JSONObject jo, String key, String defaultValue) {
		String result = defaultValue;
		try {
			result = jo.isNull(key) ? defaultValue : jo.getString(key);
		} catch (JSONException e) {
			result = defaultValue;
			e.printStackTrace();
		}
		return result;
	}

	public static int getInteger(JSONObject jo, String key, int defaultValue) {
		int result = defaultValue;
		try {
			if (!jo.isNull(key)) {
				String r = jo.getString(key);
				result = Integer.valueOf(r);
			}
		} catch (Exception e) {
			result = defaultValue;
			e.printStackTrace();
		}
		return result;
	}

	public static double getDouble(JSONObject jo, String key, double defaultValue) {
		double result = defaultValue;
		try {
			if (!jo.isNull(key)) {
				String r = jo.getString(key);
				result = Double.valueOf(r);
			}
		} catch (Exception e) {
			result = defaultValue;
			e.printStackTrace();
		}
		return result;
	}

	public static Date getDate(JSONObject jo, String key, Date defaultValue) {
		Date result = defaultValue;
		try {
			if (!jo.isNull(key)) {
				long t = jo.getLong(key);
				result = new Date(t);
			}
		} catch (Exception e) {
			result = defaultValue;
			e.printStackTrace();
		}
		return result;
	}

	public static Date getPhpDate(JSONObject jo, String key, Date defaultValue) {
		Date result = defaultValue;
		try {
			if (!jo.isNull(key)) {
				long t = jo.getLong(key);
				t *= 1000;
				result = new Date(t);
			}
		} catch (Exception e) {
			result = defaultValue;
			e.printStackTrace();
		}
		return result;
	}

	public static JSONObject getJSONObject(JSONObject jo, String key) {
		JSONObject resultJo = null;

		try {
			String result = jo.isNull(key) ? "" : jo.getString(key);
			if (!(result.equals("") || result.toLowerCase().equals("null"))) {
				resultJo = new JSONObject(result);
			}
		} catch (JSONException e) {
			resultJo = null;
			e.printStackTrace();
		}

		return resultJo;
	}

	public static JSONArray getJSONArray(JSONObject jo, String key) {
		JSONArray resultJa = null;

		try {
			String result = jo.isNull(key) ? "" : jo.getString(key);
			if (!(result.equals("") || result.toLowerCase().equals("null"))) {
				resultJa = new JSONArray(result);
			}
		} catch (JSONException e) {
			resultJa = null;
			e.printStackTrace();
		}

		return resultJa;
	}
}
