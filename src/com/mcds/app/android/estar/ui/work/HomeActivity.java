package com.mcds.app.android.estar.ui.work;

import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.MatterList;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.ui.BaseTabActivity;

/**
 * 工作
 * 
 * @author TangChao
 */
public class HomeActivity extends BaseTabActivity implements OnItemClickListener, OnItemLongClickListener
{
	private static final String Tag = "HOME_ACTIVITY";
	/**
	 * 标题栏文字
	 */
	private TextView title_name;
	/**
	 * 标题选项列表
	 */
	private LinearLayout myList;
	/**
	 * 我参与的事项
	 */
	private LinearLayout myAttendList;
	/**
	 * 我负责的事项
	 */
	private LinearLayout myResponsiblityList;
	/**
	 * 我创建的事项
	 */
	private LinearLayout myCreateList;
	private ViewPager frameViewPager;
	private ArrayList<View> framePageViews;
	private View view0; // 进行中
	private View view1; // 逾期的
	private View view2; // 已完成
	private ImageView sortBtn0;// 最早创建的
	private ImageView sortBtn1;// 最新创建的
	private ImageView sortBtn2;// 快到期的
	private TextView tab0_text;
	private TextView tab1_text;
	private TextView tab2_text;
	private ListView view0Lv;
	private ListView view1Lv;
	private ListView view2Lv;
	private WorkAdapter adapter0;
	private ReturnResult<MatterList> rrWork;
	/**
	 * 0(myAttendList),1(myResponsiblityList),2(myCreateList)
	 */
	public static int messageType = 0;
	/**
	 * 侧滑对象
	 */
	private MenuDrawer workHomeMenuDrawer;
	private RelativeLayout workHomeMenuDrawerLayout;
	/**
	 * 排序变量
	 */
	private String sort1 = null;//0:未开始 1:正在进行 2:已完结 3:删除 4: 逾期
	private String sort2 = null;//0:我参与的 1:我负责的 2:我创建的
	private String sort3 = null;//0:最早创建的排最前 1:最新创建的排最前 2:快到期的排最前
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		/*--------------------------------------*/
		workHomeMenuDrawer = MenuDrawer.attach(this, Position.RIGHT);
		workHomeMenuDrawer.closeMenu();
		workHomeMenuDrawer.setContentView(R.layout.work_home);
		initTabNavigate(R.id.workHome_tabNavigate, TAB_INDEX_WORK);
		workHomeMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		workHomeMenuDrawerLayout = (RelativeLayout) RelativeLayout.inflate(this, R.layout.work_home_side_slip_layout, null);
		workHomeMenuDrawer.setMenuView(workHomeMenuDrawerLayout);
		/*-----------------------------------------*/
		tab0_text = (TextView) findViewById(R.id.workHome_tab0_text);
		tab1_text = (TextView) findViewById(R.id.workHome_tab1_text);
		tab2_text = (TextView) findViewById(R.id.workHome_tab2_text);
		title_name = (TextView) findViewById(R.id.work_home_mission_detail_title_name);
		myList = (LinearLayout) findViewById(R.id.workHome_msgTypeList);
		myAttendList = (LinearLayout) findViewById(R.id.workHome_msgTypeList_participate);
		myResponsiblityList = (LinearLayout) findViewById(R.id.workHome_msgTypeList_responsible);
		myCreateList = (LinearLayout) findViewById(R.id.workHome_msgTypeList_create);
		/*------------------------------------*/
		initFrameViewPager();
		view0Lv = (ListView) view0.findViewById(R.id.workHome0_lv);
		view1Lv = (ListView) view1.findViewById(R.id.workHome1_lv);
		view2Lv = (ListView) view2.findViewById(R.id.workHome2_lv);
		adapter0 = new WorkAdapter();
		view0Lv.setAdapter(adapter0);
		view0Lv.setOnItemClickListener(this);
		view0Lv.setOnItemLongClickListener(this);
		/*-------------------------------------------*/
		sortBtn0 = (ImageView) findViewById(R.id.work_home_sort_status_image0);
		sortBtn1 = (ImageView) findViewById(R.id.work_home_sort_status_image1);
		sortBtn2 = (ImageView) findViewById(R.id.work_home_sort_status_image2);
		sortBtn0.setOnClickListener(new SlidingOnClickListener());
		sortBtn1.setOnClickListener(new SlidingOnClickListener());
		sortBtn2.setOnClickListener(new SlidingOnClickListener());
		if (sort3 != null)
		{
			if (sort3.equals("0"))
			{
				sortBtn0.setBackgroundResource(R.drawable.work_home_sliding_item_checked_icon);
				sortBtn1.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
				sortBtn2.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
			}
			else if (sort3.equals("1"))
			{
				sortBtn0.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
				sortBtn1.setBackgroundResource(R.drawable.work_home_sliding_item_checked_icon);
				sortBtn2.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
			}
			else if (sort3.equals("2"))
			{
				sortBtn0.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
				sortBtn1.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
				sortBtn2.setBackgroundResource(R.drawable.work_home_sliding_item_checked_icon);
			}
		}
		else
		{
			sortBtn0.setBackgroundResource(R.drawable.work_home_sliding_item_checked_icon);
			sortBtn1.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
			sortBtn2.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
		}
		/*---------------------------------------------*/
		findViewById(R.id.under_line_0).setBackgroundResource(R.drawable.work_home_tab_cursor);
		findViewById(R.id.under_line_1).setBackgroundResource(0);
		findViewById(R.id.under_line_2).setBackgroundResource(0);
		tab0_text.setTextColor(0xffeb4666);
		tab1_text.setTextColor(0xff3a3a3a);
		tab2_text.setTextColor(0xff3a3a3a);
		/*----------------------------------------*/
		if (sort3 != null)
		{
			getData("1", "0", sort3);
		}
		else
		{
			getData("1", "0", "0");
		}
		/**
		 * 新建事项
		 */
		findViewById(R.id.work_home_matter_list_add_matter).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(HomeActivity.this, AddMatterActivity.class);
				intent.putExtra("TAG", Tag);
				startActivity(intent);
				
			}
		});
		/**
		 * 按创建事项的先后顺序排序
		 */
		findViewById(R.id.work_home_matter_list_sort).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				workHomeMenuDrawer.toggleMenu();
			}
		});
		/**
		 * 我参与的
		 */
		myAttendList.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
