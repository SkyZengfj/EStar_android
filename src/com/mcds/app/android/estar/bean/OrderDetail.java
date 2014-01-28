package com.mcds.app.android.estar.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class OrderDetail implements IParseJson {

	public String code;
	public Date create_time;
	public String status;
	public Address address;
	public List<Shopcar> products;
	public String cancelable; // 1:yes ; 0:no

	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

		code = JsonUtility.getString(jo, "bookid", "");
		create_time = JsonUtility.getPhpDate(jo, "create_time", null);
		cancelable = JsonUtility.getString(jo, "cancelable", "0");

		JSONObject addressJo = JsonUtility.getJSONObject(jo, "address");
		address = new Address();
		address.parseJson(addressJo);

		products = new ArrayList<Shopcar>();
		JSONArray productJa = JsonUtility.getJSONArray(jo, "products");
		if (productJa != null) {
			for (int i = 0; i < productJa.length(); i++) {
				Shopcar item = new Shopcar();

				try {
					JSONObject shopcarJo = productJa.getJSONObject(i);

					item.name = JsonUtility.getString(shopcarJo, "products_name", "");
					item.integral = JsonUtility.getInteger(shopcarJo, "products_integral", 0);
					item.count = JsonUtility.getInteger(shopcarJo, "products_number", 0);
					item.productCode = JsonUtility.getString(shopcarJo, "products_code", "");
					item.img = JsonUtility.getString(shopcarJo, "img", "");

					products.add(item);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}

		int statusCode = JsonUtility.getInteger(jo, "status", -100);
		switch (statusCode) {
			case -2:
				status = "待确定订单";
				break;
			case -1:
				status = "取消订单";
				break;
			case 0:
				status = "未结算订单";
				break;
			case 1:
				status = "正常订单";
				break;
			case 2:
				status = "异常订单";
				break;
			case 10:
				status = "订单完成";
				break;
			default:
				break;
		}

		return true;
	}

}
