package com.mcds.app.android.estar.manager;

import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.provider.ConnectProvider;

public class UserManager {
	public static String userName;
	public static String password;
	public static String uid;

	public static ReturnResult<String> authenticate() {
		ReturnResult<String> rr = ConnectProvider.login(userName, password);

		uid = "";
		if (rr.status.equals("0")) {
			if (rr.getDatas().size() > 0) {
				uid = rr.getDatas().get(0);
			}
		}

		return rr;
	}
}
