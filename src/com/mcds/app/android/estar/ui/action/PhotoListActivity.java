package com.mcds.app.android.estar.ui.action;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.PhotoList;
import com.mcds.app.android.estar.bean.PhotoListDate;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseActivity;

public class PhotoListActivity extends BaseActivity {

	private String curActionID;
	private ReturnResult<PhotoList> rrPhotolist;
	public String base64pic;

	private static final int ALBUM_SELECT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_detail_photolist);
		Bundle bundle = getIntent().getExtras();
		curActionID = bundle.getString("activity_id");

		init();
		getData();
	}

	private void init() {
		// TODO Auto-generated method stub
		((ImageView) this.findViewById(R.id.action_detail_photolist_uploadpic))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						intent.setType("image/*");
						startActivityForResult(intent, ALBUM_SELECT);
					}
				});
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
					rrPhotolist = ConnectProviderZX
							.getActivityPhotos(curActionID);
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
				if (rrPhotolist.status.equals("0")) {

					int photoWidth = (getApplicationContext().getResources()
							.getDisplayMetrics().widthPixels - 0) / 4;
					int photoHeight = (100 * photoWidth) / 160;
					List<PhotoListDate> photoDates = new ArrayList<PhotoListDate>();
					List<String> timelist = new ArrayList<String>();
					if (rrPhotolist.getDatas().size() != 0) {
						for (int i = 0; i < rrPhotolist.getDatas().size(); i++) {
							timelist.add(rrPhotolist.getDatas().get(i).time);
						}
						Set set = new HashSet();
						List newList = new ArrayList();
						for (Iterator iter = timelist.iterator(); iter
								.hasNext();) {
							Object element = iter.next();
							if (set.add(element))
								newList.add(element);
						}
						timelist = newList; // 去时间重复
						for (int j = 0; j < timelist.size(); j++) {
							PhotoListDate itemDate = new PhotoListDate();
							itemDate.time = timelist.get(j);
							for (int k = 0; k < rrPhotolist.getDatas().size(); k++) {

								if (timelist.get(j).equals(
										rrPhotolist.getDatas().get(k).time)) {
									PhotoList items = new PhotoList();
									items.photo = rrPhotolist.getDatas().get(k).photo;
									items.time = rrPhotolist.getDatas().get(k).time;
									itemDate.photos.add(items);
								}
							}
							photoDates.add(itemDate);
						}
					}

					((LinearLayout) findViewById(R.id.actionDetailPhotoList_imgContainer))
					.removeAllViews();
					
					for (int i = 0; i < photoDates.size(); i++) {
						Log.i("xunhuan", i + "----photoDates.size()");
						LinearLayout imgContainer = (LinearLayout) LayoutInflater
								.from(getApplicationContext()).inflate(
										R.layout.action_photo_list_item, null);

						((TextView) imgContainer
								.findViewById(R.id.actionPhotoListItem_txtTime))
								.setText(photoDates.get(i).time);

						LinearLayout ll = new LinearLayout(
								getApplicationContext());
						ll.setOrientation(LinearLayout.HORIZONTAL);
						for (int j = 0; j < photoDates.get(i).photos.size(); j++) {
							Log.i("xunhuan", j
									+ "----photoDates.get(i).photos.size");
							if (j % 4 == 0) {
								imgContainer.addView(ll);
								ll = new LinearLayout(getApplicationContext());
								ll.setOrientation(LinearLayout.HORIZONTAL);
								ll.setPadding(20, 0, 20, 0);
							}
							ImageView iv = new ImageView(
									getApplicationContext());
							LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.LayoutParams.WRAP_CONTENT);
							lp.setMargins(5, 0, 5, 0);
							iv.setLayoutParams(lp);

							Bitmap bmp = new AsyncBitmapLoader().loadBitmap(iv,
									photoDates.get(i).photos.get(j).photo,
									photoWidth + 20, photoHeight + 20,
									new ImageCallBack() {

										@Override
										public void imageLoad(
												ImageView imageView,
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
								iv.setImageBitmap(bmp);
							}

							ll.addView(iv);
						}
						imgContainer.addView(ll);
						
						((LinearLayout) findViewById(R.id.actionDetailPhotoList_imgContainer))
								.addView(imgContainer);
					}

				}
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			String sdCardState = Environment.getExternalStorageState();
			if (!sdCardState.equals(Environment.MEDIA_MOUNTED)) {

				return;

			} else {
				ContentResolver resolver = getContentResolver();
				Uri imgUri = data.getData();
				try {
					Bitmap photo = MediaStore.Images.Media.getBitmap(resolver,
							imgUri);
					base64pic = bitmaptoString(photo);
					new Thread(new Runnable() {

						@Override
						public void run() {
							if (!curActionID.equals("")
									&& !"".equals(base64pic)) {

								showWaitingDialog();
								ReturnResult<String> rrbase64 = ConnectProviderZX
										.upActivityPhoto(UserManager.uid,
												curActionID, base64pic);
								if (rrbase64.status.equals("0")) {
									getData();
									getData();
								} else {
									Toast.makeText(getApplicationContext(),
											"上传图片失败！", Toast.LENGTH_SHORT)
											.show();
								}
								hideWaitingDialog();
							}
						}
					}).start();

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			Toast.makeText(getApplicationContext(), "相册获取图片失败！",
					Toast.LENGTH_SHORT).show();
		}
	}

	protected void onResume() {
		super.onResume();

	}

	public void onBackPressed() {
		super.onBackPressed();
		base64pic = "";
	}

	// 将Bitmap转换成字符串
	public String bitmaptoString(Bitmap bitmap) {
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;
	}

}
