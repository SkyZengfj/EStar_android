package com.mcds.app.android.estar.ui.news;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.HeartHomeList;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.TestList;
import com.mcds.app.android.estar.manager.NewsTestManager;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.ui.BaseTabActivity;
import com.mcds.app.android.estar.util.LayoutUtility;
import com.mcds.app.android.estar.util.Utility;

public class HeartTestActivity extends BaseActivity {

	private ViewPager testPager;// 页卡内容
	private List<View> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private TextView t_gexing, t_aiqing, t_shiye, t_quwei;// 页卡头标
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号

	private ListView testListView0;
	private ImageView newsListImgBig0;
	private TextView newsListTitleBig0;

	private ListView testListView1;
	private ImageView newsListImgBig1;
	private TextView newsListTitleBig1;

	private ListView testListView2;
	private ImageView newsListImgBig2;
	private TextView newsListTitleBig2;

	private ListView testListView3;
	private ImageView newsListImgBig3;
	private TextView newsListTitleBig3;
	
//	private ScrollView scrollview_0;
//	private ScrollView scrollview_1;
//	private ScrollView scrollview_2;
//	private ScrollView scrollview_3;
	
//	private Runnable scrolltorun_0 = new Runnable() {
//
//		@Override
//		public void run() {
//			scrollview_0.scrollTo(0, 0);// 改变滚动条的位置
//		}
//	};
//	private Runnable scrolltorun_1 = new Runnable() {
//
//		@Override
//		public void run() {
//			scrollview_1.scrollTo(0, 0);// 改变滚动条的位置
//		}
//	};
//	private Runnable scrolltorun_2 = new Runnable() {
//
//		@Override
//		public void run() {
//			scrollview_2.scrollTo(0, 0);// 改变滚动条的位置
//		}
//	};
//	private Runnable scrolltorun_3 = new Runnable() {
//
//		@Override
//		public void run() {
//			scrollview_3.scrollTo(0, 0);// 改变滚动条的位置
//		}
//	};

	private TestAdapter_0 adapter_0;
	private TestAdapter_1 adapter_1;
	private TestAdapter_2 adapter_2;
	private TestAdapter_3 adapter_3;
	
	private ReturnResult<TestList> rrTest_0;
	private ReturnResult<TestList> rrTest_1;
	private ReturnResult<TestList> rrTest_2;
	private ReturnResult<TestList> rrTest_3;
	
	private LinearLayout llToplayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_heart_test);
		init();
		InitViewPager();
		InitTextView();
		InitImageView();
	}

	private void init() {
		// TODO Auto-generated method stub

	}

	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		// offset = (screenW / 4 - bmpW) / 2;// 计算偏移量
		offset = screenW / 4;
		Matrix matrix = new Matrix();
		matrix.postTranslate(0, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	private void InitTextView() {
		t_gexing = (TextView) findViewById(R.id.news_heart_test_gexing);
		t_aiqing = (TextView) findViewById(R.id.news_heart_test_aiqing);
		t_shiye = (TextView) findViewById(R.id.news_heart_test_shiye);
		t_quwei = (TextView) findViewById(R.id.news_heart_test_quwei);

		t_gexing.setOnClickListener(new TestOnClickListener(0));
		t_aiqing.setOnClickListener(new TestOnClickListener(1));
		t_shiye.setOnClickListener(new TestOnClickListener(2));
		t_quwei.setOnClickListener(new TestOnClickListener(3));
	}

	private void InitViewPager() {
		testPager = (ViewPager) findViewById(R.id.newsTestPager);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.news_test_home_0, null));
		listViews.add(mInflater.inflate(R.layout.news_test_home_1, null));
		listViews.add(mInflater.inflate(R.layout.news_test_home_2, null));
		listViews.add(mInflater.inflate(R.layout.news_test_home_3, null));
		testPager.setAdapter(new TestPagerAdapter(listViews));
		testPager.setCurrentItem(0);
		testPager.setOnPageChangeListener(new TestOnPageChangeListener());

		testListView0 = (ListView) listViews.get(0).findViewById(
				R.id.newsTest_listView_0);
		newsListImgBig0 = (ImageView) listViews.get(0).findViewById(
				R.id.news_test_big_imgs_0);
		newsListTitleBig0 = (TextView) listViews.get(0).findViewById(
				R.id.news_test_big_title_0);
