// package com.mcds.app.android.estar.ui.work;
//
// import java.io.File;
// import java.io.FileOutputStream;
// import java.nio.charset.Charset;
// import java.util.ArrayList;
//
// import org.json.JSONObject;
//
// import android.app.Activity;
// import android.app.AlertDialog;
// import android.content.Intent;
// import android.database.Cursor;
// import android.graphics.Bitmap;
// import android.graphics.PixelFormat;
// import android.net.Uri;
// import android.os.Bundle;
// import android.os.Handler;
// import android.os.Message;
// import android.provider.MediaStore;
// import android.util.Log;
// import android.view.View;
// import android.view.View.OnClickListener;
// import android.view.Window;
// import android.view.WindowManager;
// import android.widget.EditText;
// import android.widget.GridView;
// import android.widget.ImageView;
// import android.widget.ImageView.ScaleType;
// import android.widget.RelativeLayout;
// import android.widget.TextView;
// import android.widget.Toast;
//
// import com.mcds.app.android.estar.R;
//
// /**
// * 上传照片
// *
// * @author TangChao
// */
// public class UpLoadPicture extends Activity
// {
// /**
// * 帖子标题
// */
// private String postTitleStr = "";
// /**
// * 帖子内容
// */
// private String postContentStr = "";
// /**
// * 帖子标题编辑框
// */
// private EditText postTitleView = null;
// /**
// * 帖子内容编辑框
// */
// private EditText postContentView = null;
// /**
// * 图片
// */
// private GridView photoGridView = null;
// /**
// * 栏目ID
// */
// private String fid = null;
// /**
// * 城市ID
// */
// private int cityId = 0;
//
// @Override
// protected void onCreate(Bundle savedInstanceState)
// {
// // TODO Auto-generated method stub
// super.onCreate(savedInstanceState);
// // fid = getIntent().getStringExtra("FID");
// // cityId = getIntent().getIntExtra("CID", 0);
// // savePath = C.filePath + "/Post";
// // File file = new File(savePath);
// // if (!file.exists())
// // {
// // file.mkdirs();
// // }
// getWindow().setFormat(PixelFormat.RGBA_8888);
// // setContentView(R.layout.send_post_layout);
// // TextView titleView = (TextView) findViewById(R.id.titleView);
// // titleView.setText(titleView.getText().toString() + "(" +
// // C.defaultTown.name + ")");
// // ImageView backView = (ImageView) findViewById(R.id.backView);
// // backView.setOnClickListener(onClickListener);
// // TextView selectPictureView = (TextView)
// // findViewById(R.id.selectPictureView);
// // selectPictureView.setOnClickListener(onClickListener);
// // TextView sendPostView = (TextView) findViewById(R.id.sendPostView);
// // sendPostView.setOnClickListener(onClickListener);
// // postTitleView = (EditText) findViewById(R.id.postTitleView);
// // postContentView = (EditText) findViewById(R.id.postContentView);
// // photoGridView = (GridView) findViewById(R.id.photoGridView);
// // photoAdapter = new PhotoAdapter(this, dataList);
// // photoGridView.setAdapter(photoAdapter);
// }
//
// /**
// * 点击监听
// */
// private OnClickListener onClickListener = new OnClickListener()
// {
// @Override
// public void onClick(View v)
// {
// // TODO Auto-generated method stub
// // switch (v.getId())
// // {
// // case R.id.backView:
// // finish();
// // break;
// // case R.id.selectPictureView:
// // if (dataList.size() < 5)
// // {
// // openSelectPictureDialog();
// // }
// // else
// // {
// // C.showToast(SendPostActivity.this, R.string.photoNumError,
// // Toast.LENGTH_SHORT);
// // }
// // break;
// // case R.id.sendPostView:
// // postTitleStr = postTitleView.getText().toString();
// // postContentStr = postContentView.getText().toString();
// // if (postTitleStr == null || postTitleStr.equals(""))
// // {
// // C.showToast(SendPostActivity.this, R.string.titleNull,
// // Toast.LENGTH_SHORT);
// // return;
// // }
// // if (postContentStr == null || postContentStr.equals(""))
// // {
// // C.showToast(SendPostActivity.this, R.string.contentNull,
// // Toast.LENGTH_SHORT);
// // return;
// // }
// // if (postContentStr.length() < 10)
// // {
// // C.showToast(SendPostActivity.this, R.string.contentNumError,
// // Toast.LENGTH_SHORT);
// // return;
// // }
// // sendPost(cityId + "", fid, postTitleStr, postContentStr,
// // dataList);
// // break;
// // default:
// // break;
// // }
// }
// };
//
// /**
// * 发送操作
// *
// * @param cityId
// * @param fid
// * @param postTitle
// * @param postContent
// * @param dataList
// */
// private void sendPost(String cityId, String fid, String postTitle, String
// postContent, ArrayList<DisplayConfig> dataList)
// {
// try
// {
// C.openProgressDialog(SendPostActivity.this, null, R.string.snedPosting);
// ArrayList<KeyValue<String, Object>> paramList = new
// ArrayList<KeyValue<String, Object>>();
// paramList.add(new KeyValue<String, Object>("city_id", new StringBody(cityId,
// Charset.forName("utf-8"))));
// paramList.add(new KeyValue<String, Object>("fid", new StringBody(fid,
// Charset.forName("utf-8"))));
// paramList.add(new KeyValue<String, Object>("subject", new
// StringBody(postTitle, Charset.forName("utf-8"))));
// paramList.add(new KeyValue<String, Object>("message", new
// StringBody(postContent, Charset.forName("utf-8"))));
// // String appauth = new
// //
// DES("shengxun").getEncrypt(C.uid+"#"+C.softwareSP.getSValue(C.TAG_USER_ACCOUNT,
// // ""));
// String appauth = C.getDesStr(C.uid + "#" +
// C.softwareSP.getSValue(C.TAG_USER_ACCOUNT, ""), "shengxun");
// paramList.add(new KeyValue<String, Object>("appauth", new StringBody(appauth,
// Charset.forName("utf-8"))));
// for (int i = 0; i < dataList.size(); i++)
// {
// paramList.add(new KeyValue<String, Object>("attchment" + (i + 1), new
// FileBody(new File(dataList.get(i).path))));
// }
// RequestHttpPostThread requestHttpPostThread = new
// RequestHttpPostThread(requestHttpListener, U.SEND_POST, paramList);
// requestHttpPostThread.start();
// }
// catch (Exception e)
// {
// C.closeProgressDialog();
// Log.e(C.LOG_TAG, "发表帖子异常------>" + e.toString());
// }
// }
//
// /**
// * 请求监听
// */
// private RequestHttpListener requestHttpListener = new RequestHttpListener()
// {
// @Override
// public void requestHttp(Message msg)
// {
// // TODO Auto-generated method stub
// msg.what = 1;
// handler.sendMessage(msg);
// }
// };
// /**
// * 拍照
// */
// private final int PT_CAMERA = 100;
// /**
// * 相册
// */
// private final int PT_PHOTO = 101;
// /**
// * 照片路径
// */
// private String photoPath = "";
// /**
// * 保存路径
// */
// private String savePath = "";
//
// private void openSelectPictureDialog()
// {
// final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
// alertDialog.setCancelable(true);
// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
// alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
// WindowManager.LayoutParams.FLAG_FULLSCREEN);
// alertDialog.show();
// alertDialog.setContentView(R.layout.select_picture_dialog_layout);
// OnClickListener onClickListener = new OnClickListener()
// {
// @Override
// public void onClick(View v)
// {
// // TODO Auto-generated method stub
// switch (v.getId())
// {
// case R.id.takePhotoLayout:
// {
// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
// photoPath = savePath + "/" +
// TimeConversion.getTime(System.currentTimeMillis(), "yyyy_MM_dd_HH_mm_ss")
// + ".jpg";
// File file = new File(photoPath);
// Uri mOutPutFileUri = Uri.fromFile(file);
// intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
// startActivityForResult(intent, PT_CAMERA);
// alertDialog.dismiss();
// }
// break;
// case R.id.albumLayout:
// {
// // Intent getImage = new
// // Intent(Intent.ACTION_GET_CONTENT);
// // getImage.addCategory(Intent.CATEGORY_OPENABLE);
// // getImage.setType("image/*");
// // startActivityForResult(getImage, PT_PHOTO);
// Intent picture = new Intent(Intent.ACTION_PICK,
// android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// startActivityForResult(picture, PT_PHOTO);
// alertDialog.dismiss();
// }
// break;
// default:
// break;
// }
// }
// };
// RelativeLayout relativeLayout = (RelativeLayout)
// alertDialog.findViewById(R.id.takePhotoLayout);
// relativeLayout.setOnClickListener(onClickListener);
// RelativeLayout relativeLayout2 = (RelativeLayout)
// alertDialog.findViewById(R.id.albumLayout);
// relativeLayout2.setOnClickListener(onClickListener);
// }
// @Override
// protected void onActivityResult(int requestCode, int resultCode, Intent data)
// {
// super.onActivityResult(requestCode, resultCode, data);
// switch (requestCode)
// {
// case PT_CAMERA:// 照相
// if (resultCode == RESULT_OK)
// {
// new GetPhotoThread(photoPath, true).start();
// }
// break;
// case PT_PHOTO:
// if (resultCode == RESULT_OK)
// {
// Uri uri = data.getData();
// if (uri != null)
// {
// Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
// cursor.moveToFirst();
// int column_index =
// cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
// // 最后根据索引值获取图片路径
// String photoPath = cursor.getString(column_index);
// new GetPhotoThread(photoPath, false).start();
// }
// }
// break;
// default:
// break;
// }
// }
// /**
// * 缩放存储图片
// *
// * @param bitmapPath
// * @param isCamera
// * @return
// */
// private String scaleSaveBitmap(String bitmapPath, boolean isCamera)
// {
// final int MAX_W = 800;
// if (bitmapPath == null || bitmapPath.equals(""))
// {
// return bitmapPath;
// }
// try
// {
// Bitmap srcBitmap = BitmapUtils.decodeSampledBitmapFromFile(bitmapPath, MAX_W,
// MAX_W, null);
// if (srcBitmap != null)
// {
// int maxEdge = srcBitmap.getHeight();
// if (srcBitmap.getWidth() >= maxEdge)
// {
// maxEdge = srcBitmap.getWidth();
// }
// float scale = MAX_W / (float) maxEdge;
// Bitmap resultBitmap = null;
// if (scale >= 1.0f)
// {
// resultBitmap = srcBitmap;
// }
// else
// {
// resultBitmap = BitmapUtils.getScaledBitmap(srcBitmap, scale,
// ScaleType.MATRIX);
// }
// if (isCamera)
// {
// saveBitmap(bitmapPath, resultBitmap, "jpg");
// }
// else
// {
// String bitmapType = ".jpg";
// if (!bitmapPath.endsWith(bitmapType))
// {
// bitmapType = ".png";
// }
// String newBitmapPath = savePath + "/"
// + TimeConversion.getTime(System.currentTimeMillis(), "yyyy_MM_dd_HH_mm_ss") +
// bitmapType;
// saveBitmap(newBitmapPath, resultBitmap, bitmapType);
// return newBitmapPath;
// }
// }
// }
// catch (Exception e)
// {
// System.out.println("图片写入到SD卡异常--------->" + e.toString());
// }
// return bitmapPath;
// }
// /**
// * 图片保存到本地
// *
// * @param bitmapPath
// * @param mBitmap
// * @throws Exception
// */
// private void saveBitmap(String bitmapPath, Bitmap mBitmap, String bitmapType)
// throws Exception
// {
// FileOutputStream fos = new FileOutputStream(bitmapPath);
// if (bitmapType.equals(".png"))
// {
// mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
// }
// else
// {
// mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
// }
// fos.flush();
// fos.close();
// }
//
// /**
// * 图片列表
// */
// private ArrayList<DisplayConfig> dataList = new ArrayList<DisplayConfig>();
// /**
// * 图片适配器
// */
// private PhotoAdapter photoAdapter = null;
//
// /**
// * 处理图片的线程
// *
// * @author Administrator
// */
// private class GetPhotoThread extends Thread
// {
// /**
// * 图片路径
// */
// private String photoPath = null;
// /**
// * 是否是拍照
// */
// private boolean isCamera = false;
//
// public GetPhotoThread(String photoPath, boolean isCamera)
// {
// this.photoPath = photoPath;
// this.isCamera = isCamera;
// }
// @Override
// public void run()
// {
// // TODO Auto-generated method stub
// super.run();
// String path = scaleSaveBitmap(photoPath, isCamera);
// DisplayConfig displayConfig = new DisplayConfig();
// displayConfig.zoomModel = DisplayConst.ZOOM_THUMBNAIL;
// displayConfig.url = null;
// displayConfig.path = path;
// Log.i(C.LOG_TAG, path);
// displayConfig.w = C.SCR_W / 5;
// displayConfig.h = C.SCR_W / 5;
// displayConfig.defaultRes = R.drawable.default_small_image;
// dataList.add(displayConfig);
// handler.sendEmptyMessage(0);
// }
// }
//
// private Handler handler = new Handler()
// {
// @Override
// public void handleMessage(Message msg)
// {
// // TODO Auto-generated method stub
// super.handleMessage(msg);
// if (msg.what == 0)
// {
// if (dataList.size() <= 0)
// {
// photoGridView.setVisibility(View.GONE);
// }
// else
// {
// photoGridView.setVisibility(View.VISIBLE);
// }
// photoAdapter.notifyDataSetChanged();
// photoGridView.setFocusable(false);
// }
// else if (msg.what == 1)
// {
// try
// {
// Bundle bundle = msg.getData();
// int result = bundle.getInt(HttpConst.ACTION_RESULT, HttpConst.ACTION_FAIL);
// C.closeProgressDialog();
// if (result == HttpConst.ACTION_SUCCESS)
// {
// byte dataArr[] = bundle.getByteArray(HttpConst.DATA_ARR);
// String dataStr = new String(dataArr, "utf-8");
// JSONObject jsonObject = new JSONObject(dataStr);
// if
// (jsonObject.getJSONObject("data").getString("result").equals(C.STATE_SUCCESS))
// {
// postTitleView.setText("");
// postContentView.setText("");
// dataList.clear();
// photoAdapter.notifyDataSetChanged();
// photoGridView.setVisibility(View.GONE);
// C.showToast(SendPostActivity.this, R.string.sendPostSuccess,
// Toast.LENGTH_SHORT);
// }
// else
// {
// C.showToast(SendPostActivity.this, jsonObject.getString("error_desc"),
// Toast.LENGTH_SHORT);
// }
// }
// else if (result == HttpConst.ACTION_FAIL)
// {
// C.showToast(SendPostActivity.this, R.string.sendPostFail,
// Toast.LENGTH_SHORT);
// }
// }
// catch (Exception e)
// {
// Log.e(C.LOG_TAG, "--->发帖异常" + e.toString());
// }
// }
// }
// };
// }
