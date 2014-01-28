package com.mcds.app.android.estar.ui.my;

import java.util.Date;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.MyAtMeInfo;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.ui.weibo.HomeListDetailsActivity;
import com.mcds.app.android.estar.util.JsonUtility;
import com.mcds.app.android.estar.util.Utility;

import android.R.integer;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MyAtMeActivity extends BaseActivity {

	
	private TextView my_at_me_layout_title;
	private ImageButton my_at_me_back_button;
	private ListView my_at_me_layout_listView;
	private MyAtMeAdapter mamAdapter;
	private ReturnResult<MyAtMeInfo> myAtMeInfos;
	private int page_num = 0;
	private final String PAGE_SIZE = "10";
	private String at_type = "0";//0:全部动态（默认） 1:我关注的 2:原创
	private String w_type = "0";//0:全部 1:肯定 2:祝福 3:信息 4:活动 5.上传文档
	private int type = 0;//0：动态;1:提到我的动态;2：收藏
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_at_me);
		Intent in = getIntent();
		type = in.getIntExtra("type", type);
		my_at_me_layout_title = (TextView) findViewById(R.id.my_at_me_layout_title);
		my_at_me_back_button = (ImageButton) findViewById(R.id.my_at_me_back_button);
		my_at_me_layout_listView = (ListView) findViewById(R.id.my_at_me_layout_listView);
		if (type==0) {
			my_at_me_layout_title.setText("动态");
		}else if(type==1) {
			my_at_me_layout_title.setText("提到我的动态");
		}else if(type==2) {
			my_at_me_layout_title.setText("收藏");
		}
		mamAdapter = new MyAtMeAdapter();
		my_at_me_layout_listView.setAdapter(mamAdapter);
		
		//返回按钮
		my_at_me_back_button.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent i = new Intent(MyAtMeActivity.this, com.mcds.app.android.estar.ui.my.HomeActivity.class);
//				startActivity(i);
				MyAtMeActivity.this.finish();
			}
		});
		
		// 列表点击事件
		my_at_me_layout_listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					 MyAtMeInfo item = (MyAtMeInfo)arg0.getAdapter().getItem(arg2);
					 String weibo_id = item.weibo_id;
					
					 Intent intent = new Intent(MyAtMeActivity.this, HomeListDetailsActivity.class);
					 intent.putExtra("weibo_id", weibo_id);
					 startActivity(intent);
				}
			});
		my_at_me_layout_listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						page_num = page_num + 1;
						getData();
						mamAdapter.notifyDataSetChanged();
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

				if (!"".equals(UserManager.uid)) {
					if (type==0) {//动态
						myAtMeInfos = ConnectProvider.getMyselfWeiboList(UserManager.uid, w_type,"", String.valueOf(page_num), PAGE_SIZE);
					}else if (type==1) {//提到我的动态
						myAtMeInfos = ConnectProvider.getAtList(UserManager.uid,at_type, String.valueOf(page_num), PAGE_SIZE);
					}else if (type==2) {//动态收藏
						myAtMeInfos = ConnectProvider.getWeiboFavorList(UserManager.uid, String.valueOf(page_num), PAGE_SIZE);
					}
					
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
				if (myAtMeInfos.status.equals("0")) {
					mamAdapter.addItems(myAtMeInfos.getDatas());
					mamAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	TextView user_name = null;
	ImageView user_avatar = null;
	TextView user_from = null;//	微博来源（0:手机 1:网站 2:微信）
	TextView attention_flag = null;
	ImageView type_icon = null;
	String weibo_type = "";//	微博类型（0:肯定动态 1:祝福动态 2:信息动态 3:活动动态 4: 上传文档动态 5:其他）
	String weibo_type_b = "";//	"0":求助动态  "1":其他活动动态  ""/null:当 weibo_type不为3的时候此字段无值，也不用考虑
	TextView message_content = null;
	TextView share_num = null;//	转发
	TextView comment_num = null;//	评论 
	TextView good_num = null;//	赞

	
	//列表数据
		private class MyAtMeAdapter extends BaseListAdapter<MyAtMeInfo> {
			private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				final MyAtMeInfo item = this.getItem(position);
				if (convertView==null) {
					convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_at_me_item, null);
					user_name = (TextView) convertView.findViewById(R.id.my_at_me_item_user_name);
					user_avatar = (ImageView) convertView.findViewById(R.id.my_at_me_item_user_avatar);
					user_from = (TextView) convertView.findViewById(R.id.my_at_me_item_user_from);
					type_icon = (ImageView) convertView.findViewById(R.id.my_at_me_item_message_type_icon);
					attention_flag = (TextView) convertView.findViewById(R.id.my_at_me_item_attention_flag);
					message_content = (TextView) convertView.findViewById(R.id.my_at_me_item_message_content);
					share_num = (TextView) convertView.findViewById(R.id.my_at_me_item_forwording_count);
					comment_num = (TextView) convertView.findViewById(R.id.my_at_me_item_comment_count);
					good_num = (TextView) convertView.findViewById(R.id.my_at_me_item_praise_count);
					
				}
				user_name.setText(item.user_name);
				user_from.setText("来源："+item.from);
				share_num.setText(item.share_num);
				comment_num.setText(item.comment_num);
				good_num.setText(item.good_num);
				if (!"".equals(item.time)) {
					long l = Long.parseLong(item.time);
					long mss = new Date().getTime() - l;
					long days = mss / (1000 * 60 * 60 * 24);  
				    long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);  
				    long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);  
//				    long seconds = (mss % (1000 * 60)) / 1000;  
				    StringBuffer sb = new StringBuffer();
				    if (days>0) {
						sb.append(days+"天");
					}else {
						if (hours>0) {
					    	sb.append(hours+"小时");
						}else {
							if (minutes>0) {
						    	sb.append(minutes+"分钟前");
							}else {
								sb.append("刚刚");
							}
						}
					}
				    
					attention_flag.setText(sb.toString());
				}
				message_content.setText(item.content_text);
				if ("0".equals(item.weibo_type)) {
					type_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_affirmation));
				}else if ("1".equals(item.weibo_type)) {
					type_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_blessing));
				}else if ("2".equals(item.weibo_type)) {
					type_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_message));
				}else if ("3".equals(item.weibo_type)) {
					if (type == 2) {//收藏时
						type_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_help));
					}else {
						if ("0".equals(item.weibo_type_b)) {
							type_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_help));
						}else if ("1".equals(item.weibo_type_b)) {
							type_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_activity));
						}
					}
				}else {
					type_icon.setVisibility(LinearLayout.GONE);
				}
				
				if (!"".equals(item.uid)) {
					user_avatar.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent itt = new Intent(MyAtMeActivity.this,HisHomeActivity.class);
							itt.putExtra("hisId", item.uid);
							startActivity(itt);
						}
					});
				}

				Bitmap bmp = asyncLoader.loadBitmap(user_avatar, item.user_img, Utility.dip2px(MyAtMeActivity.this, (float) 40.0), Utility.dip2px(MyAtMeActivity.this, (float) 40.0), new ImageCallBack() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp != null) {
					user_avatar.setImageBitmap(bmp);
				}
				
				return convertView;
			}
		}
}
