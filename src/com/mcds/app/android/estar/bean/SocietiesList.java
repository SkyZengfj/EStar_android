package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

/**
 * 社团列表
 * 
 * @author TangChao
 */
public class SocietiesList implements IParseJson
{
	public String club_id;
	public String name;
	public String img;
	public String new_num;
	
	@Override
	public boolean parseJson(JSONObject jo)
	{
		if (jo == null)
		{
			return false;
		}
		this.club_id = JsonUtility.getString(jo, "club_id", "");
		this.name = JsonUtility.getString(jo, "name", "");
		this.img = JsonUtility.getString(jo, "img", "");
		this.new_num = JsonUtility.getString(jo, "new_num", "");
		return true;
	}
}
