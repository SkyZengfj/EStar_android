package com.mcds.app.android.estar.ui.my;

import android.graphics.PixelFormat;
import android.os.Bundle;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 共享
 * @author TangChao
 *
 */
public class SocitiesShareActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.my_societies_share);
	}
	
}
