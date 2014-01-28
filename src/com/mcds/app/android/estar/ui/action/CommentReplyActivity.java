package com.mcds.app.android.estar.ui.action;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.ui.BaseActivity;

public class CommentReplyActivity extends BaseActivity {

	private ViewPager frameViewPager;
	private ArrayList<View> framePageViews;
	private String curActionID;

	private LinearLayout llhide;
	private ImageView expression_1;
	private EditText dynamic_edit;

	private ReturnResult<String> rrsendcommend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_detail_onlinehelp_reply);
		Bundle bundle = getIntent().getExtras();
		curActionID = bundle.getString("activity_id");
		//
		init();
		// getData();
	}

	private void init() {
		// TODO Auto-generated method stub
		frameViewPager = (ViewPager) findViewById(R.id.onlinehelp_vPager);
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

		llhide = (LinearLayout) findViewById(R.id.action_detail_onlinehelp_action_list);
		dynamic_edit = (EditText) findViewById(R.id.action_detail_edit_text);

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
		// action_detail_textView_dynamic_menu
		((TextView) findViewById(R.id.action_detail_textView_dynamic_menu))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO 取消
						Intent intent = new Intent(
								CommentReplyActivity.this,
								com.mcds.app.android.estar.ui.action.OnlineHelpDetailActivity.class);
						Bundle bd = new Bundle();
						bd.putString("activity_id", curActionID);
						intent.putExtras(bd);
						startActivity(intent);
					}
				});

		((ImageView) findViewById(R.id.action_detail_edit_btn_affirmation))
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

		((TextView) findViewById(R.id.action_detail_imageview_edit))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 发送
						new Thread(new Runnable() {

							@Override
							public void run() {
								showWaitingDialog();

								if (!curActionID.equals("")) {
									rrsendcommend = ConnectProviderZX
											.replyActivityHelp(curActionID,
													UserManager.uid,
													dynamic_edit.getText()
															.toString());
									if (rrsendcommend.status.equals("0")) {
										Intent intent = new Intent(
												CommentReplyActivity.this,
												com.mcds.app.android.estar.ui.action.OnlineHelpDetailActivity.class);
										Bundle bd = new Bundle();
										bd.putString("activity_id", curActionID);
										intent.putExtras(bd);
										startActivity(intent);
									}
								}
								

								hideWaitingDialog();
							}
						}).start();
					}
				});
	}

	public void onBackPressed() {
		if (llhide.getVisibility() == 0) {
			llhide.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
		}
	}

}
