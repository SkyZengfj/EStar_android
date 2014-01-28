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

public class MyDeleteMissionListDialogActivity extends BaseActivity implements OnClickListener
{
	private TextView deleteBtn;
	private ReturnResult<String> rrDelete;
	private String mission_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.work_home_delete_dialog);
		deleteBtn = (TextView) findViewById(R.id.work_home_delete_list_delete_btn);
		mission_id = getIntent().getStringExtra("mission_id");
		deleteBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.work_home_delete_list_delete_btn)
		{
			deleteMission(mission_id);
		}
	}
	/**
	 * 删除任务
	 */
	private void deleteMission(final String mission_id)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!UserManager.uid.equals(""))
				{
					rrDelete = ConnectProviderTC.deleteMission(UserManager.uid, mission_id);
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
					Intent intent = new Intent(MyDeleteMissionListDialogActivity.this, MissionListActivity.class);
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
