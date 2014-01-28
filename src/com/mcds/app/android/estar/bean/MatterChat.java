package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

/**
 * 获取主题消息返回的字段
 * 
 * @author TangChao
 */
public class MatterChat implements IParseJson
{
	public String uid;
	public String name;
	public String img;
	public String content;
	public String time;
	
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
		this.content = JsonUtility.getString(jo, "content", "");
		this.time = JsonUtility.getString(jo, "time", "");
		return true;
	}
}
