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
import com.mcds.app.android.estar.bean.WeiboShare;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderGC;
import com.mcds.app.android.estar.ui.BaseActivity;

public class HomeListForwordingActivity extends BaseActivity {
	private ViewPager frameViewPager;
	private ArrayList<View> framePageViews;
	
	private TextView sendWeiboTextView;
	private TextView tv_original_userName;
	private TextView tv_original_content;
	
	private ImageView affirmation_btn;
	private ImageView expression_1;
	
	private String weibo_id;
	private String original_userName;
	private String original_content;
	
	private EditText forwardEditText;
	
	private ReturnResult<WeiboShare> result_WeiboShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_home_forwarding_edit);
		
		weibo_id = getIntent().getStringExtra("weibo_id");
		original_userName = getIntent().getStringExtra("userName");
		original_content = getIntent().getStringExtra("content");
		
		tv_original_userName = (TextView) findViewById(R.id.weiboHome_forward_textview_original_username);
		tv_original_userName.setText(original_userName);
		tv_original_content = (TextView) findViewById(R.id.weiboHome_forward_textview_original_content);
		tv_original_content.setText(original_content);
		
		frameViewPager = (ViewPager) findViewById(R.id.forward_vPager);
		framePageViews = new ArrayList<View>();

		View v1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.expression_panel_1, null);
		framePageViews.add(v1);
//		LinearLayout l1=(LinearLayout)v1.findViewById(R.id.expression_panel_1_row_1);
//		for (int i = 0; i < l1.getChildCount(); i++) {
//			l1.getChildAt(i).setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//				String tagName = arg0.getTag().toString();
//					
//				}
//			});
//		}
//		LinearLayout l2=(LinearLayout)v1.findViewById(R.id.expression_panel_1_row_2);
//		for (int i = 0; i < l1.getChildCount(); i++) {
//			l1.getChildAt(i).setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//				String tagName = arg0.getTag().toString();
//					
//				}
//			});
//		}
//		LinearLayout l3=(LinearLayout)v1.findViewById(R.id.expression_panel_1_row_3);
//		for (int i = 0; i < l1.getChildCount(); i++) {
//			l1.getChildAt(i).setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//				String tagName = arg0.getTag().toString();
//					
//				}
//			});
//		}

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
				int index = forwardEditText.getSelectionStart();
				Editable editable = forwardEditText.getText();
				editable.insert(index, "[呲牙]");
			}
		});
		
		affirmation_btn = (ImageView) findViewById(R.id.weiboHome_forward_imageview_affirmation_btn);
		affirmation_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(forwardEditText.getWindowToken(), 0);
				
				if(frameViewPager.getVisibility() == LinearLayout.VISIBLE) {
					frameViewPager.setVisibility(LinearLayout.GONE);
				}else{
					frameViewPager.setVisibility(LinearLayout.VISIBLE);
				}
			}
		});
		
		forwardEditText = (EditText) findViewById(R.id.weiboHome_forward_edittext);
		
		sendWeiboTextView = (TextView) findViewById(R.id.weiboHome_forward_textview_send);
		sendWeiboTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showWaitingDialog();
				
				if (!UserManager.uid.equals("")) {
					Editable editable = forwardEditText.getText();
					String content = editable + "";
					result_WeiboShare = ConnectProviderGC.shareWeibo(UserManager.uid, weibo_id, content, original_content);
					if (result_WeiboShare.status.equals("0")) {
						Toast toast = Toast.makeText(HomeListForwordingActivity.this, "发布成功", Toast.LENGTH_LONG);
						toast.show();
						Intent intent = new Intent();
						intent.putExtra("forward_result", "0");
						setResult(1, intent);
						finish();
					}else{
						Toast toast = Toast.makeText(HomeListForwordingActivity.this, result_WeiboShare.info, Toast.LENGTH_LONG);
						toast.show();
					}
					
				}else{
					
				}
				
				hideWaitingDialog();
			}
		});
	}

}
