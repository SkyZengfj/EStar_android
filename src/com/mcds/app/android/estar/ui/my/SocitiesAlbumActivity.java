package com.mcds.app.android.estar.ui.my;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 社团相册
 * 
 * @author TangChao
 */
public class SocitiesAlbumActivity extends BaseActivity implements OnClickListener, OnItemClickListener
{
	private ImageButton cancelBtn;
	private ImageButton addBtn;
	private GridView gridView;
	
	// private AlbumAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.my_societies_album);
		cancelBtn = (ImageButton) findViewById(R.id.societies_album_cancel);
		addBtn = (ImageButton) findViewById(R.id.societies_album_add);
		gridView = (GridView) findViewById(R.id.societies_album_list_grid_view);
		cancelBtn.setOnClickListener(this);
		addBtn.setOnClickListener(this);
		// adapter = new AlbumAdapter();
		// gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
	}
	/**
	 * 请求相册列表
	 */
	private void getSocietiesAlbum()
	{
	}
	/**
	 * 社团相册适配器
	 */
	// private class AlbumAdapter extends BaseListAdapter<T>
	// {
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent)
	// {
	// // TODO Auto-generated method stub
	// return null;
	// }
	// }
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.societies_album_cancel:// 取消
				finish();
				break;
			case R.id.societies_album_add:// 新建相册
				 Intent intent = new Intent(this,SocietiesAddAlbumActivity.class);
				 startActivity(intent);
				break;
			default:
				break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		Intent intent = new Intent(this, SocietiesAlbumDetail.class);
		startActivity(intent);
	}
}
