package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

public class MyDetailInfo implements IParseJson{
	public String user_code; //登录名
	public String user_name;//用户名
	public String user_sex;//
	public String user_work;
	public String user_orgname;
	public String user_img;
	public String birthday;
	public String phone;
	public String personal_address;
	public String blog;
	public String weibo;
	public String direct_boss;//上级
	public String join_time;//入职时间
	public String intrest;//兴趣爱好
	public String declaration;//交友宣言
	
	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}
		this.user_code = JsonUtility.getString(jo, "user_code", ""); //登录名
		this.user_name =Utility.decodeUnicode(JsonUtility.getString(jo, "user_name", ""));//用户名
		this.user_sex = JsonUtility.getString(jo, "user_sex", "");//
		this.user_work = Utility.decodeUnicode(JsonUtility.getString(jo, "user_work", ""));
		this.user_orgname = JsonUtility.getString(jo, "user_orgname", "");
		this.user_img = JsonUtility.getString(jo, "user_img", "");
		this.birthday = JsonUtility.getString(jo, "birthday", "");
		this.phone = JsonUtility.getString(jo, "phone", "");
		this.personal_address = Utility.decodeUnicode(JsonUtility.getString(jo, "personal_address", ""));
		this.blog = Utility.decodeUnicode(JsonUtility.getString(jo, "blog", ""));
		this.weibo = Utility.decodeUnicode(JsonUtility.getString(jo, "weibo", ""));
		this.direct_boss = Utility.decodeUnicode(JsonUtility.getString(jo, "direct_boss", ""));
		this.join_time = JsonUtility.getString(jo, "join_time", "");
		this.intrest = Utility.decodeUnicode(JsonUtility.getString(jo, "intrest", ""));
		this.declaration = Utility.decodeUnicode(JsonUtility.getString(jo, "declaration", ""));
		return true;
	}

}
