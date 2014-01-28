package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateFormat;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class ActivitTestQues implements IParseJson{

	public String problem_id;
	
	public String problem_type;
	
	public String question_content;
	
	public String qustion_img;
	
	public List<ActivitAnswer> answers;
	
	public String problem_scores;
	
	@Override
	public boolean parseJson(JSONObject jo) {
		// TODO Auto-generated method stub
		if (jo == null) {
			return false;
		}

		this.problem_id = JsonUtility.getString(jo, "problem_id", "0");
		this.problem_type = JsonUtility.getString(jo, "problem_type", "");
		this.question_content = JsonUtility.getString(jo, "question_content", "");
		this.qustion_img = JsonUtility.getString(jo, "qustion_img", "");
		problem_scores = JsonUtility.getString(jo, "problem_scores", "");
		
		JSONArray answerJa = JsonUtility.getJSONArray(jo, "answers");
		answers = new ArrayList<ActivitAnswer>();
		for (int i = 0; i < answerJa.length(); i++) {
			ActivitAnswer question = new ActivitAnswer();
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
