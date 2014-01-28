package com.mcds.app.android.estar.ui.my;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.R.id;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.AttentionSomeone;
import com.mcds.app.android.estar.bean.HeartHomeList;
import com.mcds.app.android.estar.bean.MyFans;
import com.mcds.app.android.estar.bean.My_FindFriends;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.TestList;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.ConnectProviderCL;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.ui.BaseTabActivity;
import com.mcds.app.android.estar.util.LayoutUtility;
import com.mcds.app.android.estar.util.Utility;

/**
 * 找朋友
 * @author chenliang
 *
 */

public class FindFriendActivity extends BaseActivity {
	private final String PAGE_SIZE = "9";
	private int page_num = 0;
	
	private ViewPager my_findFriendPager;// 页卡内容
	private List<View> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private TextView my_findfriends_DatingHall,my_findfriend_GuessYouKnow,my_findfriend_CommonInterest,my_findfriend_Grassroots;
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号

	private LinearLayout my_findFriendRoom_1;
	private LinearLayout my_findFriendRoom_2;
	private LinearLayout my_findFriendRoom_3;
	private LinearLayout my_findFriendRoom_4;
	private LinearLayout my_findFriendRoom_5;
	private LinearLayout my_findFriendRoom_6;
	private LinearLayout my_findFriendRoom_7;
	private LinearLayout my_findFriendRoom_8;
	private LinearLayout my_findFriendRoom_9;
	private ImageView my_findFriendRoom_img_1;
	private ImageView my_findFriendRoom_img_2;
	private ImageView my_findFriendRoom_img_3;
	private ImageView my_findFriendRoom_img_4;
	private ImageView my_findFriendRoom_img_5;
	private ImageView my_findFriendRoom_img_6;
	private ImageView my_findFriendRoom_img_7;
	private ImageView my_findFriendRoom_img_8;
	private ImageView my_findFriendRoom_img_9;
	
	private TextView my_findFriendRoom_userName_1;
	private TextView my_findFriendRoom_userName_2;
	private TextView my_findFriendRoom_userName_3;
	private TextView my_findFriendRoom_userName_4;
	private TextView my_findFriendRoom_userName_5;
	private TextView my_findFriendRoom_userName_6;
	private TextView my_findFriendRoom_userName_7;
	private TextView my_findFriendRoom_userName_8;
	private TextView my_findFriendRoom_userName_9;
	private TextView my_findFriendRoom_work_1;
	private TextView my_findFriendRoom_work_2;
	private TextView my_findFriendRoom_work_3;
	private TextView my_findFriendRoom_work_4;
	private TextView my_findFriendRoom_work_5;
	private TextView my_findFriendRoom_work_6;
	private TextView my_findFriendRoom_work_7;
	private TextView my_findFriendRoom_work_8;
	private TextView my_findFriendRoom_work_9;
	private Button my_findFriendRoom_changeBtn;
	
//	private ListView findFriendListView0;
//	private ImageView newsListImgBig0;
//	private TextView newsListTitleBig0;
//
	private ListView my_findFriends_page1_listview;
//	private ImageView newsListImgBig1;
//	private TextView newsListTitleBig1;
//
	private ListView my_findFriends_page2_listview;
//	private ImageView newsListImgBig2;
//	private TextView newsListTitleBig2;
//
	private ListView my_findFriends_page3_listview;
//	private ImageView newsListImgBig3;
//	private TextView newsListTitleBig3;

