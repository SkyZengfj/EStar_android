package com.mcds.app.android.estar.ui.weibo;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.R.integer;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.BlessingModelInfo;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.WeiboWrite;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.ConnectProviderGC;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseActivity;

public class HomeListDynamicEditActivity extends BaseActivity {
	private ViewPager frameViewPager;
	private ArrayList<View> framePageViews;
	
	private TextView textView_dynamicType;
	private TextView count;

	private LinearLayout dynamicTypeList;
	private LinearLayout photoContainer1;
	private LinearLayout photoContainer2;
	private LinearLayout photoContainer3;
	private LinearLayout photoContainer4;
	
	private RelativeLayout msgTypeList_affirmation;
	private RelativeLayout msgTypeList_blessing;
	private RelativeLayout msgTypeList_message;
	private RelativeLayout msgTypeList_help;
	private RelativeLayout msgTypeList_activity;

	private ImageView affirmation_selected;
	private ImageView blessing_selected;
	private ImageView message_selected;
	private ImageView help_selected;
	private ImageView activity_selected;
	private ImageView triangle;
	private ImageView img_select_btn;
	private ImageView at_select_btn;
	private ImageView matter_select_btn;
	private ImageView expression_btn;
	private ImageView expression_1;
	
	private ImageButton dynamic_send_btn;

	private EditText dynamic_edit;

	private static final int CAMERA_TAKE = 1;
	private static final int CAMERA_SELECT = 2;
	private static final int Matter_Actvity = 3;
	private static final int Contact_Activity = 4;

	private String pic_name;
	private String groupMatterString = "";
	private String childMatterString = "";
	private final String PAGE_SIZE = "10";
	
	private ArrayList<String> atUserID;
	private ArrayList<String> atUserName;

	private boolean isBig = false;
	
	private int screenWidth;
	private int screenHeight;

	private static final String PATH = Environment
			.getExternalStorageDirectory() + "/EStar/picture";

	// the message type: 0(all dynamic), 1(affirmation), 2(blessing),
	// 3(message), 4(help), 5(activity)
	public static int messageType = 1;

	private List<ImageView> photos;
	
	private ReturnResult<WeiboWrite> result_WeiboWrite;
	
	private ReturnResult<BlessingModelInfo> BlessingModelInfos;
	
