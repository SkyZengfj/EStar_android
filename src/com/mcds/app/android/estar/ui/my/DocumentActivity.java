package com.mcds.app.android.estar.ui.my;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.MyDetailInfo;
import com.mcds.app.android.estar.bean.MyInfo;
import com.mcds.app.android.estar.bean.My_SetBlog;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseTabActivity;

/**
 * 我的资料
 * @author chenliang
 *
 */

public class DocumentActivity extends BaseTabActivity implements OnClickListener {
	private ImageView my_iv_document_userImg;
	private TextView my_txt_document_userCode;
	private TextView my_txt_document_userName;
	private TextView my_txt_document_birthday;
	private TextView my_txt_document_phone;
	private TextView my_txt_document_personalAddress;
	private TextView my_txt_document_blog;
	private TextView my_txt_document_weibo;
	private TextView my_txt_document_deptName;
	private TextView my_txt_document_post;
	private TextView my_txt_document_directBoss;
	private TextView my_txt_document_joinTime;
	private TextView my_txt_document_intrest;
	private TextView my_txt_document_declaration;
	private ReturnResult<MyDetailInfo> rrMyDetailInfo;

	private LinearLayout my_btn_setInvite;
	private LinearLayout my_btn_document_personalAddress;
	private LinearLayout my_btn_document_Blog;
	private LinearLayout my_document_weibo;
	private LinearLayout my_document_friendsDeclaration;
	private LinearLayout my_btn_document_intrest;

	public static String my_Str_interest = " ";
	public static String[] my_Str_interestArray ;

	private static final int CAMERA_TAKE = 1;
	private static final int CAMERA_SELECT = 2;
	private String my_headpic_name;
	private boolean isBig = false;
	private static final String PATH = Environment.getExternalStorageDirectory() + "/EStar/picture";
	private static final String headerPath = Environment.getExternalStorageDirectory() + "/EStar/picture/header.jpg";
	private Bitmap headerBmp = null;
	public static Bitmap my_headIntentBitmap = null;
	private String friendsDeclarationString = "";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_document);
		initButton();
		getData();

	}
	
	

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrMyDetailInfo = ConnectProvider.getMyDetailInfo(UserManager.uid);
					doListViewUI();
				}

				hideWaitingDialog();
			}
		}).start();
	}

	private void doListViewUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (rrMyDetailInfo.status.equals("0")) {
					MyDetailInfo info = new MyDetailInfo();
					info = rrMyDetailInfo.getDatas().get(0);

					/*
					 * 获取用户头像
					 */
					AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
					Bitmap bmp = asyncLoader.loadBitmap(my_iv_document_userImg, info.user_img, 100, 100, new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (bitmap == null) {
//								bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.my_icon_fans_headpic);
							}
							imageView.setImageBitmap(bitmap);
						}
					});
					// 设置用户头像
					if (bmp != null) {
						my_iv_document_userImg.setImageBitmap(bmp);
						my_headIntentBitmap = bmp;
					}

					my_txt_document_userCode.setText(info.user_code);
					my_txt_document_userName.setText(info.user_name);
					my_txt_document_birthday.setText(info.birthday);
					my_txt_document_phone.setText(info.phone);
					my_txt_document_personalAddress.setText(info.personal_address);
					my_txt_document_blog.setText(info.blog);
					my_txt_document_weibo.setText(info.weibo);
//					my_txt_document_deptName.setText(info.);
					my_txt_document_post.setText(info.user_work);
					my_txt_document_directBoss.setText(info.direct_boss);
					my_txt_document_joinTime.setText(info.join_time);
					my_txt_document_intrest.setText(info.intrest);
					my_txt_document_declaration.setText(info.declaration);
					friendsDeclarationString = info.declaration;
					
					my_Str_interest = info.intrest;
					my_Str_interestArray = my_Str_interest.split(" ");
					
				}
			}
		});
	}

	private void initButton() {
		// TODO Auto-generated method stub
		my_iv_document_userImg = (ImageView) findViewById(R.id.my_iv_document_userImg);
		my_txt_document_userCode = (TextView) findViewById(R.id.my_txt_document_userCode);
		my_txt_document_userName = (TextView) findViewById(R.id.my_txt_document_userName);
		my_txt_document_birthday = (TextView) findViewById(R.id.my_txt_document_birthday);
		my_txt_document_phone = (TextView) findViewById(R.id.my_txt_document_phone);
		my_txt_document_personalAddress = (TextView) findViewById(R.id.my_txt_document_personalAddress);
		my_txt_document_blog = (TextView) findViewById(R.id.my_txt_document_blog);
		my_txt_document_weibo = (TextView) findViewById(R.id.my_txt_document_weibo);
		my_txt_document_deptName = (TextView) findViewById(R.id.my_txt_document_deptName);
		my_txt_document_post = (TextView) findViewById(R.id.my_txt_document_post);
		my_txt_document_directBoss = (TextView) findViewById(R.id.my_txt_document_directBoss);
		my_txt_document_joinTime = (TextView) findViewById(R.id.my_txt_document_joinTime);
		my_txt_document_intrest = (TextView) findViewById(R.id.my_txt_document_intrest);
		my_txt_document_declaration = (TextView) findViewById(R.id.my_txt_document_declaration);

		my_btn_setInvite = (LinearLayout) findViewById(R.id.my_btn_setInvite);

//		/**
//		 * 设置头像处理
//		 */
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("选择照片");
//
//		builder.setItems(new String[] { "拍照", "从相册选择" },
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int item) {
//						if (item == 0) {
//
//							takePhoto();
//
//						} else {
//							Intent intent = new Intent(
//									Intent.ACTION_PICK,
//									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//							intent.setType("image/*");
//							startActivityForResult(intent, CAMERA_SELECT);
//						}
//					}
//				});
//		final AlertDialog alert = builder.create();

		my_btn_setInvite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				alert.show(); //方案1
//				doPickHeaderPhotoAction(); //方案2				
				
				Intent intent = new Intent (DocumentActivity.this,My_SetHeadPicActivity.class);
				startActivity(intent);
			}
		});

		my_btn_document_personalAddress = (LinearLayout) findViewById(R.id.my_btn_document_personalAddress);
		my_btn_document_personalAddress.setOnClickListener(this);

		my_btn_document_Blog = (LinearLayout) findViewById(R.id.my_btn_document_Blog);
		my_btn_document_Blog.setOnClickListener(this);

		my_document_weibo = (LinearLayout) findViewById(R.id.my_document_weibo);
		my_document_weibo.setOnClickListener(this);

		my_document_friendsDeclaration = (LinearLayout) findViewById(R.id.my_document_friendsDeclaration);
		my_document_friendsDeclaration.setOnClickListener(this);

		my_btn_document_intrest = (LinearLayout) findViewById(R.id.my_btn_document_intrest);
		my_btn_document_intrest.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

