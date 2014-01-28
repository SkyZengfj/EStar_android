package com.mcds.app.android.estar.bean;

import java.io.Serializable;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class TestList implements IParseJson, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3915569519531742472L;
	/**
	 * 测试题的id
	 */
	public String test_id;
	/**
	 * 测试题的图片地址
	 */
	public String image_url;
	/**
	 * 测试题的标题
	 */
	public String test_title;
	/**
	 * 测试题的内容
	 */
	public String test_content;
	/**
	 * 测试题的赞的个数
	 */
	public String test_praise;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}
		this.test_id = JsonUtility.getString(jo, "test_id", "0");
		this.image_url = JsonUtility.getString(jo, "image_url", "");
		this.test_title = JsonUtility.getString(jo, "test_title", "");
		this.test_content = Utility.decodeUnicode(JsonUtility.getString(jo, "test_content", ""));
		this.test_praise = JsonUtility.getString(jo, "test_praise", "");

		return true;
	}

	

}
