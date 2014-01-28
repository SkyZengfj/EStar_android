package com.mcds.app.android.estar.ui.work;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.MatterDetail;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderTC;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 自定义对话框
 * 
 * @author TangChao
 */
public class MyMatterDialog extends BaseActivity implements OnClickListener
{
	private static final String Tag = "MY_MATTER_DIALOG";
	private TextView matter_edit_btn;
	private TextView matter_sign_btn;
	private TextView matter_cancel_btn;
	private TextView matter_delete_btn;
	private MatterDetail matterDetail;
	private String theme_id;
	/**
	 * 设置主题完成
	 */
	private ReturnResult<String> rrSetMatterComplete;
	/**
	 * 删除主题
	 */
	private ReturnResult<String> rrDeleteTheme;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		this.setContentView(R.layout.work_home_matter_detail_edit_dialog);
		matter_edit_btn = (TextView) findViewById(R.id.matter_edit_btn);
		matter_sign_btn = (TextView) findViewById(R.id.matter_sign_btn);
		matter_delete_btn = (TextView) findViewById(R.id.matter_delete_btn);
		matter_cancel_btn = (TextView) findViewById(R.id.matter_cancel_btn);
		matter_edit_btn.setOnClickListener(this);
		matter_sign_btn.setOnClickListener(this);
		matter_delete_btn.setOnClickListener(this);
		matter_cancel_btn.setOnClickListener(this);
		Intent intent = getIntent();
		theme_id = intent.getStringExtra("theme_id");
		matterDetail = (MatterDetail) intent.getSerializableExtra("MATTER_DETAIL_OBJECT");
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		finish();
	}
	/**
	 * 设置事项完成请求
	 */
	private void setMatterComplete(final String theme_id)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!UserManager.uid.equals(""))
				{
					rrSetMatterComplete = ConnectProviderTC.setThemeComplete(UserManager.uid, theme_id);
					doUIForSign();
				}
				hideWaitingDialog();
			}
		}).start();
	}
	/**
	 * 删除事项的请求
	 */
	private void deleteTheme(final String theme_id)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!UserManager.uid.equals(""))
				{
					rrDeleteTheme = ConnectProviderTC.deleteMatter(UserManager.uid, theme_id);
					doUIForDelete();
				}
				hideWaitingDialog();
			}
		}).start();
	}
	/**
	 * 请求成功后通知界面刷新
	 */
	private void doUIForDelete()
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Log.i("deleteMatter.status----------->", rrDeleteTheme.status);
				if (rrDeleteTheme.status.equals("0"))
				{
					Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MyMatterDialog.this, HomeActivity.class);
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
	private void doUIForSign()
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Log.i("setMatterComplete.status----------->", rrSetMatterComplete.status);
				if (rrSetMatterComplete.status.equals("0"))
				{
					Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MyMatterDialog.this, MatterDetialActivity.class);
					intent.putExtra("theme_id", theme_id);
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
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.matter_edit_btn:// 编辑
				Intent intent = new Intent(this, AddMatterActivity.class);
				intent.putExtra("theme_id", theme_id);
				intent.putExtra("SerializableObject", matterDetail);
				intent.putExtra("TAG", Tag);
				startActivity(intent);
				break;
			case R.id.matter_sign_btn:// 标记
				setMatterComplete(theme_id);
				break;
			case R.id.matter_delete_btn:// 删除
				deleteTheme(theme_id);
				break;
			case R.id.matter_cancel_btn:// 取消
				if (!this.isFinishing())
				{
					finish();
				}
				break;
			default:
				break;
		}
	}
}
