package com.mcds.app.android.estar.bean;


import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class My_FindFriends implements IParseJson{
	public String uid; //成员id
	public String name;	 //成员名称
	public String img;  //成员头像
	public String sex; //成员性别
	public String work; //成员岗位
	public String attention_flag; //0:未关注   1：已关注  2： 互粉
	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

		this.uid = JsonUtility.getString(jo, "uid", "");
		this.name = JsonUtility.getString(jo, "name", "");
		this.img = JsonUtility.getString(jo, "img", "");
		this.sex = JsonUtility.getString(jo, "sex", "");
		this.work = JsonUtility.getString(jo, "work", "");
		this.attention_flag = JsonUtility.getString(jo, "attention_flag", "");
		return true;
	}

}