//				if (messageType != 0)
//				{
//					switch (messageType)
//					{
//						case 1:
//							myResponsiblityList.setBackgroundDrawable(getResources().getDrawable(
//									R.drawable.main_frame_top_msg_type_top_item_unselected_bg));
//							break;
//						case 2:
//							myCreateList.setBackgroundDrawable(getResources().getDrawable(
//									R.drawable.main_frame_top_msg_type_top_item_unselected_bg));
//							break;
//						default:
//							break;
//					}
//					messageType = 0;
//					myList.setVisibility(LinearLayout.INVISIBLE);
					title_name.setText("我参与的事项");
					myResponsiblityList.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.main_frame_top_msg_type_top_item_unselected_bg));
					myCreateList.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.main_frame_top_msg_type_top_item_unselected_bg));
					myAttendList.setBackgroundDrawable(getResources().getDrawable(R.drawable.main_frame_top_msg_type_top_item_selected_bg));
					myList.setVisibility(LinearLayout.INVISIBLE);
//					if (sort1 != null && sort2 != null & sort3 != null)
//					{
//						sort2 = "0";
//						getData(sort1, sort2, sort3);
//					}
//					else
//					{
						getData("1", "0", "1");
//					}
//				}
//				else
//				{
//					myList.setVisibility(LinearLayout.INVISIBLE);
//				}
			}
		});
		/**
		 * 我负责的
		 */
		myResponsiblityList.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
//				if (messageType != 1)
//				{
//					switch (messageType)
//					{
//						case 0:
							myAttendList.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.main_frame_top_msg_type_top_item_unselected_bg));
//							break;
//						case 2:
							myCreateList.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.main_frame_top_msg_type_top_item_unselected_bg));
//							break;
//						default:
//							break;
//					}
//					messageType = 1;
//					myList.setVisibility(LinearLayout.INVISIBLE);
					title_name.setText("我负责的事项");
					myResponsiblityList.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.main_frame_top_msg_type_top_item_selected_bg));
					myList.setVisibility(LinearLayout.INVISIBLE);
//					if (sort1 != null && sort2 != null & sort3 != null)
//					{
//						sort2 = "1";
//						getData(sort1, sort2, sort3);
//					}
//					else
//					{
						getData("1", "1", "1");
//					}
				}
//				else
//				{
//					myList.setVisibility(LinearLayout.INVISIBLE);
//				}
//			}
		});
		/**
		 * 我创建的
		 */
		myCreateList.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
//				if (messageType != 2)
//				{
//					switch (messageType)
//					{
//						case 0:
							myAttendList.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.main_frame_top_msg_type_top_item_unselected_bg));
//							break;
//						case 1:
							myResponsiblityList.setBackgroundDrawable(getResources().getDrawable(
									R.drawable.main_frame_top_msg_type_top_item_unselected_bg));
