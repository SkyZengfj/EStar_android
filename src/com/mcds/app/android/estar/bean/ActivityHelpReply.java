package com.mcds.app.android.estar.bean;

import java.util.Date;
import org.json.JSONObject;

import android.text.format.DateFormat;

import com.mcds.app.android.estar.util.JsonUtility;

public class ActivityHelpReply implements IParseJson{

	/**
	 * 回复id
	 */
	public String reply_id;
	
	/**
	 * 用户id
	 */
	public String uid;
	
	/**
	 * 姓名
	 */
	public String name;
	
	/**
	 * 用户头像
	 */
	public String img;
	
	/**
	 * 回复时间
	 */
	public String time;
	
	/**
	 * 回复内容
	 */
	public String content;
	
	/**
	 * 该回复评论数
	 */
	public String commit_num;
	
	/**
	 * 该回复转发数
	 */
	public String share_num;

	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}

		this.reply_id = JsonUtility.getString(jo, "reply_id", "0");
		this.uid = JsonUtility.getString(jo, "uid", "0");
		this.name = JsonUtility.getString(jo, "name", "");
		this.img = JsonUtility.getString(jo, "img", "");
		Date d = JsonUtility.getDate(jo, "time", null);
		if (d != null) {
			this.time = (String) DateFormat.format("yyyy-MM-dd", d);
		}
		this.content = JsonUtility.getString(jo, "content", "");
		this.commit_num = JsonUtility.getString(jo, "commit_num", "");
		this.share_num = JsonUtility.getString(jo, "share_num", "");

		return true;
	}
}
