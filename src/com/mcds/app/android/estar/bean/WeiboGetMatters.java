package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class WeiboGetMatters implements IParseJson {
	public String item_title;
	public List<String> items;

	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

		items = new ArrayList<String>();
		
		this.item_title = JsonUtility.getString(jo, "item_title", "");
		try {
			JSONArray ja = JsonUtility.getJSONArray(jo, "items");
			if (ja != null) {
				for (int i = 0; i < ja.length(); i++) {
					JSONObject itemsJo;
				    itemsJo = ja.getJSONObject(i);
					items.add(JsonUtility.getString(itemsJo, "subtitle", ""));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return true;
	}
}