	private My_findFriendsAdapter adapter;
	private My_findFriendsAdapter_1 my_findFriendsAdatper_1;
	private My_findFriendsAdapter_2 my_findFriendsAdatper_2;
	private My_findFriendsAdapter_3 my_findFriendsAdatper_3;
	private ReturnResult<My_FindFriends> rrFindFriend_0;
	private ReturnResult<My_FindFriends> rrFindFriend_1;
	private ReturnResult<My_FindFriends> rrFindFriend_2;
	private ReturnResult<My_FindFriends> rrFindFriend_3;
	private ReturnResult<AttentionSomeone> rrAttentionSomeone; 
	private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_findfriend);
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
		my_findfriends_DatingHall = (TextView) findViewById(R.id.my_findfriends_DatingHall);
		my_findfriend_GuessYouKnow = (TextView) findViewById(R.id.my_findfriend_GuessYouKnow);
		my_findfriend_CommonInterest = (TextView) findViewById(R.id.my_findfriend_CommonInterest);
		my_findfriend_Grassroots = (TextView) findViewById(R.id.my_findfriend_Grassroots);

		my_findfriends_DatingHall.setOnClickListener(new TestOnClickListener(0));
		my_findfriend_GuessYouKnow.setOnClickListener(new TestOnClickListener(1));
		my_findfriend_CommonInterest.setOnClickListener(new TestOnClickListener(2));
		my_findfriend_Grassroots.setOnClickListener(new TestOnClickListener(3));
	}

	private void InitViewPager() {
		my_findFriendPager = (ViewPager) findViewById(R.id.my_findFriendPager);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.my_findfriends_page_0, null));
		listViews.add(mInflater.inflate(R.layout.my_findfriends_page_1, null));
		listViews.add(mInflater.inflate(R.layout.my_findfriends_page_2, null));
		listViews.add(mInflater.inflate(R.layout.my_findfriends_page_3, null));
		my_findFriendPager.setAdapter(new findFriendViewPagerAdapter(listViews));
		my_findFriendPager.setCurrentItem(0);
		my_findFriendPager.setOnPageChangeListener(new TestOnPageChangeListener());

		
		my_findFriendRoom_1 = (LinearLayout) listViews.get(0).findViewById(R.id.my_findFriendRoom_1);
		my_findFriendRoom_2 = (LinearLayout) listViews.get(0).findViewById(R.id.my_findFriendRoom_2);
		my_findFriendRoom_3 = (LinearLayout) listViews.get(0).findViewById(R.id.my_findFriendRoom_3);
		my_findFriendRoom_4 = (LinearLayout) listViews.get(0).findViewById(R.id.my_findFriendRoom_4);
		my_findFriendRoom_5 = (LinearLayout) listViews.get(0).findViewById(R.id.my_findFriendRoom_5);
		my_findFriendRoom_6 = (LinearLayout) listViews.get(0).findViewById(R.id.my_findFriendRoom_6);
		my_findFriendRoom_7 = (LinearLayout) listViews.get(0).findViewById(R.id.my_findFriendRoom_7);
		my_findFriendRoom_8 = (LinearLayout) listViews.get(0).findViewById(R.id.my_findFriendRoom_8);
		my_findFriendRoom_9 = (LinearLayout) listViews.get(0).findViewById(R.id.my_findFriendRoom_9);
		my_findFriendRoom_changeBtn =  (Button) listViews.get(0).findViewById(R.id.my_findFriendRoom_changeBtn);
		my_findFriendRoom_1.setOnClickListener(new My_findFriendListener());
		my_findFriendRoom_2.setOnClickListener(new My_findFriendListener());
		my_findFriendRoom_3.setOnClickListener(new My_findFriendListener());
		my_findFriendRoom_4.setOnClickListener(new My_findFriendListener());
		my_findFriendRoom_5.setOnClickListener(new My_findFriendListener());
		my_findFriendRoom_6.setOnClickListener(new My_findFriendListener());
		my_findFriendRoom_7.setOnClickListener(new My_findFriendListener());
		my_findFriendRoom_8.setOnClickListener(new My_findFriendListener());
		my_findFriendRoom_9.setOnClickListener(new My_findFriendListener());
		my_findFriendRoom_changeBtn.setOnClickListener(new My_findFriendListener());
		my_findFriendRoom_img_1 = (ImageView) listViews.get(0).findViewById(R.id.my_findFriendRoom_img_1);
		my_findFriendRoom_img_2 = (ImageView) listViews.get(0).findViewById(R.id.my_findFriendRoom_img_2);
		my_findFriendRoom_img_3 = (ImageView) listViews.get(0).findViewById(R.id.my_findFriendRoom_img_3);
		my_findFriendRoom_img_4 = (ImageView) listViews.get(0).findViewById(R.id.my_findFriendRoom_img_4);
		my_findFriendRoom_img_5 = (ImageView) listViews.get(0).findViewById(R.id.my_findFriendRoom_img_5);
		my_findFriendRoom_img_6 = (ImageView) listViews.get(0).findViewById(R.id.my_findFriendRoom_img_6);
		my_findFriendRoom_img_7 = (ImageView) listViews.get(0).findViewById(R.id.my_findFriendRoom_img_7);
		my_findFriendRoom_img_8 = (ImageView) listViews.get(0).findViewById(R.id.my_findFriendRoom_img_8);
		my_findFriendRoom_img_9 = (ImageView) listViews.get(0).findViewById(R.id.my_findFriendRoom_img_9);
		
		my_findFriendRoom_userName_1 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_userName_1);
		my_findFriendRoom_userName_2 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_userName_2);
		my_findFriendRoom_userName_3 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_userName_3);
		my_findFriendRoom_userName_4 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_userName_4);
		my_findFriendRoom_userName_5 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_userName_5);
		my_findFriendRoom_userName_6 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_userName_6);
		my_findFriendRoom_userName_7 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_userName_7);
		my_findFriendRoom_userName_8 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_userName_8);
		my_findFriendRoom_userName_9 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_userName_9);
		
		my_findFriendRoom_work_1 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_work_1);
		my_findFriendRoom_work_2 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_work_2);
		my_findFriendRoom_work_3 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_work_3);
		my_findFriendRoom_work_4 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_work_4);
		my_findFriendRoom_work_5 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_work_5);
		my_findFriendRoom_work_6 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_work_6);
		my_findFriendRoom_work_7 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_work_7);
		my_findFriendRoom_work_8 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_work_8);
		my_findFriendRoom_work_9 = (TextView) listViews.get(0).findViewById(R.id.my_findFriendRoom_work_9);
