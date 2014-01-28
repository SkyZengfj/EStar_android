package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

/**
 * 請求成員列表返回的字段
 * 
 * @author TangChao
 * 
 */
public class MatterMemberList implements IParseJson
{
	public String uid;
	public String name;
	public String work_position;
	public String head_img;
	public String sex;

	@Override
	public boolean parseJson(JSONObject jo)
	{
		if (jo == null)
		{
			return false;
		}
		this.uid = JsonUtility.getString(jo, "uid", "");
		this.name = JsonUtility.getString(jo, "name", "");
		this.work_position = JsonUtility.getString(jo, "work_position", "");
		this.head_img = JsonUtility.getString(jo, "head_img", "");
		this.sex = JsonUtility.getString(jo, "sex", "");
		return true;
	}

}