	private int model_page_num = 0;
	private String model_page_size = "3";
	private TextView dynamic_detail_edit_textview_counter;
	//选择的模板ID
	private String model_id = "";
	private TextView weiboHomeDynamiEdit_other;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_home_dynamic_edit);

		screenWidth  = getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindowManager().getDefaultDisplay().getHeight(); 
		
		textView_dynamicType = (TextView) findViewById(R.id.dynamic_detail_textview_dynamic_type_name);
		
		messageType = getIntent().getIntExtra("sendMessageType", 1);
		switch (messageType) {
		case 1:
			textView_dynamicType.setText("肯定");
			break;
		case 2:
			textView_dynamicType.setText("祝福");
			break;
		case 3:
			textView_dynamicType.setText("信息");
			break;
		case 4:
			textView_dynamicType.setText("帮助");
			break;

		default:
			break;
		}
		
		frameViewPager = (ViewPager) findViewById(R.id.vPager);
		framePageViews = new ArrayList<View>();

		View v1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.expression_panel_1, null);
		framePageViews.add(v1);

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

		dynamicTypeList = (LinearLayout) findViewById(R.id.dynamic_detail_msgTypeList);
		photoContainer1 = (LinearLayout) findViewById(R.id.weiboHomeDynamiEdit_photoContainer1);
		photoContainer2 = (LinearLayout) findViewById(R.id.weiboHomeDynamiEdit_photoContainer2);
		photoContainer3 = (LinearLayout) findViewById(R.id.weiboHomeDynamiEdit_photoContainer3);
		photoContainer4 = (LinearLayout) findViewById(R.id.weiboHomeDynamiEdit_photoContainer4);
		
		dynamic_detail_edit_textview_counter = (TextView)findViewById(R.id.dynamic_detail_edit_textview_counter);
		
		weiboHomeDynamiEdit_other = (TextView)findViewById(R.id.weiboHomeDynamiEdit_other);
		
		dynamic_edit = (EditText) findViewById(R.id.dynamic_detail_edit_text);
		count = (TextView) findViewById(R.id.dynamic_detail_edit_textview_counter);

		msgTypeList_affirmation = (RelativeLayout) findViewById(R.id.dynamic_detail_msgTypeList_affirmation);
		msgTypeList_blessing = (RelativeLayout) findViewById(R.id.dynamic_detail_msgTypeList_blessing);
		msgTypeList_message = (RelativeLayout) findViewById(R.id.dynamic_detail_msgTypeList_message);
		msgTypeList_help = (RelativeLayout) findViewById(R.id.dynamic_detail_msgTypeList_help);
		msgTypeList_activity = (RelativeLayout) findViewById(R.id.dynamic_detail_msgTypeList_activity);

		affirmation_selected = (ImageView) findViewById(R.id.dynamic_detail_msgTypeList_affirmation_selected);
		blessing_selected = (ImageView) findViewById(R.id.dynamic_detail_msgTypeList_blessing_selected);
		message_selected = (ImageView) findViewById(R.id.dynamic_detail_msgTypeList_message_selected);
		help_selected = (ImageView) findViewById(R.id.dynamic_detail_msgTypeList_help_selected);
		activity_selected = (ImageView) findViewById(R.id.dynamic_detail_msgTypeList_activity_selected);
		triangle = (ImageView) findViewById(R.id.dynamic_detail_top_triangle);
		img_select_btn = (ImageView) findViewById(R.id.dynamic_detail_edit_btn_img);
		at_select_btn = (ImageView) findViewById(R.id.dynamic_detail_edit_btn_at);
		matter_select_btn = (ImageView) findViewById(R.id.dynamic_detail_edit_btn_matter);
		expression_btn = (ImageView) findViewById(R.id.dynamic_detail_edit_btn_affirmation);
		
		dynamic_send_btn = (ImageButton) findViewById(R.id.dynamic_detail_imageButton_edit);
		dynamic_send_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Editable editable = dynamic_edit.getText();
				String content_text = editable.toString();
				String weibo_sort = String.valueOf(messageType);
				List<String> content_img = new ArrayList<String>();
				
				char a = '@';
				char c = '#';
				int a_num = 0;
				int c_num = 0;
				char[] chars = content_text.toCharArray();
				
				if(content_text.length() != 0) {
					if(messageType == 1) {
						for(int i = 0; i < chars.length; i++){
						    if(a == chars[i])
						    {
						       a_num++;
						    }
						}
						
						for(int i = 0; i < chars.length; i++){
						    if(c == chars[i])
						    {
						       c_num++;
						    }
						}
								
						if(a_num == 0) {
							Toast toast = Toast.makeText(HomeListDynamicEditActivity.this, "请选择联系人", Toast.LENGTH_LONG);
							toast.show();
							return;
						}
						
						if(c_num < 2) {
							Toast toast = Toast.makeText(HomeListDynamicEditActivity.this, "请选择事项", Toast.LENGTH_LONG);
							toast.show();
							return;
						}
					}else{
						if (!"".equals(UserManager.uid)) {
							if (messageType==2 && "".equals(model_id)) {
								return;
							}
							result_WeiboWrite = ConnectProviderGC.writeWeibo(UserManager.uid, content_text, weibo_sort, content_img,model_id);
							System.out.println(result_WeiboWrite.status + "============" + result_WeiboWrite.info);
							if (result_WeiboWrite.status.equals("0")) {
								Toast toast = Toast.makeText(HomeListDynamicEditActivity.this, "发布成功", Toast.LENGTH_LONG);
								toast.show();
							}
						}
					}
					
				}else{
					Toast toast = Toast.makeText(HomeListDynamicEditActivity.this, "请输入内容", Toast.LENGTH_LONG);
					toast.show();
				}
				
				
			}
		});
		
		expression_1 = (ImageView) v1.findViewById(R.id.expression_panel_1_row_1_col_1);
		expression_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int index = dynamic_edit.getSelectionStart();
				Editable editable = dynamic_edit.getText();
				editable.insert(index, "[呲牙]");
			}
		});
		

		photos = new ArrayList<ImageView>();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择照片");

		builder.setItems(new String[] { "拍照", "从相册选择" },
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (item == 0) {

							takePhoto();

						} else {
							Intent intent = new Intent(
									Intent.ACTION_PICK,
									android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							intent.setType("image/*");
							startActivityForResult(intent, CAMERA_SELECT);
						}
					}
				});
		final AlertDialog alert = builder.create();

		dynamic_edit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean getFocus) {
				// TODO Auto-generated method stub
				if (getFocus) {
					frameViewPager.setVisibility(LinearLayout.GONE);
					photoContainer1.setVisibility(LinearLayout.GONE);
					photoContainer2.setVisibility(LinearLayout.GONE);
					photoContainer3.setVisibility(LinearLayout.GONE);
					photoContainer4.setVisibility(LinearLayout.GONE);
				} else {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(dynamic_edit.getWindowToken(), 0);
				}
			}
		});
		
		dynamic_edit.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				temp = s;
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				int number = 120 - s.length();
				count.setText(number+"个字");
				selectionStart = dynamic_edit.getSelectionStart();
			    selectionEnd = dynamic_edit.getSelectionEnd();
			    
				if (temp.length() > 120)  {
				s.delete(selectionStart - 1, selectionEnd);
				int tempSelection = selectionStart;
				dynamic_edit.setText(s);
				dynamic_edit.setSelection(tempSelection);
				}

				
			}
		});

		img_select_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(dynamic_edit.getWindowToken(), 0);
				if (messageType!=2) {
					frameViewPager.setVisibility(LinearLayout.GONE);
					photoContainer1.setVisibility(LinearLayout.VISIBLE);
					photoContainer2.setVisibility(LinearLayout.VISIBLE);
					photoContainer3.setVisibility(LinearLayout.GONE);
					dynamic_detail_edit_textview_counter.setVisibility(View.VISIBLE);
					weiboHomeDynamiEdit_other.setVisibility(View.GONE);
//					dynamic_detail_edit_textview_counter.re
					alert.show();
				}else {
					frameViewPager.setVisibility(LinearLayout.GONE);
					photoContainer1.setVisibility(LinearLayout.GONE);
					photoContainer2.setVisibility(LinearLayout.GONE);
					photoContainer3.setVisibility(LinearLayout.VISIBLE);
					dynamic_detail_edit_textview_counter.setVisibility(View.GONE);
					weiboHomeDynamiEdit_other.setVisibility(View.VISIBLE);
					weiboHomeDynamiEdit_other.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							model_page_num += 1;
							getData();
						}
					});
					getData();
				}
			}
		});

		at_select_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(dynamic_edit.getWindowToken(), 0);
				Intent intent = new Intent(HomeListDynamicEditActivity.this, ContactActivity.class);
				startActivityForResult(intent, Contact_Activity);
			}
		});

		matter_select_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(dynamic_edit.getWindowToken(), 0);
				Intent intent = new Intent(HomeListDynamicEditActivity.this, DynamicMatterActivity.class);
				intent.putExtra("messageType", messageType);
				startActivityForResult(intent, Matter_Actvity);
			}
		});

		expression_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(dynamic_edit.getWindowToken(), 0);
				photoContainer1.setVisibility(LinearLayout.GONE);
				photoContainer2.setVisibility(LinearLayout.GONE);
				photoContainer3.setVisibility(LinearLayout.GONE);
				frameViewPager.setVisibility(LinearLayout.VISIBLE);
			}
		});

		textView_dynamicType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (dynamicTypeList.getVisibility() == View.VISIBLE) {
					triangle.setBackgroundResource(R.drawable.top_btn_bg_triangle_down);
					dynamicTypeList.setVisibility(View.INVISIBLE);
				} else {
					triangle.setBackgroundResource(R.drawable.top_btn_bg_triangle);
					switch (messageType) {
					case 1:
						affirmation_selected.setVisibility(View.VISIBLE);
						blessing_selected.setVisibility(View.INVISIBLE);
						message_selected.setVisibility(View.INVISIBLE);
						help_selected.setVisibility(View.INVISIBLE);
						break;
					case 2:
						affirmation_selected.setVisibility(View.INVISIBLE);
						blessing_selected.setVisibility(View.VISIBLE);
						message_selected.setVisibility(View.INVISIBLE);
						help_selected.setVisibility(View.INVISIBLE);					
						break;
					case 3:
						affirmation_selected.setVisibility(View.INVISIBLE);
						blessing_selected.setVisibility(View.INVISIBLE);
						message_selected.setVisibility(View.VISIBLE);
						help_selected.setVisibility(View.INVISIBLE);
						break;
					case 4:
						affirmation_selected.setVisibility(View.INVISIBLE);
						blessing_selected.setVisibility(View.INVISIBLE);
						message_selected.setVisibility(View.INVISIBLE);
						help_selected.setVisibility(View.VISIBLE);
						break;

					default:
						break;
					}
					dynamicTypeList.setVisibility(View.VISIBLE);
				}
			}
		});

		msgTypeList_affirmation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (messageType != 1) {
					switch (messageType) {
					case 2:
						blessing_selected.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 3:
						message_selected.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 4:
						help_selected.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 5:
						activity_selected.setVisibility(LinearLayout.INVISIBLE);
						break;

					default:
						break;
					}
					messageType = 1;
					textView_dynamicType.setText("肯定");
					dynamic_edit.setText("");
					affirmation_selected.setVisibility(LinearLayout.VISIBLE);
					dynamicTypeList.setVisibility(LinearLayout.INVISIBLE);
				} else {
					dynamicTypeList.setVisibility(LinearLayout.INVISIBLE);
				}
			}
		});

		msgTypeList_blessing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (messageType != 2) {
					switch (messageType) {
					case 1:
						affirmation_selected
								.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 3:
						message_selected.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 4:
						help_selected.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 5:
						activity_selected.setVisibility(LinearLayout.INVISIBLE);
						break;

					default:
						break;
					}
					messageType = 2;
					textView_dynamicType.setText("祝福");
					dynamic_edit.setText("");
					blessing_selected.setVisibility(LinearLayout.VISIBLE);
					dynamicTypeList.setVisibility(LinearLayout.INVISIBLE);
				} else {
					dynamicTypeList.setVisibility(LinearLayout.INVISIBLE);
				}
			}
		});

		msgTypeList_message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (messageType != 3) {
					switch (messageType) {
					case 1:
						affirmation_selected
								.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 2:
						blessing_selected.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 4:
						help_selected.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 5:
						activity_selected.setVisibility(LinearLayout.INVISIBLE);
						break;

					default:
						break;
					}
					messageType = 3;
					textView_dynamicType.setText("信息");
					dynamic_edit.setText("");
					message_selected.setVisibility(LinearLayout.VISIBLE);
					dynamicTypeList.setVisibility(LinearLayout.INVISIBLE);
				} else {
					dynamicTypeList.setVisibility(LinearLayout.INVISIBLE);
				}
			}
		});

		msgTypeList_help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (messageType != 4) {
					switch (messageType) {
					case 1:
						affirmation_selected
								.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 2:
						blessing_selected.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 3:
						message_selected.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 5:
						activity_selected.setVisibility(LinearLayout.INVISIBLE);
						break;

					default:
						break;
					}
					messageType = 4;
					textView_dynamicType.setText("帮助");
					dynamic_edit.setText("");
					help_selected.setVisibility(LinearLayout.VISIBLE);
					dynamicTypeList.setVisibility(LinearLayout.INVISIBLE);
				} else {
					dynamicTypeList.setVisibility(LinearLayout.INVISIBLE);
				}
			}
		});

		msgTypeList_activity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (messageType != 5) {
					switch (messageType) {
					case 1:
						affirmation_selected
								.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 2:
						blessing_selected.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 3:
						message_selected.setVisibility(LinearLayout.INVISIBLE);
						break;
					case 4:
						help_selected.setVisibility(LinearLayout.INVISIBLE);
						break;

					default:
						break;
					}
					messageType = 5;
					textView_dynamicType.setText("活动");
					dynamic_edit.setText("");
					activity_selected.setVisibility(LinearLayout.VISIBLE);
					dynamicTypeList.setVisibility(LinearLayout.INVISIBLE);
				} else {
					dynamicTypeList.setVisibility(LinearLayout.INVISIBLE);
				}
			}
		});
	}
	
	private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
	int width_height = 0;
	private void getData(){
		showWaitingDialog();
		BlessingModelInfos = ConnectProvider.getBlessingList(String.valueOf(model_page_num), model_page_size);
		if (BlessingModelInfos!= null && BlessingModelInfos.status.equals("0")) {
			if (BlessingModelInfos.getDatas().size()<=0) {
//				Toast.makeText(HomeListDynamicEditActivity.this, "没有模板了", Toast.LENGTH_SHORT).show();
				new AlertDialog.Builder(this).setTitle("没有模板了,是否重新查看？")  
			    .setIcon(android.R.drawable.ic_dialog_info)  
			    .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
			  
			        @Override  
			        public void onClick(DialogInterface dialog, int which) {  
			        // 点击“确认”后的操作  
			        	model_page_num = 0;
			        	getData();
			  
			        }  
			    })  
			    .setNegativeButton("返回", new DialogInterface.OnClickListener() {  
			  
			        @Override  
			        public void onClick(DialogInterface dialog, int which) {  
			        // 点击“返回”后的操作,这里不设置没有任何操作  
			        	
			        }  
			    }).show();  
				hideWaitingDialog();
				return;
			}else {
				photoContainer3.removeAllViews();
			}
			for (int i = 0; i < BlessingModelInfos.getDatas().size(); i++) {
				String url = BlessingModelInfos.getDatas().get(i).imagepath;
				System.out.println("id:"+BlessingModelInfos.getDatas().get(i).bt_id);
				if (!"".equals(url)) {
					int screenWidth  = getWindowManager().getDefaultDisplay().getWidth();
					width_height = screenWidth/3;
					ImageView img_1 = new ImageView(HomeListDynamicEditActivity.this);
					img_1.setLayoutParams(new LayoutParams(width_height, width_height));
					img_1.setScaleType(ImageView.ScaleType.CENTER_CROP);
					
					Bitmap bmp = asyncLoader.loadBitmap(img_1, url, width_height-4, width_height-4, new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (bitmap == null) {
								bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
							}
							imageView.setImageBitmap(bitmap);
						}
					});

					if (bmp != null) {
						img_1.setImageBitmap(bmp);
					}
					img_1.setPadding(2, 2, 2, 2);
					