//		findFriendListView0 = (ListView) listViews.get(0).findViewById(
//				R.id.newsTest_listView_0);
//		newsListImgBig0 = (ImageView) listViews.get(0).findViewById(
//				R.id.news_test_big_imgs_0);
//		newsListTitleBig0 = (TextView) listViews.get(0).findViewById(
//				R.id.news_test_big_title_0);
		my_findFriends_page1_listview = (ListView) listViews.get(1).findViewById(
				R.id.my_findFriends_page1_listview);
//		newsListImgBig1 = (ImageView) listViews.get(1).findViewById(
//				R.id.news_test_big_imgs_1);
//		newsListTitleBig1 = (TextView) listViews.get(1).findViewById(
//				R.id.news_test_big_title_1);
		my_findFriends_page2_listview = (ListView) listViews.get(2).findViewById(
				R.id.my_findFriends_page2_listview);
//		newsListImgBig2 = (ImageView) listViews.get(2).findViewById(
//				R.id.news_test_big_imgs_2);
//		newsListTitleBig2 = (TextView) listViews.get(2).findViewById(
//				R.id.news_test_big_title_2);
		my_findFriends_page3_listview = (ListView) listViews.get(3).findViewById(
				R.id.my_findFriends_page3_listview);
//		newsListImgBig3 = (ImageView) listViews.get(3).findViewById(
//				R.id.news_test_big_imgs_3);
//		newsListTitleBig3 = (TextView) listViews.get(3).findViewById(
//				R.id.news_test_big_title_3);

		adapter = new My_findFriendsAdapter();
