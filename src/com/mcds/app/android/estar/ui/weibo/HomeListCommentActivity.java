package com.mcds.app.android.estar.ui.weibo;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.WeiboCommit;
import com.mcds.app.android.estar.bean.WeiboForward;
import com.mcds.app.android.estar.bean.WeiboShare;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderGC;
import com.mcds.app.android.estar.ui.BaseActivity;

public class HomeListCommentActivity extends BaseActivity {
	private ViewPager frameViewPager;
	private ArrayList<View> framePageViews;
	
	private TextView sendWeiboTextView;
	
	private String weibo_id;
	private String original_weibo_id = "";
	
	private EditText commentEditText;
	
	private ReturnResult<WeiboCommit> result_WeiboCommit;
	private ReturnResult<WeiboShare> result_WeiboForward;
	
	private boolean isForward = false;
	
	private ImageView forwardMark_icon;
	private ImageView expression_1;
	private ImageView affirmation_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_home_comment_reply_edit);
		
		weibo_id = getIntent().getStringExtra("weibo_id");
		original_weibo_id = getIntent().getStringExtra("original_weibo_id");
		
		result_WeiboCommit = new ReturnResult<WeiboCommit>();
		result_WeiboForward = new ReturnResult<WeiboShare>();
		
		forwardMark_icon = (ImageView) findViewById(R.id.isForward_mark);
		affirmation_btn = (ImageView) findViewById(R.id.weiboHome_comment_imageview_affirmation_btn);
		
		commentEditText = (EditText) findViewById(R.id.weiboHome_comment_edittext);
		
		sendWeiboTextView = (TextView) findViewById(R.id.weiboHome_comment_send_textview);
		
		frameViewPager = (ViewPager) findViewById(R.id.comment_vPager);
		framePageViews = new ArrayList<View>();

		View v1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.expression_panel_1, null);
		framePageViews.add(v1);

		View v2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.expression_panel_2, null);
		framePageViews.add(v2);

		View v3 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.expression_panel_3, null);
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
				((ViewPager) container).removeView(framePageViews.get(position));
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
//				if (position > 2) {
//					startActivity(new Intent(WelcomeActivity.this, HomeMainFrameActivity.class));
//					PreferencesProvider.put(PreferencesProvider.RUN_FIRST, false);
//					finish();  
//				}
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
		
		expression_1 = (ImageView) v1.findViewById(R.id.expression_panel_1_row_1_col_1);
		expression_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int index = commentEditText.getSelectionStart();
				Editable editable = commentEditText.getText();
				editable.insert(index, "[呲牙]");
			}
		});
		
		affirmation_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
				if(frameViewPager.getVisibility() == View.VISIBLE) {
					frameViewPager.setVisibility(LinearLayout.GONE);
				}else{
					frameViewPager.setVisibility(LinearLayout.VISIBLE);
				}
				
			}
		});
		
		sendWeiboTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Editable editable = commentEditText.getText();
				String content = editable.toString();
				content = content.replace(" ", "");
				
				if(content.length() == 0) {
					Toast toast = Toast.makeText(HomeListCommentActivity.this, "请输入内容", Toast.LENGTH_LONG);
					toast.show();
				}else{
					showWaitingDialog();
					
					if (!UserManager.uid.equals("")) {
						result_WeiboCommit = ConnectProviderGC.commitWeibo(UserManager.uid, weibo_id, content);
						if(isForward) {
							result_WeiboForward = ConnectProviderGC.shareWeibo(UserManager.uid, weibo_id, content, original_weibo_id);
						}
						if (result_WeiboCommit.status.equals("0")) {
							Intent intent = new Intent();
							intent.putExtra("comment_result", "0");
							intent.putExtra("comment_forward_result", result_WeiboForward.status);
							setResult(2, intent);
							finish();
						}else{
							Toast toast = Toast.makeText(HomeListCommentActivity.this, result_WeiboCommit.info, Toast.LENGTH_LONG);
							toast.show();
						}
					}else{
						Toast toast = Toast.makeText(HomeListCommentActivity.this, "身份过期，请重新登录", Toast.LENGTH_LONG);
						toast.show();
					}
					
					hideWaitingDialog();
				}
			}
		});
		
		forwardMark_icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isForward) {
					forwardMark_icon.setBackgroundResource(R.drawable.contact_image_button_unselected_mark_icon);
					isForward = false;
				}else{
					forwardMark_icon.setBackgroundResource(R.drawable.contact_image_button_selected_mark_icon);
					isForward = true;
				}
			}
		});
	}

}
