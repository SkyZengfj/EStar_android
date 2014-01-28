package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.format.DateFormat;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class ActivitAnswer implements IParseJson{

	public String answer_id;
	
	public String answer_content;
	
	public String answer_img;
	
	public String score;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}

		this.answer_id = JsonUtility.getString(jo, "answer_id", "0");
		this.answer_content = Utility.decodeUnicode(JsonUtility.getString(jo, "answer_content", ""));
		this.answer_img = JsonUtility.getString(jo, "answer_img", "");
		this.score = JsonUtility.getString(jo, "score", "");
		

		return true;
	}

}
