package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

/**
 * 请求文档列表返回的字段
 * 
 * @author TangChao
 */
public class Document implements IParseJson
{
	public String document_id;
	public String document_url;
	public String name;
	public String time;
	public String size;
	
	@Override
	public boolean parseJson(JSONObject jo)
	{
		if (jo == null)
		{
			return false;
		}
		this.document_id = JsonUtility.getString(jo, "document_id", "");
		this.document_url = JsonUtility.getString(jo, "document_url", "");
		this.name = JsonUtility.getString(jo, "name", "");
		this.time = JsonUtility.getString(jo, "time", "");
		this.size = JsonUtility.getString(jo, "size", "");
		return true;
	}
}
