package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.format.DateFormat;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class MyInfo implements IParseJson {
	public String user_name;
	public String user_sex;
	public String user_work;
	public String user_img;//头像地址
	public String intro;//简介
	public String praise_num;//肯定数
	public String approved_num;//认可数目
	public String attention_num;//关注数目
	public String attentioned_num;//粉丝数
	public String score_total;//总积分
	public String score_access;//可用积分
	public String get_praise_num;//获得肯定数
	public String send_praise_num;//发出肯定数
	public String get_praise_rank;//获得肯定排名
	public String send_praise_rank;//发出肯定排名
	public String weibo_num;//发布动态数
	public String remind_num;//提醒数
	public String team_num;//社团数
	public String dynamic_collection_num;  //(动态收藏数)
	public String user_mood;//个人心情
	
	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}
		this.user_name = JsonUtility.getString(jo, "user_name", "");
		this.user_sex = JsonUtility.getString(jo, "user_sex", "");
		this.user_work = JsonUtility.getString(jo, "user_work", "");
		this.user_img = JsonUtility.getString(jo, "user_img", "");//头像地址
		this.intro = JsonUtility.getString(jo, "intro", "");//简介
		this.praise_num = JsonUtility.getString(jo, "praise_num", "");//肯定数
		this.approved_num = JsonUtility.getString(jo, "approved_num", "");//认可数目
		this.attention_num = JsonUtility.getString(jo, "attention_num", "");//关注数目
		this.attentioned_num = JsonUtility.getString(jo, "attentioned_num", "");//粉丝数
		this.score_total = JsonUtility.getString(jo, "score_total", "");//总积分
		this.score_access = JsonUtility.getString(jo, "score_access", "");//可用积分
		this.get_praise_num = JsonUtility.getString(jo, "get_praise_num", "");//获得肯定数
		this.send_praise_num = JsonUtility.getString(jo, "send_praise_num", "");//发出肯定数
		this.get_praise_rank = JsonUtility.getString(jo, "get_praise_rank", "");//获得肯定排名
		this.send_praise_rank = JsonUtility.getString(jo, "send_praise_rank", "");//发出肯定排名
		this.weibo_num = JsonUtility.getString(jo, "weibo_num", "");//发布动态数
		this.remind_num = JsonUtility.getString(jo, "remind_num", "");;//提醒数
		this.team_num = JsonUtility.getString(jo, "team_num", "");//社团数
		this.dynamic_collection_num = JsonUtility.getString(jo, "dynamic_collection_num", "");
		this.user_mood = JsonUtility.getString(jo, "user_mood", "");
		return true;
	}
}