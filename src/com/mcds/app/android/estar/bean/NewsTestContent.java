package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class NewsTestContent implements IParseJson {

	public String test_title;
	public String test_content;
	public String test_img;

	public List<NewsTestQuestion> questions;

	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

		test_title = JsonUtility.getString(jo, "test_title", "");
		test_content = JsonUtility.getString(jo, "test_content", "");
		test_img = JsonUtility.getString(jo, "test_img", "");

		JSONArray questionJa = JsonUtility.getJSONArray(jo, "test_question");
		questions = new ArrayList<NewsTestQuestion>();
		for (int i = 0; i < questionJa.length(); i++) {
			NewsTestQuestion question = new NewsTestQuestion();
			try {
				if (question.parseJson(questionJa.getJSONObject(i))) {
					questions.add(question);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}
