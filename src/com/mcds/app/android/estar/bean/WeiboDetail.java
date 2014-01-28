package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.format.DateFormat;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class WeiboDetail implements IParseJson {
	public String user_id;
	public String user_img;
	public String user_name;
	public String work;
	public String content_text;
	public String time;
	public List<String> content_imgs;
	public String from;
	public String weibo_type;
	public String weibo_type_b;
	public String share_num;
	public String comment_num;
	public String good_num;
	public String original_uid;
	public String original_weibo_id;
	public String original_user_img;
	public String original_user_name;
	public String original_content;
	public List<String> original_imgs;

	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

		this.user_id = JsonUtility.getString(jo, "user_id", "0");
		this.user_name = JsonUtility.getString(jo, "user_name", "");
		this.work = JsonUtility.getString(jo, "work", "");
		this.content_text = Utility.decodeUnicode(JsonUtility.getString(jo, "content_text", ""));
		this.from = JsonUtility.getString(jo, "from", "");
		Date d = JsonUtility.getDate(jo, "time", null);
		if (d != null) {
			this.time = (String) DateFormat.format("yyyy-MM-dd", d);
		}
		this.user_img = JsonUtility.getString(jo, "user_img", "");
		this.weibo_type = JsonUtility.getString(jo, "weibo_type", "");
		this.weibo_type_b = JsonUtility.getString(jo, "weibo_type_b", "");
		this.share_num = JsonUtility.getString(jo, "share_num", "");
		this.comment_num = JsonUtility.getString(jo, "comment_num", "");
		this.good_num = JsonUtility.getString(jo, "good_num", "");
		this.original_uid = JsonUtility.getString(jo, "original_uid", "");
		this.original_weibo_id = JsonUtility.getString(jo, "original_weibo_id", "");
		this.original_user_img = JsonUtility.getString(jo, "original_user_img", "");
		this.original_user_name = JsonUtility.getString(jo, "original_user_name", "");
		this.original_content = JsonUtility.getString(jo, "original_content", "");

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
		
		try {
			JSONArray jb = JsonUtility.getJSONArray(jo, "original_imgs");
			if (jb != null) {
				for (int i = 0; i < jb.length(); i++) {
					JSONObject imgJo = jb.getJSONObject(i);
					original_imgs.add(JsonUtility.getString(imgJo, "img", ""));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}
}