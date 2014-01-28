package com.mcds.app.android.estar.bean;

import java.io.Serializable;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class NewsList implements IParseJson,Serializable{

	/**
	 * 企业动态的id
	 */
	public String news_id;
	
	/**
	 * 企业动态的图片地址
	 */
	public String image_url;
	
	/**
	 * 企业动态的标题
	 */
	public String news_title;
	
	/**
	 * 企业动态的内容
	 */
	public String news_content;
	
	/**
	 * 企业动态的赞的个数
	 */
	public String news_praise;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}
		this.news_id = JsonUtility.getString(jo, "news_id", "0");
		this.image_url = JsonUtility.getString(jo, "image_url", "");
		this.news_title = Utility.decodeUnicode(JsonUtility.getString(jo, "news_title", ""));
		this.news_content = Utility.decodeUnicode(JsonUtility.getString(jo, "news_content", ""));
		this.news_praise = JsonUtility.getString(jo, "news_praise", "");

		return true;
	}

}
