package com.mcds.app.android.estar.ui.my;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.AttentionSomeone;
import com.mcds.app.android.estar.bean.MyFans;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseTabActivity;

/**
 * 粉丝
 * @author chenliang
 *
 */
public class FansActivity extends BaseTabActivity{
	private ImageView my_fans_relation;
	private int page_num = 0;
	private final String PAGE_SIZE = "9";
	private ListView my_fansListView;
	private MyFansAdapter adapter;
	private ReturnResult<MyFans> rrFans;
	private ReturnResult<AttentionSomeone> rrAttentionSomeone; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_fans);
		my_fansListView = (ListView) findViewById(R.id.my_fansListView);
		my_fans_relation = (ImageView) findViewById(R.id.my_fans_relation);
		adapter = new MyFansAdapter();
		my_fansListView.setAdapter(adapter);
		my_fansListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						page_num = page_num + 1;
						getData();
						adapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
		
		getData();
		
				
	}
	
	

	private void getData() {
		new Thread(new Runnable() {
 
			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrFans = ConnectProvider.getMyFollowerList(UserManager.uid, String.valueOf(page_num), PAGE_SIZE);
					doListViewUI();
				}

				hideWaitingDialog();
			}
		}).start();
	}
	

	private void doListViewUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				//还要判断一次解决断网退出的异常
				if (rrFans.status.equals("0")) {
					adapter.addItems(rrFans.getDatas());
					adapter.notifyDataSetChanged();
				}
			}
		});
		
	}

	private class MyFansAdapter extends BaseListAdapter<MyFans> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_fans_item, null);
				holder = new ViewHolder();
				holder.my_fans_headPic = (ImageView) convertView.findViewById(R.id.my_fans_headPic);
				holder.my_fans_userName = (TextView) convertView.findViewById(R.id.my_fans_userName);
				holder.my_fans_sex_icon = (ImageView) convertView.findViewById(R.id.my_fans_sex_icon);
				holder.my_fans_attentionWork = (TextView) convertView.findViewById(R.id.my_fans_attentionWork);
				holder.my_fans_relation = (ImageButton) convertView.findViewById(R.id.my_fans_relation);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder != null) {
				final MyFans item = this.getItem(position);
				holder.my_fans_userName.setText(item.user_name);
				holder.my_fans_attentionWork.setText(item.user_work);
				
				/*
				 * 粉丝头像
				 */
				Bitmap bmp = asyncLoader.loadBitmap(holder.my_fans_headPic, item.user_img, 100, 100, new ImageCallBack() {
					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp != null) {
					holder.my_fans_headPic.setImageBitmap(bmp);
				}
				
				/*
				 * 粉丝性别图标
				 */
				if (item.user_sex.equals ("10107")) {
					holder.my_fans_sex_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_sex_man));
				}else{
					holder.my_fans_sex_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_sex_women));
				}
				
				/*
				 * 粉丝关系
				 *0：为关注 1：已关注  2：互粉 
				 */
				if (item.attention_flag.equals("0")) {
					holder.my_fans_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_addattention_selector));
				}if (item.attention_flag.equals("1")) {
					holder.my_fans_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioned_selector));
				}if (item.attention_flag.equals("2")) {
					holder.my_fans_relation.setClickable(false);
					holder.my_fans_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioneach_selector));
				}
				
				/*
				 * 点击+关注  
				 */
				holder.my_fans_relation.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new Thread(new Runnable() {
							 
							@Override
							public void run() {
								showWaitingDialog();

								if (!UserManager.uid.equals("")) {
									rrAttentionSomeone = ConnectProvider.attentionSomeone(UserManager.uid, item.uid, "0");
								}
								
								if (rrAttentionSomeone.status.equals("0")) {
									System.out.println("关注返回结果："+rrAttentionSomeone.info);
									if (rrAttentionSomeone.info.equals("ok")) {
										getData();
									}
								}else{
									Toast toast = Toast.makeText(FansActivity.this, "添加关注失败",Toast.LENGTH_SHORT );
									toast.show();

								}
								hideWaitingDialog();
							}
						}).start();
						
					}
				});
				
			}
			return convertView;
		}

		
		
		
	}

	private class ViewHolder {
		
		ImageView my_fans_headPic;
		TextView my_fans_userName;
		ImageView my_fans_sex_icon;
		TextView my_fans_attentionWork;
		ImageButton my_fans_relation;
	}
	
	
	
}
