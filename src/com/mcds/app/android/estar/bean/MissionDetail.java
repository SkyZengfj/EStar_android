package com.mcds.app.android.estar.bean;

import java.io.Serializable;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

/**
 * 任务详情返回的字段
 * 
 * @author TangChao
 */
public class MissionDetail implements IParseJson, Serializable
{
	/**
	 * 对象序列化
	 */
	private static final long serialVersionUID = 4L;
	public String theme_id;
	public String mission_id;
	public String name;
	public String leader;
	public String start_time;
	public String end_time;
	public String status;
	public String members;
	public String content;
	
	@Override
	public boolean parseJson(JSONObject jo)
	{
		if (jo == null)
		{
			return false;
		}
		this.theme_id = JsonUtility.getString(jo, "theme_id", "");
		this.mission_id = JsonUtility.getString(jo, "mission_id", "0");
		this.name = JsonUtility.getString(jo, "name", "");
		this.leader = JsonUtility.getString(jo, "leader", "");
		this.start_time = JsonUtility.getString(jo, "start_time", "");
		this.end_time = JsonUtility.getString(jo, "end_time", "");
		this.status = JsonUtility.getString(jo, "status", "");
		this.members = JsonUtility.getString(jo, "members", "");
		this.content = JsonUtility.getString(jo, "content", "");
		return true;
	}
}
