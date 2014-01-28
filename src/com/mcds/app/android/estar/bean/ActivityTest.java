package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class ActivityTest implements IParseJson{

	public String test_id;
	
	public String test_type;
	
	public String test_title;
	
	public List<ActivitTestQues> test_question;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		
		if (jo == null) {
			return false;
		}

		this.test_id = JsonUtility.getString(jo, "test_id", "");
		this.test_type = JsonUtility.getString(jo, "test_type", "");
		this.test_title = JsonUtility.getString(jo, "test_title", "");
		
		
		JSONArray questionJa = JsonUtility.getJSONArray(jo, "test_question");
		test_question = new ArrayList<ActivitTestQues>();
		for (int i = 0; i < questionJa.length(); i++) {
			ActivitTestQues question = new ActivitTestQues();
			try {
				if (question.parseJson(questionJa.getJSONObject(i))) {
					test_question.add(question);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

}
