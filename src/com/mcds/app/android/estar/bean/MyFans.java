package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class MyFans implements IParseJson{

	public String user_name;
	public String user_sex;
	public String user_work;
	public String user_img;
	public String attention_flag;
	public String uid;
	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

		this.user_name = JsonUtility.getString(jo, "user_name", "");
		this.user_sex = JsonUtility.getString(jo, "user_sex", "");
		this.user_work = Utility.decodeUnicode(JsonUtility.getString(jo, "user_work", ""));
		this.user_img = JsonUtility.getString(jo, "user_img", "");
		this.attention_flag = JsonUtility.getString(jo, "attention_flag", "");
		this.uid = JsonUtility.getString(jo, "uid", "");
		return true;
	}
	
}
