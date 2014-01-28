package com.mcds.app.android.estar.bean;

import java.io.Serializable;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class HeartHomeList implements IParseJson, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8889290627167445344L;
	public String image_url;
	public String title;
	public String content;
	public String praise;
	public String news_id;
	public String time;
	public String from;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}
		this.image_url = JsonUtility.getString(jo, "image_url", "");
		this.title = Utility.decodeUnicode(JsonUtility.getString(jo, "title", ""));
		this.content = Utility.decodeUnicode(JsonUtility.getString(jo, "content", ""));
		this.praise = JsonUtility.getString(jo, "commit_num", "");
		this.news_id = JsonUtility.getString(jo, "news_id", "");
		this.time = JsonUtility.getString(jo, "time", "");
		this.from = JsonUtility.getString(jo, "from", "");
		return true;
	}

}
