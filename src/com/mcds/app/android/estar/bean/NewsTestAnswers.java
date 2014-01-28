package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class NewsTestAnswers implements IParseJson{

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

		answer_id = JsonUtility.getString(jo, "answer_id", "");
		answer_content = JsonUtility.getString(jo, "answer_content", "");
		answer_img = JsonUtility.getString(jo, "answer_img", "");
		score = JsonUtility.getString(jo, "score", "");

		return true;
	}

}
