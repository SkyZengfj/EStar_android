package com.mcds.app.android.estar.ui.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.AttentionSomeone;
import com.mcds.app.android.estar.bean.MyAttentionMain;
import com.mcds.app.android.estar.bean.MyInfo;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.ui.BaseTabActivity;
import com.mcds.app.android.estar.ui.XxxxActivity;
import com.mcds.app.android.estar.ui.my.attention.MyAttentionMainActivity;
import com.mcds.app.android.estar.ui.shop.GiftListActivity;
import com.mcds.app.android.estar.ui.shop.OrderListActivity;
import com.mcds.app.android.estar.ui.shop.ShopcarActivity;

public class HomeActivity extends BaseTabActivity implements OnClickListener {
	private RelativeLayout my_btn_accept,// 被认可
			my_btn_attention,// 关注
			my_btn_fans,// 粉丝
			my_btn_collect;// 收藏
	
	private LinearLayout
//						my_btn_getYes,//获得肯定
//						my_btn_sendYesCount,//发出肯定
			my_btn_dynamic,// 动态
			my_btn_remind,// 提醒
			my_btn_Societies,// 社团
			my_btn_findFriend,// 找朋友
			my_btn_integration,// 积分
			my_btn_rewards,// 积分换礼
			my_btn_shoppingCart,// 购物车
			my_btn_allOrders;// 全部订单
	private ReturnResult<MyInfo> rrMyInfo;
	private ImageView my_icon_homeedit;
	private ImageView my_icon_home_remind;
	private LinearLayout my_btn_homeSignature;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_home);
		initTabNavigate(R.id.myHome_tabNavigate, TAB_INDEX_MY);
		controlsInit();// 初始化控件
		controlsSetOnclick();// 为控件设置监听
		getData();
	}

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					rrMyInfo = ConnectProvider.getMyInfo(UserManager.uid);
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
				if (rrMyInfo.status.equals("0")) {
					System.out.println("返回状态" + rrMyInfo.info);

					MyInfo info = new MyInfo();
					info = rrMyInfo.getDatas().get(0);
					ImageView myHome_myAvatar = (ImageView) findViewById(R.id.myHome_myAvatar);
					TextView userNameTextView = (TextView) findViewById(R.id.myHome_userName);
					ImageView myHome_sexIcon = (ImageView) findViewById(R.id.myHome_sexIcon);
					TextView myHome_deptName = (TextView) findViewById(R.id.myHome_deptName);// 部门
					TextView myHome_puesto = (TextView) findViewById(R.id.myHome_puesto);// 职位
					TextView my_homeSignature = (TextView) findViewById(R.id.my_homeSignature);// 个性签名
					TextView my_attentionCount = (TextView) findViewById(R.id.my_attentionCount);// 关注人数
					TextView my_acceptCount = (TextView) findViewById(R.id.my_acceptCount);// 认可人数
					TextView my_fansCount = (TextView) findViewById(R.id.my_fansCount); // 粉丝数
					TextView my_favCount = (TextView) findViewById(R.id.my_favCount); // 收藏数
					TextView my_txtSendYesCount = (TextView) findViewById(R.id.my_txtSendYesCount);
					TextView my_txtGetYesCount = (TextView) findViewById(R.id.my_txtGetYesCount);
					TextView my_txtDynamicCount = (TextView) findViewById(R.id.my_txtDynamicCount);
					TextView my_txtRemind = (TextView) findViewById(R.id.my_txtRemind);
					TextView my_txtScoreAccess = (TextView) findViewById(R.id.my_txtScoreAccess);
					/*
					 * 获取用户头像
					 */
					AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
					Bitmap bmp = asyncLoader.loadBitmap(myHome_myAvatar, info.user_img, 200, 195, new ImageCallBack() {

						@Override
						public void imageLoad(ImageView imageView, Bitmap bitmap) {
							if (bitmap == null) {
								bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.my_icon_homeheadpic);
							}
							imageView.setImageBitmap(bitmap);
						}
					});
					// 设置用户头像
					if (bmp != null) {
						myHome_myAvatar.setImageBitmap(bmp);
					}

					userNameTextView.setText(info.user_name);// 设置用户名
					// 男女图标
					if (info.user_sex.equals("男")) {
						myHome_sexIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_sex_man));
					} else {
						myHome_sexIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_icon_sex_women));
					}
