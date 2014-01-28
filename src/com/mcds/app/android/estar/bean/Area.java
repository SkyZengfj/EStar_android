package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

/**
 * 省市区域
 * 
 * @author Sky
 * 
 */
public class Area implements IParseJson {

	public String pk;
	public String name;

	@Override
	public boolean parseJson(JSONObject jo) {
		if(jo == null){
			return false;
		}
		
		pk = JsonUtility.getString(jo, "id", "");
		name = JsonUtility.getString(jo, "name", "");
		
		return true;
	}

}
