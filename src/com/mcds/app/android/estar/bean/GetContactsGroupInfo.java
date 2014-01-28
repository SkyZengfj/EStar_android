package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class GetContactsGroupInfo implements IParseJson {
	public String group_id;
	public String group_name;

	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null)
		{
			return false;
		}
		
		this.group_id = JsonUtility.getString(jo, "group_id", "");
		this.group_name = JsonUtility.getString(jo, "group_name", "");
		
		return true;
	}

}
