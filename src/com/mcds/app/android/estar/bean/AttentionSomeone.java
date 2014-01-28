package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class AttentionSomeone implements IParseJson{

	public String my_id;
	public String the_id;
	public String attention_type;

	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

		this.my_id = JsonUtility.getString(jo, "my_id", "");
		this.the_id = JsonUtility.getString(jo, "the_id", "");
		this.attention_type = JsonUtility.getString(jo, "attention_type", "");
		return true;
	}
}
