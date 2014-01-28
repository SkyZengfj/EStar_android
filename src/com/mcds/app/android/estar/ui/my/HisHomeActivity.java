package com.mcds.app.android.estar.ui.my;

import java.util.Date;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.MyAtMeInfo;
import com.mcds.app.android.estar.bean.MyInfo;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.ui.weibo.HomeListDetailsActivity;
import com.mcds.app.android.estar.util.LayoutUtility;
import com.mcds.app.android.estar.util.Utility;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 他人主页
 * @author chenliang
 *
 */
public class HisHomeActivity extends BaseActivity{
	private ReturnResult<MyInfo> rrHisInfo;
	private ImageView hisHome_hisAvatar;
	private TextView hisHome_userName;
	private ImageView hisHome_sexIcon;
	private TextView hisHome_deptName; 
	private LinearLayout his_btn_homeSignature; 
	private TextView hisHome_puesto; //职位
	private TextView his_homeSignature; //个性签名
	
	private RelativeLayout his_btn_attention;
	private TextView his_attentionCount;
	
	private RelativeLayout his_btn_accept;
	private TextView his_acceptCount;
	
	private RelativeLayout his_btn_fans;
	private TextView his_fansCount;
	
	private RelativeLayout his_btn_relationWithMe;
	private ImageView his_img_relation;
	private TextView his_favCount;
	
	
	private ListView his_home_list_selfweibo;
	private ScrollView scrollview;
	private HisHomeAdapter hisHomeAdapter;
	private ReturnResult<MyAtMeInfo> myAtMeInfos;
	private int page_num = 0;
	private final String PAGE_SIZE = "10";
	private String w_type = "0";//0:全部 1:肯定 2:祝福 3:信息 4:活动 5.上传文档
	private String hisId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.his_home);
		hisHome_hisAvatar = (ImageView) findViewById(R.id.hisHome_hisAvatar);
		hisHome_userName = (TextView) findViewById(R.id.hisHome_userName);
		hisHome_sexIcon = (ImageView) findViewById(R.id.hisHome_sexIcon);
		hisHome_deptName = (TextView) findViewById(R.id.hisHome_deptName);
		his_btn_homeSignature = (LinearLayout) findViewById(R.id.his_btn_homeSignature);
		hisHome_puesto = (TextView) findViewById(R.id.hisHome_puesto); //职位
		his_homeSignature  = (TextView) findViewById(R.id.his_homeSignature);//个性签名
		
		his_btn_attention = (RelativeLayout) findViewById(R.id.his_btn_attention);
		his_attentionCount = (TextView) findViewById(R.id.his_attentionCount);
		his_btn_attention.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent  = new Intent(getApplicationContext(),HisAttentionActivity.class);
				startActivity(intent);
			}
		});
		
		his_btn_accept = (RelativeLayout) findViewById(R.id.his_btn_accept);
		his_acceptCount = (TextView) findViewById(R.id.his_acceptCount);
		
		his_btn_fans = (RelativeLayout) findViewById(R.id.his_btn_fans);
		his_fansCount = (TextView) findViewById(R.id.his_fansCount);
		his_btn_fans.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),HisFansActivity.class);
				startActivity(intent);
			}
		});
		
		his_btn_relationWithMe = (RelativeLayout) findViewById(R.id.his_btn_relationWithMe);
		his_favCount = (TextView) findViewById(R.id.his_favCount);
		his_img_relation = (ImageView) findViewById(R.id.his_img_relation);
		hisHome_hisAvatar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent  intent= new Intent (getApplicationContext(),HisDocumentActivity.class);
				intent.putExtra("hisDocument", 22);
				
				startActivity(intent);
			}
		});
		
		
		
		
		Intent in = getIntent();
		hisId = in.getStringExtra("hisId");
		his_btn_accept = (RelativeLayout) findViewById(R.id.his_btn_accept);
		his_home_list_selfweibo = (ListView) findViewById(R.id.his_home_list_selfweibo);
		scrollview = (ScrollView)HisHomeActivity.this.findViewById(R.id.his_home_scrollview);
		hisHomeAdapter = new HisHomeAdapter();
		his_home_list_selfweibo.setAdapter(hisHomeAdapter);
		//认可事件
		his_btn_accept.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!"".equals(hisId)&&hisId!=null) {
					Intent intentGetYes = new Intent(HisHomeActivity.this, MyWeiboPraisedActivity.class);
					intentGetYes.putExtra("type", 2);
					intentGetYes.putExtra("uid", hisId);
					startActivity(intentGetYes);
				}
			}
		});
		// 列表点击事件
		his_home_list_selfweibo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				 MyAtMeInfo item = (MyAtMeInfo)arg0.getAdapter().getItem(arg2);
				 String weibo_id = item.weibo_id;
				
				 Intent intent = new Intent(HisHomeActivity.this, HomeListDetailsActivity.class);
				 intent.putExtra("weibo_id", weibo_id);
				 startActivity(intent);
			}
		});
		
		his_home_list_selfweibo.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					if (view.getLastVisiblePosition() == view.getCount() - 1) {
						page_num = page_num + 1;
						getData();
						hisHomeAdapter.notifyDataSetChanged();
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
				
				
				if (!"".equals(hisId)) {
					rrHisInfo = ConnectProvider.getMyInfo(UserManager.uid);
					myAtMeInfos = ConnectProvider.getMyselfWeiboList(UserManager.uid, w_type,"", String.valueOf(page_num), PAGE_SIZE);
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
				if (rrHisInfo.status.equals("0")) {
					System.out.println("返回状态" + rrHisInfo.info);

					MyInfo info = new MyInfo();
					info = rrHisInfo.getDatas().get(0);
					
					/*
					 * 获取用户头像
					 */
					AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
					Bitmap bmp = asyncLoader.loadBitmap(hisHome_hisAvatar, info.user_img, 200, 195, new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (bitmap == null) {
								bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.my_icon_homeheadpic);
							}
							imageView.setImageBitmap(bitmap);
						}
					});
					// 设置用户头像
					if (bmp != null) {
						hisHome_hisAvatar.setImageBitmap(bmp);
					}
					hisHome_userName.setText(info.user_name);// 设置用户名
					// 男女图标
					if (info.user_sex.equals("男")) {
						hisHome_sexIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_sex_man));
					} else {
						hisHome_sexIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_sex_women));
					}
