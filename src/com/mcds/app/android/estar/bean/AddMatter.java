package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

/**
 * 新建事项
 * 
 * @author TangChao
 */
public class AddMatter implements IParseJson
{
	public String theme_id;
	
	@Override
	public boolean parseJson(JSONObject jo)
	{
		this.theme_id = JsonUtility.getString(jo, "theme_id", "");
		return true;
	}
}
