package com.mcds.app.android.estar.ui;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.PersistenceProvider;
import com.mcds.app.android.estar.ui.weibo.HomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	private ReturnResult<String> loginRR;
	private String userName;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		if (!UserManager.uid.equals("")) {
//			startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//			finish();
//		}

		setContentView(R.layout.login);

		userName = PersistenceProvider.getString(PersistenceProvider.USER_NAME, "");
		if (userName.length() > 0) {
			((EditText) findViewById(R.id.login_txtUsername)).setText(userName);
			findViewById(R.id.login_txtPassword).requestFocus();
		}

		findViewById(R.id.login_btnLogin).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userName = ((EditText) findViewById(R.id.login_txtUsername)).getText().toString().trim();
				if (userName.length() == 0) {
					Toast.makeText(getApplicationContext(), "请输入用户名！", Toast.LENGTH_SHORT).show();
					findViewById(R.id.login_txtUsername).requestFocus();
					return;
				}
				PersistenceProvider.put(PersistenceProvider.USER_NAME, userName);

				password = ((EditText) findViewById(R.id.login_txtPassword)).getText().toString().trim();
				if (password.length() == 0) {
					Toast.makeText(getApplicationContext(), "请输入密码！", Toast.LENGTH_SHORT).show();
					findViewById(R.id.login_txtPassword).requestFocus();
					return;
				}
				PersistenceProvider.put(PersistenceProvider.PASSWORD, password);

				login();
			}
		});
	}

	private void login() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				UserManager.userName = userName;
				UserManager.password = password;
				loginRR = UserManager.authenticate();

				doLogin();

				hideWaitingDialog();
			}
		}).start();
	}

	private void doLogin() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (loginRR.status.equals("0")) {
					startActivity(new Intent(LoginActivity.this, HomeActivity.class));
					finish();
				} else {
					Toast.makeText(getApplicationContext(), loginRR.info, Toast.LENGTH_SHORT).show();
				}
				
				startActivity(new Intent(LoginActivity.this, HomeActivity.class));
				finish();
			}
		});
	}
}