//					myHome_deptName.setText(info.user_work);
					myHome_puesto.setText(info.user_work);
					my_homeSignature.setText(info.user_mood);
					my_attentionCount.setText(info.attention_num);// 有疑问
					my_acceptCount.setText(info.approved_num); // 认可
					my_fansCount.setText(info.attentioned_num); //
					my_favCount.setText(info.dynamic_collection_num); //收藏
					my_txtSendYesCount.setText(info.send_praise_num);
					my_txtGetYesCount.setText(info.get_praise_num);
					my_txtDynamicCount.setText("共有" + info.weibo_num + "条动态");
					if (!info.remind_num.equals("0")) {
						my_icon_home_remind.setBackgroundResource(R.drawable.my_icon_remind_new);
						my_txtRemind.setText("共有" + info.remind_num + "条提醒");
					}
					
					my_txtScoreAccess.setText("可用积分" + info.score_access);
				}
			}
		});
	}

	/**
	 * 控件初始化
	 */
	private void controlsInit() {
		my_btn_homeSignature = (LinearLayout) findViewById(R.id.my_btn_homeSignature);
		my_icon_home_remind = (ImageView) findViewById(R.id.my_icon_home_remind);
		my_icon_homeedit = (ImageView) findViewById(R.id.my_icon_homeedit);

		my_btn_accept = (RelativeLayout) findViewById(R.id.my_btn_accept);// 被认可
		my_btn_attention = (RelativeLayout) findViewById(R.id.my_btn_attention);// 关注
		my_btn_fans = (RelativeLayout) findViewById(R.id.my_btn_fans);// 粉丝
		my_btn_collect = (RelativeLayout) findViewById(R.id.my_btn_collect);
//		my_btn_getYes = (LinearLayout) findViewById(R.id.my_btn_getYes);//获得肯定
//		my_btn_sendYesCount = (LinearLayout) findViewById(R.id.my_btn_sendYesCount);//发出肯定
		my_btn_dynamic = (LinearLayout) findViewById(R.id.my_btn_dynamic); // 动态
		my_btn_remind = (LinearLayout) findViewById(R.id.my_btn_remind);// 提醒
		my_btn_Societies = (LinearLayout) findViewById(R.id.my_btn_Societies);// 社团
		my_btn_findFriend = (LinearLayout) findViewById(R.id.my_btn_findFriend); // 找朋友
		my_btn_integration = (LinearLayout) findViewById(R.id.my_btn_integration);// 积分
		my_btn_rewards = (LinearLayout) findViewById(R.id.my_btn_rewards);// 积分换礼
		my_btn_shoppingCart = (LinearLayout) findViewById(R.id.my_btn_shoppingCart);// 购物车
		my_btn_allOrders = (LinearLayout) findViewById(R.id.my_btn_allOrders);// 全部订单
	}

	/**
	 * 为控件设置监听
	 */
	private void controlsSetOnclick() {
		my_btn_homeSignature.setOnClickListener(this);
		my_icon_homeedit.setOnClickListener(this);
		my_btn_accept.setOnClickListener(this);
		my_btn_attention.setOnClickListener(this);// 关注
		my_btn_fans.setOnClickListener(this);// 粉丝
		my_btn_collect.setOnClickListener(this);// 收藏
//		my_btn_getYes.setOnClickListener(this);//获得肯定
		findViewById(R.id.my_btn_getYes).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intentGetYes = new Intent(HomeActivity.this, MyWeiboPraisedActivity.class);
				intentGetYes.putExtra("type", 2);
				startActivity(intentGetYes);
			}
		});
