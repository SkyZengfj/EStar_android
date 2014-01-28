package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class AllContactList implements IParseJson {
	public String uid;
	public String user_name;
	public String first_char;
	public String user_sex;
	public String user_work;
	public String user_img;
	public String attention_flag;
	public String isSelected = "0";

	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if(jo == null) {
			return false;
		}
		
		this.uid = JsonUtility.getString(jo, "uid", "");
		this.user_name = JsonUtility.getString(jo, "user_name", "");
		this.first_char = JsonUtility.getString(jo, "first_char", "");
		this.user_sex = JsonUtility.getString(jo, "user_sex", "");
		this.user_work = JsonUtility.getString(jo, "user_work", "");
		this.user_img = JsonUtility.getString(jo, "user_img", "");
		this.attention_flag = JsonUtility.getString(jo, "attention_flag", "");
		
		return true;
	}

}
