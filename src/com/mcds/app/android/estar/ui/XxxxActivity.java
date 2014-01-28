package com.mcds.app.android.estar.ui;

import java.util.ArrayList;
import java.util.List;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.MyAttentionMain;
import com.mcds.app.android.estar.bean.MyDetailInfo;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.Weibo;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.util.LayoutUtility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class XxxxActivity extends BaseTabActivity {
	private ListView lv;
	private WeiboAdapter adapter;
	private List<String> rrWeibo;

	private List<String> rrWeibo1;

	private ListView lv1;
	private WeiboAdapter adapter1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xxxx);

		initTabNavigate(R.id.xxxx_tabNavigate, TAB_INDEX_NONE);

		lv = (ListView) findViewById(R.id.xxxx_lv);
		adapter = new WeiboAdapter();
		lv.setAdapter(adapter);

		lv1 = (ListView) findViewById(R.id.xxxx_lv1);
		adapter1 = new WeiboAdapter();
		lv1.setAdapter(adapter1);

		findViewById(R.id.xxxx_btnTest).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				test();

			}
		});

		lv1.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
////				if (event.getAction() == MotionEvent.ACTION_MOVE) {
////					((TextView) findViewById(R.id.xxxx_txt)).setText(String.valueOf(event.getY()));
////				}
//				lv.dispatchTouchEvent(event);
////				lv1.dispatchTouchEvent(event);
//				return false;
//				int eventaction = event.getAction();
//			    switch (eventaction) {
//			    case MotionEvent.ACTION_DOWN: // touch down so check if the
//			            // finger is on a ball
//			      
//			     break;
//			    case MotionEvent.ACTION_MOVE: // touch drag with the ball
//			     Parcelable state = lv1.onSaveInstanceState(); 
//			     lv.onRestoreInstanceState(state); 
//			     break;
//			    }
				return false;
			}
		});

		lv.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//				((TextView) findViewById(R.id.xxxx_txt)).setText(String.valueOf(view.get));
				Parcelable state = lv.onSaveInstanceState();
				lv1.onRestoreInstanceState(state);
			}
		});

		lv1.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				Parcelable state = lv1.onSaveInstanceState();
				lv.onRestoreInstanceState(state);
			}
		});

		getData();
	}

	private void test() {
		ConnectProvider.getMyInfo("u01");
	}

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

//				if (!UserManager.uid.equals("")) {
//					rrWeibo = ConnectProvider.getWeiboList("u01", "1", "0", "0", "10");
//					doListViewUI();
//				}
				rrWeibo = new ArrayList<String>();
				for (int i = 0; i < 100; i++) {
					rrWeibo.add(String.valueOf(i));
				}

				rrWeibo1 = new ArrayList<String>();
				for (int i = 0; i < 100; i++) {
					rrWeibo1.add(String.valueOf(i));
				}

				doListViewUI();

				hideWaitingDialog();
			}
		}).start();
	}

	private void doListViewUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
//				if (rrWeibo.status.equals("0")) {
				adapter.setItems(rrWeibo);
//				LayoutUtility.setListViewHeightBasedOnChildren(lv);
//				((TextView) findViewById(R.id.xxxx_txt)).setText(String.valueOf(lv.getHeight()));
				adapter.notifyDataSetChanged();
				
				adapter1.setItems(rrWeibo1);
				adapter1.notifyDataSetChanged();
//				}
			}
		});
	}

	private class WeiboAdapter extends BaseListAdapter<String> {
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.xxxx_list_item, null);
//
//			Weibo item = getItem(position);
//			((TextView) convertView.findViewById(R.id.xxxxListItem_txtName)).setText(item.user_name);
//
//			return convertView;

			ViewHolder holder = null;

			if (convertView == null) {
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.xxxx_list_item, null);

				holder = new ViewHolder();
				holder.txtName = (TextView) convertView.findViewById(R.id.xxxxListItem_txtName);
				holder.icWeibo = (ImageView) convertView.findViewById(R.id.xxxxListItem_icWeibo);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder != null) {
				String item = this.getItem(position);
				holder.txtName.setText(item);

//				Bitmap bmp = asyncLoader.loadBitmap(holder.icWeibo, item.user_img, 100, 100, new ImageCallBack() {
//
//					@Override
//					public void imageLoad(ImageView imageView, Bitmap bitmap) {
//						if (bitmap == null) {
//							bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.list_item_ic_default);
//						}
//						imageView.setImageBitmap(bitmap);
//					}
//				});
//
//				if (bmp != null) {
//					holder.icWeibo.setImageBitmap(bmp);
//				}
			}

			int height = convertView.getHeight();
			height++;
			convertView.setTag(height);
			return convertView;
		}

	}

	private class ViewHolder {
		TextView txtName;
		ImageView icWeibo;
	}
}
