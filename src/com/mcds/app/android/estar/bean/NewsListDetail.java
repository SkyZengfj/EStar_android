package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class NewsListDetail implements IParseJson{

	/**
	 * 企业动态的标题
	 */
	public String news_title;
	
	/**
	 * 企业动态图片地址
	 */
	public String image_url;
	
	/**
	 * 企业动态的内容
	 */
	public String content;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}
		this.image_url = JsonUtility.getString(jo, "image_url", "");
		this.news_title = Utility.decodeUnicode(JsonUtility.getString(jo, "news_title", ""));
		this.content = Utility.decodeUnicode(JsonUtility.getString(jo, "news_content", ""));

		return true;
	}

	

}
