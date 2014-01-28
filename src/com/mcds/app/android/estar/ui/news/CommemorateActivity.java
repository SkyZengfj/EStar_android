package com.mcds.app.android.estar.ui.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.HonorsList;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.ui.BaseTabActivity;
import com.mcds.app.android.estar.util.Utility;

public class CommemorateActivity extends BaseTabActivity {

	private ListView comListView;
	private CommAdapter adapter;
	private ReturnResult<HonorsList> rrHonors;
	private ReturnResult<String> rrpra;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_commemorate);
		initTabNavigate(R.id.newsComm_tabNavigate, TAB_INDEX_NEWS);
		initListView();
	}

	private void initListView() {
		// TODO Auto-generated method stub
		comListView = (ListView)findViewById(R.id.newsComm_listView);
		adapter = new CommAdapter();
		comListView.setAdapter(adapter);
		getData();
	}
	
	/**
	 * 获取活动列表
	 */
	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrHonors = ConnectProviderZX.getHonorsList(UserManager.uid);
				}
				doListViewUI();

				hideWaitingDialog();
			}
		}).start();
	}

	/**
	 * 更新界面
	 */
	private void doListViewUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (rrHonors.status.equals("0")) {
					adapter.setItems(rrHonors.getDatas());
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	private class CommAdapter extends BaseListAdapter<HonorsList>{

		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null){
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.news_commemorate_item, null);
				holder = new ViewHolder();
				holder.commItemName = (TextView)convertView.findViewById(R.id.news_commemorate_item_name);
				holder.commItemType = (TextView)convertView.findViewById(R.id.news_commemorate_item_type);
				holder.commItemContent = (TextView)convertView.findViewById(R.id.news_commemorate_txt_content);
				holder.commItemAppr = (TextView)convertView.findViewById(R.id.news_commemorate_item_appr);
				
				
				holder.commItemHeaderImage = (ImageView)convertView.findViewById(R.id.news_commemorate_item_header_img);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			
			
			if (holder != null) {
				HonorsList item = this.getItem(position);
				holder.commItemName.setText(item.name);
				holder.commItemType.setText(item.recognition);
				holder.commItemContent.setText(item.content);
				holder.commItemAppr.setText(item.praise);

				if(item.praise.equals("")){
					holder.commItemAppr.setText("0");
				}
				
				
				holder.commItemAppr.setTag(item);
				holder.commItemAppr.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final HonorsList item = (HonorsList)v.getTag();
						new Thread(new Runnable() {

							@Override
							public void run() {
								
								if (!UserManager.uid.equals("")) {
									rrpra = ConnectProviderZX.goodNews(UserManager.uid, item.news_id);
									if(rrpra.status.equals("0")){
										getData();
									}
								}
								
							}
						}).start();
					}
				});
				
				Bitmap bmp = asyncLoader.loadBitmap(
						holder.commItemHeaderImage, 
						item.image_url, 
						Utility.dip2px(CommemorateActivity.this, 100), 
						Utility.dip2px(CommemorateActivity.this, 100), 
						new ImageCallBack() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp != null) {
					holder.commItemHeaderImage.setImageBitmap(bmp);
				}
			}
			return convertView;
		}
		
	}
	
	private class ViewHolder {
		ImageView commItemHeaderImage;
		TextView commItemType;
		TextView commItemName;
		TextView commItemContent;
		TextView commItemAppr;
	}
	
}
