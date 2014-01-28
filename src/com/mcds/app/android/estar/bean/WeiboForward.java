package com.mcds.app.android.estar.bean;

import java.util.Date;

import org.json.JSONObject;

import android.text.format.DateFormat;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class WeiboForward implements IParseJson {
	public String user_id;
	public String user_img;
	public String user_name;
	public String forward_content;
	public String time;

	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

		this.user_id = JsonUtility.getString(jo, "uid", "");
		this.user_name = JsonUtility.getString(jo, "user_name", "");
		this.forward_content = Utility.decodeUnicode(JsonUtility.getString(jo, "content", ""));
		Date d = JsonUtility.getDate(jo, "time", null);
		if (d != null) {
			this.time = (String) DateFormat.format("yyyy-MM-dd", d);
		}
		this.user_img = JsonUtility.getString(jo, "user_img", "");
		
		return true;
	}

}
