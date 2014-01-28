package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class BlessingModelInfo implements IParseJson {

	
	public String bt_id;//祝福模板ID
	public String imagepath;//模板图片地址;
	
	@Override
	public boolean parseJson(JSONObject jo) {

		if (jo == null) {
			return false;
		}
		bt_id = JsonUtility.getString(jo, "bt_id", "");
		imagepath = JsonUtility.getString(jo, "imagepath", "");
		
		return true;
	}


}
