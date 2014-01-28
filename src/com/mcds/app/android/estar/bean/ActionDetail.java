package com.mcds.app.android.estar.bean;

import java.util.Date;

import org.json.JSONObject;

import android.text.format.DateFormat;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class ActionDetail implements IParseJson{
	
	/**
	 * id
	 */
	public String activity_id;
	
	/**
	 * 活动类型
	 */
	public String type;
	
	/**
	 * 活动名
	 */
	public String title;
	
	/**
	 * 活动图片地址
	 */
	public String img;
	
	/**
	 * 活动状态
	 */
	public String status;
	
	/**
	 * 开始时间
	 */
	public String time_start;
	
	/**
	 * 结束时间
	 */
	public String time_end;
	
	/**
	 * 发起人
	 */
	public String writer;
	
	/**
	 * 奖励类型
	 */
	public String award;
	
	/**
	 * 组织者
	 */
	public String organizer;
	
	/**
	 * 活动费用
	 */
	public String expend;
	
	/**
	 * 活动地址
	 */
	public String address;
	
	/**
	 * 参与人数
	 */
	public String join_num;
	
	/**
	 * 感兴趣人数
	 */
	public String favor_num;
	
	/**
	 * 活动介绍
	 */
	public String content;
	
	/**
	 * 评论数
	 */
	public String commit_num;
	
	/**
	 * 照片数
	 */
	public String photo_num;

	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}
		this.activity_id = JsonUtility.getString(jo, "activity_id", "0");
		this.type = JsonUtility.getString(jo, "type", "");
		this.title = Utility.decodeUnicode(JsonUtility.getString(jo, "title", ""));
		this.img = JsonUtility.getString(jo, "img", "");
		this.status = JsonUtility.getString(jo, "status", "");
		
		Date startDate = JsonUtility.getDate(jo, "time_start", null);
		if (startDate != null) {
			this.time_start = (String) DateFormat.format("yyyy-MM-dd", startDate);
		}
		Date endDate = JsonUtility.getDate(jo, "time_end", null);
		if (endDate != null) {
			this.time_end = (String) DateFormat.format("yyyy-MM-dd", endDate);
		}
		
		this.writer = JsonUtility.getString(jo, "writer", "");
		this.award = JsonUtility.getString(jo, "award", "");
		this.organizer = JsonUtility.getString(jo, "organizer", "");
		this.expend = JsonUtility.getString(jo, "expend", "");
		this.address = JsonUtility.getString(jo, "address", "");
		this.join_num = JsonUtility.getString(jo, "join_num", "");
		this.favor_num = JsonUtility.getString(jo, "favor_num", "");
		this.content = Utility.decodeUnicode(JsonUtility.getString(jo, "content", ""));
		this.commit_num = JsonUtility.getString(jo, "commit_num", "");
		this.photo_num = JsonUtility.getString(jo, "photo_num", "");

		return true;
	}

}
