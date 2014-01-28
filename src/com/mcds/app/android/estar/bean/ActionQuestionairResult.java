package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class ActionQuestionairResult implements IParseJson{

	/**
	 * id
	 */
	public String activity_type;
	
	/**
	 * 活动类型
	 */
	public String content;
	
	/**
	 * 活动名
	 */
	public String content_img;
	
	/**
	 * 活动图片地址
	 */
	public String score;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}
		this.activity_type = JsonUtility.getString(jo, "activity_type", "");
		this.content_img = JsonUtility.getString(jo, "content_img", "");
		this.content = Utility.decodeUnicode(JsonUtility.getString(jo, "content", ""));
		this.score = JsonUtility.getString(jo, "score", "");
		return true;
	}

}
