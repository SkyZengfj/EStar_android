package com.mcds.app.android.estar.ui.action;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.ActivitCommitList;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.util.Utility;

public class CommentListDetailActivity extends BaseActivity {

	private String curActionID;
	private ReturnResult<ActivitCommitList> rrActionComm;
	private String page_num = "0";
	private String page_size = "5";

	private ActionCommAdapter adapter;
	private ListView commListView;

	private ViewPager frameViewPager;
	private ArrayList<View> framePageViews;

	private LinearLayout llhide;
	private ImageView expression_1;
	private EditText dynamic_edit;
	
	private ReturnResult<String> rrsendcommend;
	private RelativeLayout rlcommTop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_detail_comments);
		Bundle bundle = getIntent().getExtras();
		curActionID = bundle.getString("activity_id");

		init();
		getData();
	}

	private void init() {
		// TODO Auto-generated method stub
		rlcommTop = (RelativeLayout)this.findViewById(R.id.action_detail_comments_layout_top);
		rlcommTop.setFocusable(true);
		rlcommTop.setFocusableInTouchMode(true);
		rlcommTop.requestFocus(); 
		
		frameViewPager = (ViewPager) findViewById(R.id.actionvPager);
		framePageViews = new ArrayList<View>();

		View v1 = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.expression_panel_1, null);
		framePageViews.add(v1);

		View v2 = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.expression_panel_2, null);
		framePageViews.add(v2);

		View v3 = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.expression_panel_3, null);
		framePageViews.add(v3);

		PagerAdapter framePagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return framePageViews.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container)
						.removeView(framePageViews.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(framePageViews.get(position));
				return framePageViews.get(position);
			}
		};

		OnPageChangeListener framePageChangeListener = new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		};

		frameViewPager.setAdapter(framePagerAdapter);
		frameViewPager.setOnPageChangeListener(framePageChangeListener);

		llhide = (LinearLayout) findViewById(R.id.action_detail_action_list);
		dynamic_edit = (EditText) findViewById(R.id.action_detail_comments_content);

		expression_1 = (ImageView) v1
				.findViewById(R.id.expression_panel_1_row_1_col_1);
		expression_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int index = dynamic_edit.getSelectionStart();
				Editable editable = dynamic_edit.getText();
				editable.insert(index, "[呲牙]");
			}
		});

		((ImageView) findViewById(R.id.action_detail_comments_smile))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 添加表情
						if (llhide.getVisibility() == 0) {
							llhide.setVisibility(View.GONE);
						} else {
							llhide.setVisibility(View.VISIBLE);
						}
					}
				});

		((ImageView) findViewById(R.id.action_detail_comments_add))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 添加内容
						new Thread(new Runnable() {

							@Override
							public void run() {
								showWaitingDialog();

								if (!curActionID.equals("")) {
									rrsendcommend = ConnectProviderZX
											.commentActivity(curActionID,
													UserManager.uid,
													dynamic_edit.getText()
															.toString());
									if (rrsendcommend.status.equals("0")) {
										getData();
									}
								}
								

								hideWaitingDialog();
							}
						}).start();
					}
				});

		findViewById(R.id.action_detail_comments_back_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onBackPressed();
					}
				});

		commListView = (ListView) findViewById(R.id.action_detail_comments_listview);
		adapter = new ActionCommAdapter();
		commListView.setAdapter(adapter);
	}

	/**
	 * 获取活动详情
	 */
	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!curActionID.equals("")) {
					rrActionComm = ConnectProviderZX.getActivitCommitList(
							curActionID, page_num, page_size);
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
				if (rrActionComm.status.equals("0")) {
					dynamic_edit.setText("");
					adapter.setItems(rrActionComm.getDatas());
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	private class ActionCommAdapter extends BaseListAdapter<ActivitCommitList> {

		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.action_detail_comments_item, null);
				holder = new ViewHolder();
				holder.commentListHeader = (ImageView) convertView
						.findViewById(R.id.action_detail_comments_item_header_img);
				holder.commentListContent = (TextView) convertView
						.findViewById(R.id.action_detail_comments_item_content);
				holder.commentListForwNum = (TextView) convertView
						.findViewById(R.id.action_detail_comments_item_forward_num);
				holder.commentListCommNum = (TextView) convertView
						.findViewById(R.id.action_detail_comments_item_comments_num);
				holder.commentListName = (TextView) convertView
						.findViewById(R.id.action_detail_comments_item_name);
				holder.commentListTime = (TextView) convertView
						.findViewById(R.id.action_detail_comments_item_time);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder != null) {
				ActivitCommitList item = this.getItem(position);
				holder.commentListContent.setText(item.content_text);
				holder.commentListForwNum.setText("0");
				holder.commentListCommNum.setText("0");
				holder.commentListName.setText(item.user_name);
				holder.commentListTime.setText(item.time);

				Bitmap bmp = asyncLoader.loadBitmap(holder.commentListHeader,
						item.user_img,
						Utility.dip2px(CommentListDetailActivity.this, 50),
						Utility.dip2px(CommentListDetailActivity.this, 50),
						new ImageCallBack() {

							@Override
							public void imageLoad(ImageView imageView,
									Bitmap bitmap) {
								if (bitmap == null) {
									bitmap = BitmapFactory.decodeResource(
											getResources(),
											R.drawable.list_item_ic_default);
								}
								imageView.setImageBitmap(bitmap);
							}
						});

				if (bmp != null) {
					holder.commentListHeader.setImageBitmap(bmp);
				}
			}
			return convertView;
		}

	}

	private class ViewHolder {
		private ImageView commentListHeader;
		private TextView commentListName;
		private TextView commentListContent;
		private TextView commentListTime;
		private TextView commentListCommNum;
		private TextView commentListForwNum;
	}

}
