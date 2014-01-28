package com.mcds.app.android.estar.bean;

import org.json.JSONObject;

import com.mcds.app.android.estar.util.JsonUtility;

/**
 * 收货地址
 * 
 * @author Sky
 * 
 */
public class Address implements IParseJson {

	public String address_id;
	public String name;
	public String phone;
	public String province;
	public String city;
	public String area;
	public String address;
	public String postal_code;
	public String districtid;
	public String is_default;

	@Override
	public boolean parseJson(JSONObject jo) {
		if (jo == null) {
			return false;
		}

		address_id = JsonUtility.getString(jo, "id", "");
		name = JsonUtility.getString(jo, "consignee", "");
		phone = JsonUtility.getString(jo, "phone", "");
		address = JsonUtility.getString(jo, "address", "");
		postal_code = JsonUtility.getString(jo, "postal_code", "");
		is_default = JsonUtility.getString(jo, "is_default", "0");

		return true;
	}

}
