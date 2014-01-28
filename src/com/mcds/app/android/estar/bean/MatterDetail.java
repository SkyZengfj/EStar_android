package com.mcds.app.android.estar.bean;

import java.io.Serializable;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

/**
 * 请求主题详情返回的字段
 * 
 * @author TangChao
 */
public class MatterDetail implements IParseJson, Serializable
{
	public static final long serialVersionUID = 2L;
	public String name;
	public String status;
	public String content;
	public String writer;
	public String leader;
	public String time_start;
	public String time_end;
	public String member_num;
	public String document_num;
	public String discuss_num;
	public String missions_num;
	
	@Override
	public boolean parseJson(JSONObject jo)
	{
		if (jo == null)
		{
			return false;
		}
		this.name = JsonUtility.getString(jo, "name", "");
		this.status = JsonUtility.getString(jo, "status", "");
		this.content = JsonUtility.getString(jo, "content", "");
		this.writer = JsonUtility.getString(jo, "writer", "");
		this.leader = JsonUtility.getString(jo, "leader", "");
		this.time_start = JsonUtility.getString(jo, "time_start", "");
		this.time_end = JsonUtility.getString(jo, "time_end", "");
		this.member_num = JsonUtility.getString(jo, "member_num", "");
		this.document_num = JsonUtility.getString(jo, "document_num", "");
		this.discuss_num = JsonUtility.getString(jo, "discuss_num", "");
		this.missions_num = JsonUtility.getString(jo, "missions_num", "");
		return true;
	}
}
