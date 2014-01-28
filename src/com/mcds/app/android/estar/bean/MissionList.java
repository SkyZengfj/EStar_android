package com.mcds.app.android.estar.bean;

import java.io.Serializable;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

/**
 * 任务列表返回的字段
 * 
 * @author TangChao
 */
public class MissionList implements IParseJson, Serializable
{
	public static final long serialVersionUID = 3L;
	public String schedule;
	public String mission_id;
	public String name;
	public String leader;
	public String end_time;
	public String status;
	public String content;
	
	@Override
	public boolean parseJson(JSONObject jo)
	{
		if (jo == null)
		{
			return false;
		}
		this.mission_id = JsonUtility.getString(jo, "mission_id", "");
		this.name = JsonUtility.getString(jo, "name", "未设置任务名");
		this.leader = JsonUtility.getString(jo, "leader", "未制定负责人");
		this.end_time = JsonUtility.getString(jo, "end_time", "未设置截止时间");
		this.schedule = JsonUtility.getString(jo, "schedule", "0");
		this.status = JsonUtility.getString(jo, "status", "0");
		this.content = JsonUtility.getString(jo, "content", "暂无详情");
		return true;
	}
}
