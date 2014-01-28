package com.mcds.app.android.estar.bean;

import java.util.Date;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class Order implements IParseJson {
	public String code;
	public String total_integral;
	public String status;
	public Date create_time;

	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

		code = JsonUtility.getString(jo, "bookid", "");
		total_integral = JsonUtility.getString(jo, "total_integral", "0");
		create_time = JsonUtility.getPhpDate(jo, "create_time", null);

//		-2:待确定订单 -1	取消订单
//		0	未结算订单【未从机构扣款】
//		1	正常订单【已扣款】 2:异常订单 10:订单完成
		int statusCode = JsonUtility.getInteger(jo, "status", -100);
		switch (statusCode) {
			case -2:
				status="待确定订单";
				break;
			case -1:
				status="取消订单";
				break;
			case 0:
				status="未结算订单";
				break;
			case 1:
				status="正常订单";
				break;
			case 2:
				status="异常订单";
				break;
			case 10:
				status="订单完成";
				break;
			default:
				break;
		}

		return true;
	}

}
