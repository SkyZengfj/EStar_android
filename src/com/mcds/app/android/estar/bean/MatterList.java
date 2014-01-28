package com.mcds.app.android.estar.bean;

import java.io.Serializable;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class MatterList implements IParseJson, Serializable
{
	public static final long serialVersionUID = 1L;
	public String theme_id;
	public String name;
	public String content;
	public String time_end;
	public String time_setup;
	public String missions_num;
	
	@Override
	public boolean parseJson(JSONObject jo)
	{
		if (jo == null)
		{
			return false;
		}
		this.theme_id = JsonUtility.getString(jo, "theme_id", "0");
		this.name = JsonUtility.getString(jo, "name", "");
		this.content = JsonUtility.getString(jo, "content", "");
		this.time_end = JsonUtility.getString(jo, "time_end", "");
		this.time_setup = JsonUtility.getString(jo, "time_setup", "");
		this.missions_num = JsonUtility.getString(jo, "missions_num", "");
		return true;
	}
}
