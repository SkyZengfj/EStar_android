package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class My_GetReminds  implements IParseJson{
	public String at_num;
	public String commit_num;
	public String activity_num;
	public String system_num;
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null ) {
			return false;
		}
		
		this.at_num = JsonUtility.getString(jo, "at_num", "");
		this.commit_num = JsonUtility.getString(jo, "commit_num", "");
		this.activity_num = JsonUtility.getString(jo, "activity_num", "");
		this.system_num = JsonUtility.getString(jo, "system_num", "");
		return true;
	}

}