//		case R.id.my_btn_setInvite:
//			Toast toast = Toast.makeText(DocumentActivity.this, "去设置头像", Toast.LENGTH_SHORT);
//			toast.show();
//			alert.show();
//			
//			break;
			case R.id.my_btn_document_personalAddress:
				Intent intentPersonalAddress = new Intent(DocumentActivity.this, SetPersonalAddressActivity.class);
				startActivity(intentPersonalAddress);

				break;
			case R.id.my_btn_document_Blog:
				Intent intentBlog = new Intent(DocumentActivity.this, SetBlogActivity.class);
				startActivity(intentBlog);
				break;
			case R.id.my_document_weibo:
				Intent intentWeiBo = new Intent(DocumentActivity.this, My_SetWeiBoActivity.class);
				startActivity(intentWeiBo);
				break;
			case R.id.my_document_friendsDeclaration:
				Intent intentFriendDeclaration = new Intent(DocumentActivity.this, My_FriendsDeclarationActivity.class);
				intentFriendDeclaration.putExtra("friendString", friendsDeclarationString);
				startActivityForResult(intentFriendDeclaration, 10);
				break;
			case R.id.my_btn_document_intrest:
				Intent intentIntrest = new Intent(DocumentActivity.this, My_InterestActivity.class);
				startActivity(intentIntrest);
				break;
			default:
				break;
		}
	}
	
	
	
	
	

	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 拍照的照片存储位置 */
	private static final File PHOTO_DIR = new File(PATH);

	private File mCurrentPhotoFile;// 照相机拍照得到的图片

	private void doPickHeaderPhotoAction() {
		Context context = DocumentActivity.this;

		// Wrap our context to inflate list items using correct theme
		final Context dialogContext = new ContextThemeWrapper(context, android.R.style.Theme_Light);
		String cancel = "返回";
		String[] choices;
		choices = new String[2];
		choices[0] = "拍照"; // 拍照
		choices[1] = "从相册中选择"; // 从相册中选择
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, choices);

		final AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
		builder.setTitle("选择照片来源");
		builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				switch (which) {
					case 0: {
						String status = Environment.getExternalStorageState();
						if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
							doTakePhoto();// 用户点击了从照相机获取
						} else {
							Toast.makeText(getApplicationContext(), "没有SD卡", Toast.LENGTH_LONG).show();
						}
						break;

					}
					case 1:
						doPickPhotoFromGallery();// 从相册中去获取
						break;
				}
			}
		});
		builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}

		});
		builder.create().show();
	}

	/**
	 * 拍照获取图片
	 * 
	 */
	protected void doTakePhoto() {
		try {
			// Launch camera to take photo for selected contact
			PHOTO_DIR.mkdirs();// 创建照片的存储目录
			mCurrentPhotoFile = new File(headerPath);// 头像照片文件命名
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "未找到照片.", Toast.LENGTH_LONG).show();
		}
	}

	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

// 请求Gallery程序  
	protected void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "未找到相片.", Toast.LENGTH_LONG).show();
		}
	}

// 封装请求Gallery的intent  
	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		return intent;
	}

// 因为调用了Camera和Gally所以要判断他们各自的返回情况,他们启动时是这样的startActivityForResult  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 123) {
			System.out.println("@@@@@@@@@@@@");
			String getrrString = data.getStringExtra("123");
			my_txt_document_declaration.setText(getrrString);	
		}
		
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		
			case PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
				final Bitmap photo = data.getParcelableExtra("data");
				// 下面就是显示照片了
				headerBmp = photo;
				my_iv_document_userImg.setImageBitmap(photo);
				break;
			}
			case CAMERA_WITH_DATA: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
				doCropPhoto(mCurrentPhotoFile);
				break;
			}
				
		}
	}

	protected void doCropPhoto(File f) {
		try {
			// 启动gallery去剪辑这个照片
			final Intent intent = getCropImageIntent(Uri.fromFile(f));
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (Exception e) {
			Toast.makeText(this, "未找到图片.", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Constructs an intent for image cropping. 调用图片剪辑程序
	 */
	public static Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		return intent;
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getData();
	}

}
