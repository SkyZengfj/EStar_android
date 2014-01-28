package com.mcds.app.android.estar.ui.action;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.Action;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.ui.BaseTabActivity;
import com.mcds.app.android.estar.util.Utility;

public class HomeActivity extends BaseTabActivity {

	private TextView textView_Name;
	private ImageView textView_Pick;
	private LinearLayout msgTypeList;
	private ListView actionHomeListView;
	private RelativeLayout actionHomeLayout, actionHomeMyActionLayout,
			actionHomeDeployLayout;
	private LinearLayout actionMenuItemCatagoryLayout,
			actionMenuItemStateLayout, actionMenuItemRewardLayout,
			actionMenuItemTaskLayout;
	private LinearLayout actionMenuItemSubCatagoryLayout,
			actionMenuItemSubStateLayout, actionMenuItemSubRewardLayout,
			actionMenuItemSubTaskLayout;
	private ImageView actionHomeMenuArrowCatagory,
			actionHomeMenuArrowDownCatagory;
	private ImageView actionHomeMenuArrowState, actionHomeMenuArrowDownState;
	private ImageView actionHomeMenuArrowReward, actionHomeMenuArrowDownReward,
			actionHomeMenuArrowTask, actionHomeMenuArrowDownTask;
	private ImageView actionHomeTypeArrow;

	private MenuDrawer actionHomeMenuDrawer;
	private LinearLayout actionHomeMenuLayout;

	private TextView actionHomeMenuSubCat_tvQuanbu;
	private TextView actionHomeMenuSubCat_tvRenwuhuodong;
	private TextView actionHomeMenuSubCat_tvGongsihuodong;
	private TextView actionHomeMenuSubCat_tvYulehuodong;
	private TextView actionHomeMenuSubCat_tvDiaochawenjuan;
	private TextView actionHomeMenuSubCat_tvZaixianceshi;
	private TextView actionHomeMenuSubCat_tvZaixianqiuzhu;
	private TextView actionHomeMenuSubSta_tvQuanbu;
	private TextView actionHomeMenuSubSta_tvJijiangkaishi;
	private TextView actionHomeMenuSubSta_tvZhengzaijinxing;
	private TextView actionHomeMenuSubSta_tvYijingjieshu;
	private TextView actionHomeMenuSubRew_tvQuanbu;
	private TextView actionHomeMenuSubRew_tvJifen;
	private TextView actionHomeMenuSubRew_tvXunzang;
	private TextView actionHomeMenuSubRew_tvQita;

	private FrameLayout actionHomeListItemAwardFramelayout;

	private ImageView action_home_top_quanbuhuodong,
			action_home_top_wodehuodong, action_home_top_fabudehuodong;

	private ImageButton actionHomeImageBackButton;
	private ActionAdapter adapter;
	private ReturnResult<Action> rrAction;

	public static int actionType = 0; // 活动类型
	public static int actionSort = 0; // 活动类别
	public static int actionStatus = 0; // 活动状态
	public static int actionAward = 0; // 活动奖励

	public static boolean isActionTask = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		actionHomeMenuDrawer = MenuDrawer.attach(this, Position.RIGHT);
		actionHomeMenuDrawer.closeMenu();
		actionHomeMenuDrawer.setContentView(R.layout.action_home);

