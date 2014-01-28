package com.mcds.app.android.estar.ui.weibo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.GoodWeibo;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.Weibo;
import com.mcds.app.android.estar.bean.WeiboComment;
import com.mcds.app.android.estar.bean.WeiboDetail;
import com.mcds.app.android.estar.bean.WeiboForward;
import com.mcds.app.android.estar.bean.WeiboPraise;
import com.mcds.app.android.estar.bean.WeiboSave;
import com.mcds.app.android.estar.bean.WeiboShare;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProviderGC;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.util.Utility;

public class HomeListDetailsActivity extends BaseActivity {
	private ViewFlipper list_viewFlipper;
	
	private LinearLayout comment_list_linearLayout;
	private LinearLayout forwarding_list_linearLayout;
	private LinearLayout praise_list_linearLayout;
	
	private TextView userName;
	private TextView userPost;
	private TextView content;
	private TextView time;
	private TextView title_comment_count;
	private TextView title_comment;
	private TextView title_forward_count;
	private TextView title_forward;
	private TextView title_praise_count;
	private TextView title_praise;
	
	private ImageView avatar;
	private ImageView type_mark_icon;
	private ImageView forwardImageView;
	private ImageView commentImageView;
	private ImageView praiseImageView;
	
	private String weibo_id;
	
	private int list_type = 0;
	
	private ReturnResult<WeiboDetail> result_WeiboDetail;
	private ReturnResult<WeiboComment> result_WeiboComment;
	private ReturnResult<WeiboForward> result_WeiboForward;
	private ReturnResult<WeiboPraise> result_WeiboPraise;
	private ReturnResult<GoodWeibo> result_GoodWeibo;
	private ReturnResult<WeiboSave> result_SaveWeibo;
	
	private AsyncBitmapLoader asyncLoader;
	
	private ListView comment_listView;
	
	private ListView praise_listView;
	private ListView forward_listView;
	private ImageButton more_btn;
	
	private CommentListAdapter comment_listAdapter;
	private ForwardListAdapter forward_listAdapter;
	private PraiseListAdapter praise_listAdapter;
	
	// the list view loading message count in once
	private final String PAGE_SIZE = "10";
	// the list view loading count
	private int page_num = 0;
	
	private boolean isPraise = false;
	
