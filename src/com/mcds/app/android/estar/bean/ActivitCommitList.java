package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.format.DateFormat;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class ActivitCommitList implements IParseJson{


	/**
	 * 微博id
	 */
	public String weibo_id;
	
	/**
	 * 用户id
	 */
	public String uid;
	
	/**
	 * 微博名
	 */
	public String user_name;
	
	/**
	 * 距发布该微博的时间
	 */
	public String time;
	
	/**
	 * 用户头像地址
	 */
	public String user_img;
	
	/**
	 * 文本内容
	 */
	public String content_text;
	
	/**
	 * 图片地址
	 */
	public List<String> content_imgs;
	
	/**
	 * 微博来源（0:手机 1:网站 2:微信）
	 */
	public String from;
	
	/**
	 * 微博类型（0:肯定动态 1:祝福动态 2:信息动态 3:求助动态）
	 */
	public String weibo_type;
	
	
	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

		this.weibo_id = JsonUtility.getString(jo, "weibo_id", "0");
		this.uid = JsonUtility.getString(jo, "uid", "0");
		this.user_name = JsonUtility.getString(jo, "user_name", "");
		this.content_text = Utility.decodeUnicode(JsonUtility.getString(jo, "content_text", ""));
		this.from = JsonUtility.getString(jo, "from", "");
//		Date d = JsonUtility.getDate(jo, "time", null);
//		if (d != null) {
//			this.time = (String) DateFormat.format("yyyy-MM-dd", d);
//		}
		this.time = JsonUtility.getString(jo, "time", "");
		this.user_img = JsonUtility.getString(jo, "user_img", "");
		this.weibo_type = JsonUtility.getString(jo, "weibo_type", "");

		content_imgs = new ArrayList<String>();
		try {
			JSONArray ja = JsonUtility.getJSONArray(jo, "content_imgs");
			if (ja != null) {
				for (int i = 0; i < ja.length(); i++) {
					JSONObject imgJo = ja.getJSONObject(i);
					content_imgs.add(JsonUtility.getString(imgJo, "img", ""));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

}
