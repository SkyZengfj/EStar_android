package com.mcds.app.android.estar.provider;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.transport.HttpResponseException;

import android.util.Log;

import com.mcds.app.android.estar.bean.AddMatter;
import com.mcds.app.android.estar.bean.Address;
import com.mcds.app.android.estar.bean.Area;
import com.mcds.app.android.estar.bean.AttentionSomeone;
import com.mcds.app.android.estar.bean.BaseReturnResult;
import com.mcds.app.android.estar.bean.BlessingModelInfo;
import com.mcds.app.android.estar.bean.CommitInfo;
import com.mcds.app.android.estar.bean.Gift;
import com.mcds.app.android.estar.bean.GiftCategory;
import com.mcds.app.android.estar.bean.GiftDetail;
import com.mcds.app.android.estar.bean.MatterDetail;
import com.mcds.app.android.estar.bean.MatterList;
import com.mcds.app.android.estar.bean.MyActivityNotifyInfo;
import com.mcds.app.android.estar.bean.MyAtMeInfo;
import com.mcds.app.android.estar.bean.MyAttentionMain;
import com.mcds.app.android.estar.bean.MyDetailInfo;
import com.mcds.app.android.estar.bean.MyFans;
import com.mcds.app.android.estar.bean.MyInfo;
import com.mcds.app.android.estar.bean.MyWeiboPraisedInfo;
import com.mcds.app.android.estar.bean.My_FriendsDeclaration;
import com.mcds.app.android.estar.bean.My_GetReminds;
import com.mcds.app.android.estar.bean.My_IntegrationScoreList;
import com.mcds.app.android.estar.bean.My_SetBlog;
import com.mcds.app.android.estar.bean.My_SetIntrest;
import com.mcds.app.android.estar.bean.My_SetWeiBo;
import com.mcds.app.android.estar.bean.Order;
import com.mcds.app.android.estar.bean.OrderDetail;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.SetPersonalAddress;
import com.mcds.app.android.estar.bean.Shopcar;
import com.mcds.app.android.estar.bean.Weibo;
import com.mcds.app.android.estar.bean.WeiboComment;
import com.mcds.app.android.estar.bean.WeiboDetail;
import com.mcds.app.android.estar.bean.WeiboForward;
import com.mcds.app.android.estar.bean.WeiboPraise;
import com.mcds.app.android.estar.util.ConnectUtility;
import com.mcds.app.android.estar.util.JsonUtility;

