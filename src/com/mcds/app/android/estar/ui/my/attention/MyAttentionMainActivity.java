package com.mcds.app.android.estar.ui.my.attention;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.MyAttentionMain;
import com.mcds.app.android.estar.bean.MyFans;
import com.mcds.app.android.estar.bean.MyInfo;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.Weibo;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseTabActivity;
import com.mcds.app.android.estar.ui.my.FansActivity;
import com.mcds.app.android.estar.ui.my.attention.SideBar.OnTouchingLetterChangedListener;

public class MyAttentionMainActivity extends BaseTabActivity {
	private ListView myAttentionSortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter sortAdapter;
	private ClearEditText mClearEditText;
	private int page_num = 0;
	private final String PAGE_SIZE = "10";
	private ReturnResult<MyAttentionMain> rrMyAttentionMain;
	private ArrayList<String> userName_ArrayList;
	
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_attention_main);
		initViews();
		getData();
	}

	
	private void getData() {
		new Thread(new Runnable() {
 
			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrMyAttentionMain = ConnectProvider.getMyAttentionList(UserManager.uid, String.valueOf(page_num), PAGE_SIZE);
					
					doListViewUI();
				}

				hideWaitingDialog();
			}
		}).start();
	}
	
	private ArrayList<String> getUserName() {
		MyAttentionMain userName = new MyAttentionMain();
		for(int i=0; i<rrMyAttentionMain.getDatas().size(); i++) {
			userName = rrMyAttentionMain.getDatas().get(i);
			userName_ArrayList.add(userName.user_name);
		}
		userName = null;
		return userName_ArrayList;
	}
	

	private void doListViewUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				
				if (rrMyAttentionMain.status.equals("0")) {
					System.out.println("我的关注返回结果："+rrMyAttentionMain.info);
					ImageView my_attention_headPic = (ImageView) findViewById(R.id.my_attention_headPic);
				    TextView my_attention_userName = (TextView) findViewById(R.id.my_attention_userName);
				    ImageView my_attention_sex_icon = (ImageView) findViewById(R.id.my_attention_sex_icon);
				    TextView my_attention_Work = (TextView) findViewById(R.id.my_attention_Work);
				    ImageView my_attention_relation = (ImageView) findViewById(R.id.my_attention_relation);
				    MyAttentionMain info = new MyAttentionMain();
					info = rrMyAttentionMain.getDatas().get(0);
					
					/*
					 *获取用户头像 
					 */
					AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
					Bitmap bmp = asyncLoader.loadBitmap(my_attention_headPic, info.user_img, 100, 100, new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (bitmap == null) {
								bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.my_icon_fans_headpic);
							}
							imageView.setImageBitmap(bitmap);
						}
					});
					//设置头像
					if (bmp != null) {
						my_attention_headPic.setImageBitmap(bmp);
					}
					
					my_attention_userName.setText(info.user_name);//关注人名字
					/*
					 * 性别图标
					 */
					if (info.user_sex.equals ("10107")) {
						my_attention_sex_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_sex_man));
					}else{
						my_attention_sex_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_sex_women));
					}
					my_attention_Work.setText(info.user_work);
					/*
					 * 关系
					 *0：为关注 1：已关注  2：互粉 
					 */
					if (info.attention_flag.equals("0")) {
						my_attention_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_addattention_selector));
					}if (info.attention_flag.equals("1")) {
						my_attention_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioned_selector));
					}if (info.attention_flag.equals("2")) {
						my_attention_relation.setClickable(false);
						my_attention_relation.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_fans_attentioneach_selector));
					}
					
					
				
					
					sortAdapter.setItems(rrMyAttentionMain.getDatas());
					sortAdapter.notifyDataSetChanged();
					
				}
				
	
			}
		});
	}
	
	private void initViews() {
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		
		//设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = sortAdapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					myAttentionSortListView.setSelection(position);
				}
			}
		});
		
		myAttentionSortListView = (ListView) findViewById(R.id.my_attentionListView);
//		myAttentionSortListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
//				Toast.makeText(getApplication(), ((SortModel)sortAdapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
//			}
//		});
		
		
		//测试数据源
		SourceDateList = filledData(getResources().getStringArray(R.array.date));
//		SourceDateList = filledData(getResources().getStringArray(R.array.date));
		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		sortAdapter = new SortAdapter(this, SourceDateList);
		myAttentionSortListView.setAdapter(sortAdapter);
		
		
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		
		//根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}


	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		
		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		sortAdapter.updateListView(filterDateList);
	}
	

	
}
