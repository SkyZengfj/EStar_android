package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class BirthdayList implements IParseJson{

	/**
	 * 生日祝福卡图片地址
	 */
	public String card_imageurl;
	
	/**
	 * 发送生日祝福卡的人物头像地址
	 */
	public String image_url;
	
	/**
	 * 收到生日祝福卡的人的姓名
	 */
	public String received_name;
	
	/**
	 * 生日祝福的内容
	 */
	public String content;
	
	/**
	 * 发出生日祝福卡的人的姓名
	 */
	public String from_name;
	
	/**
	 * 生日祝福的赞的个数
	 */
	public String praise;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}
		this.card_imageurl = JsonUtility.getString(jo, "card_imageurl", "");
		this.image_url = JsonUtility.getString(jo, "image_url", "");
		this.received_name = JsonUtility.getString(jo, "received_name", "");
		this.content = Utility.decodeUnicode(JsonUtility.getString(jo, "content", ""));
		this.from_name = JsonUtility.getString(jo, "from_name", "");
		this.praise = JsonUtility.getString(jo, "praise", "");

		return true;
	}

	

}
