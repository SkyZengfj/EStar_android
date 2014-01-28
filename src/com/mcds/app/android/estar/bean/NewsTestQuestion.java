package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class NewsTestQuestion implements IParseJson{

	public String test_type;
	
	public String question_content;
	
	public String qustion_img;
	
	public List<NewsTestAnswers> answers;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}

		test_type = JsonUtility.getString(jo, "test_type", "");
		question_content = JsonUtility.getString(jo, "question_content", "");
		qustion_img = JsonUtility.getString(jo, "qustion_img", "");

		JSONArray answerJa = JsonUtility.getJSONArray(jo, "answers");
		answers = new ArrayList<NewsTestAnswers>();
		for (int i = 0; i < answerJa.length(); i++) {
			NewsTestAnswers question = new NewsTestAnswers();
			try {
				if (question.parseJson(answerJa.getJSONObject(i))) {
					answers.add(question);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}
