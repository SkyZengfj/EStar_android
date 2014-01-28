package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class MyWeiboPraisedInfo implements IParseJson{
//	public String weiboid;//	微博ID
//	public String user_name;//	姓名
//	public String user_sex;//	性别
//	public String user_work;//	职位
//	public String user_img;//	头像地址
//	public String attention_flag;//	0:未关注1:已关注2:互粉
	public String message_id;//	消息id
	public String get_time;//	消息发出时间
	public String classification;//	消息类别名称
	public String content_text;//	消息内容
	public List<String> content_imgs;//	图片列表
	public String sender;//	消息发送者
	public String sender_img;//	发送者头像
	public String recipient;//	接收者

	
	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}
//		weiboid = JsonUtility.getString(jo, "weiboid", "");
//		user_name = JsonUtility.getString(jo, "user_name", "");
//		user_sex = JsonUtility.getString(jo, "user_sex", "");
//		user_work = JsonUtility.getString(jo, "user_work", "");
//		user_img = JsonUtility.getString(jo, "user_img", "");
//		attention_flag = JsonUtility.getString(jo, "attention_flag", "");
		
		message_id = JsonUtility.getString(jo, "message_id", "");
		get_time = JsonUtility.getString(jo, "get_time", "");
		classification = JsonUtility.getString(jo, "classification", "");
		content_text = JsonUtility.getString(jo, "content_text", "");
		sender = JsonUtility.getString(jo, "user_img", "");
		sender_img = JsonUtility.getString(jo, "attention_flag", "");
		recipient = JsonUtility.getString(jo, "user_img", "");
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
			return false;
		}
		
		
		return true;
	}








}
