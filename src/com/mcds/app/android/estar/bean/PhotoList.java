package com.mcds.app.android.estar.bean;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.text.format.DateFormat;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class PhotoList implements IParseJson{

	/**
	 * 上传时间
	 */
	public String time;
	
	/**
	 * 图片链接
	 */
	public String photo;
	
//	public List<String> photos;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}
		this.photo = JsonUtility.getString(jo, "photo", "");
		Date ds = JsonUtility.getDate(jo, "time", null);
		if (ds != null) {
			this.time = (String) DateFormat.format("yyyy-MM-dd", ds);
		}

		return true;
	}

	
}
