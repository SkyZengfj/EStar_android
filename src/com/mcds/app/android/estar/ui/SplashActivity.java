package com.mcds.app.android.estar.ui;

import java.io.IOException;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.CacheProvider;
import com.mcds.app.android.estar.provider.DatabaseProvider;
import com.mcds.app.android.estar.provider.PersistenceProvider;
import com.mcds.app.android.estar.provider.SdCardProvider;
import com.mcds.app.android.estar.ui.shop.GiftDetailActivity;
import com.mcds.app.android.estar.ui.shop.GiftListActivity;
import com.mcds.app.android.estar.ui.shop.OrderDetailActivity;
import com.mcds.app.android.estar.ui.shop.OrderListActivity;
import com.mcds.app.android.estar.ui.shop.PayActivity;
import com.mcds.app.android.estar.ui.shop.ShopcarActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);

		doInit();
	}

	private void doInit() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					SdCardProvider.init();
					DatabaseProvider.init(getApplicationContext());
					CacheProvider.init();

					UserManager.userName = PersistenceProvider.getString(PersistenceProvider.USER_NAME, "");
					UserManager.password = PersistenceProvider.getString(PersistenceProvider.PASSWORD, "");
					UserManager.authenticate();

				} catch (final IOException e) {
					SplashActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException ee) {
					ee.printStackTrace();
				}

				startActivity();
			}
		}).start();
	}

	private void startActivity() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//				startActivity(new Intent(SplashActivity.this, XxxxActivity.class));
				finish();
			}
		});
	}
}
