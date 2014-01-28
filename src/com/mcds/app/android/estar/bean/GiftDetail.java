package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class GiftDetail implements IParseJson {

	public String product_title;
	public List<String> imgs;
	public String integral;
	public String stock;
	public String code;

	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

		this.product_title = JsonUtility.getString(jo, "product_title", "");
//		img = JsonUtility.getString(jo, "img", "");
		integral = JsonUtility.getString(jo, "ntegral", "0");
		stock = JsonUtility.getString(jo, "stock", "0");
		code = JsonUtility.getString(jo, "code", "");

		imgs = new ArrayList<String>();
		JSONArray ja = JsonUtility.getJSONArray(jo, "imgs");
		if (ja != null) {
			for (int i = 0; i < ja.length(); i++) {
				try {
					imgs.add(ja.getString(i));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		return true;
	}
}
