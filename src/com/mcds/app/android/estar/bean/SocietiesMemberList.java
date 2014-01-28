package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

/**
 * 社团成员列表
 * 
 * @author TangChao
 */
public class SocietiesMemberList implements IParseJson
{
	public String uid;
	public String name;
	public String img;
	public String sex;
	public String work;
	public String laeder_flag;
	public String attention_flag;
	
	@Override
	public boolean parseJson(JSONObject jo)
	{
		if (jo == null)
		{
			return false;
		}
		this.uid = JsonUtility.getString(jo, "uid", "");
		this.name = JsonUtility.getString(jo, "name", "");
		this.img = JsonUtility.getString(jo, "img", "");
		this.sex = JsonUtility.getString(jo, "sex", "");
		this.work = JsonUtility.getString(jo, "work", "");
		this.laeder_flag = JsonUtility.getString(jo, "leader_flag", "");
		this.attention_flag = JsonUtility.getString(jo, "atention_flag", "");
		return true;
	}
}
