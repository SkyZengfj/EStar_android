package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class My_IntegrationScoreList implements IParseJson{

//	public String totle;//总积分
//	public  String access;//可用积分
	public  String expend;//支出积分
	public static String my_score_totle = " ";
	public static String my_score_access = " ";//可用积分
	public static String my_score_expend = " ";//支出积分
//	public IntegrationScoreItems[] items;
	public String score_id;//积分时间id
	public String score_content; //积分事件
	public String score_time;//积分时间
	public String score_type;// 0：获得   1：支出
	public String score_num; //获得或支出积分数
	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

//		this.totle = JsonUtility.getString(jo, "totle", "");
//		this.access = JsonUtility.getString(jo, "access", "");
		this.expend = JsonUtility.getString(jo, "expend", "");
		this.score_id = JsonUtility.getString(jo, "score_id", "");
		this.score_content = JsonUtility.getString(jo,"score_content","");
		this.score_time = JsonUtility.getString(jo, "score_time", "");
		this.score_type = JsonUtility.getString(jo,"score_type", "");
		this.score_num = JsonUtility.getString(jo, "score_num","");
		
		return true;
	}

	
	class IntegrationScoreItems implements IParseJson{
		public String score_content;
		public String score_id;
		public String score_num;
		public String score_time;
		public String score_type;
		public boolean parseJson(JSONObject jo) {
			if (jo == null) {
				return false;
			}
			this.score_content = JsonUtility.getString(jo, "score_content", "");
			this.score_id = JsonUtility.getString(jo, "score_id", "");
			this.score_num = JsonUtility.getString(jo, "score_num", "");
			this.score_time = JsonUtility.getString(jo, "score_time", "");
			this.score_type = JsonUtility.getString(jo, "score_type", "");
			return true;
		}
	}
	
}
