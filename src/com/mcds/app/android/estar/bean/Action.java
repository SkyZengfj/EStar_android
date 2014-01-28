package com.mcds.app.android.estar.bean;

import java.util.Date;

import org.json.JSONObject;

import android.text.format.DateFormat;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

/**
 * 活动
 * @author zhou
 *
 */
public class Action implements IParseJson{

	/**
	 * ID
	 */
	public String activity_id;
	
	/**
	 * 图片链接
	 */
	public String img;
	
	/**
	 * 活动标题
	 */
	public String title;
	
	/**
	 * 活动类型
	 */
	public String sort;
	
	/**
	 * 活动奖励
	 */
	public String award;
	
	/**
	 * 参与人数
	 */
	public String join_num;
	
	/**
	 * 感兴趣人数
	 */
	public String favor_num;
	
	/**
	 * 开始时间
	 */
	public String startTime;
	
	/**
	 * 结束时间
	 */
	public String endTime;
	
	/**
	 * 是否任务
	 */
	public String istask;
	
	/**
	 * 评论数
	 */
	public String commit_num;
	
	/**
	 * 活动状态
	 */
	public String status;
	
	public String content;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}
		this.activity_id = JsonUtility.getString(jo, "activity_id", "0");
		this.img = JsonUtility.getString(jo, "img", "");
		this.title = Utility.decodeUnicode(JsonUtility.getString(jo, "title", ""));
		this.content = Utility.decodeUnicode(JsonUtility.getString(jo, "content", ""));
		this.sort = JsonUtility.getString(jo, "sort", "");
		this.award = JsonUtility.getString(jo, "award", "");
		this.join_num = JsonUtility.getString(jo, "join_num", "");
		this.favor_num = JsonUtility.getString(jo, "favor_num", "");
		Date ds = JsonUtility.getDate(jo, "startTime", null);
		if (ds != null) {
			this.startTime = (String) DateFormat.format("yyyy-MM-dd", ds);
		}
		Date de = JsonUtility.getDate(jo, "endTime", null);
		if (de != null) {
			this.endTime = (String) DateFormat.format("yyyy-MM-dd", de);
		}
		istask = JsonUtility.getString(jo, "istask", "");
		commit_num = JsonUtility.getString(jo, "commit_num", "");
		status = JsonUtility.getString(jo, "status", "");

		return true;
	}

}
