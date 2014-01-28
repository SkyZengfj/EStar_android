package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class CommitInfo implements IParseJson {

	public String commit_id;//	评论id
	public String user_name;//	微博名
	public String user_work;//	部门职位
	public String time;//	距发布该微博的时间
	public String user_img;//	用户头像地址
	public String content_text;//	文本内容
	public String from;//	微博来源（0:手机 1:网站 2:微信）
	public String reply_to;//	原文内容
	public String weibo_id;//	微博id
	public String weibo_time;//	微博时间
	public String weibo_user_img;//	发送微博人员头像
	public String share_num;//	转发
	public String comment_num;//	评论 
	public String good_num;//	赞
	public String uid;
	
	@Override
	public boolean parseJson(JSONObject jo) {

		if (jo == null) {
			return false;
		}
		
		commit_id = JsonUtility.getString(jo, "commit_id", "");
		user_name = JsonUtility.getString(jo, "user_name", "");
		user_work = JsonUtility.getString(jo, "user_work", "");
		content_text = JsonUtility.getString(jo, "content_text", "");
		time = JsonUtility.getString(jo, "time", "");
		user_img = JsonUtility.getString(jo, "user_img", "");
		from = JsonUtility.getString(jo, "from", "");
		reply_to = JsonUtility.getString(jo, "reply_to", "");
		weibo_id = JsonUtility.getString(jo, "weibo_id", "");
		weibo_time = JsonUtility.getString(jo, "weibo_time", "");
		weibo_user_img = JsonUtility.getString(jo, "weibo_user_img", "");
		share_num = JsonUtility.getString(jo, "share_num", "0");
		comment_num = JsonUtility.getString(jo, "comment_num", "0");
		good_num = JsonUtility.getString(jo, "good_num", "0");
		uid = JsonUtility.getString(jo, "uid", "");
		
		return true;
	}

}