	private WeiboDetail detail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_home_dynamic_details);
		
		weibo_id = getIntent().getStringExtra("weibo_id");

		detail = new WeiboDetail();
		
		userName = (TextView) findViewById(R.id.weiboHome_dynamic_details_userName);
		userPost = (TextView) findViewById(R.id.weiboHome_dynamic_details_userPost);
		content = (TextView) findViewById(R.id.weiboHome_dynamic_details_message_content);
		time = (TextView) findViewById(R.id.weiboHome_dynamic_details_message_send_time);
		title_comment_count = (TextView) findViewById(R.id.weiboHome_dynamic_details_comment_count);
		title_comment = (TextView) findViewById(R.id.weiboHome_dynamic_details_comment);
		title_forward_count = (TextView) findViewById(R.id.weiboHome_dynamic_details_forwarding_count);
		title_forward = (TextView) findViewById(R.id.weiboHome_dynamic_details_forwarding);
		title_praise_count = (TextView) findViewById(R.id.weiboHome_dynamic_details_praise_count);
		title_praise = (TextView) findViewById(R.id.weiboHome_dynamic_details_praise);
		
		more_btn = (ImageButton) findViewById(R.id.weiboHome_dynamic_detail_more_btn);
		
		forwardImageView = (ImageView) findViewById(R.id.dynamic_details_bottom_menu_forwarding);
		commentImageView = (ImageView) findViewById(R.id.dynamic_details_bottom_menu_comment);
		praiseImageView = (ImageView) findViewById(R.id.dynamic_details_bottom_menu_praise);
		
		avatar = (ImageView) findViewById(R.id.weiboHome_dynamic_details_user_avatar);
		type_mark_icon = (ImageView) findViewById(R.id.weiboHome_dynamic_details_message_type_icon);
		
		list_viewFlipper = (ViewFlipper) findViewById(R.id.dynamic_details_bottom_list);
		
		comment_listView = (ListView) findViewById(R.id.dynamic_details_bottom_comment_list);
		comment_listAdapter = new CommentListAdapter();
		comment_listView.setAdapter(comment_listAdapter);
		
		forward_listView = (ListView) findViewById(R.id.dynamic_details_bottom_forward_list);
		forward_listAdapter = new ForwardListAdapter();
		forward_listView.setAdapter(forward_listAdapter);
		
		praise_listView = (ListView) findViewById(R.id.dynamic_details_bottom_praise_list);
		praise_listAdapter = new PraiseListAdapter();
		praise_listView.setAdapter(praise_listAdapter);
		
		comment_list_linearLayout = (LinearLayout) findViewById(R.id.weiboHome_dynamic_details_comment_count_info);
		forwarding_list_linearLayout = (LinearLayout) findViewById(R.id.weiboHome_dynamic_details_forwarding_count_info);
		praise_list_linearLayout = (LinearLayout) findViewById(R.id.weiboHome_dynamic_details_praise_count_info);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择操作");

		builder.setItems(new String[] { "收藏", "分享到新浪微博" },
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (item == 0) {
							if (!UserManager.uid.equals("")) {
								result_SaveWeibo = ConnectProviderGC.SaveWeibo(UserManager.uid, weibo_id);
								System.out.println(result_SaveWeibo.status + "========" + result_SaveWeibo.info);
								if (result_SaveWeibo.status.equals("0")) {
									Toast toast = Toast.makeText(HomeListDetailsActivity.this, "收藏成功", Toast.LENGTH_LONG);
									toast.show();
								}else{
									Toast toast = Toast.makeText(HomeListDetailsActivity.this, result_SaveWeibo.info, Toast.LENGTH_LONG);
									toast.show();
								}
							}
						} else {
							
						}
					}
				});
		final AlertDialog alert = builder.create();
		
		more_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alert.show();
			}
		});
		
		forwardImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeListDetailsActivity.this, HomeListForwordingActivity.class);
				intent.putExtra("weibo_id", weibo_id);
				intent.putExtra("userName", userName.getText());
				intent.putExtra("content", content.getText());
				startActivityForResult(intent, 1);
			}
		});
		
		commentImageView.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeListDetailsActivity.this, HomeListCommentActivity.class);
				intent.putExtra("weibo_id", weibo_id);
				intent.putExtra("original_weibo_id", detail.original_weibo_id);
				startActivityForResult(intent, 2);
			}
		});
		
		praiseImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!isPraise) {
					if (!UserManager.uid.equals("")) {
						result_GoodWeibo = ConnectProviderGC.goodWeibo(UserManager.uid, weibo_id);
						if (result_GoodWeibo.status.equals("0")) {
							String num = String.valueOf(Integer.parseInt((String) title_praise_count.getText()) + 1);
							title_praise_count.setText(num);
							isPraise = true;
						}
					}	
				}else{
					Toast toast = Toast.makeText(HomeListDetailsActivity.this, "你已经赞过该条动态", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
		
		comment_list_linearLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(list_type != 0) {
					if(list_type == 1) {
						comment_list_linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.dynamic_details_bottom_title_selected_bg));
						forwarding_list_linearLayout.setBackgroundColor(0x00000000);
						title_forward.setTextColor(0xff898989);
						title_forward_count.setTextColor(0xff898989);
					}else if(list_type == 2) {
						comment_list_linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.dynamic_details_bottom_title_selected_bg));
						praise_list_linearLayout.setBackgroundColor(0x00000000);
						title_praise.setTextColor(0xff898989);
						title_praise_count.setTextColor(0xff898989);
					}
					title_comment_count.setTextColor(0xff38b6df);
					title_comment.setTextColor(0xff000000);
					list_viewFlipper.setDisplayedChild(0);
					list_type = 0;
				}
			}
		});
		
		forwarding_list_linearLayout.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(list_type != 1) {
							if(list_type == 0) {
								forwarding_list_linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.dynamic_details_bottom_title_selected_bg));
								comment_list_linearLayout.setBackgroundColor(0x00000000);
								title_comment.setTextColor(0xff898989);
								title_comment_count.setTextColor(0xff898989);
							}else if(list_type == 2) {
								forwarding_list_linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.dynamic_details_bottom_title_selected_bg));
								praise_list_linearLayout.setBackgroundColor(0x00000000);
								title_praise.setTextColor(0xff898989);
								title_praise_count.setTextColor(0xff898989);
							}
							title_forward_count.setTextColor(0xff38b6df);
							title_forward.setTextColor(0xff000000);
							list_viewFlipper.setDisplayedChild(1);
							list_type = 1;
						}
					}
				});
		
		praise_list_linearLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(list_type != 2) {
					if(list_type == 0) {
						praise_list_linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.dynamic_details_bottom_title_selected_bg));
						comment_list_linearLayout.setBackgroundColor(0x00000000);
						title_comment.setTextColor(0xff898989);
						title_comment_count.setTextColor(0xff898989);
					}else if(list_type == 1) {
						praise_list_linearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.dynamic_details_bottom_title_selected_bg));
						forwarding_list_linearLayout.setBackgroundColor(0x00000000);
						title_forward.setTextColor(0xff898989);
						title_forward_count.setTextColor(0xff898989);
					}
					title_praise_count.setTextColor(0xff38b6df);
					title_praise.setTextColor(0xff000000);
					list_viewFlipper.setDisplayedChild(2);
					list_type = 2;
				}
			}
		});
		
		getData();
		getCommentData();
		getForwardData();
		getPraiseData();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == 1) {
			if(data.getStringExtra("forward_result").equals("0")) {
				forward_listAdapter.clearItems();
				getForwardData();
				forward_listAdapter.notifyDataSetChanged();
			}
		}else if(resultCode == 2) {
			if(data.getStringExtra("comment_result").equals("0")) {
				comment_listAdapter.clearItems();
				getCommentData();
				comment_listAdapter.notifyDataSetChanged();
			}
			if(data.getStringExtra("comment_forward_result").equals("0")) {
				forward_listAdapter.clearItems();
				getForwardData();
				forward_listAdapter.notifyDataSetChanged();
			}
		}
	}
	
	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					result_WeiboDetail = ConnectProvider.getWeiboDetail(weibo_id);
				}
				
				doWeiboDetailViewUI();

				hideWaitingDialog();
			}
		}).start();
	}
	
	private void getCommentData() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				showWaitingDialog();
				
				if (!UserManager.uid.equals("")) {
					result_WeiboComment = ConnectProvider.getWeiboCommentList(weibo_id, String.valueOf(page_num), PAGE_SIZE);
				}
				
				doCommentListViewUI();
				
				hideWaitingDialog();
			}
		}).start();
	}
	
	private void getForwardData() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				showWaitingDialog();
				
				if (!UserManager.uid.equals("")) {
					result_WeiboForward = ConnectProvider.getWeiboShareList(weibo_id, String.valueOf(page_num), PAGE_SIZE);
				}
				
				doForwardListViewUI();
				
				hideWaitingDialog();
			}
		}).start();
	}
	
	private void getPraiseData() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				showWaitingDialog();
				
				if (!UserManager.uid.equals("")) {
					result_WeiboPraise = ConnectProvider.getWeiboGoodList(weibo_id);
				}
				doPraisetListViewUI();
				
				hideWaitingDialog();
			}
		}).start();
	}
	
	private void doWeiboDetailViewUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (result_WeiboDetail.status.equals("0")) {
					detail = result_WeiboDetail.getDatas().get(0);
					
					userName.setText(detail.user_name);
					userPost.setText(detail.work);
					content.setText(detail.content_text);
					time.setText(detail.time);
					title_comment_count.setText(detail.comment_num);
					title_forward_count.setText(detail.share_num);
					title_praise_count.setText(detail.good_num);
					
					// set message type icon
					if (detail.weibo_type.equals("1")) {
						type_mark_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_affirmation));
					} else if (detail.weibo_type.equals("2")) {
						type_mark_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_blessing));
					} else if (detail.weibo_type.equals("3")) {
						type_mark_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_message));
					} else if (detail.weibo_type.equals("4")) {
						if (detail.weibo_type_b.equals("0")) {
							type_mark_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_help));
						} else if (detail.weibo_type_b.equals("1")) {
							type_mark_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_activity));
						}
					} else {
						type_mark_icon.setVisibility(LinearLayout.GONE);
					}
					
					// SyncLoader user's avatar
					asyncLoader = new AsyncBitmapLoader();
					Bitmap bmp = asyncLoader.loadBitmap(avatar, detail.user_img, Utility.dip2px(HomeListDetailsActivity.this, (float) 40.0), Utility.dip2px(HomeListDetailsActivity.this, (float) 40.0), new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (bitmap == null) {
								bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
							}
							imageView.setImageBitmap(bitmap);
						}
					});

					if (bmp != null) {
						avatar.setImageBitmap(bmp);
					}
					
				}
			}
		});
	}
	
	private void doCommentListViewUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (result_WeiboComment.status.equals("0")) {
					comment_listAdapter.addItems(result_WeiboComment.getDatas());
					comment_listAdapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	private void doForwardListViewUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (result_WeiboForward.status.equals("0")) {
					forward_listAdapter.addItems(result_WeiboForward.getDatas());
					forward_listAdapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	private void doPraisetListViewUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (result_WeiboPraise.status.equals("0")) {
					praise_listAdapter.addItems(result_WeiboPraise.getDatas());
					praise_listAdapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	private class CommentListAdapter extends BaseListAdapter<WeiboComment> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			
			if(convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_dynamic_details_comment_listview, null);
				
				holder = new ViewHolder();
				
				holder.comment_user_name = (TextView) convertView.findViewById(R.id.dynamic_details_comment_list_name);
				holder.comment_content = (TextView) convertView.findViewById(R.id.dynamic_details_comment_list_content);
				holder.comment_time = (TextView) convertView.findViewById(R.id.dynamic_details_comment_list_time);
				
				holder.comment_user_img = (ImageView) convertView.findViewById(R.id.dynamic_details_comment_list_avatar);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(holder != null) {
				WeiboComment item = this.getItem(position);
				
				holder.comment_user_name.setText(item.user_name);
				holder.comment_content.setText(item.comment_content);
				holder.comment_time.setText(item.time);
				
				// SyncLoader user's avatar_pic
				Bitmap bmp = asyncLoader.loadBitmap(holder.comment_user_img, item.user_img, Utility.dip2px(HomeListDetailsActivity.this, (float) 40.0), Utility.dip2px(HomeListDetailsActivity.this, (float) 40.0), new ImageCallBack() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp != null) {
					holder.comment_user_img.setImageBitmap(bmp);
				}
			}
			return convertView;
		}
		
	}
	
	private class ForwardListAdapter extends BaseListAdapter<WeiboForward> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			
			if(convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_dynamic_details_forward_listview, null);
				
				holder = new ViewHolder();
				
				holder.forward_user_name = (TextView) convertView.findViewById(R.id.dynamic_details_forward_list_name);
				holder.forward_content = (TextView) convertView.findViewById(R.id.dynamic_details_forward_list_content);
				holder.forward_time = (TextView) convertView.findViewById(R.id.dynamic_details_forward_list_time);
				
				holder.forward_user_img = (ImageView) convertView.findViewById(R.id.dynamic_details_forward_list_avatar);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			if(holder != null) {
				WeiboForward item = this.getItem(position);
				
				holder.forward_user_name.setText(item.user_name);
				holder.forward_content.setText(item.forward_content);
				holder.forward_time.setText(item.time);
				
				// SyncLoader user's avatar_pic
				Bitmap bmp = asyncLoader.loadBitmap(holder.forward_user_img, item.user_img, Utility.dip2px(HomeListDetailsActivity.this, (float) 40.0), Utility.dip2px(HomeListDetailsActivity.this, (float) 40.0), new ImageCallBack() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp != null) {
					holder.forward_user_img.setImageBitmap(bmp);
				}
			}
			return convertView;
		}
		
	}
	
	private class PraiseListAdapter extends BaseListAdapter<WeiboPraise> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder praise_holder = null;
			
			if(convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_dynamic_details_praise_listview, null);
				
				praise_holder = new ViewHolder();
				
				praise_holder.praise_user_name_1 = (TextView) convertView.findViewById(R.id.praise_list_user_name_1);
				praise_holder.praise_user_name_2 = (TextView) convertView.findViewById(R.id.praise_list_user_name_2);
				praise_holder.praise_user_name_3 = (TextView) convertView.findViewById(R.id.praise_list_user_name_3);
				praise_holder.praise_user_name_4 = (TextView) convertView.findViewById(R.id.praise_list_user_name_4);
				
				praise_holder.praise_pic_1 = (ImageView) convertView.findViewById(R.id.praise_list_pic_1);
				praise_holder.praise_pic_2 = (ImageView) convertView.findViewById(R.id.praise_list_pic_2);
				praise_holder.praise_pic_3 = (ImageView) convertView.findViewById(R.id.praise_list_pic_3);
				praise_holder.praise_pic_4 = (ImageView) convertView.findViewById(R.id.praise_list_pic_4);
				
				convertView.setTag(praise_holder);
			} else {
				praise_holder = (ViewHolder) convertView.getTag();
			}
			
			if(praise_holder != null) {
				WeiboPraise item = this.getItem(position);
				
				praise_holder.praise_user_name_1.setText(item.user_name);
				praise_holder.praise_user_name_2.setText(item.user_name);
				praise_holder.praise_user_name_3.setText(item.user_name);
				praise_holder.praise_user_name_4.setText(item.user_name);
				
				// SyncLoader praise user's avatar_1
				Bitmap bmp_1 = asyncLoader.loadBitmap(praise_holder.praise_pic_1, item.user_img, Utility.dip2px(HomeListDetailsActivity.this, (float) 50.0), Utility.dip2px(HomeListDetailsActivity.this, (float) 50.0), new ImageCallBack() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp_1 != null) {
					praise_holder.praise_pic_1.setImageBitmap(bmp_1);
				}
				
				// SyncLoader praise user's avatar_2
				Bitmap bmp_2 = asyncLoader.loadBitmap(praise_holder.praise_pic_2, item.user_img, Utility.dip2px(HomeListDetailsActivity.this, (float) 50.0), Utility.dip2px(HomeListDetailsActivity.this, (float) 50.0), new ImageCallBack() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp_2 != null) {
					praise_holder.praise_pic_2.setImageBitmap(bmp_2);
				}
				
				// SyncLoader praise user's avatar_3
				Bitmap bmp_3 = asyncLoader.loadBitmap(praise_holder.praise_pic_3, item.user_img, Utility.dip2px(HomeListDetailsActivity.this, (float) 50.0), Utility.dip2px(HomeListDetailsActivity.this, (float) 50.0), new ImageCallBack() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp_3 != null) {
					praise_holder.praise_pic_3.setImageBitmap(bmp_3);
				}
				
				// SyncLoader praise user's avatar_4
				Bitmap bmp_4 = asyncLoader.loadBitmap(praise_holder.praise_pic_4, item.user_img, Utility.dip2px(HomeListDetailsActivity.this, (float) 50.0), Utility.dip2px(HomeListDetailsActivity.this, (float) 50.0), new ImageCallBack() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp_4 != null) {
					praise_holder.praise_pic_4.setImageBitmap(bmp_4);
				}
			}
			return convertView;
		}
		
	}
	
	private class ViewHolder {
		ImageView comment_user_img;
		ImageView forward_user_img;
		
		ImageView praise_pic_1;
		ImageView praise_pic_2;
		ImageView praise_pic_3;
		ImageView praise_pic_4;
		
		TextView comment_user_name;
		TextView comment_content;
		TextView comment_time;
		
		TextView forward_user_name;
		TextView forward_content;
		TextView forward_time;
		
		TextView praise_user_name_1;
		TextView praise_user_name_2;
		TextView praise_user_name_3;
		TextView praise_user_name_4;
	}
	
}
