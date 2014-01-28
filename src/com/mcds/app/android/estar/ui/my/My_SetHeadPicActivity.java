package com.mcds.app.android.estar.ui.my;

import java.io.ByteArrayOutputStream;
import java.io.File;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.My_SetHeadPic;
import com.mcds.app.android.estar.bean.My_SetIntrest;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.ConnectProviderCL;
import com.mcds.app.android.estar.ui.BaseTabActivity;


/**
 * 设置个人头像
 * @author chenliang
 *
 */
public class My_SetHeadPicActivity extends BaseTabActivity implements OnClickListener{
	private ImageButton my_setheadpic_back;
	private ImageButton my_btn_setHeadPic_complete;
	private Button my_setheadpic_clicksetBtn;
	private ImageView my_setHeadPic_img_show;
	private static final String PATH = Environment.getExternalStorageDirectory() + "/EStar/picture";
	private static final String headerPath = Environment.getExternalStorageDirectory() + "/EStar/picture/header.jpg";
	private Bitmap headerBmp = null;
	private ReturnResult<My_SetHeadPic> rrSetHeadPic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_setheadpic);
		my_setheadpic_back = (ImageButton) findViewById(R.id.my_setheadpic_back);
		my_setheadpic_back.setOnClickListener(this);
		my_btn_setHeadPic_complete = (ImageButton) findViewById(R.id.my_btn_setHeadPic_complete);
		my_btn_setHeadPic_complete.setOnClickListener(this);
		my_setheadpic_clicksetBtn = (Button) findViewById(R.id.my_setheadpic_clicksetBtn);
		my_setheadpic_clicksetBtn.setOnClickListener(this);
		
		my_setHeadPic_img_show = (ImageView) findViewById(R.id.my_setHeadPic_img_show);
		my_setHeadPic_img_show.setImageBitmap(DocumentActivity.my_headIntentBitmap);
		
	}
	
	public String bitmaptoString(Bitmap bitmap){
		//将Bitmap转换成字符串
		    String string=null;
		    ByteArrayOutputStream bStream=new ByteArrayOutputStream();
		    bitmap.compress(CompressFormat.PNG,100,bStream);
		    byte[]bytes=bStream.toByteArray();
		    string=Base64.encodeToString(bytes,Base64.DEFAULT);
		    return string;
		    }
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.my_setheadpic_back:
			Intent intent = new Intent(getApplicationContext(),DocumentActivity.class);
			startActivity(intent);
			break;
		
		case R.id.my_btn_setHeadPic_complete:
			new Thread(new Runnable() {
				 
				@Override 
				public void run() {  
					showWaitingDialog();
					String getStringBitmap ="" ;
					getStringBitmap = bitmaptoString(headerBmp);
					if (!UserManager.uid.equals("")) {
						rrSetHeadPic= ConnectProviderCL.setUserImage(UserManager.uid,getStringBitmap);
					}
					System.out.println("设置照片返回结果："+rrSetHeadPic.info);
					if (rrSetHeadPic.status.equals("0")) {
						

					}
					hideWaitingDialog();
				
				}
			}).start();
			
			
			break;
			
		case R.id.my_setheadpic_clicksetBtn:
			doPickHeaderPhotoAction();
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
		Context context = My_SetHeadPicActivity.this;

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
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
			case PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
				final Bitmap photo = data.getParcelableExtra("data");
				// 下面就是显示照片了
				headerBmp = photo;
				my_setHeadPic_img_show.setImageBitmap(photo);
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

	
}
