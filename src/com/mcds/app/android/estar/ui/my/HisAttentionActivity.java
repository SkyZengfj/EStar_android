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
import com.mcds.app.android.estar.bean.MyAttentionMain;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseTabActivity;

/**
 * 他人关注
 * @author chenliang
 *
 */
public class HisAttentionActivity extends BaseTabActivity{
	private ImageView his_attention_relation;
	private int page_num = 0;
	private final String PAGE_SIZE = "10";
	private ListView his_attentionListView;
	private HisAttentionAdapter adapter;
	private ReturnResult<MyAttentionMain> rrattention;
	private ReturnResult<AttentionSomeone> rrAttentionSomeone; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hisattention);
		his_attentionListView = (ListView) findViewById(R.id.his_attentionListView);
		his_attention_relation = (ImageView) findViewById(R.id.his_attention_relation);
		adapter = new HisAttentionAdapter();
		his_attentionListView.setAdapter(adapter);
		getData();
	}
	
	

	private void getData() {
		new Thread(new Runnable() {
 
			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrattention = ConnectProvider.getMyAttentionList(UserManager.uid, String.valueOf(page_num), PAGE_SIZE);
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
				if (rrattention.status.equals("0")) {
					adapter.setItems(rrattention.getDatas());
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	private class HisAttentionAdapter extends BaseListAdapter<MyAttentionMain> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.xxxx_list_item, null);
//
//			Weibo item = getItem(position);
//			((TextView) convertView.findViewById(R.id.xxxxListItem_txtName)).setText(item.user_name);
//
//			return convertView;

			ViewHolder holder = null;

			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.his_attention_item, null);

				holder = new ViewHolder();
				holder.his_attention_headPic = (ImageView) convertView.findViewById(R.id.his_attention_headPic);
				holder.his_attention_userName = (TextView) convertView.findViewById(R.id.his_attention_userName);
				holder.his_attention_sex_icon = (ImageView) convertView.findViewById(R.id.his_attention_sex_icon);
				holder.his_attention_attentionWork = (TextView) convertView.findViewById(R.id.his_attention_attentionWork);
				holder.his_attention_relation = (ImageButton) convertView.findViewById(R.id.his_attention_relation);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder != null) {
				final MyAttentionMain item = this.getItem(position);
				holder.his_attention_userName.setText(item.user_name);
				holder.his_attention_attentionWork.setText(item.user_work);
				
				/*
				 * 关注头像
				 */
				Bitmap bmp = asyncLoader.loadBitmap(holder.his_attention_headPic, item.user_img, 100, 100, new ImageCallBack() {
					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp != null) {
					holder.his_attention_headPic.setImageBitmap(bmp);
				}
				
				/*
				 * 关注性别图标
				 */
				if (item.user_sex.equals ("10107")) {
					holder.his_attention_sex_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_sex_man));
				}else{
					holder.his_attention_sex_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_sex_women));
				}
				
				/*
				 * 关注关系
				 *0：为关注 1：已关注  2：互粉 
				 */
				if (item.attention_flag.equals("0")) {
					holder.his_attention_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_addattention_selector));
				}if (item.attention_flag.equals("1")) {
					holder.his_attention_relation.setClickable(false);
					holder.his_attention_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioned_selector));
				}if (item.attention_flag.equals("2")) {
					holder.his_attention_relation.setClickable(false);
					holder.his_attention_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioneach_selector));
				}
				
				/*
				 * 点击+关注  
				 */
				holder.his_attention_relation.setOnClickListener(new View.OnClickListener() {
					
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
									Toast toast = Toast.makeText(HisAttentionActivity.this, "添加关注失败",Toast.LENGTH_SHORT );
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
		
		ImageView his_attention_headPic;
		TextView his_attention_userName;
		ImageView his_attention_sex_icon;
		TextView his_attention_attentionWork;
		ImageButton his_attention_relation;
	}
}