//					Bitmap bt = BitmapUtility.getHttpBitmap(url, width_height-4, width_height-4);
//					img_1.setImageBitmap(bt);
//					img_1.setId(id);
					img_1.setTag(R.id.BlessingModelInfos_tag,BlessingModelInfos.getDatas().get(i));
					img_1.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							ImageView iv = new ImageView(HomeListDynamicEditActivity.this);
							BlessingModelInfo bm =  (BlessingModelInfo) arg0.getTag(R.id.BlessingModelInfos_tag);
							Bitmap bmp = asyncLoader.loadBitmap(iv, bm.imagepath, width_height-4, width_height-4, new ImageCallBack() {

								@Override
								public void imageLoad(ImageView imageView, Bitmap bitmap) {
									if (bitmap == null) {
										bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_frame_listview_default_avatar);
									}
									imageView.setImageBitmap(bitmap);
								}
							});

							if (bmp != null) {
								iv.setImageBitmap(bmp);
							}
							photoContainer4.removeAllViews();
							photoContainer4.setVisibility(LinearLayout.VISIBLE);
							photoContainer4.addView(iv);
							model_id = bm.bt_id;
							Toast.makeText(HomeListDynamicEditActivity.this,"你选择了：tag="+model_id, Toast.LENGTH_SHORT).show();
						}
					});
					photoContainer3.addView(img_1);
				}
			}
		}else {
			Toast.makeText(HomeListDynamicEditActivity.this, "读取模板失败", Toast.LENGTH_SHORT).show();
		}
		hideWaitingDialog();
	}

	public void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		new DateFormat();
		pic_name = DateFormat.format("yyyyMMdd_hhmmss",
				Calendar.getInstance(Locale.CHINA))
				+ ".jpg";
		Uri imageUri = Uri.fromFile(new File(PATH, pic_name));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, CAMERA_TAKE);
	}

	private void setPhotos(ImageView iv) {

		// hotKeyContainer.removeAllViews();
		if (photos.size() < 4) {
			photoContainer1.addView(iv);

		} else if (photos.size() < 8) {
			photoContainer2.addView(iv);
		}

		photos.add(iv);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			String sdCardState = Environment.getExternalStorageState();
			if (!sdCardState.equals(Environment.MEDIA_MOUNTED)) {

				return;

			} else {

				switch (requestCode) {
				case CAMERA_TAKE:
					Bitmap bitmap = BitmapFactory.decodeFile(PATH + "/"
							+ pic_name);
					DisplayMetrics dm = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(dm);
					float scale = bitmap.getWidth() / (float) dm.widthPixels;
					Bitmap newBitMap = null;
					if (scale > 1) {
						newBitMap = zoomBitmap(bitmap, bitmap.getWidth() / scale, bitmap.getHeight() / scale);
						bitmap.recycle();
						isBig = true;
					}

					ImageView img = new ImageView(this);

					img.setLayoutParams(new LayoutParams((screenWidth-10)/4, (screenWidth-10)/4));
					img.setScaleType(ImageView.ScaleType.CENTER_CROP);

					if(photos.size()<4) {
						img.setPadding(2, 0, 0, 2);
						if (isBig) {
							img.setImageBitmap(newBitMap);
							isBig = false;
						} else {
							img.setImageBitmap(bitmap);
						}
					}else{
						img.setPadding(2, 0, 0, 0);
						if (isBig) {
							img.setImageBitmap(newBitMap);
							isBig = false;
						} else {
							img.setImageBitmap(bitmap);
						}
					}

					img.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							photos.remove(v);
							photoContainer1.removeAllViews();
							photoContainer2.removeAllViews();
							for(int i=0; i<photos.size(); i++) {
								if (i < 4) {
									photoContainer1.addView(photos.get(i));

								} else if (i < 8) {
									photoContainer2.addView(photos.get(i));
								}
							}
						}
					});
					setPhotos(img);
					break;

				case CAMERA_SELECT:
					ContentResolver resolver_2 = getContentResolver();
					Uri imgUri_2 = data.getData();
					try {
						Bitmap photo = MediaStore.Images.Media.getBitmap(
								resolver_2, imgUri_2);

						DisplayMetrics dm_2 = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(dm_2);

						float scale_2 = photo.getWidth() / (float) dm_2.widthPixels;

						Bitmap newBitMap_2 = null;
						if (scale_2 > 1) {
							newBitMap_2 = zoomBitmap(photo, photo.getWidth() / scale_2, photo.getHeight() / scale_2);
							photo.recycle();
							isBig = true;
						}

						ImageView img_2 = new ImageView(this);

						img_2.setLayoutParams(new LayoutParams((screenWidth-10)/4, (screenWidth-10)/4));
						img_2.setScaleType(ImageView.ScaleType.CENTER_CROP);
						
						if (photos.size()<4) {
							img_2.setPadding(2, 0, 0, 2);
							if (scale_2 > 1) {
								img_2.setImageBitmap(newBitMap_2);
								isBig = false;
							} else {
								img_2.setImageBitmap(photo);
							}
						}else{
							img_2.setPadding(2, 0, 0, 0);
							if (scale_2 > 1) {
								img_2.setImageBitmap(newBitMap_2);
								isBig = false;
							} else {
								img_2.setImageBitmap(photo);
							}
						}
						System.out.println(photos.size()+"=============");
						img_2.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								photos.remove(v);
								photoContainer1.removeAllViews();
								photoContainer2.removeAllViews();
								for(int i=0; i<photos.size(); i++) {
									if (i < 4) {
										photoContainer1.addView(photos.get(i));

									} else if (i < 8) {
										photoContainer2.addView(photos.get(i));
									}
								}
							}
						});
						setPhotos(img_2);
					} catch (Exception e) {
						// TODO: handle exception
					}

					break;
				case Matter_Actvity:
					groupMatterString = data.getStringExtra("groupMatter");
					childMatterString = data.getStringExtra("childMatter");
					
					if (groupMatterString.length() != 0 && childMatterString.length() == 0) {
						int index = dynamic_edit.getSelectionStart();
						Editable editable = dynamic_edit.getText();
						editable.insert(index, "#"+groupMatterString+"#");
					}else if (groupMatterString.length() == 0 && childMatterString.length() != 0) {
						int index = dynamic_edit.getSelectionStart();
						Editable editable = dynamic_edit.getText();
						editable.insert(index, "#"+childMatterString+"#");
					}else {
						
					}
					break;
				default:
					break;
				}
			}

		}else if(resultCode == 3)
		{
			groupMatterString = data.getStringExtra("groupMatter");
			childMatterString = data.getStringExtra("childMatter");
			
			if (groupMatterString.length() != 0 && childMatterString.length() == 0) {
				int index = dynamic_edit.getSelectionStart();
				Editable editable = dynamic_edit.getText();
				editable.insert(index, "#"+groupMatterString+"#");  
			}else if (groupMatterString.length() == 0 && childMatterString.length() != 0) {
				int index = dynamic_edit.getSelectionStart();
				Editable editable = dynamic_edit.getText();
				editable.insert(index, "#"+childMatterString+"#");
			}else {
				
			}
		}else if(resultCode == 4)
		{
			atUserName = data.getStringArrayListExtra("atUserName");
			atUserID = data.getStringArrayListExtra("atUserID");
			
			if(!atUserName.isEmpty()) {
				String singleName;
				StringBuilder sb = new StringBuilder();
				
				for(int i=0; i<atUserName.size(); i++) {
					singleName = "@"+atUserName.get(i);
					sb.append(singleName);
					sb.toString();
				}
				int index = dynamic_edit.getSelectionStart();
				Editable editable = dynamic_edit.getText();
				editable.insert(index, sb);
			}
		}

	}

	public Bitmap zoomBitmap(Bitmap bitmap, float width, float height) {

		int w = bitmap.getWidth();

		int h = bitmap.getHeight();

		Matrix matrix = new Matrix();

		float scaleWidth = ((float) width / w);

		float scaleHeight = ((float) height / h);

		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

		return newbmp;

	}

}