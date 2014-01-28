package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

public class Gift implements IParseJson {

	public String product_title;
	public String img;
	public String integral;
	public String stock;
	public String products_code;
	public String prodcuts_model;

	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}
		
		this.product_title = JsonUtility.getString(jo, "product_title", "");
		img = JsonUtility.getString(jo, "img", "");
		integral = JsonUtility.getString(jo, "ntegral", "0");
		stock = JsonUtility.getString(jo, "stock", "0");
		products_code = JsonUtility.getString(jo, "products_code", "");
		prodcuts_model = JsonUtility.getString(jo, "products_model", "");
		
		return true;
	}

}