//							break;
//						default:
//							break;
//					}
//					messageType = 2;
//					myList.setVisibility(LinearLayout.INVISIBLE);
					title_name.setText("我创建的事项");
					myCreateList.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.main_frame_top_msg_type_top_item_selected_bg));
					myList.setVisibility(LinearLayout.INVISIBLE);
//					if (sort1 != null && sort2 != null & sort3 != null)
//					{
//						sort2 = "2";
//						getData(sort1, sort2, sort3);
//					}
//					else
//					{
						getData("1", "2", "1");
//					}
//				}
//				else
//				{
//					myList.setVisibility(LinearLayout.INVISIBLE);
//				}
			}
		});
		title_name.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (myList.getVisibility() == LinearLayout.VISIBLE)
				{
					myList.setVisibility(LinearLayout.INVISIBLE);
				}
				else
				{
					myList.setVisibility(LinearLayout.VISIBLE);
				}
			}
		});
	}
	private void initFrameViewPager()
	{
		frameViewPager = (ViewPager) findViewById(R.id.workHome_vp);
		framePageViews = new ArrayList<View>();
		view0 = getLayoutInflater().inflate(R.layout.work_home_0, null);
		framePageViews.add(view0);
		view1 = getLayoutInflater().inflate(R.layout.work_home_1, null);
		framePageViews.add(view1);
		view2 = getLayoutInflater().inflate(R.layout.work_home_2, null);
		framePageViews.add(view2);
		PagerAdapter framePagerAdapter = new PagerAdapter()
		{
			@Override
			public boolean isViewFromObject(View arg0, Object arg1)
			{
				return arg0 == arg1;
			}
			@Override
			public int getCount()
			{
				return framePageViews.size();
			}
			@Override
			public void destroyItem(View container, int position, Object object)
			{
				((ViewPager) container).removeView(framePageViews.get(position));
			}
			@Override
			public Object instantiateItem(View container, int position)
			{
				((ViewPager) container).addView(framePageViews.get(position));
				return framePageViews.get(position);
			}
		};
		OnPageChangeListener framePageChangeListener = new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
				switch (position)
				{
					case 0:// 进行中
						findViewById(R.id.under_line_0).setBackgroundResource(R.drawable.work_home_tab_cursor);
						findViewById(R.id.under_line_1).setBackgroundResource(0);
						findViewById(R.id.under_line_2).setBackgroundResource(0);
						tab0_text.setTextColor(0xffeb4666);
						tab1_text.setTextColor(0xff3a3a3a);
						tab2_text.setTextColor(0xff3a3a3a);
						view0Lv.setAdapter(adapter0);
						view0Lv.setOnItemClickListener(HomeActivity.this);
						view0Lv.setOnItemLongClickListener(HomeActivity.this);
						if (sort1 != null && sort2 != null & sort3 != null)
						{
							sort1 = "1";
							getData(sort1, sort2, sort3);
						}
						else
						{
							getData("1", "0", "1");
						}
						break;
					case 1:// 逾期的
						findViewById(R.id.under_line_0).setBackgroundResource(0);
						findViewById(R.id.under_line_1).setBackgroundResource(R.drawable.work_home_tab_cursor);
						findViewById(R.id.under_line_2).setBackgroundResource(0);
						tab0_text.setTextColor(0xff3a3a3a);
						tab1_text.setTextColor(0xffeb4666);
						tab2_text.setTextColor(0xff3a3a3a);
						view1Lv.setAdapter(adapter0);
						view1Lv.setOnItemClickListener(HomeActivity.this);
						view1Lv.setOnItemLongClickListener(HomeActivity.this);
						if (sort1 != null && sort2 != null & sort3 != null)
						{
							sort1 = "4";
							getData(sort1, sort2, sort3);
						}
						else
						{
							getData("1", "0", "1");
						}
						break;
					case 2:// 已完成
						findViewById(R.id.under_line_0).setBackgroundResource(0);
						findViewById(R.id.under_line_1).setBackgroundResource(0);
						findViewById(R.id.under_line_2).setBackgroundResource(R.drawable.work_home_tab_cursor);
						tab0_text.setTextColor(0xff3a3a3a);
						tab1_text.setTextColor(0xff3a3a3a);
						tab2_text.setTextColor(0xffeb4666);
						view2Lv.setAdapter(adapter0);
						view2Lv.setOnItemClickListener(HomeActivity.this);
						view2Lv.setOnItemLongClickListener(HomeActivity.this);
						if (sort1 != null && sort2 != null & sort3 != null)
						{
							sort1 = "2";
							getData(sort1, sort2, sort3);
						}
						else
						{
							getData("1", "0", "1");
						}
						break;
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
			}
			@Override
			public void onPageScrollStateChanged(int arg0)
			{
			}
		};
		frameViewPager.setAdapter(framePagerAdapter);
		frameViewPager.setOnPageChangeListener(framePageChangeListener);
	}
	/**
	 * 請求事項列表
	 * 
	 * @param theme_sort1
	 * @param theme_sort2
	 * @param theme_sort3
	 */
	private void getData(final String theme_sort1, final String theme_sort2, final String theme_sort3)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!UserManager.uid.equals(""))
				{
					rrWork = ConnectProvider.getThemeList(UserManager.uid, theme_sort1, theme_sort2, theme_sort3);
					Log.i("UID", UserManager.uid);
					doUI();
				}
				hideWaitingDialog();
				sort1 = theme_sort1;
				sort2 = theme_sort2;
				sort3 = theme_sort3;
			}
		}).start();
	}
	/**
	 * 刷新界面
	 */
	private void doUI()
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if (rrWork.status.equals("0"))
				{
					adapter0.setItems(rrWork.getDatas());
					adapter0.notifyDataSetChanged();
				}
			}
		});
	}
	
	private class WorkAdapter extends BaseListAdapter<MatterList>
	{
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder = null;
			if (convertView == null)
			{
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.work_home_0_listview_item, null);
				holder = new ViewHolder();
				holder.work_name = (TextView) convertView.findViewById(R.id.name);
				holder.work_start_time = (TextView) convertView.findViewById(R.id.start_time);
				holder.work_content = (TextView) convertView.findViewById(R.id.content);
				holder.work_mission_number = (TextView) convertView.findViewById(R.id.task_number);
				holder.work_end_time = (TextView) convertView.findViewById(R.id.end_time);
				holder.work_mission_number_show = (TextView) convertView.findViewById(R.id.task_show);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			if (holder != null)
			{
				MatterList item = this.getItem(position);
				holder.work_name.setText(item.name);
				holder.work_start_time.setText(item.time_setup);
				holder.work_content.setText(item.content);
				holder.work_mission_number.setText(item.missions_num);
				holder.work_end_time.setText(item.time_end + " 截止");
				holder.work_mission_number_show.setText(item.missions_num + " 我的任务");
			}
			return convertView;
		}
	}
	
	private class ViewHolder
	{
		TextView work_name;
		TextView work_content;
		TextView work_start_time;
		TextView work_end_time;
		TextView work_mission_number;
		TextView work_mission_number_show;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		MatterList work = (MatterList) arg0.getItemAtPosition(arg2);
		Intent intent = new Intent(this, MatterDetialActivity.class);
		intent.putExtra("theme_id", work.theme_id);
		startActivity(intent);
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		MatterList work = (MatterList) arg0.getItemAtPosition(arg2);
		Intent intent = new Intent(HomeActivity.this, MyDeleteMatterListDialogActivity.class);
		intent.putExtra("theme_id", work.theme_id);
		startActivity(intent);
		return true;
	}
	
	private class SlidingOnClickListener implements OnClickListener
	{
		public void onClick(View v)
		{
			switch (v.getId())
			{
				case R.id.work_home_sort_status_image0:// 最早创建
					Log.i("OnClick", "最早创建");
					sortBtn0.setBackgroundResource(R.drawable.work_home_sliding_item_checked_icon);
					sortBtn1.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
					sortBtn2.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
					if (sort1 != null && sort2 != null & sort3 != null)
					{
						sort3 = "0";
						getData(sort1, sort2, sort3);
					}
					else
					{
						getData("1", "0", "1");
					}
					workHomeMenuDrawer.closeMenu();
					break;
				case R.id.work_home_sort_status_image1:// 最新创建
					Log.i("OnClick", "最新创建");
					sortBtn0.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
					sortBtn1.setBackgroundResource(R.drawable.work_home_sliding_item_checked_icon);
					sortBtn2.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
					if (sort1 != null && sort2 != null & sort3 != null)
					{
						sort3 = "1";
						getData(sort1, sort2, sort3);
					}
					else
					{
						getData("1", "0", "1");
					}
					workHomeMenuDrawer.closeMenu();
					break;
				case R.id.work_home_sort_status_image2:// 快到期的
					Log.i("OnClick", "快到期");
					sortBtn0.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
					sortBtn1.setBackgroundResource(R.drawable.work_home_sliding_item_unchecked_icon);
					sortBtn2.setBackgroundResource(R.drawable.work_home_sliding_item_checked_icon);
					if (sort1 != null && sort2 != null & sort3 != null)
					{
						sort3 = "2";
						getData(sort1, sort2, sort3);
					}
					else
					{
						getData("1", "0", "1");
					}
					workHomeMenuDrawer.closeMenu();
					break;
				default:
					break;
			}
		}
	};
}