//		my_btn_sendYesCount.setOnClickListener(this);//发出肯定
		findViewById(R.id.my_btn_sendYesCount).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentSendYesCount = new Intent(HomeActivity.this, MyWeiboPraisedActivity.class);
				intentSendYesCount.putExtra("type", 6);
				startActivity(intentSendYesCount);
			}
		});
		my_btn_dynamic.setOnClickListener(this);
		my_btn_remind.setOnClickListener(this);
		my_btn_Societies.setOnClickListener(this);
		my_btn_findFriend.setOnClickListener(this);
		my_btn_integration.setOnClickListener(this);
		my_btn_rewards.setOnClickListener(this);
		my_btn_shoppingCart.setOnClickListener(this);
		my_btn_allOrders.setOnClickListener(this);
		findViewById(R.id.myHome_myAvatar).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentDocument = new Intent(HomeActivity.this, DocumentActivity.class);
				startActivity(intentDocument);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 111) {
			String getrrString = data.getStringExtra("123");
			((TextView) findViewById(R.id.my_homeSignature)).setText(getrrString);
		}
		
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_btn_homeSignature:
			Intent intentSignature = new Intent(HomeActivity.this,SetIntroActivity.class);
//			startActivity(intentSignature);
			startActivityForResult(intentSignature, 111);
			break;
//		my_btn_sendYesCount;//发出肯定
			case R.id.my_btn_accept:
			Intent intentAccept = new Intent(HomeActivity.this,AcceptActivity.class);
//				Intent intentAccept = new Intent(HomeActivity.this, XxxxActivity.class);
				startActivity(intentAccept);
				break;
			case R.id.my_btn_attention:
//				Intent intentAttention = new Intent(HomeActivity.this, MyAttentionMainActivity.class);
			Intent intentAttention = new Intent(HomeActivity.this,AttentionActivity.class);
				startActivity(intentAttention);
				break;
			case R.id.my_btn_fans:
				Intent intentFans = new Intent(HomeActivity.this, FansActivity.class);
				startActivity(intentFans);
				break;
			case R.id.my_btn_collect:
				Intent ine = new Intent(getApplicationContext(), MyAtMeActivity.class);
				ine.putExtra("type", 2);//提到我的动态
				startActivity(ine);
				break;
//		case R.id.my_btn_document:
//			Intent intentDocument = new Intent(HomeActivity.this,DocumentActivity.class);
//			startActivity(intentDocument);
//			break;
//		case R.id.my_btn_getYes:
//			Intent intentGetYes = new Intent(HomeActivity.this,GetYesActivity.class);
//			startActivity(intentGetYes);	
//			break;
//		case R.id.my_btn_sendYesCount:
//			Intent intentSendYesCount = new Intent(HomeActivity.this,SendYesCountActivity.class);
//			startActivity(intentSendYesCount);
//			break;

			case R.id.my_btn_dynamic:
				Intent myin = new Intent(getApplicationContext(), MyAtMeActivity.class);
				myin.putExtra("type", 0);//动态
				startActivity(myin);
				break;
			case R.id.my_btn_remind:
				Intent intentRemind = new Intent(HomeActivity.this, RemindActivity.class);
				startActivity(intentRemind);
				break;
			case R.id.my_btn_Societies:
				Intent intentSocieties = new Intent(HomeActivity.this, SocietiesActivity.class);
				startActivity(intentSocieties);
				break;
			case R.id.my_btn_findFriend:
				Intent intentFindFriend = new Intent(HomeActivity.this, FindFriendActivity.class);
				startActivity(intentFindFriend);
				break;
			case R.id.my_btn_integration:
				Intent intentIntegration = new Intent(HomeActivity.this, IntegrationActivity.class);
				startActivity(intentIntegration);
				break;
			case R.id.my_btn_rewards:
//			Intent intentRewards = new Intent(HomeActivity.this,RewardsActivity.class);
				Intent intentRewards = new Intent(HomeActivity.this, GiftListActivity.class);
				startActivity(intentRewards);
				break;
			case R.id.my_btn_shoppingCart:
//			Intent intentShoppingCarts = new Intent(HomeActivity.this,ShoppingCartActivity.class);
				Intent intentShoppingCarts = new Intent(HomeActivity.this, ShopcarActivity.class);
				startActivity(intentShoppingCarts);
				break;
			case R.id.my_btn_allOrders:
//			Intent intentAllOrders = new Intent(HomeActivity.this,AllOrdersActivity.class);
				Intent intentAllOrders = new Intent(HomeActivity.this, OrderListActivity.class);
				startActivity(intentAllOrders);
				break;
			default:
				break;
		}
	}

}
