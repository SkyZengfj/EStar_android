package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class MyAtMeInfo implements IParseJson {
	
	public String weibo_id;//	微博id
	public String user_name;//	微博名
	public String time;//	距发布该微博的时间
	public String user_img;//	用户头像地址
	public String content_text;//	文本内容
	public List<String> content_imgs;//	内容图片地址集合
	public String from;//	微博来源（0:手机 1:网站 2:微信）
	public String weibo_type_b;//	"0":求助动态  "1":其他活动动态  ""/null:当 weibo_type不为3的时候此字段无值，也不用考虑
	public String weibo_type;//	微博类型（0:肯定动态 1:祝福动态 2:信息动态 3:活动动态 4: 上传文档动态 5:其他）
	public String share_num;//	转发
	public String comment_num;//	评论 
	public String good_num;//	赞
	public String uid;


	
	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}
		weibo_id = JsonUtility.getString(jo, "weibo_id", "");
		user_name = JsonUtility.getString(jo, "user_name", "");
		time = JsonUtility.getString(jo, "time", "");
		content_text = JsonUtility.getString(jo, "content_text", "");
		user_img = JsonUtility.getString(jo, "user_img", "");
		from = JsonUtility.getString(jo, "from", "");
		weibo_type_b = JsonUtility.getString(jo, "weibo_type_b", "");
		weibo_type = JsonUtility.getString(jo, "weibo_type", "");
		share_num = JsonUtility.getString(jo, "share_num", "0");
		comment_num = JsonUtility.getString(jo, "comment_num", "0");
		good_num = JsonUtility.getString(jo, "good_num", "0");
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