//					myHome_deptName.setText(info.user_work);
					hisHome_puesto.setText(info.user_work);
					his_homeSignature.setText(info.intro);
					his_attentionCount.setText(info.attention_num);// 有疑问
					his_acceptCount.setText(info.approved_num); // 认可
					his_fansCount.setText(info.attentioned_num); //
					
					
				}
				
				if (myAtMeInfos.status.equals("0")) {
					hisHomeAdapter.addItems(myAtMeInfos.getDatas());
					LayoutUtility.setListViewHeightBasedOnChildren(his_home_list_selfweibo);
					hisHomeAdapter.notifyDataSetChanged();
					scrollview.smoothScrollTo(0,0);
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
	TextView good_num = null;//		赞

	
	//列表数据
		private class HisHomeAdapter extends BaseListAdapter<MyAtMeInfo> {
			private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				MyAtMeInfo item = this.getItem(position);
				if (convertView==null) {
					convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.his_home_list_item, null);
					user_name = (TextView) convertView.findViewById(R.id.his_home_list_item_user_name);
					user_avatar = (ImageView) convertView.findViewById(R.id.his_home_list_item_user_avatar);
					user_from = (TextView) convertView.findViewById(R.id.his_home_list_item_user_from);
					type_icon = (ImageView) convertView.findViewById(R.id.his_home_list_item_message_type_icon);
					attention_flag = (TextView) convertView.findViewById(R.id.his_home_list_item_attention_flag);
					message_content = (TextView) convertView.findViewById(R.id.his_home_list_item_message_content);
					share_num = (TextView) convertView.findViewById(R.id.his_home_list_item_forwording_count);
					comment_num = (TextView) convertView.findViewById(R.id.his_home_list_item_comment_count);
					good_num = (TextView) convertView.findViewById(R.id.his_home_list_item_praise_count);
					
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
						sb.append(days+"天前");
					}else {
						if (hours>0) {
					    	sb.append(hours+"小时前");
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
					if ("0".equals(item.weibo_type_b)) {
						type_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_help));
					}else if ("1".equals(item.weibo_type_b)) {
						type_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_activity));
					}
				}else {
					type_icon.setVisibility(LinearLayout.GONE);
				}
//				user_avatar.setOnClickListener(new View.OnClickListener() {
//					
//					@Override
//					public void onClick(View arg0) {
//						// TODO Auto-generated method stub
//						Toast.makeText(HisHomeActivity.this, "点了头像", Toast.LENGTH_SHORT).show();
//					}
//				});
				
				Bitmap bmp = asyncLoader.loadBitmap(user_avatar, item.user_img, Utility.dip2px(HisHomeActivity.this, (float) 40.0), Utility.dip2px(HisHomeActivity.this, (float) 40.0), new ImageCallBack() {

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