//		scrollview_0 = (ScrollView)listViews.get(0).findViewById(
//				R.id.news_test_big_layout_0);
		
		testListView1 = (ListView) listViews.get(1).findViewById(
				R.id.newsTest_listView_1);
		newsListImgBig1 = (ImageView) listViews.get(1).findViewById(
				R.id.news_test_big_imgs_1);
		newsListTitleBig1 = (TextView) listViews.get(1).findViewById(
				R.id.news_test_big_title_1);
//		scrollview_1 = (ScrollView)listViews.get(1).findViewById(
//				R.id.news_test_big_layout_1);
		
		testListView2 = (ListView) listViews.get(2).findViewById(
				R.id.newsTest_listView_2);
		newsListImgBig2 = (ImageView) listViews.get(2).findViewById(
				R.id.news_test_big_imgs_2);
		newsListTitleBig2 = (TextView) listViews.get(2).findViewById(
				R.id.news_test_big_title_2);
//		scrollview_2 = (ScrollView)listViews.get(2).findViewById(
//				R.id.news_test_big_layout_2);
		
		testListView3 = (ListView) listViews.get(3).findViewById(
				R.id.newsTest_listView_3);
		newsListImgBig3 = (ImageView) listViews.get(3).findViewById(
				R.id.news_test_big_imgs_3);
		newsListTitleBig3 = (TextView) listViews.get(3).findViewById(
				R.id.news_test_big_title_3);
//		scrollview_3 = (ScrollView)listViews.get(3).findViewById(
//				R.id.news_test_big_layout_3);
		
