package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class GiftCategory implements IParseJson {

	public String id;
	public String name;

	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}
		
		this.id = JsonUtility.getString(jo, "pk", "");
		this.name = JsonUtility.getString(jo, "name", "");

		return true;
	}

}