//		findFriendListView0.setAdapter(adapter);
//		findFriendListView0.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HeartTestStart.class);
//				Bundle bundle = new Bundle();
//				switch(currIndex){
//				case 0:
//					if(rrTest_0 != null){
//						bundle.putSerializable("rr_test", rrTest_0.getDatas().get(arg2));
//					}
//					break;
//				}
//                intent.putExtras(bundle);
//				startActivity(intent);
//			}
//		});
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
					rrFindFriend_0 = ConnectProviderCL.searchFriendBySystem(UserManager.uid, "0", page_num+"", PAGE_SIZE);
					rrFindFriend_1 = ConnectProviderCL.searchFriendBySystem(UserManager.uid, "1", "0", "10");
					rrFindFriend_2 = ConnectProviderCL.searchFriendBySystem(UserManager.uid, "2", "0", "10");
					rrFindFriend_3 =ConnectProviderCL.searchFriendBySystem(UserManager.uid, "3", "0", "10");	
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
				
				switch (currIndex) {
				case 0:
					if (rrFindFriend_0 != null && rrFindFriend_0.status.equals("0")) {
						My_FindFriends info = new My_FindFriends();
						
						if(rrFindFriend_0.getDatas().size()<9){
							page_num=0;
						}
						
						for (int i = 0; i < rrFindFriend_0.getDatas().size(); i++) {
							info = rrFindFriend_0.getDatas().get(i);
							System.out.println("交友大厅的人："+info.name);
							switch (i) {
							case 0 :
								Bitmap bmp1 = asyncLoader.loadBitmap(my_findFriendRoom_img_1, info.img, 100, 100, new ImageCallBack() {
									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (bmp1 != null) {
									my_findFriendRoom_img_1.setImageBitmap(bmp1);
								}
								my_findFriendRoom_userName_1.setText(info.name);
								my_findFriendRoom_work_1.setText(info.work);
								break;
							case 1 :
								Bitmap bmp2 = asyncLoader.loadBitmap(my_findFriendRoom_img_2, info.img, 100, 100, new ImageCallBack() {
									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (bmp2 != null) {
									my_findFriendRoom_img_2.setImageBitmap(bmp2);
								}
								my_findFriendRoom_userName_2.setText(info.name);
								my_findFriendRoom_work_2.setText(info.work);
								break;
							case 2 :
								Bitmap bmp3 = asyncLoader.loadBitmap(my_findFriendRoom_img_3, info.img, 100, 100, new ImageCallBack() {
									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (bmp3 != null) {
									my_findFriendRoom_img_3.setImageBitmap(bmp3);
								}
								my_findFriendRoom_userName_3.setText(info.name);
								my_findFriendRoom_work_3.setText(info.work);
								break;
							case 3 :
								Bitmap bmp4 = asyncLoader.loadBitmap(my_findFriendRoom_img_4, info.img, 100, 100, new ImageCallBack() {
									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (bmp4 != null) {
									my_findFriendRoom_img_4.setImageBitmap(bmp4);
								}
								my_findFriendRoom_userName_4.setText(info.name);
								my_findFriendRoom_work_4.setText(info.work);
								break;
							case 4 :
								Bitmap bmp5 = asyncLoader.loadBitmap(my_findFriendRoom_img_5, info.img, 100, 100, new ImageCallBack() {
									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (bmp5 != null) {
									my_findFriendRoom_img_5.setImageBitmap(bmp5);
								}
								my_findFriendRoom_userName_5.setText(info.name);
								my_findFriendRoom_work_5.setText(info.work);
								break;
							case 5 :
								Bitmap bmp6 = asyncLoader.loadBitmap(my_findFriendRoom_img_6, info.img, 100, 100, new ImageCallBack() {
									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (bmp6 != null) {
									my_findFriendRoom_img_6.setImageBitmap(bmp6);
								}
								my_findFriendRoom_userName_6.setText(info.name);
								my_findFriendRoom_work_6.setText(info.work);
								break;
							case 6 :
								Bitmap bmp7 = asyncLoader.loadBitmap(my_findFriendRoom_img_7, info.img, 100, 100, new ImageCallBack() {
									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (bmp7 != null) {
									my_findFriendRoom_img_7.setImageBitmap(bmp7);
								}
								my_findFriendRoom_userName_7.setText(info.name);
								my_findFriendRoom_work_7.setText(info.work);
								break;
								
							case 7 :
								Bitmap bmp8 = asyncLoader.loadBitmap(my_findFriendRoom_img_8, info.img, 100, 100, new ImageCallBack() {
									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (bmp8 != null) {
									my_findFriendRoom_img_8.setImageBitmap(bmp8);
								}
								my_findFriendRoom_userName_8.setText(info.name);
								my_findFriendRoom_work_8.setText(info.work);
								break;
							case 8 :
								Bitmap bmp9 = asyncLoader.loadBitmap(my_findFriendRoom_img_9, info.img, 100, 100, new ImageCallBack() {
									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap) {
										if (bitmap == null) {
											bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
										}
										imageView.setImageBitmap(bitmap);
									}
								});

								if (bmp9 != null) {
									my_findFriendRoom_img_9.setImageBitmap(bmp9);
								}
								my_findFriendRoom_userName_9.setText(info.name);
								my_findFriendRoom_work_9.setText(info.work);
								break;
							default:
								break;
							}
				
								
								

					
						}
						
						
						
						
//						adapter.setItems(rrFindFriend_0.getDatas());
////						LayoutUtility
////						.setListViewHeightBasedOnChildren(findFriendListView0);
//						adapter.notifyDataSetChanged();
					}
					
					break;
				case 1:
					if (rrFindFriend_1 != null && rrFindFriend_1.status.equals("0")) {
						my_findFriendsAdatper_1.setItems(rrFindFriend_1.getDatas());
						LayoutUtility
						.setListViewHeightBasedOnChildren(my_findFriends_page1_listview);
						my_findFriendsAdatper_1.notifyDataSetChanged();
					}
					break;
				case 2:
					if (rrFindFriend_2 != null && rrFindFriend_2.status.equals("0")) {
						my_findFriendsAdatper_2.setItems(rrFindFriend_2.getDatas());
						LayoutUtility
						.setListViewHeightBasedOnChildren(my_findFriends_page2_listview);
						my_findFriendsAdatper_2.notifyDataSetChanged();
					}
					break;
				case 3:
					if (rrFindFriend_3 != null && rrFindFriend_3.status.equals("0")) {
						my_findFriendsAdatper_3.setItems(rrFindFriend_3.getDatas());
						LayoutUtility
						.setListViewHeightBasedOnChildren(my_findFriends_page3_listview);
						my_findFriendsAdatper_3.notifyDataSetChanged();
					}
					break;
				}
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
			my_findFriendPager.setCurrentItem(index);
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
				adapter = new My_findFriendsAdapter();
//				findFriendListView0.setAdapter(adapter);
//				findFriendListView0.setOnItemClickListener(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//							long arg3) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
				my_findfriends_DatingHall.setTextColor(Color.parseColor("#eb4666"));
				my_findfriend_GuessYouKnow.setTextColor(Color.parseColor("#444444"));
				my_findfriend_CommonInterest.setTextColor(Color.parseColor("#444444"));
				my_findfriend_Grassroots.setTextColor(Color.parseColor("#444444"));
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(0, offset, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(offset * 2, offset, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(offset * 3, offset, 0, 0);
				}
				my_findFriendsAdatper_1 = new My_findFriendsAdapter_1();
				my_findFriends_page1_listview.setAdapter(my_findFriendsAdatper_1);
//				my_findFriends_page1_listview.setOnItemClickListener(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//							long arg3) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
				my_findfriends_DatingHall.setTextColor(Color.parseColor("#444444"));
				my_findfriend_GuessYouKnow.setTextColor(Color.parseColor("#eb4666"));
				my_findfriend_CommonInterest.setTextColor(Color.parseColor("#444444"));
				my_findfriend_Grassroots.setTextColor(Color.parseColor("#444444"));
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
				my_findFriendsAdatper_2 = new My_findFriendsAdapter_2();
				my_findFriends_page2_listview.setAdapter(my_findFriendsAdatper_2);
//				findFriendListView2.setOnItemClickListener(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//							long arg3) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
				my_findfriends_DatingHall.setTextColor(Color.parseColor("#444444"));
				my_findfriend_GuessYouKnow.setTextColor(Color.parseColor("#444444"));
				my_findfriend_CommonInterest.setTextColor(Color.parseColor("#eb4666"));
				my_findfriend_Grassroots.setTextColor(Color.parseColor("#444444"));
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
				my_findFriendsAdatper_3 = new My_findFriendsAdapter_3();
				my_findFriends_page3_listview.setAdapter(my_findFriendsAdatper_3);
//				findFriendListView3.setOnItemClickListener(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//							long arg3) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
				my_findfriends_DatingHall.setTextColor(Color.parseColor("#444444"));
				my_findfriend_GuessYouKnow.setTextColor(Color.parseColor("#444444"));
				my_findfriend_CommonInterest.setTextColor(Color.parseColor("#444444"));
				my_findfriend_Grassroots.setTextColor(Color.parseColor("#eb4666"));
				break;
			}

			currIndex = arg0;
			doListViewUI();
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);

		}

	}

	public class findFriendViewPagerAdapter extends PagerAdapter {

		public List<View> mListViews;

		public findFriendViewPagerAdapter(List<View> mListViews) {
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

	private class My_findFriendsAdapter extends BaseListAdapter<My_FindFriends> {

		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.my_findfriend_item, null);
			My_FindFriends item = this.getItem(position);
			
			Log.i("estar", "--" + currIndex);
			
			if (position == 0) {
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);
				int width = (metrics.widthPixels * 720) / 720;
				int height = (metrics.widthPixels * 396) / 720;

				switch (currIndex) {
				case 0:
//					newsListTitleBig0
//							.setText(rrTest_0.getDatas().get(0).test_title);
//					Bitmap bmpbig0 = asyncLoader.loadBitmap(newsListImgBig0,
//							item.image_url, width, height, new ImageCallBack() {
//
//								@Override
//								public void imageLoad(ImageView imageView,
//										Bitmap bitmap) {
//									if (bitmap == null) {
//										bitmap = BitmapFactory
//												.decodeResource(
//														getResources(),
//														R.drawable.list_item_ic_default);
//									}
//									imageView.setImageBitmap(bitmap);
//								}
//							});
//
//					if (bmpbig0 != null) {
//						newsListImgBig0.setImageBitmap(bmpbig0);
//					}
					break;
				case 1:
//					newsListTitleBig1
//							.setText(rrTest_1.getDatas().get(0).test_title);
//
//					Bitmap bmpbig1 = asyncLoader.loadBitmap(newsListImgBig1,
//							item.image_url, width, height, new ImageCallBack() {
//
//								@Override
//								public void imageLoad(ImageView imageView,
//										Bitmap bitmap) {
//									if (bitmap == null) {
//										bitmap = BitmapFactory
//												.decodeResource(
//														getResources(),
//														R.drawable.list_item_ic_default);
//									}
//									imageView.setImageBitmap(bitmap);
//								}
//							});
//
//					if (bmpbig1 != null) {
//						newsListImgBig1.setImageBitmap(bmpbig1);
//					}
					break;
				case 2:
//					newsListTitleBig2
//							.setText(rrTest_2.getDatas().get(0).test_title);
////
//					Bitmap bmpbig2 = asyncLoader.loadBitmap(newsListImgBig2,
//							item.image_url, width, height, new ImageCallBack() {
//
//								@Override
//								public void imageLoad(ImageView imageView,
//										Bitmap bitmap) {
//									if (bitmap == null) {
//										bitmap = BitmapFactory
//												.decodeResource(
//														getResources(),
//														R.drawable.list_item_ic_default);
//									}
//									imageView.setImageBitmap(bitmap);
//								}
//							});
//
//					if (bmpbig2 != null) {
//						newsListImgBig2.setImageBitmap(bmpbig2);
//					}
					break;
				case 3:
//					newsListTitleBig3
//							.setText(rrTest_3.getDatas().get(0).test_title);
//
//					Bitmap bmpbig3 = asyncLoader.loadBitmap(newsListImgBig3,
//							item.image_url, width, height, new ImageCallBack() {
//
//								@Override
//								public void imageLoad(ImageView imageView,
//										Bitmap bitmap) {
//									if (bitmap == null) {
//										bitmap = BitmapFactory
//												.decodeResource(
//														getResources(),
//														R.drawable.list_item_ic_default);
//									}
//									imageView.setImageBitmap(bitmap);
//								} 
//							});
//
//					if (bmpbig3 != null) {
//						newsListImgBig3.setImageBitmap(bmpbig3);
//					}
					break;
				}

			}

//			((TextView) convertView.findViewById(R.id.news_testlist_item_title))
//					.setText(item.test_title);
//			((TextView) convertView
//					.findViewById(R.id.news_testlist_item_content))
//					.setText(item.test_content);
//			((TextView) convertView
//					.findViewById(R.id.news_testlist_item_appraise))
//					.setText(item.test_praise);
//
//			Bitmap bmp = asyncLoader.loadBitmap((ImageView) convertView
//					.findViewById(R.id.news_testlist_item_img), item.image_url,
//					Utility.dip2px(FindFriendActivity.this, 100), Utility
//							.dip2px(FindFriendActivity.this, 100),
//					new ImageCallBack() {
//
//						@Override
//						public void imageLoad(ImageView imageView, Bitmap bitmap) {
//							if (bitmap == null) {
//								bitmap = BitmapFactory.decodeResource(
//										getResources(),
//										R.drawable.list_item_ic_default);
//							}
//							imageView.setImageBitmap(bitmap);
//						}
//					});
//
//			if (bmp != null) {
//				((ImageView) convertView
//						.findViewById(R.id.news_testlist_item_img))
//						.setImageBitmap(bmp);
//			}

			return convertView;
		}

	}
	
	private class My_findFriendListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.my_findFriendRoom_1:
				Toast toast1 = Toast.makeText(getApplicationContext(), "朋友1", Toast.LENGTH_SHORT);
				toast1.show();
				
				break;
			case R.id.my_findFriendRoom_2:
				Toast toast2 = Toast.makeText(getApplicationContext(), "朋友2", Toast.LENGTH_SHORT);
				toast2.show();
				break;
			case R.id.my_findFriendRoom_3:
				Toast toast3 = Toast.makeText(getApplicationContext(), "朋友3", Toast.LENGTH_SHORT);
				toast3.show();
				break;
				
			case R.id.my_findFriendRoom_changeBtn:
//				Toast toast = Toast.makeText(getApplicationContext(), "换一换", Toast.LENGTH_SHORT);
//				toast.show();
				page_num++;
				getData();
				break;
			default:
				break;
			}
		}
		
	}
	
	private class My_findFriendsAdapter_1 extends BaseListAdapter<My_FindFriends> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder_1 holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_findfriends_page_1_item, null);
				holder = new ViewHolder_1();
				holder.my_findfriends_page_1_headPic = (ImageView) convertView.findViewById(R.id.my_findfriends_page_1_headPic);
				holder.my_findfriends_page_1_userName = (TextView) convertView.findViewById(R.id.my_findfriends_page_1_userName);
				holder.my_findfriends_page_1_attentionWork = (TextView) convertView.findViewById(R.id.my_findfriends_page_1_attentionWork);
				holder.my_findfriends_page_1_relation = (ImageButton) convertView.findViewById(R.id.my_findfriends_page_1_relation);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder_1) convertView.getTag();
			}

			if (holder != null) {
				final My_FindFriends item = this.getItem(position);
				holder.my_findfriends_page_1_userName.setText(item.name);
				holder.my_findfriends_page_1_attentionWork.setText(item.work);
				
				/*
				 * 头像
				 */
				Bitmap bmp = asyncLoader.loadBitmap(holder.my_findfriends_page_1_headPic, item.img, 100, 100, new ImageCallBack() {
					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp != null) {
					holder.my_findfriends_page_1_headPic.setImageBitmap(bmp);
				}
				/*
				 * 粉丝关系
				 *0：为关注 1：已关注  2：互粉 
				 */
				if (item.attention_flag.equals("0")) {
					holder.my_findfriends_page_1_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_addattention_selector));
				}if (item.attention_flag.equals("1")) {
					holder.my_findfriends_page_1_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioned_selector));
				}if (item.attention_flag.equals("2")) {
					holder.my_findfriends_page_1_relation.setClickable(false);
					holder.my_findfriends_page_1_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioneach_selector));
				}
				
				/*
				 * 点击+关注  
				 */
				holder.my_findfriends_page_1_relation.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new Thread(new Runnable() {
							 
							@Override
							public void run() {
								showWaitingDialog();

								if (!UserManager.uid.equals("")) {
									rrAttentionSomeone = ConnectProvider.attentionSomeone(UserManager.uid, item.uid, "0");
								}
								
								if (rrAttentionSomeone.status.equals("0")) {
									System.out.println("关注返回结果："+rrAttentionSomeone.info);
									if (rrAttentionSomeone.info.equals("ok")) {
										getData();
									}
								}else{
									Toast toast = Toast.makeText(getApplicationContext(), "添加关注失败",Toast.LENGTH_SHORT );
									toast.show();

								}
								hideWaitingDialog();
							}
						}).start();
						
					}
				});
				
			}
			return convertView;
		}

	}
	private class ViewHolder_1 {
		
		ImageView my_findfriends_page_1_headPic;
		TextView my_findfriends_page_1_userName;
		TextView my_findfriends_page_1_attentionWork;
		ImageButton my_findfriends_page_1_relation;
	}

	
	private class My_findFriendsAdapter_2 extends BaseListAdapter<My_FindFriends> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder_2 holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_findfriends_page_2_item, null);
				holder = new ViewHolder_2();
				holder.my_findfriends_page_2_headPic = (ImageView) convertView.findViewById(R.id.my_findfriends_page_2_headPic);
				holder.my_findfriends_page_2_userName = (TextView) convertView.findViewById(R.id.my_findfriends_page_2_userName);
				holder.my_findfriends_page_2_attentionWork = (TextView) convertView.findViewById(R.id.my_findfriends_page_2_attentionWork);
				holder.my_findfriends_page_2_relation = (ImageButton) convertView.findViewById(R.id.my_findfriends_page_2_relation);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder_2) convertView.getTag();
			}

			if (holder != null) {
				final My_FindFriends item = this.getItem(position);
				holder.my_findfriends_page_2_userName.setText(item.name);
				holder.my_findfriends_page_2_attentionWork.setText(item.work);
				
				/*
				 * 头像
				 */
				Bitmap bmp = asyncLoader.loadBitmap(holder.my_findfriends_page_2_headPic, item.img, 100, 100, new ImageCallBack() {
					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp != null) {
					holder.my_findfriends_page_2_headPic.setImageBitmap(bmp);
				}
				/*
				 * 粉丝关系
				 *0：为关注 1：已关注  2：互粉 
				 */
				if (item.attention_flag.equals("0")) {
					holder.my_findfriends_page_2_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_addattention_selector));
				}if (item.attention_flag.equals("1")) {
					holder.my_findfriends_page_2_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioned_selector));
				}if (item.attention_flag.equals("2")) {
					holder.my_findfriends_page_2_relation.setClickable(false);
					holder.my_findfriends_page_2_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioneach_selector));
				}
				
				/*
				 * 点击+关注  
				 */
				holder.my_findfriends_page_2_relation.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new Thread(new Runnable() {
							 
							@Override
							public void run() {
								showWaitingDialog();

								if (!UserManager.uid.equals("")) {
									rrAttentionSomeone = ConnectProvider.attentionSomeone(UserManager.uid, item.uid, "0");
								}
								
								if (rrAttentionSomeone.status.equals("0")) {
									System.out.println("关注返回结果："+rrAttentionSomeone.info);
									if (rrAttentionSomeone.info.equals("ok")) {
										getData();
									}
								}else{
									Toast toast = Toast.makeText(getApplicationContext(), "添加关注失败",Toast.LENGTH_SHORT );
									toast.show();

								}
								hideWaitingDialog();
							}
						}).start();
						
					}
				});
				
			}
			return convertView;
		}

	}
	private class ViewHolder_2 {
		
		ImageView my_findfriends_page_2_headPic;
		TextView my_findfriends_page_2_userName;
		TextView my_findfriends_page_2_attentionWork;
		ImageButton my_findfriends_page_2_relation;
	}
	
	private class My_findFriendsAdapter_3 extends BaseListAdapter<My_FindFriends> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder_3 holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.my_findfriends_page_3_item, null);
				holder = new ViewHolder_3();
				holder.my_findfriends_page_3_headPic = (ImageView) convertView.findViewById(R.id.my_findfriends_page_3_headPic);
				holder.my_findfriends_page_3_userName = (TextView) convertView.findViewById(R.id.my_findfriends_page_3_userName);
				holder.my_findfriends_page_3_attentionWork = (TextView) convertView.findViewById(R.id.my_findfriends_page_3_attentionWork);
				holder.my_findfriends_page_3_relation = (ImageButton) convertView.findViewById(R.id.my_findfriends_page_3_relation);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder_3) convertView.getTag();
			}

			if (holder != null) {
				final My_FindFriends item = this.getItem(position);
				holder.my_findfriends_page_3_userName.setText(item.name);
				holder.my_findfriends_page_3_attentionWork.setText(item.work);
				
				/*
				 * 头像
				 */
				Bitmap bmp = asyncLoader.loadBitmap(holder.my_findfriends_page_3_headPic, item.img, 100, 100, new ImageCallBack() {
					@Override
					public void imageLoad(ImageView imageView, Bitmap bitmap) {
						if (bitmap == null) {
							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
						}
						imageView.setImageBitmap(bitmap);
					}
				});

				if (bmp != null) {
					holder.my_findfriends_page_3_headPic.setImageBitmap(bmp);
				}
				/*
				 * 粉丝关系
				 *0：为关注 1：已关注  2：互粉 
				 */
				if (item.attention_flag.equals("0")) {
					holder.my_findfriends_page_3_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_addattention_selector));
				}if (item.attention_flag.equals("1")) {
					holder.my_findfriends_page_3_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioned_selector));
				}if (item.attention_flag.equals("2")) {
					holder.my_findfriends_page_3_relation.setClickable(false);
					holder.my_findfriends_page_3_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioneach_selector));
				}
				
				/*
				 * 点击+关注  
				 */
				holder.my_findfriends_page_3_relation.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new Thread(new Runnable() {
							 
							@Override
							public void run() {
								showWaitingDialog();

								if (!UserManager.uid.equals("")) {
									rrAttentionSomeone = ConnectProvider.attentionSomeone(UserManager.uid, item.uid, "0");
								}
								
								if (rrAttentionSomeone.status.equals("0")) {
									System.out.println("关注返回结果："+rrAttentionSomeone.info);
									if (rrAttentionSomeone.info.equals("ok")) {
										getData();
									}
								}else{
									Toast toast = Toast.makeText(getApplicationContext(), "添加关注失败",Toast.LENGTH_SHORT );
									toast.show();

								}
								hideWaitingDialog();
							}
						}).start();
						
					}
				});
				
			}
			return convertView;
		}

	}
	private class ViewHolder_3 {
		
		ImageView my_findfriends_page_3_headPic;
		TextView my_findfriends_page_3_userName;
		TextView my_findfriends_page_3_attentionWork;
		ImageButton my_findfriends_page_3_relation;
	}
}

