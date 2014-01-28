package com.mcds.app.android.estar.ui.work;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderTC;
import com.mcds.app.android.estar.ui.BaseActivity;

public class MyDeleteMatterListDialogActivity extends BaseActivity implements OnClickListener
{
	private String theme_id;
	/**
	 * 删除按钮
	 */
	private TextView deleteBtn;
	/**
	 * 请求响应
	 */
	private ReturnResult<String> rrDelete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.work_home_delete_dialog);
		deleteBtn = (TextView) findViewById(R.id.work_home_delete_list_delete_btn);
		deleteBtn.setOnClickListener(this);
		theme_id = getIntent().getStringExtra("theme_id");
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		finish();
	}
	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.work_home_delete_list_delete_btn)
		{
			if (v.getId() == R.id.work_home_delete_list_delete_btn)
			{
				deleteMatter(theme_id);
			}
		}
	}
	/**
	 * 删除请求
	 */
	private void deleteMatter(final String theme_id)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!UserManager.uid.equals(""))
				{
					rrDelete = ConnectProviderTC.deleteMatter(UserManager.uid, theme_id);
					doUI();
				}
				hideWaitingDialog();
			}
		}).start();
	}
	/**
	 * 
	 */
	private void doUI()
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if (rrDelete.status.equals("0"))
				{
					Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MyDeleteMatterListDialogActivity.this, HomeActivity.class);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "操作失败", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		});
	}
}