//		
		llToplayout = (LinearLayout)this.findViewById(R.id.news_heart_test_layout_top);
		
		llToplayout.setFocusable(true);
		llToplayout.setFocusableInTouchMode(true);
		llToplayout.requestFocus(); 



		adapter_0 = new TestAdapter_0();
		testListView0.setAdapter(adapter_0);
		testListView0.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HeartTestStart.class);
				Bundle bundle = new Bundle();
				if(rrTest_0 != null){
					bundle.putSerializable("rr_test", rrTest_0.getDatas().get(arg2));
				}
				NewsTestManager.test_list = null;
                intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		adapter_1 = new TestAdapter_1();
		testListView1.setAdapter(adapter_1);
		testListView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HeartTestStart.class);
				Bundle bundle = new Bundle();
				if(rrTest_1 != null){
					bundle.putSerializable("rr_test", rrTest_1.getDatas().get(arg2));
				}
				NewsTestManager.test_list = null;
                intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		adapter_2 = new TestAdapter_2();
		testListView2.setAdapter(adapter_2);
		testListView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HeartTestStart.class);
				Bundle bundle = new Bundle();
				if(rrTest_2 != null){
					bundle.putSerializable("rr_test", rrTest_2.getDatas().get(arg2));
				}
				NewsTestManager.test_list = null;
                intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		adapter_3 = new TestAdapter_3();
		testListView3.setAdapter(adapter_3);
		testListView3.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HeartTestStart.class);
				Bundle bundle = new Bundle();
				if(rrTest_3 != null){
					bundle.putSerializable("rr_test", rrTest_3.getDatas().get(arg2));
				}
				NewsTestManager.test_list = null;
                intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		getData();
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
					rrTest_0 = ConnectProviderZX.getTestList(UserManager.uid,
							"5");
					rrTest_1 = ConnectProviderZX.getTestList(UserManager.uid,
							"1");
					rrTest_2 = ConnectProviderZX.getTestList(UserManager.uid,
							"2");
					rrTest_3 = ConnectProviderZX.getTestList(UserManager.uid,
							"3");
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
				
				if (rrTest_0 != null && rrTest_0.status.equals("0")) {
					adapter_0.setItems(rrTest_0.getDatas());
					LayoutUtility
					.setListViewHeightBasedOnChildren(testListView0);
					adapter_0.notifyDataSetChanged();
				}
				if (rrTest_1 != null && rrTest_1.status.equals("0")) {
					adapter_1.setItems(rrTest_1.getDatas());
					LayoutUtility
					.setListViewHeightBasedOnChildren(testListView1);
					adapter_1.notifyDataSetChanged();
				}
				if (rrTest_2 != null && rrTest_2.status.equals("0")) {
					adapter_2.setItems(rrTest_2.getDatas());
					LayoutUtility
					.setListViewHeightBasedOnChildren(testListView2);
					adapter_2.notifyDataSetChanged();
				}
				if (rrTest_3 != null && rrTest_3.status.equals("0")) {
					adapter_3.setItems(rrTest_3.getDatas());
					LayoutUtility
					.setListViewHeightBasedOnChildren(testListView3);
					adapter_3.notifyDataSetChanged();
				}
				
				llToplayout.setFocusable(true);
				llToplayout.setFocusableInTouchMode(true);
				llToplayout.requestFocus(); 
			}
		});
	}

	public class TestOnClickListener implements View.OnClickListener {
		private int index = 0;

		public TestOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			testPager.setCurrentItem(index);
		}
	};

	public class TestOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(offset, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(offset * 2, 0, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(offset * 3, 0, 0, 0);
				}
				t_gexing.setTextColor(Color.parseColor("#eb4666"));
				t_aiqing.setTextColor(Color.parseColor("#444444"));
				t_shiye.setTextColor(Color.parseColor("#444444"));
				t_quwei.setTextColor(Color.parseColor("#444444"));
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, offset, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(offset * 2, offset, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(offset * 3, offset, 0, 0);
				} 
				t_gexing.setTextColor(Color.parseColor("#444444"));
				t_aiqing.setTextColor(Color.parseColor("#eb4666"));
				t_shiye.setTextColor(Color.parseColor("#444444"));
				t_quwei.setTextColor(Color.parseColor("#444444"));
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, offset * 2, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(offset, offset * 2, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(offset * 3, offset * 2,
							0, 0);
				}
				t_gexing.setTextColor(Color.parseColor("#444444"));
				t_aiqing.setTextColor(Color.parseColor("#444444"));
				t_shiye.setTextColor(Color.parseColor("#eb4666"));
				t_quwei.setTextColor(Color.parseColor("#444444"));
				break;
			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, offset * 3, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(offset, offset * 3, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(offset * 2, offset * 3,
							0, 0);
				}
				t_gexing.setTextColor(Color.parseColor("#444444"));
				t_aiqing.setTextColor(Color.parseColor("#444444"));
				t_shiye.setTextColor(Color.parseColor("#444444"));
				t_quwei.setTextColor(Color.parseColor("#eb4666"));
				break;
			}
			
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);

		}

	}

	public class TestPagerAdapter extends PagerAdapter {

		public List<View> mListViews;

		public TestPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == (arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public Object instantiateItem(View view, int position) {
			((ViewPager) view).addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

	}
	
	public class TestAdapter_0 extends BaseListAdapter<TestList>{

		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.news_newslist_item_0, null);
			TestList item = this.getItem(position);
			
			if (position == 0) {
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);
				int width = (metrics.widthPixels * 720) / 720;
				int height = (metrics.widthPixels * 396) / 720;
				
				newsListTitleBig0
				.setText(rrTest_0.getDatas().get(0).test_title);

				Bitmap bmpbig0 = asyncLoader.loadBitmap(newsListImgBig0,
						item.image_url, width, height, new ImageCallBack() {
		
							@Override
							public void imageLoad(ImageView imageView,
									Bitmap bitmap) {
								if (bitmap == null) {
									bitmap = BitmapFactory
											.decodeResource(
													getResources(),
													R.drawable.list_item_ic_default);
								}
								imageView.setImageBitmap(bitmap);
							}
						});
		
				if (bmpbig0 != null) {
					newsListImgBig0.setImageBitmap(bmpbig0);
				}
			}
			
			((TextView) convertView.findViewById(R.id.news_testlist_item_title0))
					.setText(item.test_title);
			((TextView) convertView
					.findViewById(R.id.news_testlist_item_content0))
					.setText(item.test_content);
			((TextView) convertView
					.findViewById(R.id.news_testlist_item_appraise0))
					.setText(item.test_praise);
			
			Bitmap bmp = asyncLoader.loadBitmap(
					(ImageView) convertView.findViewById(R.id.news_testlist_item_img0), 
					item.image_url,
					Utility.dip2px(HeartTestActivity.this, 100), 
					Utility.dip2px(HeartTestActivity.this, 100),
					new ImageCallBack() {
						@Override
						public void imageLoad(ImageView imageView,
								Bitmap bitmap) {
							if (bitmap == null) {
								bitmap = BitmapFactory
										.decodeResource(
												getResources(),
												R.drawable.list_item_ic_default);
							}
							imageView.setImageBitmap(bitmap);
						}
					});

			if (bmp != null) {
				((ImageView) convertView
						.findViewById(R.id.news_testlist_item_img0))
						.setImageBitmap(bmp);
			}
			
			return convertView;
		}
		
	}
	
	public class TestAdapter_1 extends BaseListAdapter<TestList>{

		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.news_newslist_item_1, null);
			TestList item = this.getItem(position);
			
			if (position == 0) {
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);
				int width = (metrics.widthPixels * 720) / 720;
				int height = (metrics.widthPixels * 396) / 720;
				
				newsListTitleBig1
				.setText(rrTest_1.getDatas().get(0).test_title);

				Bitmap bmpbig1 = asyncLoader.loadBitmap(newsListImgBig1,
						item.image_url, width, height, new ImageCallBack() {
		
							@Override
							public void imageLoad(ImageView imageView,
									Bitmap bitmap) {
								if (bitmap == null) {
									bitmap = BitmapFactory
											.decodeResource(
													getResources(),
													R.drawable.list_item_ic_default);
								}
								imageView.setImageBitmap(bitmap);
							}
						});
		
				if (bmpbig1 != null) {
					newsListImgBig1.setImageBitmap(bmpbig1);
				}
			}
			
			((TextView) convertView.findViewById(R.id.news_testlist_item_title1))
					.setText(item.test_title);
			((TextView) convertView
					.findViewById(R.id.news_testlist_item_content1))
					.setText(item.test_content);
			((TextView) convertView
					.findViewById(R.id.news_testlist_item_appraise1))
					.setText(item.test_praise);
			
			Bitmap bmp = asyncLoader.loadBitmap((ImageView) convertView
					.findViewById(R.id.news_testlist_item_img1), item.image_url,
					Utility.dip2px(HeartTestActivity.this, 100), Utility
							.dip2px(HeartTestActivity.this, 100),
					new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (bitmap == null) {
								bitmap = BitmapFactory.decodeResource(
										getResources(),
										R.drawable.list_item_ic_default);
							}
							imageView.setImageBitmap(bitmap);
						}
					});

			if (bmp != null) {
				((ImageView) convertView
						.findViewById(R.id.news_testlist_item_img1))
						.setImageBitmap(bmp);
			}
			
			return convertView;
		}
		
	}