		initTabNavigate(R.id.actionHome_tabNavigate, TAB_INDEX_ACTION);
		initMenu();
		initTypeList();
		initListView();
	}

	private void initMenu() {
		actionHomeMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
		actionHomeMenuLayout = (LinearLayout) LinearLayout.inflate(this,
				R.layout.action_home_menu, null);
		actionHomeMenuDrawer.setMenuView(actionHomeMenuLayout);

		actionHomeMenuArrowCatagory = (ImageView) findViewById(R.id.action_home_menu_arrow_catagory);
		actionHomeMenuArrowDownCatagory = (ImageView) findViewById(R.id.action_home_menu_arrow_down_catagory);
		actionHomeMenuArrowState = (ImageView) findViewById(R.id.action_home_menu_arrow_state);
		actionHomeMenuArrowDownState = (ImageView) findViewById(R.id.action_home_menu_arrow_down_state);
		actionHomeMenuArrowReward = (ImageView) findViewById(R.id.action_home_menu_arrow_reward);
		actionHomeMenuArrowDownReward = (ImageView) findViewById(R.id.action_home_menu_arrow_down_reward);
		actionHomeMenuArrowTask = (ImageView) findViewById(R.id.action_home_menu_arrow_task);
		actionHomeMenuArrowDownTask = (ImageView) findViewById(R.id.action_home_menu_arrow_down_task);

		actionMenuItemCatagoryLayout = (LinearLayout) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_catagory_layout);
		actionMenuItemStateLayout = (LinearLayout) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_state_layout);
		actionMenuItemRewardLayout = (LinearLayout) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_reward_layout);
		actionMenuItemTaskLayout = (LinearLayout) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_task_layout);
		actionMenuItemSubCatagoryLayout = (LinearLayout) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_catagory_layout);
		actionMenuItemSubStateLayout = (LinearLayout) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_state_layout);
		actionMenuItemSubRewardLayout = (LinearLayout) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_reward_layout);
		actionMenuItemSubTaskLayout = (LinearLayout) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_task_layout);

		// 活动分类子类TextView初始化
		actionHomeMenuSubCat_tvQuanbu = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_catagory_quanbu);
		// actionHomeMenuSubtask_tvRenwuhuodong =
		// (TextView)actionHomeMenuLayout.findViewById(R.id.action_home_menu_item_sub_task_quanbu);
		actionHomeMenuSubCat_tvGongsihuodong = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_catagory_gongsihuodong);
		actionHomeMenuSubCat_tvYulehuodong = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_catagory_yulehuodong);
		actionHomeMenuSubCat_tvDiaochawenjuan = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_catagory_diaochawenjuan);
		actionHomeMenuSubCat_tvZaixianceshi = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_catagory_zaixianceshi);
		actionHomeMenuSubCat_tvZaixianqiuzhu = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_catagory_zaixianqiuzhu);

		actionHomeMenuSubCat_tvQuanbu
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionSort = 0;
						((TextView) findViewById(R.id.action_home_menu_item_catagory_selected))
								.setText(actionHomeMenuSubCat_tvQuanbu
										.getText());

						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});
		((TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_task_quanbu))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionSort = 0;
						((TextView) findViewById(R.id.action_home_menu_item_catagory_selected)).setText(((TextView) actionHomeMenuLayout
								.findViewById(R.id.action_home_menu_item_sub_task_quanbu))
								.getText());

						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});
		actionHomeMenuSubCat_tvGongsihuodong
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionSort = 2;
						((TextView) findViewById(R.id.action_home_menu_item_catagory_selected))
								.setText(actionHomeMenuSubCat_tvGongsihuodong
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});
		actionHomeMenuSubCat_tvYulehuodong
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionSort = 3;
						((TextView) findViewById(R.id.action_home_menu_item_catagory_selected))
								.setText(actionHomeMenuSubCat_tvYulehuodong
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});
		actionHomeMenuSubCat_tvDiaochawenjuan
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionSort = 4;
						((TextView) findViewById(R.id.action_home_menu_item_catagory_selected))
								.setText(actionHomeMenuSubCat_tvDiaochawenjuan
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});
		actionHomeMenuSubCat_tvZaixianceshi
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionSort = 5;
						((TextView) findViewById(R.id.action_home_menu_item_catagory_selected))
								.setText(actionHomeMenuSubCat_tvZaixianceshi
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});
		actionHomeMenuSubCat_tvZaixianqiuzhu
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionSort = 6;
						((TextView) findViewById(R.id.action_home_menu_item_catagory_selected))
								.setText(actionHomeMenuSubCat_tvZaixianqiuzhu
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});

		// 活动状态子类TextView初始化
		actionHomeMenuSubSta_tvQuanbu = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_state_quanbu);
		actionHomeMenuSubSta_tvJijiangkaishi = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_state_jijiangkaishi);
		actionHomeMenuSubSta_tvZhengzaijinxing = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_state_zhengzaijinxing);
		actionHomeMenuSubSta_tvYijingjieshu = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_state_yijingjieshu);

		actionHomeMenuSubSta_tvQuanbu
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionStatus = 0;
						((TextView) findViewById(R.id.action_home_menu_item_state_selected))
								.setText(actionHomeMenuSubSta_tvQuanbu
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});
		actionHomeMenuSubSta_tvJijiangkaishi
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionStatus = 2;
						((TextView) findViewById(R.id.action_home_menu_item_state_selected))
								.setText(actionHomeMenuSubSta_tvJijiangkaishi
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});
		actionHomeMenuSubSta_tvZhengzaijinxing
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionStatus = 1;
						((TextView) findViewById(R.id.action_home_menu_item_state_selected))
								.setText(actionHomeMenuSubSta_tvZhengzaijinxing
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});
		actionHomeMenuSubSta_tvYijingjieshu
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionStatus = 3;
						((TextView) findViewById(R.id.action_home_menu_item_state_selected))
								.setText(actionHomeMenuSubSta_tvYijingjieshu
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});

		// 奖励子类TextView初始化
		actionHomeMenuSubRew_tvQuanbu = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_reward_quanbu);
		actionHomeMenuSubRew_tvJifen = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_reward_jifen);
		actionHomeMenuSubRew_tvXunzang = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_reward_xunzang);
		actionHomeMenuSubRew_tvQita = (TextView) actionHomeMenuLayout
				.findViewById(R.id.action_home_menu_item_sub_reward_qita);

		actionHomeMenuSubRew_tvQuanbu
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionAward = 0;
						((TextView) findViewById(R.id.action_home_menu_item_reward_selected))
								.setText(actionHomeMenuSubRew_tvQuanbu
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});
		actionHomeMenuSubRew_tvJifen
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionAward = 1;
						((TextView) findViewById(R.id.action_home_menu_item_reward_selected))
								.setText(actionHomeMenuSubRew_tvJifen.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});
		actionHomeMenuSubRew_tvXunzang
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionAward = 2;
						((TextView) findViewById(R.id.action_home_menu_item_reward_selected))
								.setText(actionHomeMenuSubRew_tvXunzang
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});
		actionHomeMenuSubRew_tvQita
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						actionAward = 3;
						((TextView) findViewById(R.id.action_home_menu_item_reward_selected))
								.setText(actionHomeMenuSubRew_tvQita.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}

				});

		((TextView) findViewById(R.id.action_home_menu_item_sub_task_yes))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						actionSort = 1;
						isActionTask = true;
						((TextView) findViewById(R.id.action_home_menu_item_task_selected))
								.setText(((TextView) findViewById(R.id.action_home_menu_item_sub_task_yes))
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}
				});

		((TextView) findViewById(R.id.action_home_menu_item_sub_task_no))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						actionSort = 0;
						isActionTask = false;
						((TextView) findViewById(R.id.action_home_menu_item_task_selected))
								.setText(((TextView) findViewById(R.id.action_home_menu_item_sub_task_no))
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}
				});

		((TextView) findViewById(R.id.action_home_menu_item_sub_task_quanbu))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						actionSort = 0;
						isActionTask = false;
						((TextView) findViewById(R.id.action_home_menu_item_task_selected))
								.setText(((TextView) findViewById(R.id.action_home_menu_item_sub_task_quanbu))
										.getText());
						getData();
						actionHomeMenuDrawer.toggleMenu();
					}
				});

		// 活动分类布局点击事件
		actionMenuItemCatagoryLayout
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (actionMenuItemSubCatagoryLayout.getVisibility() == LinearLayout.VISIBLE) {
							actionMenuItemSubCatagoryLayout
									.setVisibility(LinearLayout.GONE);
							actionHomeMenuArrowCatagory
									.setVisibility(View.VISIBLE);
							actionHomeMenuArrowDownCatagory
									.setVisibility(View.GONE);

						} else {
							actionMenuItemSubCatagoryLayout
									.setVisibility(LinearLayout.VISIBLE);
							actionMenuItemSubStateLayout
									.setVisibility(LinearLayout.GONE);
							actionMenuItemSubRewardLayout
									.setVisibility(LinearLayout.GONE);
							actionMenuItemSubTaskLayout
									.setVisibility(LinearLayout.GONE);

							actionHomeMenuArrowCatagory
									.setVisibility(View.GONE);
							actionHomeMenuArrowDownCatagory
									.setVisibility(View.VISIBLE);
							actionHomeMenuArrowState
									.setVisibility(View.VISIBLE);
							actionHomeMenuArrowDownState
									.setVisibility(View.GONE);
							actionHomeMenuArrowReward
									.setVisibility(View.VISIBLE);
							actionHomeMenuArrowDownReward
									.setVisibility(View.GONE);
							actionHomeMenuArrowTask.setVisibility(View.VISIBLE);
							actionHomeMenuArrowDownTask
									.setVisibility(View.GONE);

						}
					}

				});

		// 活动状态布局点击事件
		actionMenuItemStateLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (actionMenuItemSubStateLayout.getVisibility() == LinearLayout.VISIBLE) {
					actionMenuItemSubStateLayout
							.setVisibility(LinearLayout.GONE);
					actionHomeMenuArrowState.setVisibility(View.VISIBLE);
					actionHomeMenuArrowDownState.setVisibility(View.GONE);
				} else {
					actionMenuItemSubStateLayout
							.setVisibility(LinearLayout.VISIBLE);
					actionMenuItemSubCatagoryLayout
							.setVisibility(LinearLayout.GONE);
					actionMenuItemSubRewardLayout
							.setVisibility(LinearLayout.GONE);
					actionMenuItemSubTaskLayout
							.setVisibility(LinearLayout.GONE);

					actionHomeMenuArrowState.setVisibility(View.GONE);
					actionHomeMenuArrowDownState.setVisibility(View.VISIBLE);
					actionHomeMenuArrowCatagory.setVisibility(View.VISIBLE);
					actionHomeMenuArrowDownCatagory.setVisibility(View.GONE);
					actionHomeMenuArrowReward.setVisibility(View.VISIBLE);
					actionHomeMenuArrowDownReward.setVisibility(View.GONE);
					actionHomeMenuArrowTask.setVisibility(View.VISIBLE);
					actionHomeMenuArrowDownTask.setVisibility(View.GONE);

				}
			}

		});

		// 奖励布局点击事件
		actionMenuItemRewardLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (actionMenuItemSubRewardLayout.getVisibility() == LinearLayout.VISIBLE) {
					actionMenuItemSubRewardLayout
							.setVisibility(LinearLayout.GONE);
					actionHomeMenuArrowReward.setVisibility(View.VISIBLE);
					actionHomeMenuArrowDownReward.setVisibility(View.GONE);
				} else {
					actionMenuItemSubRewardLayout
							.setVisibility(LinearLayout.VISIBLE);
					actionMenuItemSubStateLayout
							.setVisibility(LinearLayout.GONE);
					actionMenuItemSubCatagoryLayout
							.setVisibility(LinearLayout.GONE);
					actionMenuItemSubTaskLayout
							.setVisibility(LinearLayout.GONE);

					actionHomeMenuArrowReward.setVisibility(View.GONE);
					actionHomeMenuArrowDownReward.setVisibility(View.VISIBLE);
					actionHomeMenuArrowState.setVisibility(View.VISIBLE);
					actionHomeMenuArrowDownState.setVisibility(View.GONE);
					actionHomeMenuArrowCatagory.setVisibility(View.VISIBLE);
					actionHomeMenuArrowDownCatagory.setVisibility(View.GONE);
					actionHomeMenuArrowTask.setVisibility(View.VISIBLE);
					actionHomeMenuArrowDownTask.setVisibility(View.GONE);

				}
			}

		});

		// 任务布局点击事件
		((LinearLayout) findViewById(R.id.action_home_menu_item_task_layout))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (actionMenuItemSubTaskLayout.getVisibility() == LinearLayout.VISIBLE) {
							actionMenuItemSubTaskLayout
									.setVisibility(LinearLayout.GONE);
							actionHomeMenuArrowTask.setVisibility(View.VISIBLE);
							actionHomeMenuArrowDownTask
									.setVisibility(View.GONE);
						} else {
							actionMenuItemSubTaskLayout
									.setVisibility(LinearLayout.VISIBLE);
							actionMenuItemSubStateLayout
									.setVisibility(LinearLayout.GONE);
							actionMenuItemSubCatagoryLayout
									.setVisibility(LinearLayout.GONE);
							actionMenuItemSubRewardLayout
									.setVisibility(LinearLayout.GONE);

							actionHomeMenuArrowTask.setVisibility(View.GONE);
							actionHomeMenuArrowDownTask
									.setVisibility(View.VISIBLE);
							actionHomeMenuArrowState
									.setVisibility(View.VISIBLE);
							actionHomeMenuArrowDownState
									.setVisibility(View.GONE);
							actionHomeMenuArrowCatagory
									.setVisibility(View.VISIBLE);
							actionHomeMenuArrowDownCatagory
									.setVisibility(View.GONE);
							actionHomeMenuArrowReward
									.setVisibility(View.VISIBLE);
							actionHomeMenuArrowDownReward
									.setVisibility(View.GONE);

						}
					}
				});

	}

	/**
	 * 初始化活动列表
	 */
	private void initListView() {
		// TODO Auto-generated method stub
		actionHomeListView = (ListView) findViewById(R.id.actionHome_listView);
		adapter = new ActionAdapter();
		actionHomeListView.setAdapter(adapter);
		actionHomeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = null;
				Bundle bundle = new Bundle();
				if (rrAction != null) {
					String sort = rrAction.getDatas().get(position).sort;
					if (sort.equals("娱乐活动")) {
						intent = new Intent(
								getApplicationContext(),
								com.mcds.app.android.estar.ui.action.EntDetailActivity.class);
					} else if (sort.equals("公司活动")) {
						intent = new Intent(
								getApplicationContext(),
								com.mcds.app.android.estar.ui.action.CompDetailActivity.class);
					} else if (sort.equals("调查问卷")) {
						intent = new Intent(
								getApplicationContext(),
								com.mcds.app.android.estar.ui.action.QuesDetailActivity.class);
					} else if (sort.equals("在线测试")) {
						intent = new Intent(
								getApplicationContext(),
								com.mcds.app.android.estar.ui.action.OnlineTestDetailActivity.class);
					} else if (sort.equals("在线求助")) {
						intent = new Intent(
								getApplicationContext(),
								com.mcds.app.android.estar.ui.action.OnlineHelpDetailActivity.class);
					}
					bundle.putString("activity_id",
							rrAction.getDatas().get(position).activity_id);
					intent.putExtras(bundle);
					startActivity(intent);
				}

			}

		});
		getData();
	}

	/**
	 * 初始化活动类型切换
	 */
	private void initTypeList() {
		actionHomeLayout = (RelativeLayout) findViewById(R.id.actionHome_msgTypeList_home);
		actionHomeMyActionLayout = (RelativeLayout) findViewById(R.id.actionHome_msgTypeList_myAction);
		actionHomeDeployLayout = (RelativeLayout) findViewById(R.id.actionHome_msgTypeList_deploy);

		action_home_top_quanbuhuodong = (ImageView) findViewById(R.id.action_home_top_quanbuhuodong);
		action_home_top_wodehuodong = (ImageView) findViewById(R.id.action_home_top_wodehuodong);
		action_home_top_fabudehuodong = (ImageView) findViewById(R.id.action_home_top_fabudehuodong);

		actionHomeTypeArrow = (ImageView) findViewById(R.id.action_home_type_arrow);

		textView_Name = (TextView) findViewById(R.id.actionHome_textview_name);
		textView_Pick = (ImageView) findViewById(R.id.actionHome_imageview_pick);
		msgTypeList = (LinearLayout) findViewById(R.id.actionHome_msgTypeList);
		actionHomeImageBackButton = (ImageButton) findViewById(R.id.action_home_back_button);

		// actionMenuItemCatagoryLayout = ()

		// 弹出菜单
		textView_Name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (msgTypeList.getVisibility() == LinearLayout.VISIBLE) {
					msgTypeList.setVisibility(LinearLayout.INVISIBLE);
					actionHomeTypeArrow
							.setBackgroundResource(R.drawable.top_btn_bg_triangle);
				} else {
					msgTypeList.setVisibility(LinearLayout.VISIBLE);
					actionHomeTypeArrow
							.setBackgroundResource(R.drawable.top_btn_bg_triangle_down);
				}
			}

		});

		// 滑动菜单
		textView_Pick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO 点击滑动页面
				actionHomeMenuDrawer.toggleMenu();
			}

		});
		// 返回
		actionHomeImageBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO 点击返回
				onBackPressed();
			}

		});
		// 选择全部活动
		actionHomeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (msgTypeList.getVisibility() == RelativeLayout.VISIBLE) {
					actionType = 0;

					msgTypeList.setVisibility(RelativeLayout.INVISIBLE);
					actionHomeTypeArrow
							.setBackgroundResource(R.drawable.top_btn_bg_triangle);

					actionHomeLayout
							.setBackgroundResource(R.drawable.action_home_top_selected_bg);
					action_home_top_quanbuhuodong
							.setBackgroundResource(R.drawable.action_home_top_selected_btn);

					actionHomeMyActionLayout
							.setBackgroundResource(R.drawable.action_home_top_unselected_bg);
					action_home_top_wodehuodong
							.setBackgroundResource(R.drawable.action_home_top_unselected_btn);

					actionHomeDeployLayout
							.setBackgroundResource(R.drawable.action_home_top_unselected_bg);
					action_home_top_fabudehuodong
							.setBackgroundResource(R.drawable.action_home_top_unselected_btn);

					textView_Name.setText("全部活动");

					getData();
				}
			}

		});
		// 选择我参与的活动
		actionHomeMyActionLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (msgTypeList.getVisibility() == LinearLayout.VISIBLE) {
					actionType = 1;

					msgTypeList.setVisibility(LinearLayout.INVISIBLE);
					actionHomeTypeArrow
							.setBackgroundResource(R.drawable.top_btn_bg_triangle);

					actionHomeLayout
							.setBackgroundResource(R.drawable.action_home_top_unselected_bg);
					action_home_top_quanbuhuodong
							.setBackgroundResource(R.drawable.action_home_top_unselected_btn);

					actionHomeMyActionLayout
							.setBackgroundResource(R.drawable.action_home_top_selected_bg);
					action_home_top_wodehuodong
							.setBackgroundResource(R.drawable.action_home_top_selected_btn);

					actionHomeDeployLayout
							.setBackgroundResource(R.drawable.action_home_top_unselected_bg);
					action_home_top_fabudehuodong
							.setBackgroundResource(R.drawable.action_home_top_unselected_btn);

					textView_Name.setText("我参与的活动");

					getData();
				}
			}

		});
		// 选择我发布的活动
		actionHomeDeployLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (msgTypeList.getVisibility() == LinearLayout.VISIBLE) {
					actionType = 2;

					msgTypeList.setVisibility(LinearLayout.INVISIBLE);
					actionHomeTypeArrow
							.setBackgroundResource(R.drawable.top_btn_bg_triangle);

					actionHomeLayout
							.setBackgroundResource(R.drawable.action_home_top_unselected_bg);
					action_home_top_quanbuhuodong
							.setBackgroundResource(R.drawable.action_home_top_unselected_btn);

					actionHomeMyActionLayout
							.setBackgroundResource(R.drawable.action_home_top_unselected_bg);
					action_home_top_wodehuodong
							.setBackgroundResource(R.drawable.action_home_top_unselected_btn);

					actionHomeDeployLayout
							.setBackgroundResource(R.drawable.action_home_top_selected_bg);
					action_home_top_fabudehuodong
							.setBackgroundResource(R.drawable.action_home_top_selected_btn);

					textView_Name.setText("我发布的活动");

					getData();
				}
			}

		});
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
					rrAction = ConnectProviderZX.getActivityList(
							UserManager.uid, String.valueOf(actionType),
							String.valueOf(actionSort),
							String.valueOf(actionStatus),
							String.valueOf(actionAward),
							"10","0"
							);

					doListViewUI();
				}

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
				if (rrAction.status.equals("0")) {
					adapter.setItems(rrAction.getDatas());
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	/**
	 * 活动适配器
	 * 
	 * @author zhou
	 * 
	 */
	private class ActionAdapter extends BaseListAdapter<Action> {

//		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.action_home_list_item, null);
				holder = new ViewHolder();
//				holder.actionHomeItemImage = (ImageView) convertView
//						.findViewById(R.id.action_home_list_item_img);
				holder.actionHomeItemTitle = (TextView) convertView
						.findViewById(R.id.action_home_listitem_title);
				holder.actionHomeItemSort = (TextView) convertView
						.findViewById(R.id.action_home_listitem_catagory);
				holder.actionHomeItemTimeStart = (TextView) convertView
						.findViewById(R.id.action_home_list_item_time_start);
				holder.actionHomeItemTimeEnd = (TextView) convertView
						.findViewById(R.id.action_home_list_item_time_end);
				holder.actionHomeItemStatus = (TextView) convertView
						.findViewById(R.id.action_home_listitem_textview_status);
				holder.actionHomeItemReward = (TextView) convertView
						.findViewById(R.id.action_home_list_item_textview_award);
				holder.actionHomeItemJoined = (TextView) convertView
						.findViewById(R.id.action_home_list_item_textview_joined_num);
				holder.actionHomeItemInterested = (TextView) convertView
						.findViewById(R.id.action_home_list_item_textview_interested_num);
				holder.actionHomeItemComments = (TextView) convertView
						.findViewById(R.id.action_home_list_item_textview_comments_num);
				actionHomeListItemAwardFramelayout = (FrameLayout) convertView
						.findViewById(R.id.action_home_list_item_award_framelayout);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder != null) {
				Action item = this.getItem(position);
				holder.actionHomeItemTitle.setText(item.title);
				holder.actionHomeItemSort.setText(item.sort);
				holder.actionHomeItemTimeStart.setText(item.startTime);
				holder.actionHomeItemTimeEnd.setText(item.endTime);
				holder.actionHomeItemStatus.setText(item.status);
				Log.i("estar", item.award);

				if (item.award.equals("无")) {

				} else if (item.award.equals("勋章")) {
					actionHomeListItemAwardFramelayout
							.setVisibility(View.VISIBLE);
					holder.actionHomeItemReward.setText("勋章");
				} else if (item.award.equals("积分")) {
					actionHomeListItemAwardFramelayout
							.setVisibility(View.VISIBLE);
					holder.actionHomeItemReward.setText("积分");
				} else if (item.award.equals("其他")) {
					actionHomeListItemAwardFramelayout
							.setVisibility(View.VISIBLE);
					holder.actionHomeItemReward.setText("其他");
				} else if (item.award.equals("任务")) {
					actionHomeListItemAwardFramelayout
							.setVisibility(View.VISIBLE);
					holder.actionHomeItemReward.setText("任务");
				}

				if (item.istask.equals("是")) {
					((FrameLayout) convertView
							.findViewById(R.id.action_home_list_item_istask_framelayout))
							.setVisibility(View.VISIBLE);
				} else {
					((FrameLayout) convertView
							.findViewById(R.id.action_home_list_item_istask_framelayout))
							.setVisibility(View.GONE);
				}

				holder.actionHomeItemJoined.setText(item.join_num);
				holder.actionHomeItemInterested.setText(item.favor_num);
				holder.actionHomeItemComments.setText(item.commit_num);

//				action_home_list_item_introduce
				((TextView)convertView.findViewById(R.id.action_home_list_item_introduce)).setText(item.content);
				
//				DisplayMetrics metrics = new DisplayMetrics();
//				getWindowManager().getDefaultDisplay().getMetrics(metrics);
//				// int width = metrics.widthPixels - 20;
//				// int height = (int) (width * 346) / 667;
//				int width = (metrics.widthPixels * 667) / 720;
//				int height = (metrics.widthPixels * 346) / 720;
//
//				Bitmap bmp = asyncLoader.loadBitmap(holder.actionHomeItemImage,
//						item.img, width, height, new ImageCallBack() {
//
//							@Override
//							public void imageLoad(ImageView imageView,
//									Bitmap bitmap) {
//								if (bitmap == null) {
//									bitmap = BitmapFactory
//											.decodeResource(
//													getResources(),
//													R.drawable.ic_default_action_list_item);
//								}
//								imageView.setImageBitmap(bitmap);
//							}
//						});
//
//				if (bmp != null) {
//					holder.actionHomeItemImage.setImageBitmap(bmp);
//				}
			}
			return convertView;
		}

	}

	private class ViewHolder {
		ImageView actionHomeItemImage;
		TextView actionHomeItemTitle;
		TextView actionHomeItemSort;
		TextView actionHomeItemTimeStart;
		TextView actionHomeItemTimeEnd;
		TextView actionHomeItemStatus;
		TextView actionHomeItemReward;
		TextView actionHomeItemJoined;
		TextView actionHomeItemInterested;
		TextView actionHomeItemComments;
	}

	public void onBackPressed() {
		if (actionHomeMenuDrawer.getDrawerState() == MenuDrawer.STATE_CLOSED) {
			super.onBackPressed();
		} else {
			actionHomeMenuDrawer.closeMenu();
		}

	}

	public void onResume() {
		super.onResume();
		initTabNavigate(R.id.actionHome_tabNavigate, TAB_INDEX_ACTION);
		initTypeList();
		// initListView();
		actionHomeMenuDrawer.closeMenu();
	}

}
