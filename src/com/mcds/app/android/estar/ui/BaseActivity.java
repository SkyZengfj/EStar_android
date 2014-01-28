package com.mcds.app.android.estar.ui;

import com.mcds.app.android.estar.manager.ActivityTaskManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends Activity {
	protected ProgressDialog waitingDialog = null;
	private int showCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityTaskManager.getInstance().putActivity(this.getClass().toString(), this);

		waitingDialog = new ProgressDialog(BaseActivity.this);
		waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		waitingDialog.setMessage("请稍等....");
		waitingDialog.setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			if (!isNetworkConnected()) {
				Toast.makeText(getApplicationContext(), "请检查网络连接是否正常.", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		ActivityTaskManager.getInstance().closeActivity(this.getClass().toString());

		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, // 组号
				Menu.FIRST, // 唯一的ID号
				Menu.FIRST, // 排序号
				"退出"); // 标题

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case Menu.FIRST:
				new AlertDialog.Builder(BaseActivity.this)//
						.setTitle("提示")//
						.setMessage("是否退出本软件？")//
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {//
									@Override
									public void onClick(DialogInterface dialog, int i) {
										ActivityTaskManager.getInstance().closeAllActivity();
										android.os.Process.killProcess(android.os.Process.myPid());
									}
								})//
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int i) {
								dialog.dismiss();
							}
						})//
						.show();// 显示对话框
				break;
			default:
				// 对没有处理的事件，交给父类来处理
				return super.onOptionsItemSelected(item);
		}
		// 返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
		return true;
	}

	protected synchronized void showWaitingDialog() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (showCount == 0) {
					waitingDialog.show();
				}
				showCount++;
			}
		});
	}

	protected synchronized void hideWaitingDialog() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				showCount--;
				if (showCount == 0) {
//					waitingDialog.hide();
					waitingDialog.dismiss();
					showCount = 0;
				}
			}
		});
	}

	protected boolean isNetworkConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}
		return false;
	}
}
