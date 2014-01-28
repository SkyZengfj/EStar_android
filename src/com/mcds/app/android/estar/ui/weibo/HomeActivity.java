package com.mcds.app.android.estar.ui.weibo;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.GoodWeibo;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.Weibo;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseTabActivity;
import com.mcds.app.android.estar.ui.LoginActivity;
import com.mcds.app.android.estar.ui.my.FindFriendActivity;
import com.mcds.app.android.estar.ui.my.RemindActivity;
import com.mcds.app.android.estar.ui.shop.GiftListActivity;
import com.mcds.app.android.estar.ui.shop.OrderListActivity;
import com.mcds.app.android.estar.ui.shop.ShopcarActivity;
import com.mcds.app.android.estar.util.Utility;

public class HomeActivity extends BaseTabActivity {
	private TextView textView_Name;

	private ListView weiboListView;

	private MenuDrawer weiboHomeMenuDrawer;

	private LinearLayout msgTypeList;
	private LinearLayout weiboHomeMenuLayout;

	private RelativeLayout msgTypeList_allDynamic;
	private RelativeLayout msgTypeList_affirmation;
	private RelativeLayout msgTypeList_blessing;
	private RelativeLayout msgTypeList_message;
	private RelativeLayout msgTypeList_help;
	private RelativeLayout msgTypeList_activity;

	private RelativeLayout sliding_list_sendMessage_Layout;
	private RelativeLayout sliding_list_sendBlessing_Layout;
	private RelativeLayout sliding_list_sendAffirmation_Layout;
	private RelativeLayout sliding_list_help_Layout;
	private RelativeLayout sliding_list_alert_Layout;
	private RelativeLayout sliding_list_work_Layout;
	private RelativeLayout sliding_list_group_Layout;
	private RelativeLayout sliding_list_friend_Layout;
	private RelativeLayout sliding_list_integration_Layout;
	private RelativeLayout sliding_list_order_Layout;
	private RelativeLayout sliding_list_shoppingCar_Layout;
	private RelativeLayout sliding_list_checkUpdate_Layout;
	private RelativeLayout sliding_list_cleanCache_Layout;
	private RelativeLayout sliding_list_about_Layout;
	private RelativeLayout sliding_list_signOut_Layout;

	private ImageView allDynamic_selected;
	private ImageView affirmation_selected;
	private ImageView blessing_selected;
	private ImageView message_selected;
	private ImageView help_selected;
	private ImageView activity_selected;
	private ImageView triangle;

	private ImageButton imageButton_dynamicEdit;
	private ImageButton imageButton_menu;

	private WeiboListAdapter adapter;

	private ReturnResult<Weibo> result_Weibo;
	private ReturnResult<GoodWeibo> result_GoodWeibo;

	private List<ImageView> content_img_thumbnails_array;
	private List<ImageView> original_content_img_thumbnails_array;

	// the list view loading message count in once
	private final String PAGE_SIZE = "10";
	// the list view loading count
	private int page_num = 0;
	// the message type: 0(all dynamic), 1(affirmation), 2(blessing),
	// 3(message), 4(help), 5(activity)
	public static int messageType = 0;
	// the activity's type
	public static int type_b = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		weiboHomeMenuDrawer = MenuDrawer.attach(this, Position.LEFT);
		weiboHomeMenuDrawer.closeMenu();
		weiboHomeMenuDrawer.setContentView(R.layout.weibo_home);
		initTabNavigate(R.id.weiboHome_tabNavigate, TAB_INDEX_HOME);

		weiboHomeMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		weiboHomeMenuLayout = (LinearLayout) LinearLayout.inflate(this, R.layout.weibo_sliding_menu_layout, null);
		weiboHomeMenuDrawer.setMenuView(weiboHomeMenuLayout);

		content_img_thumbnails_array = new ArrayList<ImageView>();
		original_content_img_thumbnails_array = new ArrayList<ImageView>();

