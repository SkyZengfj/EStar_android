package com.mcds.app.android.estar.bean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class My_SystemMessage implements IParseJson{
	public String message_id;//消息id
	public String get_time;//消息发出时间
	public String classification;//消息类别名称
	public String content_text;//消息内容
	public List<String> content_imgs;  //图片列表
//	public String content_imgs; //
	public String sender;	//消息发送者
	public String sender_img; //发送者头像
	public String recipient;//接收者
	public String uid;
	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

		this.message_id = JsonUtility.getString(jo, "message_id", "");
//		this.get_time = JsonUtility.getString(jo, "get_time", "");
		this.get_time = JsonUtility.getString(jo, "get_time", "");
		this.classification = JsonUtility.getString(jo, "classification", "");
		this.content_text = JsonUtility.getString(jo, "content_text", "");
//		this.content_imgs = JsonUtility.getString(jo, "content_imgs", "");
		this.sender = JsonUtility.getString(jo, "sender", "");
		this.sender_img = JsonUtility.getString(jo, "sender_img", "");
		this.recipient = JsonUtility.getString(jo, "recipient", "");
		uid = JsonUtility.getString(jo, "uid", "");
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