public class ConnectProvider
{
	public static ReturnResult<String> login(String userName, String password)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("user_name", userName);
		params.put("password", password);
		try
		{
			String returnJson = ConnectUtility.connect("login", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null && ja.length() > 0)
				{
					JSONObject dataJo = ja.getJSONObject(0);
					rr.addData(JsonUtility.getString(dataJo, "uid", ""));
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
	 * user's id
	 * 
	 * @param uid
	 *            message's type: 0:all 1:affirmation 2:blessing 3:message
	 *            4:activity
	 * @param type
	 *            when type is 4, type_b: 0:help 1:activity
	 * @param type_b
	 *            the list view loading count
	 * @param page_num
	 *            the list view loading message count in once
	 * @param page_size
	 * @return
	 */
	public static ReturnResult<Weibo> getWeiboList(String uid, String weibo_type, String weibo_type_b, String page_num,
			String page_size)
	{
		ReturnResult<Weibo> rr = new ReturnResult<Weibo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("weibo_type", weibo_type);
		params.put("weibo_type_b", weibo_type_b);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			String returnJson = ConnectUtility.connect("getWeiboList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						Weibo item = new Weibo();
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
	 * the weibo's id
	 * 
	 * @param weibo_id
	 * @return
	 */
	public static ReturnResult<WeiboDetail> getWeiboDetail(String weibo_id)
	{
		ReturnResult<WeiboDetail> rr = new ReturnResult<WeiboDetail>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("weibo_id", weibo_id);
		try
		{
			String returnJson = ConnectUtility.connect("getWeiboDetail", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						WeiboDetail item = new WeiboDetail();
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
	
	public static ReturnResult<WeiboComment> getWeiboCommentList(String weibo_id, String page_num, String page_size)
	{
		ReturnResult<WeiboComment> rr = new ReturnResult<WeiboComment>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("weibo_id", weibo_id);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			String returnJson = ConnectUtility.connect("getWeiboCommentList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						WeiboComment item = new WeiboComment();
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
	
	public static ReturnResult<WeiboForward> getWeiboShareList(String weibo_id, String page_num, String page_size)
	{
		ReturnResult<WeiboForward> rr = new ReturnResult<WeiboForward>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("weibo_id", weibo_id);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			String returnJson = ConnectUtility.connect("getWeiboCommentList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						WeiboForward item = new WeiboForward();
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
	
	public static ReturnResult<WeiboPraise> getWeiboGoodList(String weibo_id)
	{
		ReturnResult<WeiboPraise> rr = new ReturnResult<WeiboPraise>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("weibo_id", weibo_id);
		try
		{
			String returnJson = ConnectUtility.connect("getWeiboGoodList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						WeiboPraise item = new WeiboPraise();
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
	 * 获取我的信息 user's id
	 * 
	 * @param uid
	 *            message's type: 0:all 1:affirmation 2:blessing 3:message
	 *            4:activity
	 * @return
	 */
	public static ReturnResult<MyInfo> getMyInfo(String uid)
	{
		ReturnResult<MyInfo> rr = new ReturnResult<MyInfo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		try
		{
			String returnJson = ConnectUtility.connect("getMyInfo", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						MyInfo item = new MyInfo();
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
	 * 获取关注列表 user's id
	 * 
	 * @param uid
	 *            message's type: 0:all 1:affirmation 2:blessing 3:message
	 *            4:activity
	 * @param page_num
	 *            the list view loading message count in once
	 * @param page_size
	 * @return
	 */
	public static ReturnResult<MyAttentionMain> getMyAttentionList(String uid, String page_num, String page_size)
	{
		ReturnResult<MyAttentionMain> rr = new ReturnResult<MyAttentionMain>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			String returnJson = ConnectUtility.connect("getMyAttentionList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						MyAttentionMain item = new MyAttentionMain();
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
	 * 获取我的粉丝 user's id
	 * 
	 * @param uid
	 *            message's type: 0:all 1:affirmation 2:blessing 3:message
	 *            4:activity
	 * @param page_num
	 *            the list view loading message count in once
	 * @param page_size
	 * @return
	 */
	public static ReturnResult<MyFans> getMyFollowerList(String uid, String page_num, String page_size)
	{
		ReturnResult<MyFans> rr = new ReturnResult<MyFans>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			String returnJson = ConnectUtility.connect("getMyFollowerList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						MyFans item = new MyFans();
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
	 * 关注某人
	 * 
	 * @param my_id
	 * @param the_id
	 * @param attention_type
	 * @return
	 */
	public static ReturnResult<AttentionSomeone> attentionSomeone(String my_id, String the_id, String attention_type)
	{
		ReturnResult<AttentionSomeone> rr = new ReturnResult<AttentionSomeone>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("my_id", my_id);
		params.put("the_id", the_id);
		params.put("attention_type", attention_type);
		try
		{
			String returnJson = ConnectUtility.connect("attentionSomeone", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						AttentionSomeone item = new AttentionSomeone();
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
	 * 设置个性域名
	 */
	public static ReturnResult<SetPersonalAddress> setPersonalAddress(String uid, String personal_address)
	{
		ReturnResult<SetPersonalAddress> rr = new ReturnResult<SetPersonalAddress>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("personal_address", personal_address);
		try
		{
			String returnJson = ConnectUtility.connect("setPersonalAddress", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						SetPersonalAddress item = new SetPersonalAddress();
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
	 * 设置个人博客
	 */
	public static ReturnResult<My_SetBlog> setBlog(String uid, String blog)
	{
		ReturnResult<My_SetBlog> rr = new ReturnResult<My_SetBlog>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("blog", blog);
		try
		{
			String returnJson = ConnectUtility.connect("setBlog", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						My_SetBlog item = new My_SetBlog();
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
	 * 设置微博
	 */
	public static ReturnResult<My_SetWeiBo> setWeibo(String uid, String weibo)
	{
		ReturnResult<My_SetWeiBo> rr = new ReturnResult<My_SetWeiBo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("weibo", weibo);
		try
		{
			String returnJson = ConnectUtility.connect("setWeibo", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						My_SetWeiBo item = new My_SetWeiBo();
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
	 * 设置兴趣爱好
	 */
	public static ReturnResult<My_SetIntrest> setIntrest(String uid, String intrest)
	{
		ReturnResult<My_SetIntrest> rr = new ReturnResult<My_SetIntrest>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("intrest", intrest);
		try
		{
			String returnJson = ConnectUtility.connect("setIntrest", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						My_SetIntrest item = new My_SetIntrest();
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
	 * 设置交友宣言
	 */
	public static ReturnResult<My_FriendsDeclaration> setDeclaration(String uid, String declaration)
	{
		ReturnResult<My_FriendsDeclaration> rr = new ReturnResult<My_FriendsDeclaration>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("declaration", declaration);
		try
		{
			String returnJson = ConnectUtility.connect("setDeclaration", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						My_FriendsDeclaration item = new My_FriendsDeclaration();
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
	 * 获取我的提醒
	 */
	public static ReturnResult<My_GetReminds> getMyReminds(String uid)
	{
		ReturnResult<My_GetReminds> rr = new ReturnResult<My_GetReminds>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		try
		{
			String returnJson = ConnectUtility.connect("getMyReminds", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						My_GetReminds item = new My_GetReminds();
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
	 * 获取我的详细信息
	 * 
	 * @param uid
	 * @return
	 */
	public static ReturnResult<MyDetailInfo> getMyDetailInfo(String uid)
	{
		ReturnResult<MyDetailInfo> rr = new ReturnResult<MyDetailInfo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		try
		{
			String returnJson = ConnectUtility.connect("getMyDetailInfo", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						MyDetailInfo item = new MyDetailInfo();
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
	// /**
	// * user's id
	// * @param uid
	// * message's type: 0:all 1:affirmation 2:blessing 3:message 4:activity
	// * @param type
	// * when type is 4, type_b: 0:help 1:activity
	// * @param type_b
	// * the list view loading count
	// * @param page_num
	// * the list view loading message count in once
	// * @param page_size
	// * @return
	// */
	// public static ReturnResult<Weibo> getMyselfWeiboList(String uid, String
	// weibo_type, String weibo_type_b, String page_num, String page_size) {
	// ReturnResult<Weibo> rr = new ReturnResult<Weibo>();
	//
	// Map<String, Object> params = new LinkedHashMap<String, Object>();
	// params.put("uid", uid);
	// params.put("weibo_type", weibo_type);
	// params.put("weibo_type_b", weibo_type_b);
	// params.put("page_num", page_num);
	// params.put("page_size", page_size);
	//
	// try {
	// String returnJson = ConnectUtility.connect("getMyselfWeiboList", params);
	//
	// JSONObject jo = new JSONObject(returnJson);
	//
	// rr.status = JsonUtility.getString(jo, "status", "-2");
	// rr.info = JsonUtility.getString(jo, "info", "");
	//
	// if (rr.status.equals("0")) {
	// JSONArray ja = JsonUtility.getJSONArray(jo, "data");
	//
	// if (ja != null) {
	// for (int i = 0; i < ja.length(); i++) {
	// Weibo item = new Weibo();
	// if (item.parseJson(ja.getJSONObject(i))) {
	// rr.addData(item);
	// }
	// }
	// }
	// }
	// } catch (Exception e) {
	// setException(e, rr);
	// }
	//
	// return rr;
	// }
	// /**
	// * user's id
	// * @param uid
	// * message's type: 0:all 1:affirmation 2:blessing 3:message 4:activity
	// * @param type
	// * when type is 4, type_b: 0:help 1:activity
	// * @param type_b
	// * the list view loading count
	// * @param page_num
	// * the list view loading message count in once
	// * @param page_size
	// * @return
	// */
	// public static ReturnResult<Weibo> getMyselfWeiboList(String uid, String
	// weibo_type, String weibo_type_b, String page_num, String page_size) {
	// ReturnResult<Weibo> rr = new ReturnResult<Weibo>();
	//
	// Map<String, Object> params = new LinkedHashMap<String, Object>();
	// params.put("uid", uid);
	// params.put("weibo_type", weibo_type);
	// params.put("weibo_type_b", weibo_type_b);
	// params.put("page_num", page_num);
	// params.put("page_size", page_size);
	//
	// try {
	// String returnJson = ConnectUtility.connect("getMyselfWeiboList", params);
	//
	// JSONObject jo = new JSONObject(returnJson);
	//
	// rr.status = JsonUtility.getString(jo, "status", "-2");
	// rr.info = JsonUtility.getString(jo, "info", "");
	//
	// if (rr.status.equals("0")) {
	// JSONArray ja = JsonUtility.getJSONArray(jo, "data");
	//
	// if (ja != null) {
	// for (int i = 0; i < ja.length(); i++) {
	// Weibo item = new Weibo();
	// if (item.parseJson(ja.getJSONObject(i))) {
	// rr.addData(item);
	// }
	// }
	// }
	// }
	// } catch (Exception e) {
	// setException(e, rr);
	// }
	//
	// return rr;
	// }
	// /**
	// * user's id
	// * @param uid
	// * message's type: 0:all 1:affirmation 2:blessing 3:message 4:activity
	// * @param type
	// * when type is 4, type_b: 0:help 1:activity
	// * @param type_b
	// * the list view loading count
	// * @param page_num
	// * the list view loading message count in once
	// * @param page_size
	// * @return
	// */
	// public static ReturnResult<Weibo> getMyselfWeiboList(String uid, String
	// weibo_type, String weibo_type_b, String page_num, String page_size) {
	// ReturnResult<Weibo> rr = new ReturnResult<Weibo>();
	//
	// Map<String, Object> params = new LinkedHashMap<String, Object>();
	// params.put("uid", uid);
	// params.put("weibo_type", weibo_type);
	// params.put("weibo_type_b", weibo_type_b);
	// params.put("page_num", page_num);
	// params.put("page_size", page_size);
	//
	// try {
	// String returnJson = ConnectUtility.connect("getMyselfWeiboList", params);
	//
	// JSONObject jo = new JSONObject(returnJson);
	//
	// rr.status = JsonUtility.getString(jo, "status", "-2");
	// rr.info = JsonUtility.getString(jo, "info", "");
	//
	// if (rr.status.equals("0")) {
	// JSONArray ja = JsonUtility.getJSONArray(jo, "data");
	//
	// if (ja != null) {
	// for (int i = 0; i < ja.length(); i++) {
	// Weibo item = new Weibo();
	// if (item.parseJson(ja.getJSONObject(i))) {
	// rr.addData(item);
	// }
	// }
	// }
	// }
	// } catch (Exception e) {
	// setException(e, rr);
	// }
	//
	// return rr;
	// }
	/**
	 * 获取事项列表
	 * 
	 * @param uid
	 * @param theme_sort
	 * @return
	 */
	public static ReturnResult<MatterList> getThemeList(String uid, String theme_sort1, String theme_sort2, String theme_sort3)
	{
		ReturnResult<MatterList> rr = new ReturnResult<MatterList>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("theme_sort1", theme_sort1);
		params.put("theme_sort2", theme_sort2);
		params.put("theme_sort3", theme_sort3);
		try
		{
			Log.i("getThemeList--------------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("getThemeList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("getThemeList------------------>", jo.toString());
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						MatterList item = new MatterList();
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
	 * 获取事项详情
	 * 
	 * @param uid
	 * @param theme_id
	 * @return
	 */
	public static ReturnResult<MatterDetail> getThemeDetail(String uid, String theme_id)
	{
		ReturnResult<MatterDetail> rr = new ReturnResult<MatterDetail>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("theme_id", theme_id);
		try
		{
			Log.i("getThemeDetail--------------------->", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("getThemeDetail", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				Log.i("getThemeDetail--------------------->", ja.toString());
				if (ja != null && ja.length() == 1)
				{
					MatterDetail item = new MatterDetail();
					if (item.parseJson(ja.getJSONObject(0)))
					{
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
	 * 新建事项
	 * 
	 * @param uid
	 * @param name
	 * @param content
	 * @param leader_id
	 * @param members_id
	 * @param time_end
	 * @return
	 */
	public static ReturnResult<String> addMatter(String uid, String name, String content, String leader_id, String members_id,
			String time_end)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("name", name);
		params.put("content", content);
		params.put("leader_id", leader_id);
		params.put("members_id", members_id);
		params.put("time_end", time_end);
		try
		{
			Log.i("addMatter------------------>", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("setUpTheme", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("addMatter----------->", jo.toString());
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 编辑事项
	 */
	public static ReturnResult<String> editMatter(String uid, String theme_id, String name, String content, String end_time,
			String leader, String members)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		HashMap<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("theme_id", theme_id);
		params.put("name", name);
		params.put("content", content);
		params.put("end_time", end_time);
		params.put("leader", leader);
		params.put("members", members);
		try
		{
			Log.i("editMatter------------------>", "请求接口……无数据返回则接口不通");
			String returnJson = ConnectUtility.connect("editTheme", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			Log.i("editMatter----------->", jo.toString());
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	/**
	 * 获取产品筛选列表
	 * 
	 * @param uid
	 */
	public static ReturnResult<GiftCategory> getGiftCategory(String uid)
	{
		ReturnResult<GiftCategory> rr = new ReturnResult<GiftCategory>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		try
		{
			String returnJson = ConnectUtility.connectIntegral("getCategory", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				for (int i = 0; i < ja.length(); i++)
				{
					GiftCategory item = new GiftCategory();
					if (item.parseJson(ja.getJSONObject(i)))
					{
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
	public static ReturnResult<Gift> getInsGiftList(String uid, String sort_type, String filter_type, String page_num,
			String page_size)
	{
		ReturnResult<Gift> rr = new ReturnResult<Gift>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("sort_type", sort_type);
		params.put("filter_type", filter_type);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			String returnJson = ConnectUtility.connectIntegral("getInsGiftList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				for (int i = 0; i < ja.length(); i++)
				{
					Gift item = new Gift();
					if (item.parseJson(ja.getJSONObject(i)))
					{
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
	public static ReturnResult<GiftDetail> getProductDetail(String uid, String code)
	{
		ReturnResult<GiftDetail> rr = new ReturnResult<GiftDetail>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("product_code", code);
		try
		{
			String returnJson = ConnectUtility.connectIntegral("getProductDetail", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONObject joData = JsonUtility.getJSONObject(jo, "data");
				if (joData != null)
				{
					GiftDetail item = new GiftDetail();
					if (item.parseJson(joData))
					{
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
	 * 获取省市区域列表
	 * 
	 * @param uid
	 * @param area_type
	 * @return
	 */
	public static ReturnResult<Area> getArea(String uid, String area_type)
	{
		ReturnResult<Area> rr = new ReturnResult<Area>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("area_type", area_type);
		try
		{
			String returnJson = ConnectUtility.connectIntegral("getAreaList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				for (int i = 0; i < ja.length(); i++)
				{
					Area item = new Area();
					if (item.parseJson(ja.getJSONObject(i)))
					{
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
	 * 新建地址收获地址
	 * 
	 * @param uid
	 * @param name
	 * @param phone
	 * @param province_pk
	 * @param city_pk
	 * @param area_pk
	 * @param address
	 * @param mail_code
	 * @return
	 */
	public static ReturnResult<String> newAddress(String uid, String name, String phone, String province_pk, String city_pk,
			String area_pk, String address, String mail_code)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("name", name);
		params.put("phone", phone);
		params.put("province_pk", province_pk);
		params.put("city_pk", city_pk);
		params.put("area_pk", area_pk);
		params.put("address", address);
		params.put("mail_code", mail_code);
		try
		{
			String returnJson = ConnectUtility.connectIntegral("newAddress", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null && ja.length() > 0)
				{
					rr.addData(JsonUtility.getString(ja.getJSONObject(0), "address_id", ""));
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
	 * 获取地址列表
	 * 
	 * @param uid
	 * @return
	 */
	public static ReturnResult<Address> getAddressList(String uid)
	{
		ReturnResult<Address> rr = new ReturnResult<Address>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		try
		{
			String returnJson = ConnectUtility.connectIntegral("getAddressList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				for (int i = 0; i < ja.length(); i++)
				{
					Address item = new Address();
					if (item.parseJson(ja.getJSONObject(i)))
					{
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
	
//	/**
//	 * 获取活动提醒列表
//	 * 
//	 * @param uid
//	 * @return
//	 */
//	public static ReturnResult<MyActivityNotifyInfo> getSystemMessageList(String uid) {
//		ReturnResult<MyActivityNotifyInfo> rr = new ReturnResult<MyActivityNotifyInfo>();
//		Map<String, Object> params = new LinkedHashMap<String, Object>();
//		params.put("uid", uid);
//		try {
//			String returnJson = ConnectUtility.connectIntegral("getSystemMessageList", params);
//			JSONObject jo = new JSONObject(returnJson);
//			rr.status = JsonUtility.getString(jo, "status", "-2");
//			rr.info = JsonUtility.getString(jo, "info", "");
//			if (rr.status.equals("0")) {
//				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
//				for (int i = 0; i < ja.length(); i++) {
//					MyActivityNotifyInfo item = new MyActivityNotifyInfo();
//					if (item.parseJson(ja.getJSONObject(i))) {
//						rr.addData(item);
//					}
//				}
//			}
//		} catch (Exception e) {
//			setException(e, rr);
//		}
//		return rr;
//	}
	/**
	 * 收到的肯定列表
	 * @param weibo_id
	 * @param page_num
	 * @param page_size
	 * @return
	 */
	public static ReturnResult<MyWeiboPraisedInfo> getMyWeiboPraised(String uid, String page_num, String page_size)
	{
		ReturnResult<MyWeiboPraisedInfo> rr = new ReturnResult<MyWeiboPraisedInfo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			String returnJson = ConnectUtility.connect("getMyWeiboPraised", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						MyWeiboPraisedInfo item = new MyWeiboPraisedInfo();
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
	 * 发出的肯定列表
	 * @param uid
	 * @param page_num
	 * @param page_size
	 * @return
	 */
	public static ReturnResult<MyWeiboPraisedInfo> getMyWeiboPraise(String uid, String page_num, String page_size)
	{
		ReturnResult<MyWeiboPraisedInfo> rr = new ReturnResult<MyWeiboPraisedInfo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			String returnJson = ConnectUtility.connect("getMyWeiboPraise", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						MyWeiboPraisedInfo item = new MyWeiboPraisedInfo();
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
	 * 获取提到我的动态列表
	 * @param weibo_id
	 * @param page_num
	 * @param page_size
	 * @return
	 */
	public static ReturnResult<MyAtMeInfo> getAtList(String uid, String at_type, String page_num, String page_size)
	{
		ReturnResult<MyAtMeInfo> rr = new ReturnResult<MyAtMeInfo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		params.put("at_type", at_type);
		try
		{
			String returnJson = ConnectUtility.connect("getAtList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						MyAtMeInfo item = new MyAtMeInfo();
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
	 * 获取我的动态列表
	 * @param weibo_id
	 * @param page_num
	 * @param page_size
	 * @return
	 */
	public static ReturnResult<MyAtMeInfo> getMyselfWeiboList(String uid, String weibo_type, String type_b, String page_num, String page_size)
	{
		ReturnResult<MyAtMeInfo> rr = new ReturnResult<MyAtMeInfo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		params.put("weibo_type", weibo_type);
		params.put("weibo_type_b", type_b);
		try
		{
			String returnJson = ConnectUtility.connect("getMyselfWeiboList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						MyAtMeInfo item = new MyAtMeInfo();
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
	 * 获取祝福模板
	 * @param weibo_id
	 * @param page_num
	 * @param page_size
	 * @return
	 */
	public static ReturnResult<BlessingModelInfo> getBlessingList(String page_num, String page_size)
	{
		ReturnResult<BlessingModelInfo> rr = new ReturnResult<BlessingModelInfo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			String returnJson = ConnectUtility.connect("getBlessingList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						BlessingModelInfo item = new BlessingModelInfo();
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
	 * 获取我收藏的动态列表
	 * @param weibo_id
	 * @param page_num
	 * @param page_size
	 * @return
	 */
	public static ReturnResult<MyAtMeInfo> getWeiboFavorList(String uid, String page_num, String page_size)
	{
		ReturnResult<MyAtMeInfo> rr = new ReturnResult<MyAtMeInfo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			String returnJson = ConnectUtility.connect("getWeiboFavorList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						MyAtMeInfo item = new MyAtMeInfo();
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
	 * 获取我的评论
	 * @param uid
	 * @param commit_type
	 * @param page_num
	 * @param page_size
	 * @return
	 */
	public static ReturnResult<CommitInfo> getCommitList(String uid,String commit_type, String page_num, String page_size)
	{
		ReturnResult<CommitInfo> rr = new ReturnResult<CommitInfo>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("commit_type", commit_type);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			String returnJson = ConnectUtility.connect("getCommitList", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null)
				{
					for (int i = 0; i < ja.length(); i++)
					{
						CommitInfo item = new CommitInfo();
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
	
	
	public static ReturnResult<String> submitOrder(String uid, String addressId, List<Shopcar> products)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("addrss_id", addressId);
		JSONArray productJa = new JSONArray();
		for (int i = 0; i < products.size(); i++)
		{
			JSONObject paramsJo = new JSONObject();
			try
			{
				paramsJo.put("product_code", products.get(i).productCode);
				paramsJo.put("number", products.get(i).count);
				productJa.put(paramsJo);
			}
			catch (JSONException e)
			{
				e.printStackTrace();
			}
		}
		params.put("products", productJa);
		try
		{
			String returnJson = ConnectUtility.connectIntegral("submitOrder", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null && ja.length() > 0)
				{
					rr.addData(ja.getString(0));
				}
			}
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	public static ReturnResult<Order> getOrderList(String uid, String order_type, String page_num, String page_size)
	{
		ReturnResult<Order> rr = new ReturnResult<Order>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("order_type", order_type);
		params.put("page_num", page_num);
		params.put("page_size", page_size);
		try
		{
			String returnJson = ConnectUtility.connectIntegral("getOrder", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				for (int i = 0; i < ja.length(); i++)
				{
					Order item = new Order();
					if (item.parseJson(ja.getJSONObject(i)))
					{
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
	public static ReturnResult<OrderDetail> getOrderDetail(String uid, String order_code)
	{
		ReturnResult<OrderDetail> rr = new ReturnResult<OrderDetail>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("order_code", order_code);
		try
		{
			String returnJson = ConnectUtility.connectIntegral("getOrderDetail", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONObject dataJo = JsonUtility.getJSONObject(jo, "data");
				OrderDetail item = new OrderDetail();
				if (item.parseJson(dataJo))
				{
					rr.addData(item);
				}
			}
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}
	public static ReturnResult<String> setDefaultAddress(String uid, String address_id)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("address_id", address_id);
		try
		{
			String returnJson = ConnectUtility.connectIntegral("setDefaultAddress", params);
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
	public static ReturnResult<String> getStock(String uid, String code)
	{
		ReturnResult<String> rr = new ReturnResult<String>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("code", code);
		try
		{
			String returnJson = ConnectUtility.connectIntegral("getStock", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
			if (rr.status.equals("0"))
			{
				JSONArray ja = JsonUtility.getJSONArray(jo, "data");
				if (ja != null && ja.length() > 0)
				{
					rr.addData(JsonUtility.getString(ja.getJSONObject(0), "stock", "0"));
				}
			}
		}
		catch (Exception e)
		{
			setException(e, rr);
		}
		return rr;
	}

	public static ReturnResult<String> cancelOrder(String uid, String order_id) {
		ReturnResult<String> rr = new ReturnResult<String>();
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("uid", uid);
		params.put("order_id", order_id);

		try {
			String returnJson = ConnectUtility.connectIntegral("updateorder", params);
			JSONObject jo = new JSONObject(returnJson);
			rr.status = JsonUtility.getString(jo, "status", "-2");
			rr.info = JsonUtility.getString(jo, "info", "");
		} catch (Exception e) {
			setException(e, rr);
		}
		return rr;
	}

	private static void setException(Exception exception, BaseReturnResult rr) {
		try {
			throw exception;
		}
		catch (JSONException jsonE)
		{
			rr.status = "-2";
			rr.info = "解析JSON错误.";
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