//	
	public class TestAdapter_2 extends BaseListAdapter<TestList>{

		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.news_newslist_item_2, null);
			TestList item = this.getItem(position);
			
			if (position == 0) {
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);
				int width = (metrics.widthPixels * 720) / 720;
				int height = (metrics.widthPixels * 396) / 720;
				
				newsListTitleBig2
				.setText(rrTest_2.getDatas().get(0).test_title);

				Bitmap bmpbig2 = asyncLoader.loadBitmap(newsListImgBig2,
						item.image_url, width, height, new ImageCallBack() {
		
							@Override
							public void imageLoad(ImageView imageView,
									Bitmap bitmap) {
								if (bitmap == null) {
									bitmap = BitmapFactory
											.decodeResource(
													getResources(),
													R.drawable.list_item_ic_default);
								}
								imageView.setImageBitmap(bitmap);
							}
						});
		
				if (bmpbig2 != null) {
					newsListImgBig2.setImageBitmap(bmpbig2);
				}
			}
			
			((TextView) convertView.findViewById(R.id.news_testlist_item_title2))
					.setText(item.test_title);
			((TextView) convertView
					.findViewById(R.id.news_testlist_item_content2))
					.setText(item.test_content);
			((TextView) convertView
					.findViewById(R.id.news_testlist_item_appraise2))
					.setText(item.test_praise);
			
			Bitmap bmp = asyncLoader.loadBitmap((ImageView) convertView
					.findViewById(R.id.news_testlist_item_img2), item.image_url,
					Utility.dip2px(HeartTestActivity.this, 100), Utility
							.dip2px(HeartTestActivity.this, 100),
					new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (bitmap == null) {
								bitmap = BitmapFactory.decodeResource(
										getResources(),
										R.drawable.list_item_ic_default);
							}
							imageView.setImageBitmap(bitmap);
						}
					});

			if (bmp != null) {
				((ImageView) convertView
						.findViewById(R.id.news_testlist_item_img2))
						.setImageBitmap(bmp);
			}
			
			return convertView;
		}
		
	}
	
	public class TestAdapter_3 extends BaseListAdapter<TestList>{

		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.news_newslist_item_3, null);
			TestList item = this.getItem(position);
			
			if (position == 0) {
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);
				int width = (metrics.widthPixels * 720) / 720;
				int height = (metrics.widthPixels * 396) / 720;
				
				newsListTitleBig3
				.setText(rrTest_3.getDatas().get(0).test_title);

				Bitmap bmpbig3 = asyncLoader.loadBitmap(newsListImgBig3,
						item.image_url, width, height, new ImageCallBack() {
		
							@Override
							public void imageLoad(ImageView imageView,
									Bitmap bitmap) {
								if (bitmap == null) {
									bitmap = BitmapFactory
											.decodeResource(
													getResources(),
													R.drawable.list_item_ic_default);
								}
								imageView.setImageBitmap(bitmap);
							}
						});
		
				if (bmpbig3 != null) {
					newsListImgBig3.setImageBitmap(bmpbig3);
				}
			}
			
			((TextView) convertView.findViewById(R.id.news_testlist_item_title3))
					.setText(item.test_title);
			((TextView) convertView
					.findViewById(R.id.news_testlist_item_content3))
					.setText(item.test_content);
			((TextView) convertView
					.findViewById(R.id.news_testlist_item_appraise3))
					.setText(item.test_praise);
			
			Bitmap bmp = asyncLoader.loadBitmap((ImageView) convertView
					.findViewById(R.id.news_testlist_item_img3), item.image_url,
					Utility.dip2px(HeartTestActivity.this, 100), Utility
							.dip2px(HeartTestActivity.this, 100),
					new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (bitmap == null) {
								bitmap = BitmapFactory.decodeResource(
										getResources(),
										R.drawable.list_item_ic_default);
							}
							imageView.setImageBitmap(bitmap);
						}
					});

			if (bmp != null) {
				((ImageView) convertView
						.findViewById(R.id.news_testlist_item_img3))
						.setImageBitmap(bmp);
			}
			
			return convertView;
		}
		
	}
	



	

}
