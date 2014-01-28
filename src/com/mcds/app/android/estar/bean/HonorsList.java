package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class HonorsList implements IParseJson{

	/**
	 * 获得荣誉表彰的人物头像地址
	 */
	public String image_url;
	
	/**
	 * 荣誉表彰的人物姓名
	 */
	public String name;
	
	/**
	 * 获得的表彰类型
	 */
	public String recognition;
	
	/**
	 * 荣誉表彰的内容
	 */
	public String content;
	
	/**
	 * 荣誉表彰的赞的个数
	 */
	public String praise;
	
	public String news_id;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}
		this.news_id = JsonUtility.getString(jo, "news_id", "");
		this.image_url = JsonUtility.getString(jo, "image_url", "");
		this.name = JsonUtility.getString(jo, "name", "");
		this.recognition = Utility.decodeUnicode(JsonUtility.getString(jo, "recognition", ""));
		this.content = Utility.decodeUnicode(JsonUtility.getString(jo, "content", ""));
		this.praise = JsonUtility.getString(jo, "praise", "");

		return true;
	}

}
