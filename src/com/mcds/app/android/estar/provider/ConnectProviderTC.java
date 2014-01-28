package com.mcds.app.android.estar.provider;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import android.text.format.Time;
import android.util.Log;

import com.mcds.app.android.estar.bean.BaseReturnResult;
import com.mcds.app.android.estar.bean.Document;
import com.mcds.app.android.estar.bean.MatterMemberList;
import com.mcds.app.android.estar.bean.MissionDetail;
import com.mcds.app.android.estar.bean.MissionList;
import com.mcds.app.android.estar.bean.MyAttentionMemberList;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.SocietiesChatReceive;
import com.mcds.app.android.estar.bean.SocietiesChatSend;
import com.mcds.app.android.estar.bean.SocietiesList;
import com.mcds.app.android.estar.bean.SocietiesMemberList;
import com.mcds.app.android.estar.bean.WorkChatReceive;
import com.mcds.app.android.estar.bean.WorkChatSend;
import com.mcds.app.android.estar.util.ConnectUtility;
import com.mcds.app.android.estar.util.JsonUtility;

public class ConnectProviderTC
{
	/**
	 * 获取任务列表
	 * 
	 * @param uid
	 * @param theme_id
	 * @return
	 */
	public static ReturnResult<MissionList> getMissionList(String uid, String theme_id)
	{
		ReturnResult<MissionList> rr = new ReturnResult<MissionList>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("theme_id", theme_id);
		try
		{
			Log.i("getMissionList-------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("getMissionList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("getMissionList--------->", jo.toString());
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						MissionList item = new MissionList();
						if (item.parseJson(ja.getJSONObject(i)))
						{
							rr.addData(item);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 获取任务详情
	 * 
	 * @param uid
	 * @param mission_id
	 * @return
	 */
	public static ReturnResult<MissionDetail> getMissionDetail(String uid, String mission_id)
	{
		ReturnResult<MissionDetail> rr = new ReturnResult<MissionDetail>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("mission_id", mission_id);
		try
		{
			Log.i("getMissionDetail---------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("getMissionDetail", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("getMissionDetail----->", jo.toString());
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						MissionDetail item = new MissionDetail();
						if (item.parseJson(ja.getJSONObject(i)))
						{
							rr.addData(item);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 删除文档的请求
	 */
	public static ReturnResult<String> deleteDocument(String uid, String document_id)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		HashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("document_id", document_id);
		try
		{
			String returnJson = ConnectUtility.connect("deleteDocument", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 获取成员列表
	 * 
	 * @param uid
	 * @param theme_id
	 * @return
	 */
	public static ReturnResult<MatterMemberList> getThemeMemberList(String uid, String theme_id)
	{
		ReturnResult<MatterMemberList> rr = new ReturnResult<MatterMemberList>();
		MatterMemberList memberList = new MatterMemberList();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("theme_id", theme_id);
		try
		{
			Log.i("getThemeMemberList----------------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("getThemeMemberList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("getThemeMemberList-------------------->", jo.toString());
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						MatterMemberList item = new MatterMemberList();
						memberList.head_img = item.head_img;
						memberList.name = item.name;
						memberList.sex = item.sex;
						memberList.uid = item.uid;
						memberList.work_position = item.work_position;
						rr.addData(item);
					}
				}
			}
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 新增成员的请求
	 */
	public static ReturnResult<String> addThemeMember(String uid, String theme_id, String member_id)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		HashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("theme_id", theme_id);
		params.put("member_id", member_id);
		try
		{
			Log.i("adThemeMember------------>", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("addThemeMember", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("addThemeMember----------->", jo.toString());
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 删除成员的请求
	 */
	public static ReturnResult<String> deleteThemeMember(String uid, String theme_id, String member_id)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		HashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("theme_id", theme_id);
		params.put("member_id", member_id);
		try
		{
			Log.i("seleteThemeMember-------------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("deleteThemeMember", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("seledeThemeMember------------>", jo.toString());
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 获取我关注的成员列表
	 */
	public static ReturnResult<MyAttentionMemberList> getMyAttentionMemberList(String uid)
	{
		ReturnResult<MyAttentionMemberList> rr = new ReturnResult<MyAttentionMemberList>();
		HashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		try
		{
			String returnJson = ConnectUtility.connect("", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			JSONArray ja = JsonUtility.getJSONArray(jo, "data");
			if (rr.status.equals("0"))
			{
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
					}
				}
			}
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 设置主题完成
	 * 
	 * @param uid
	 * @param theme_id
	 * @return
	 */
	public static ReturnResult<String> setThemeComplete(String uid, String theme_id)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("theme_id", theme_id);
		try
		{
			Log.i("setThemeComplete--------------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("setThemeComplete", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("setThemeComplete---------->", jo.toString());
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 设置任务完成
	 * 
	 * @param uid
	 * @param mission_id
	 * @return
	 */
	public static ReturnResult<String> setMissionComplete(String mission_id)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("mission_id", mission_id);
		try
		{
			Log.i("setMissionComplete------------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("setMissionComplete", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("setMissionComplete------------------>", jo.toString());
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 新建任务
	 * 
	 * @param uid
	 * @param theme_id
	 * @param name
	 * @param content
	 * @param end_time
	 * @param leader
	 * @param members_id
	 * @return
	 */
	public static ReturnResult<String> addMission(String uid, String theme_id, String name, String content, String end_time,
			String leader, String members_id)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("theme_id", theme_id);
		params.put("name", name);
		params.put("content", content);
		params.put("end_time", end_time);
		params.put("leader", leader);
		params.put("members_id", members_id);
		try
		{
			Log.i("addMission----------------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("setUpMission", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("addMission----------------->", jo.toString());
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 编辑任务
	 * 
	 * @param uid
	 * @param mission_id
	 * @param name
	 * @param content
	 * @param end_time
	 * @param leader
	 * @param members
	 * @return
	 */
	public static ReturnResult<String> editMission(String uid, String mission_id, String name, String content, String end_time,
			String leader, String members)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("mission_id", mission_id);
		params.put("name", name);
		params.put("content", content);
		params.put("end_time", end_time);
		params.put("leader", leader);
		params.put("members", members);
		try
		{
			Log.i("editMission--------------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("editMission", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("editMission--------------->", jo.toString());
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 文档列表
	 * 
	 * @return
	 */
	public static ReturnResult<Document> getDocumentList(String uid, String theme_id)
	{
		ReturnResult<Document> rr = new ReturnResult<Document>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("theme_id", theme_id);
		try
		{
			Log.i("getDocument----------------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("getDocumentList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("getDocument----------------->", jo.toString());
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				Log.i("getDocumentList", ja.toString());
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						Document item = new Document();
						if (item.parseJson(ja.getJSONObject(i)))
						{
							rr.addData(item);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 删除事项的请求
	 * 
	 * @param exception
	 * @param rr
	 */
	public static ReturnResult<String> deleteMatter(String uid, String theme_id)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		HashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("theme_id", theme_id);
		try
		{
			Log.i("deleteMatter------------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("deleteTheme", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("deleteMatter------->", jo.toString());
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 删除任务的请求
	 * 
	 * @param exception
	 * @param rr
	 */
	public static ReturnResult<String> deleteMission(String uid, String mission_id)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		HashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("mission_id", mission_id);
		try
		{
			Log.i("deleteMission--------------->", "请求接口……无数据返回则接口不通");
			String retrnJson = ConnectUtility.connect("deletemission", params);
			JSONObject jo = new JSONObject(retrnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("deleteMission--------------->", jo.toString());
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 请求社团列表
	 * 
	 * @param exception
	 * @param rr
	 */
	public static ReturnResult<SocietiesList> getMyClubList(String uid, String page_num, String page_size)
	{
		ReturnResult<SocietiesList> rr = new ReturnResult<SocietiesList>();
		HashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			Log.i("getClubList---------------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("getMyClubList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("getClubList---------------->", jo.toString());
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					SocietiesList item = new SocietiesList();
					for (int i = 0; i < ja.length(); i++)
					{
						if (item.parseJson(ja.getJSONObject(i)))
						{
							rr.addData(item);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 接收社团消息
	 * 
	 * @param exception
	 * @param rr
	 */
	public static ReturnResult<SocietiesChatReceive> getClubNewMessage(String uid, String club_id, long timestamp)
	{
		ReturnResult<SocietiesChatReceive> rr = new ReturnResult<SocietiesChatReceive>();
		HashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("club_id", club_id);
		params.put("timestamp", timestamp);
		try
		{
			Log.i("getClubNewMessage------------------>", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("getClubNewMessage", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("getClubNewMessage------------------>", jo.toString());
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						SocietiesChatReceive item = new SocietiesChatReceive();
						JSONObject object = ja.getJSONObject(i);
						item.name = object.optString("name");
						item.uid = object.optString("uid");
						item.img = object.optString("img");
						item.content = object.optString("content");
						item.time = object.optString("time");
						rr.addData(item);
					}
				}
			}
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 接收主题消息
	 * 
	 * @param exception
	 * @param rr
	 */
	public static ReturnResult<WorkChatReceive> getMatterChat(String uid, String theme_id, long timestamp)
	{
		ReturnResult<WorkChatReceive> rr = new ReturnResult<WorkChatReceive>();
		HashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("theme_id", theme_id);
		params.put("timestamp", timestamp);
		try
		{
			Log.i("getMatterChat---------->", "请求数据……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("getThemeChatList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("getMatterChat--------->", jo.toString());
			if (rr.status.equals("0"))
			{
				JSONArray ja = jo.getJSONArray("data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						WorkChatReceive item = new WorkChatReceive();
						JSONObject object = ja.getJSONObject(i);
						item.name = object.optString("name");
						item.uid = object.optString("uid");
						item.img = object.optString("img");
						item.content = object.optString("content");
						item.time = object.optString("time");
						rr.addData(item);
					}
				}
			}
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 发送社团消息
	 * 
	 * @param exception
	 * @param rr
	 */
	public static SocietiesChatSend setClubNewMessage(String uid, String club_id, String content)
	{
		SocietiesChatSend rrSend = new SocietiesChatSend();
		ReturnResult<String> rr = new ReturnResult<String>();
		HashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("club_id", club_id);
		params.put("content", content);
		try
		{
			Log.i("setClubNewMessage------------------>", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("setClubNewMessage", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("setClubNewMessage------------------>", jo.toString());
			if (rr.status.equals("0"))
			{
				rrSend.club_id = club_id;
				rrSend.uid = uid;
				rrSend.img = "";
				rrSend.content = content;
				rrSend.time = "2014-01-20";
			}
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rrSend;
	}
	/**
	 * 发送主题消息
	 * 
	 * @param exception
	 * @param rr
	 */
	public static WorkChatSend setMatterChat(String uid, String theme_id, String content)
	{
		WorkChatSend rrSend = new WorkChatSend();
		ReturnResult<WorkChatSend> rr = new ReturnResult<WorkChatSend>();
		HashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("theme_id", theme_id);
		params.put("content", content);
		try
		{
			Log.i("setMatterChat-------------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("setThemeChat", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("setMatterChat----------->", jo.toString());
			if (rr.status.equals("0"))
			{
				rrSend.content = content;
				rrSend.time = "2014-01-23";
				rrSend.theme_id = theme_id;
				rrSend.uid = uid;
				rrSend.img = "";
				rrSend.status = rr.status;
			}
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rrSend;
	}
	/**
	 * 获取社团成员
	 * 
	 * @param exception
	 * @param rr
	 */
	public static ReturnResult<SocietiesMemberList> getClubMember(String uid, String club_id, String page_num, String page_size)
	{
		ReturnResult<SocietiesMemberList> rr = new ReturnResult<SocietiesMemberList>();
		HashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("club_id", club_id);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			Log.i("getClubMember---------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("getClubMembers", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("getClubMember-------------->", jo.toString());
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					SocietiesMemberList item = new SocietiesMemberList();
					for (int i = 0; i < ja.length(); i++)
					{
						if (item.parseJson(ja.getJSONObject(i)))
						{
							rr.addData(item);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 接收主题消息
	 * 
	 * @param exception
	 * @param rr
	 */
	// public static ReturnResult<MatterChat> getMatterChat(String uid, String
	// theme_id, long timestamp)
	// {
	// ReturnResult<MatterChat> rr = new ReturnResult<MatterChat>();
	// HashMap<String, Object> params = new LinkedHashMap<String, Object>();
	// params.put("uid", uid);
	// params.put("theme_id", theme_id);
	// params.put("timestamp", timestamp);
	// try
	// {
	// Log.i("getMatterChat---------->", "请求数据……无数据返回则接口不通");
	// String returnJson = ConnectUtility.connect("getThemeChatList", params);
	// JSONObject jo = new JSONObject(returnJson);
	// rr.status = JsonUtility.getString(jo, "status", "-2");
	// rr.info = JsonUtility.getString(jo, "info", "");
	// Log.i("getMatterChat--------->", jo.toString());
	// }
	// catch (Exception e)
	// {
	// setException(e, rr);
	// }
	// return rr;
	// }
	// /**
	// * 接收社团消息
	// *
	// * @param exception
	// * @param rr
	// */
	// public static ReturnResult<SocietiesReceiveMessage>
	// getClubNewMessage(String uid, String club_id, long timestamp)
	// {
	// ReturnResult<SocietiesReceiveMessage> rr = new
	// ReturnResult<SocietiesReceiveMessage>();
	// HashMap<String, Object> params = new LinkedHashMap<String, Object>();
	// params.put("uid", uid);
	// params.put("club_id", club_id);
	// params.put("timestamp", timestamp);
	// try
	// {
	// Log.i("getClubNewMessage------------------>", "请求接口……无数据返回则接口不通");
	// String returnJson = ConnectUtility.connect("getClubNewMessage", params);
	// JSONObject jo = new JSONObject(returnJson);
	// rr.status = JsonUtility.getString(jo, "status", "-2");
	// rr.info = JsonUtility.getString(jo, "info", "");
	// Log.i("getClubNewMessage------------------>", jo.toString());
	// if (rr.status.equals("0"))
	// {
	// JSONArray ja = JsonUtility.getJSONArray(jo, "data");
	// if (ja != null)
	// {
	// SocietiesReceiveMessage item = new SocietiesReceiveMessage();
	// for (int i = 0; i < ja.length(); i++)
	// {
	// if (item.parseJson(ja.getJSONObject(i)))
	// {
	// rr.addData(item);
	// }
	// }
	// }
	// }
	// }
	// catch (Exception e)
	// {
	// setException(e, rr);
	// }
	// return rr;
	// }
	private static void setException(Exception exception, BaseReturnResult rr)
	{
		try
		{
			throw exception;
		}
		catch (JSONException jsonE)
		{
			rr.status = "-2";
			rr.info = jsonE.getMessage();
		}
		catch (SocketTimeoutException socketTimeoutE)
		{
			rr.status = "-3";
			rr.info = "连接超时.";
		}
		catch (HttpResponseException httpResponseE)
		{
			rr.status = "-4";
			rr.info = "服务器无反应.";
		}
		catch (UnknownHostException unknownHostE)
		{
			rr.status = "-5";
			rr.info = "未知服务器.";
		}
		catch (Exception e)
		{
			rr.status = "-1";
			rr.info = e.getMessage();
		}
	}
}
