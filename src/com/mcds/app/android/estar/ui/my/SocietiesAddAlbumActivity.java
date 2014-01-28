package com.mcds.app.android.estar.ui.my;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 新建相册
 * 
 * @author TangChao
 */
public class SocietiesAddAlbumActivity extends BaseActivity implements OnClickListener
{
	private TextView saveBtn;
	private TextView cancelBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.my_societies_add_album);
		saveBtn = (TextView) findViewById(R.id.societies_save);
		cancelBtn = (TextView) findViewById(R.id.societies_cancel);
		saveBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.societies_save:// 确定
				break;
			case R.id.societies_cancel:// 取消
				finish();
				break;
			default:
				break;
		}
	}
}