		textView_Name = (TextView) this.findViewById(R.id.weiboHome_textview_name);

		msgTypeList = (LinearLayout) findViewById(R.id.weiboHome_msgTypeList);
		msgTypeList_allDynamic = (RelativeLayout) findViewById(R.id.weiboHome_msgTypeList_allActivity);
		msgTypeList_affirmation = (RelativeLayout) findViewById(R.id.weiboHome_msgTypeList_affirmation);
		msgTypeList_blessing = (RelativeLayout) findViewById(R.id.weiboHome_msgTypeList_blessing);
		msgTypeList_message = (RelativeLayout) findViewById(R.id.weiboHome_msgTypeList_message);
		msgTypeList_help = (RelativeLayout) findViewById(R.id.weiboHome_msgTypeList_help);
		msgTypeList_activity = (RelativeLayout) findViewById(R.id.weiboHome_msgTypeList_activity);

		sliding_list_sendMessage_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_sendMessage);
		sliding_list_sendMessage_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				weiboHomeMenuDrawer.toggleMenu();
				Intent intent = new Intent(HomeActivity.this, HomeListDynamicEditActivity.class);
				intent.putExtra("sendMessageType", 3);
				startActivity(intent);
			}
		});
		sliding_list_sendBlessing_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_sendBlessing);
		sliding_list_sendBlessing_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				weiboHomeMenuDrawer.toggleMenu();
				Intent intent = new Intent(HomeActivity.this, HomeListDynamicEditActivity.class);
				intent.putExtra("sendMessageType", 2);
				startActivity(intent);
			}
		});
		sliding_list_sendAffirmation_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_sendAffirmation);
		sliding_list_sendAffirmation_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				weiboHomeMenuDrawer.toggleMenu();
				Intent intent = new Intent(HomeActivity.this, HomeListDynamicEditActivity.class);
				intent.putExtra("sendMessageType", 1);
				startActivity(intent);
			}
		});
		sliding_list_help_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_sendHelp);
		sliding_list_help_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				weiboHomeMenuDrawer.toggleMenu();
				Intent intent = new Intent(HomeActivity.this, HomeListDynamicEditActivity.class);
				intent.putExtra("sendMessageType", 4);
				startActivity(intent);
			}
		});
		sliding_list_alert_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_alert);
		sliding_list_alert_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentRewards = new Intent(HomeActivity.this, RemindActivity.class);
				startActivity(intentRewards);

			}
		});
		sliding_list_work_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_work);
		sliding_list_work_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO
			}
		});
		sliding_list_group_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_group);
		sliding_list_group_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		sliding_list_friend_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_friend);
		sliding_list_friend_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentRewards = new Intent(HomeActivity.this, FindFriendActivity.class);
				startActivity(intentRewards);
			}
		});
		sliding_list_integration_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_integration);
		sliding_list_integration_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentRewards = new Intent(HomeActivity.this, GiftListActivity.class);
				startActivity(intentRewards);
			}
		});
		sliding_list_order_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_order);
		sliding_list_order_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentRewards = new Intent(HomeActivity.this, OrderListActivity.class);
				startActivity(intentRewards);
			}
		});
		sliding_list_shoppingCar_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_shoppingcar);
		sliding_list_shoppingCar_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentRewards = new Intent(HomeActivity.this, ShopcarActivity.class);
				startActivity(intentRewards);
			}
		});
		sliding_list_checkUpdate_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_checkUpdate);
		sliding_list_checkUpdate_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		sliding_list_cleanCache_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_cleanCache);
		sliding_list_cleanCache_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "清除缓存成功.", Toast.LENGTH_LONG).show();
			}
		});
		sliding_list_about_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_about);
		sliding_list_about_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		sliding_list_signOut_Layout = (RelativeLayout) findViewById(R.id.weibo_sliding_menu_relativeLayout_signOut);
		sliding_list_signOut_Layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				UserManager.uid = "";
				Intent intentRewards = new Intent(HomeActivity.this, LoginActivity.class);
				startActivity(intentRewards);
				finish();
			}
		});

		allDynamic_selected = (ImageView) findViewById(R.id.weiboHome_msgTypeList_allActivity_selected);
		affirmation_selected = (ImageView) findViewById(R.id.weiboHome_msgTypeList_affirmation_selected);
		blessing_selected = (ImageView) findViewById(R.id.weiboHome_msgTypeList_blessing_selected);
		message_selected = (ImageView) findViewById(R.id.weiboHome_msgTypeList_message_selected);
		help_selected = (ImageView) findViewById(R.id.weiboHome_msgTypeList_help_selected);
		activity_selected = (ImageView) findViewById(R.id.weiboHome_msgTypeList_activity_selected);

		weiboListView = (ListView) findViewById(R.id.weiboHome_listView);
		adapter = new WeiboListAdapter();
		weiboListView.setAdapter(adapter);

		triangle = (ImageView) findViewById(R.id.weiboHome_top_triangle);

		msgTypeList_allDynamic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (messageType != 0) {
					switch (messageType) {
						case 1:
							affirmation_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 2:
							blessing_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 3:
							message_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 4:
							help_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 5:
							activity_selected.setVisibility(LinearLayout.INVISIBLE);
							break;

						default:
							break;
					}
					messageType = 0;
					textView_Name.setText("全部");
					triangle.setBackgroundResource(R.drawable.top_btn_bg_triangle);
					allDynamic_selected.setVisibility(LinearLayout.VISIBLE);
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);

					adapter.clearItems();
					getData();
					adapter.notifyDataSetChanged();
				} else {
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);
				}
			}
		});

		msgTypeList_affirmation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (messageType != 1) {
					switch (messageType) {
						case 0:
							allDynamic_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 2:
							blessing_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 3:
							message_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 4:
							help_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 5:
							activity_selected.setVisibility(LinearLayout.INVISIBLE);
							break;

						default:
							break;
					}
					messageType = 1;
					textView_Name.setText("肯定");
					triangle.setBackgroundResource(R.drawable.top_btn_bg_triangle);
					affirmation_selected.setVisibility(LinearLayout.VISIBLE);
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);

					adapter.clearItems();
					getData();
					adapter.notifyDataSetChanged();
				} else {
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);
				}
			}
		});

		msgTypeList_blessing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (messageType != 2) {
					switch (messageType) {
						case 0:
							allDynamic_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 1:
							affirmation_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 3:
							message_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 4:
							help_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 5:
							activity_selected.setVisibility(LinearLayout.INVISIBLE);
							break;

						default:
							break;
					}
					messageType = 2;
					textView_Name.setText("祝福");
					triangle.setBackgroundResource(R.drawable.top_btn_bg_triangle);
					blessing_selected.setVisibility(LinearLayout.VISIBLE);
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);

					adapter.clearItems();
					getData();
					adapter.notifyDataSetChanged();
				} else {
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);
				}
			}
		});

		msgTypeList_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (messageType != 3) {
					switch (messageType) {
						case 0:
							allDynamic_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 1:
							affirmation_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 2:
							blessing_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 4:
							help_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 5:
							activity_selected.setVisibility(LinearLayout.INVISIBLE);
							break;

						default:
							break;
					}
					messageType = 3;
					textView_Name.setText("信息");
					triangle.setBackgroundResource(R.drawable.top_btn_bg_triangle);
					message_selected.setVisibility(LinearLayout.VISIBLE);
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);

					adapter.clearItems();
					getData();
					adapter.notifyDataSetChanged();
				} else {
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);
				}
			}
		});

		msgTypeList_help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (messageType != 4) {
					switch (messageType) {
						case 0:
							allDynamic_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 1:
							affirmation_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 2:
							blessing_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 3:
							message_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 5:
							activity_selected.setVisibility(LinearLayout.INVISIBLE);
							break;

						default:
							break;
					}
					messageType = 4;
					textView_Name.setText("帮助");
					triangle.setBackgroundResource(R.drawable.top_btn_bg_triangle);
					help_selected.setVisibility(LinearLayout.VISIBLE);
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);

					adapter.clearItems();
					getData();
					adapter.notifyDataSetChanged();
				} else {
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);
				}
			}
		});

		msgTypeList_activity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (messageType != 5) {
					switch (messageType) {
						case 0:
							allDynamic_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 1:
							affirmation_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 2:
							blessing_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 3:
							message_selected.setVisibility(LinearLayout.INVISIBLE);
							break;
						case 4:
							help_selected.setVisibility(LinearLayout.INVISIBLE);
							break;

						default:
							break;
					}
					messageType = 5;
					textView_Name.setText("活动");
					triangle.setBackgroundResource(R.drawable.top_btn_bg_triangle);
					activity_selected.setVisibility(LinearLayout.VISIBLE);
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);

					adapter.clearItems();
					getData();
					adapter.notifyDataSetChanged();
				} else {
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);
				}
			}
		});

		imageButton_dynamicEdit = (ImageButton) findViewById(R.id.weiboHome_imageButton_edit);
		imageButton_dynamicEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeActivity.this, HomeListDynamicEditActivity.class);
				startActivity(intent);
			}
		});

		imageButton_menu = (ImageButton) findViewById(R.id.weibo_home_imageButton_dynamic_menu);
		imageButton_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				weiboHomeMenuDrawer.toggleMenu();
			}
		});

		weiboListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Weibo item = (Weibo) arg0.getAdapter().getItem(arg2);
				String weibo_id = item.weibo_id;

				Intent intent = new Intent(HomeActivity.this, HomeListDetailsActivity.class);
				intent.putExtra("weibo_id", weibo_id);
				startActivity(intent);
			}
		});

		// detecting whether the slide in the end portion of list view
		weiboListView.setOnScrollListener(new OnScrollListener() {

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

		textView_Name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (msgTypeList.getVisibility() == LinearLayout.VISIBLE) {
					triangle.setBackgroundResource(R.drawable.top_btn_bg_triangle);
					triangle.setImageDrawable(getResources().getDrawable(R.drawable.top_btn_bg_triangle));
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);
				} else {
					triangle.setBackgroundResource(R.drawable.top_btn_bg_triangle_down);
					msgTypeList.setVisibility(LinearLayout.VISIBLE);
				}
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
					if (messageType == 4) {
						result_Weibo = ConnectProvider.getWeiboList(UserManager.uid, "4", "0", String.valueOf(page_num), PAGE_SIZE);
					} else if (messageType == 5) {
						result_Weibo = ConnectProvider.getWeiboList(UserManager.uid, "4", "1", String.valueOf(page_num), PAGE_SIZE);
					} else {
						result_Weibo = ConnectProvider.getWeiboList(UserManager.uid, String.valueOf(messageType), "", String.valueOf(page_num), PAGE_SIZE);
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
				if (result_Weibo.status.equals("0")) {
					adapter.addItems(result_Weibo.getDatas());
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	private class WeiboListAdapter extends BaseListAdapter<Weibo> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.weibo_home_listview_item, null);

				holder = new ViewHolder();

				holder.content_img_container = (LinearLayout) convertView.findViewById(R.id.weiboHome_content_imgs_linearLayout);
				holder.original_content_img_container = (LinearLayout) convertView.findViewById(R.id.weiboHome_original_content_imgs_linearLayout);
				holder.original_content_linearLayout = (LinearLayout) convertView.findViewById(R.id.weiboHome_original_content_linearLayout);

				holder.message_type_icon = (ImageView) convertView.findViewById(R.id.weiboHome_message_type_icon);
				holder.default_avatar = (ImageView) convertView.findViewById(R.id.weiboHome_user_avatar);
				holder.original_user_img = (ImageView) convertView.findViewById(R.id.weiboHome_original_user_avatar);

				holder.userName = (TextView) convertView.findViewById(R.id.weiboHome_userName);
				holder.original_userName = (TextView) convertView.findViewById(R.id.weiboHome_original_userName);
				holder.message_sendTime = (TextView) convertView.findViewById(R.id.weiboHome_message_send_time);
				holder.userPost = (TextView) convertView.findViewById(R.id.weiboHome_userPost);
				holder.message_content = (TextView) convertView.findViewById(R.id.weiboHome_message_content);
				holder.original_message_content = (TextView) convertView.findViewById(R.id.weiboHome_original_message_content);
				holder.forwarding_count = (TextView) convertView.findViewById(R.id.weiboHome_main_frame_forwording_count);
				holder.comment_count = (TextView) convertView.findViewById(R.id.weiboHome_main_frame_comment_count);
				holder.praise_count = (TextView) convertView.findViewById(R.id.weiboHome_main_frame_praise_count);

				holder.img0 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_img0);
				holder.img1 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_img1);
				holder.img2 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_img2);
				holder.img3 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_img3);
				holder.img4 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_img4);
				holder.img5 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_img5);
				holder.img6 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_img6);
				holder.img7 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_img7);

				holder.original_img0 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_original_img0);
				holder.original_img1 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_original_img1);
				holder.original_img2 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_original_img2);
				holder.original_img3 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_original_img3);
				holder.original_img4 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_original_img4);
				holder.original_img5 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_original_img5);
				holder.original_img6 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_original_img6);
				holder.original_img7 = (ImageView) convertView.findViewById(R.id.weiboHomeListViewItem_original_img7);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder != null) {
				Weibo item = this.getItem(position);

				// set user name value
				holder.userName.setText(item.user_name);
				// set user's work
				holder.userPost.setText(item.work);
				// set send time value
				holder.message_sendTime.setText(item.time);
				// set message content text
				holder.message_content.setText(item.content_text);
				// set the number's count and color of message forwarding
				if ((item.share_num.equals("")) || (item.share_num.equals("0"))) {
					holder.forwarding_count.setText("0");
				} else {
					holder.forwarding_count.setText(item.share_num);
				}

				// set the number's count and color of message comment
				if ((item.comment_num.equals("")) || (item.comment_num.equals("0"))) {
					holder.comment_count.setText("0");
				} else {
					holder.comment_count.setText(item.comment_num);
				}

				// set the number's count and color of message praise
				if ((item.good_num.equals("")) || (item.good_num.equals("0"))) {
					holder.praise_count.setText("0");
				} else {
					holder.praise_count.setText(item.good_num);
				}

				// set message type icon
				if (item.weibo_type.equals("1")) {
					holder.message_type_icon.setBackgroundResource(R.drawable.type_mark_affirmation);
					holder.message_type_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_affirmation));
				} else if (item.weibo_type.equals("2")) {
					holder.message_type_icon.setBackgroundResource(R.drawable.type_mark_blessing);
					holder.message_type_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_blessing));
				} else if (item.weibo_type.equals("3")) {
					holder.message_type_icon.setBackgroundResource(R.drawable.type_mark_message);
					holder.message_type_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_message));
				} else if (item.weibo_type.equals("4")) {
					if (item.weibo_type_b.equals("0")) {
						holder.message_type_icon
								.setBackgroundResource(
										R.drawable.type_mark_help);
						holder.message_type_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_help));
					} else if (item.weibo_type_b.equals("1")) {
						holder.message_type_icon
								.setBackgroundResource(
										R.drawable.type_mark_activity);
						holder.message_type_icon.setImageDrawable(getResources().getDrawable(R.drawable.type_mark_activity));
					}
				} else {
					holder.message_type_icon.setVisibility(View.GONE);
				}

				// SyncLoader user's avatar_pic
				Bitmap bmp = asyncLoader.loadBitmap(holder.default_avatar, item.user_img, Utility.dip2px(HomeActivity.this, (float) 40.0), Utility.dip2px(HomeActivity.this, (float) 40.0), new ImageCallBack() {

					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp != null) {
					holder.default_avatar.setImageBitmap(bmp);
				}

				if (!item.content_imgs.isEmpty()) {
					holder.content_img_container.setVisibility(View.VISIBLE);
					for (int i = 0; i < item.content_imgs.size(); i++) {
						switch (i) {
							case 0:
								Bitmap contentImage0 = asyncLoader.loadBitmap(holder.img0, item.content_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (contentImage0 != null) {
									holder.img0.setImageBitmap(contentImage0);
								}
								break;
							case 1:
								Bitmap contentImage1 = asyncLoader.loadBitmap(holder.img1, item.content_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (contentImage1 != null) {
									holder.img1.setImageBitmap(contentImage1);
								}
								break;
							case 2:
								Bitmap contentImage2 = asyncLoader.loadBitmap(holder.img2, item.content_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (contentImage2 != null) {
									holder.img2.setImageBitmap(contentImage2);
								}
								break;
							case 3:
								Bitmap contentImage3 = asyncLoader.loadBitmap(holder.img3, item.content_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (contentImage3 != null) {
									holder.img3.setImageBitmap(contentImage3);
								}
								break;
							case 4:
								Bitmap contentImage4 = asyncLoader.loadBitmap(holder.img4, item.content_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (contentImage4 != null) {
									holder.img4.setImageBitmap(contentImage4);
								}
								break;
							case 5:
								Bitmap contentImage5 = asyncLoader.loadBitmap(holder.img5, item.content_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (contentImage5 != null) {
									holder.img5.setImageBitmap(contentImage5);
								}
								break;
							case 6:
								Bitmap contentImage6 = asyncLoader.loadBitmap(holder.img6, item.content_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (contentImage6 != null) {
									holder.img6.setImageBitmap(contentImage6);
								}
								break;
							case 7:
								Bitmap contentImage7 = asyncLoader.loadBitmap(holder.img7, item.content_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (contentImage7 != null) {
									holder.img7.setImageBitmap(contentImage7);
								}
								break;
							default:
								break;
						}
					}
				}

				if (item.original_weibo_id != null) {
					holder.original_content_linearLayout.setVisibility(View.VISIBLE);
					holder.original_userName.setText(item.original_user_name);
					holder.original_message_content.setText(item.original_content);

					// SyncLoader original user's avatar_pic
					Bitmap original_bmp = asyncLoader.loadBitmap(holder.original_user_img, item.original_user_img, Utility.dip2px(HomeActivity.this, (float) 40.0), Utility.dip2px(HomeActivity.this, (float) 40.0), new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (bitmap == null) {
								bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
							}
							imageView.setImageBitmap(bitmap);
						}
					});

					if (bmp != null) {
						holder.default_avatar.setImageBitmap(original_bmp);
					}

					if (!item.original_imgs.isEmpty()) {
						holder.original_content_img_container.setVisibility(View.VISIBLE);
						for (int i = 0; i < item.original_imgs.size(); i++) {
							switch (i) {
								case 0:
									Bitmap contentImage0 = asyncLoader.loadBitmap(holder.original_img0, item.original_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

										@Override
										public void imageLoad(ImageView imageView, Bitmap bitmap) {
											if (bitmap == null) {
												bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
											}
											imageView.setImageBitmap(bitmap);
										}
									});

									if (contentImage0 != null) {
										holder.original_img0.setImageBitmap(contentImage0);
									}
									break;
								case 1:
									Bitmap contentImage1 = asyncLoader.loadBitmap(holder.original_img1, item.original_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

										@Override
										public void imageLoad(ImageView imageView, Bitmap bitmap) {
											if (bitmap == null) {
												bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
											}
											imageView.setImageBitmap(bitmap);
										}
									});

									if (contentImage1 != null) {
										holder.original_img1.setImageBitmap(contentImage1);
									}
									break;
								case 2:
									Bitmap contentImage2 = asyncLoader.loadBitmap(holder.original_img2, item.original_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

										@Override
										public void imageLoad(ImageView imageView, Bitmap bitmap) {
											if (bitmap == null) {
												bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
											}
											imageView.setImageBitmap(bitmap);
										}
									});

									if (contentImage2 != null) {
										holder.original_img2.setImageBitmap(contentImage2);
									}
									break;
								case 3:
									Bitmap contentImage3 = asyncLoader.loadBitmap(holder.original_img3, item.original_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

										@Override
										public void imageLoad(ImageView imageView, Bitmap bitmap) {
											if (bitmap == null) {
												bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
											}
											imageView.setImageBitmap(bitmap);
										}
									});

									if (contentImage3 != null) {
										holder.original_img3.setImageBitmap(contentImage3);
									}
									break;
								case 4:
									Bitmap contentImage4 = asyncLoader.loadBitmap(holder.original_img4, item.original_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

										@Override
										public void imageLoad(ImageView imageView, Bitmap bitmap) {
											if (bitmap == null) {
												bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
											}
											imageView.setImageBitmap(bitmap);
										}
									});

									if (contentImage4 != null) {
										holder.original_img4.setImageBitmap(contentImage4);
									}
									break;
								case 5:
									Bitmap contentImage5 = asyncLoader.loadBitmap(holder.original_img5, item.original_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

										@Override
										public void imageLoad(ImageView imageView, Bitmap bitmap) {
											if (bitmap == null) {
												bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
											}
											imageView.setImageBitmap(bitmap);
										}
									});

									if (contentImage5 != null) {
										holder.original_img5.setImageBitmap(contentImage5);
									}
									break;
								case 6:
									Bitmap contentImage6 = asyncLoader.loadBitmap(holder.original_img6, item.original_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

										@Override
										public void imageLoad(ImageView imageView, Bitmap bitmap) {
											if (bitmap == null) {
												bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
											}
											imageView.setImageBitmap(bitmap);
										}
									});

									if (contentImage6 != null) {
										holder.original_img6.setImageBitmap(contentImage6);
									}
									break;
								case 7:
									Bitmap contentImage7 = asyncLoader.loadBitmap(holder.original_img7, item.original_imgs.get(i), Utility.dip2px(HomeActivity.this, (float) 80.0), Utility.dip2px(HomeActivity.this, (float) 80.0), new ImageCallBack() {

										@Override
										public void imageLoad(ImageView imageView, Bitmap bitmap) {
											if (bitmap == null) {
												bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
											}
											imageView.setImageBitmap(bitmap);
										}
									});

									if (contentImage7 != null) {
										holder.original_img7.setImageBitmap(contentImage7);
									}
									break;
								default:
									break;
							}
						}
					}

				}
			}

			return convertView;
		}

	}

	private class ViewHolder {
		LinearLayout content_img_container;
		LinearLayout original_content_img_container;
		LinearLayout original_content_linearLayout;

		ImageView message_type_icon;
		ImageView default_avatar;
		ImageView original_user_img;

		TextView userName;
		TextView original_userName;
		TextView userPost;
		TextView message_sendTime;
		TextView message_content;
		TextView original_message_content;
		TextView forwarding_count;
		TextView comment_count;
		TextView praise_count;

		ImageView img0;
		ImageView img1;
		ImageView img2;
		ImageView img3;
		ImageView img4;
		ImageView img5;
		ImageView img6;
		ImageView img7;

		ImageView original_img0;
		ImageView original_img1;
		ImageView original_img2;
		ImageView original_img3;
		ImageView original_img4;
		ImageView original_img5;
		ImageView original_img6;
		ImageView original_img7;
	}
}
