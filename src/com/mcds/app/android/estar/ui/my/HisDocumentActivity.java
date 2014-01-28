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
 * 他人资料
 * @author 
 *
 */
public class HisDocumentActivity extends BaseTabActivity implements OnClickListener {
	private ImageView his_iv_document_userImg;
	private TextView his_txt_document_userCode;
	private TextView his_txt_document_userName;
	private TextView his_txt_document_birthday;
	private TextView his_txt_document_phone;
	private TextView his_txt_document_personalAddress;
	private TextView his_txt_document_blog;
	private TextView his_txt_document_weibo;
	private TextView his_txt_document_deptName;
	private TextView his_txt_document_post;
	private TextView his_txt_document_directBoss;
	private TextView his_txt_document_joinTime;
	private TextView his_txt_document_intrest;
	private TextView his_txt_document_declaration;
	private ReturnResult<MyDetailInfo> rrhisDetailInfo;

	private LinearLayout his_btn_setInvite;
	private LinearLayout his_btn_document_personalAddress;
	private LinearLayout his_btn_document_Blog;
	private LinearLayout his_document_weibo;
	private LinearLayout his_document_friendsDeclaration;
	private LinearLayout his_btn_document_intrest;

	public static String his_Str_interest = " ";
	public static String[] his_Str_interestArray ;

	private static final int CAMERA_TAKE = 1;
	private static final int CAMERA_SELECT = 2;
	private String his_headpic_name;
	private boolean isBig = false;
	private static final String PATH = Environment.getExternalStorageDirectory() + "/EStar/picture";
	private static final String headerPath = Environment.getExternalStorageDirectory() + "/EStar/picture/header.jpg";
	private Bitmap headerBmp = null;
	public static Bitmap his_headIntentBitmap = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hisdocument);
		initButton();
		getData();

	}

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrhisDetailInfo = ConnectProvider.getMyDetailInfo(UserManager.uid);
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
				if (rrhisDetailInfo.status.equals("0")) {
					System.out.println("返回状态" + rrhisDetailInfo.info);
					MyDetailInfo info = new MyDetailInfo();
					info = rrhisDetailInfo.getDatas().get(0);

					/*
					 * 获取用户头像
					 */
					AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
					Bitmap bmp = asyncLoader.loadBitmap(his_iv_document_userImg, info.user_img, 100, 100, new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (bitmap == null) {
								bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.my_icon_fans_headpic);
							}
							imageView.setImageBitmap(bitmap);
						}
					});
					// 设置用户头像
					if (bmp != null) {
						his_iv_document_userImg.setImageBitmap(bmp);
						his_headIntentBitmap = bmp;
					}

					his_txt_document_userCode.setText(info.user_code);
					his_txt_document_userName.setText(info.user_name);
					his_txt_document_birthday.setText(info.birthday);
					his_txt_document_phone.setText(info.phone);
					his_txt_document_personalAddress.setText(info.personal_address);
					his_txt_document_blog.setText(info.blog);
					his_txt_document_weibo.setText(info.weibo);
//					his_txt_document_deptName.setText(info.);
					his_txt_document_post.setText(info.user_work);
					his_txt_document_directBoss.setText(info.direct_boss);
					his_txt_document_joinTime.setText(info.join_time);
					his_txt_document_intrest.setText(info.intrest);
					his_txt_document_declaration.setText(info.declaration);

					
					his_Str_interest = info.intrest;
					his_Str_interestArray = his_Str_interest.split(" ");
					
				}
			}
		});
	}

	private void initButton() {
		// TODO Auto-generated method stub
		his_iv_document_userImg = (ImageView) findViewById(R.id.his_iv_document_userImg);
		his_txt_document_userCode = (TextView) findViewById(R.id.his_txt_document_userCode);
		his_txt_document_userName = (TextView) findViewById(R.id.his_txt_document_userName);
		his_txt_document_birthday = (TextView) findViewById(R.id.his_txt_document_birthday);
		his_txt_document_phone = (TextView) findViewById(R.id.his_txt_document_phone);
		his_txt_document_personalAddress = (TextView) findViewById(R.id.his_txt_document_personalAddress);
		his_txt_document_blog = (TextView) findViewById(R.id.his_txt_document_blog);
		his_txt_document_weibo = (TextView) findViewById(R.id.his_txt_document_weibo);
		his_txt_document_deptName = (TextView) findViewById(R.id.his_txt_document_deptName);
		his_txt_document_post = (TextView) findViewById(R.id.his_txt_document_post);
		his_txt_document_directBoss = (TextView) findViewById(R.id.his_txt_document_directBoss);
		his_txt_document_joinTime = (TextView) findViewById(R.id.his_txt_document_joinTime);
		his_txt_document_intrest = (TextView) findViewById(R.id.his_txt_document_intrest);
		his_txt_document_declaration = (TextView) findViewById(R.id.his_txt_document_declaration);

		his_btn_setInvite = (LinearLayout) findViewById(R.id.his_btn_setInvite);



		his_btn_document_personalAddress = (LinearLayout) findViewById(R.id.his_btn_document_personalAddress);
//		his_btn_document_personalAddress.setOnClickListener(this);

		his_btn_document_Blog = (LinearLayout) findViewById(R.id.his_btn_document_Blog);
//		his_btn_document_Blog.setOnClickListener(this);

		his_document_weibo = (LinearLayout) findViewById(R.id.his_document_weibo);
//		his_document_weibo.setOnClickListener(this);

		his_document_friendsDeclaration = (LinearLayout) findViewById(R.id.his_document_friendsDeclaration);
//		his_document_friendsDeclaration.setOnClickListener(this);

		his_btn_document_intrest = (LinearLayout) findViewById(R.id.his_btn_document_intrest);
//		his_btn_document_intrest.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.his_btn_document_personalAddress:
				Intent intentPersonalAddress = new Intent(HisDocumentActivity.this, SetPersonalAddressActivity.class);
				startActivity(intentPersonalAddress);

				break;
			case R.id.his_btn_document_Blog:
				Intent intentBlog = new Intent(HisDocumentActivity.this, SetBlogActivity.class);
				startActivity(intentBlog);
				break;
			case R.id.his_document_weibo:
				Intent intentWeiBo = new Intent(HisDocumentActivity.this, My_SetWeiBoActivity.class);
				startActivity(intentWeiBo);
			case R.id.his_document_friendsDeclaration:
				Intent intentFriendDeclaration = new Intent(HisDocumentActivity.this, My_FriendsDeclarationActivity.class);
				startActivity(intentFriendDeclaration);
				break;
			case R.id.his_btn_document_intrest:
				Intent intentIntrest = new Intent(HisDocumentActivity.this, My_InterestActivity.class);
				startActivity(intentIntrest);
				break;
			default:
				break;
		}
	}


}
