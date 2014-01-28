package com.mcds.app.android.estar.ui.my;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
 * 他人的粉丝
 * @author Administrator
 *
 */
public class HisFansActivity extends BaseTabActivity{
	private ImageView his_fans_relation;
	private int page_num = 0;
	private final String PAGE_SIZE = "10";
	private ListView his_fansListView;
	private HisFansAdapter adapter;
	private ReturnResult<MyFans> rrFans;
	private ReturnResult<AttentionSomeone> rrAttentionSomeone; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.his_fans);
		his_fansListView = (ListView) findViewById(R.id.his_fansListView);
		his_fans_relation = (ImageView) findViewById(R.id.his_fans_relation);
		adapter = new HisFansAdapter();
		his_fansListView.setAdapter(adapter);
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
					adapter.setItems(rrFans.getDatas());
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	private class HisFansAdapter extends BaseListAdapter<MyFans> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.his_fans_item, null);
				holder = new ViewHolder();
				holder.his_fans_headPic = (ImageView) convertView.findViewById(R.id.his_fans_headPic);
				holder.his_fans_userName = (TextView) convertView.findViewById(R.id.his_fans_userName);
				holder.his_fans_sex_icon = (ImageView) convertView.findViewById(R.id.his_fans_sex_icon);
				holder.his_fans_attentionWork = (TextView) convertView.findViewById(R.id.his_fans_attentionWork);
				holder.his_fans_relation = (ImageButton) convertView.findViewById(R.id.his_fans_relation);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder != null) {
				final MyFans item = this.getItem(position);
				holder.his_fans_userName.setText(item.user_name);
				holder.his_fans_attentionWork.setText(item.user_work);
				
				/*
				 * 粉丝头像
				 */
				Bitmap bmp = asyncLoader.loadBitmap(holder.his_fans_headPic, item.user_img, 100, 100, new ImageCallBack() {
					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp != null) {
					holder.his_fans_headPic.setImageBitmap(bmp);
				}
				
				/*
				 * 粉丝性别图标
				 */
				if (item.user_sex.equals ("10107")) {
					holder.his_fans_sex_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_sex_man));
				}else{
					holder.his_fans_sex_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_sex_women));
				}
				
				/*
				 * 粉丝关系
				 *0：为关注 1：已关注  2：互粉 
				 */
				if (item.attention_flag.equals("0")) {
					holder.his_fans_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_addattention_selector));
				}if (item.attention_flag.equals("1")) {
					holder.his_fans_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioned_selector));
				}if (item.attention_flag.equals("2")) {
					holder.his_fans_relation.setClickable(false);
					holder.his_fans_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioneach_selector));
				}
				
				/*
				 * 点击+关注  
				 */
				holder.his_fans_relation.setOnClickListener(new View.OnClickListener() {
					
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
									Toast toast = Toast.makeText(HisFansActivity.this, "添加关注失败",Toast.LENGTH_SHORT );
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
		
		ImageView his_fans_headPic;
		TextView his_fans_userName;
		ImageView his_fans_sex_icon;
		TextView his_fans_attentionWork;
		ImageButton his_fans_relation;
	}
}
