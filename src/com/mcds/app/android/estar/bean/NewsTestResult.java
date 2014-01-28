package com.mcds.app.android.estar.bean;

import java.util.Date;

import org.json.JSONObject;

import android.text.format.DateFormat;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class NewsTestResult implements IParseJson{

	
	public String result_content;
	
	
	public String result_img;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}

		this.result_content = Utility.decodeUnicode(JsonUtility.getString(jo, "result_content", ""));
		this.result_img = JsonUtility.getString(jo, "result_img", "");

		return true;
	}

}
