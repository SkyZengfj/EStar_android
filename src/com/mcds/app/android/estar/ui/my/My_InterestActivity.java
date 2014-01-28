package com.mcds.app.android.estar.ui.my;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.simonvt.menudrawer.RightDrawer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.My_SetIntrest;
import com.mcds.app.android.estar.bean.My_SetWeiBo;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.ui.BaseTabActivity;

/**
 * 设置我的兴趣爱好
 * @author chenliang
 *
 */
public class My_InterestActivity extends BaseTabActivity implements OnClickListener {
	private ImageButton my_document_setIntrest_back;
	private ImageButton my_btn_setIntrest_complete;
	private TextView my_interest_num1, my_interest_num2, my_interest_num3, my_interest_num4, my_interest_num5;
	private ReturnResult<My_SetIntrest> rrInterest;
	private LinearLayout my_set_intrest_center;
	private String[] setIntetrestArr;

	private List<String> selectedInteres;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.my_set_intrest);

		setIntetrestArr = DocumentActivity.my_Str_interestArray;
		selectedInteres = new ArrayList<String>();
		for (int i = 0; i < setIntetrestArr.length; i++) {
			selectedInteres.add(setIntetrestArr[i]);
			System.out.println("得到的兴趣爱好："+setIntetrestArr[i]);
		}
	

		my_document_setIntrest_back = (ImageButton) findViewById(R.id.my_document_setIntrest_back);
		my_document_setIntrest_back.setOnClickListener(this);
		my_btn_setIntrest_complete = (ImageButton) findViewById(R.id.my_btn_setIntrest_complete);
		my_btn_setIntrest_complete.setOnClickListener(this);
		my_set_intrest_center = (LinearLayout) findViewById(R.id.my_set_intrest_center);

		my_interest_num1 = (TextView) findViewById(R.id.my_interest_num1);
		my_interest_num2 = (TextView) findViewById(R.id.my_interest_num2);
		my_interest_num3 = (TextView) findViewById(R.id.my_interest_num3);
		my_interest_num4 = (TextView) findViewById(R.id.my_interest_num4);
		my_interest_num5 = (TextView) findViewById(R.id.my_interest_num5);
		
		my_interest_num1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				removeInterestText(((TextView) v).getText().toString());
				
			}
		});
		my_interest_num2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				removeInterestText(((TextView) v).getText().toString());
			}
		});
		my_interest_num3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				removeInterestText(((TextView) v).getText().toString());
			}
		});
		my_interest_num4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				removeInterestText(((TextView) v).getText().toString());
			}
		});
		my_interest_num5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				removeInterestText(((TextView) v).getText().toString());
			}
		});

		LinearLayout intrestContainer1 = (LinearLayout) findViewById(R.id.mySetIntrest_intrestContainer1);
		for (int i = 0; i < intrestContainer1.getChildCount(); i++) {
			View view = intrestContainer1.getChildAt(i);
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					TextView tv = (TextView) arg0;
					if (tv != null) {
						setInterestText(tv.getText().toString());
					}
				}
			});
		}
		LinearLayout intrestContainer2 = (LinearLayout) findViewById(R.id.mySetIntrest_intrestContainer2);
		for (int i = 0; i < intrestContainer2.getChildCount(); i++) {
			View view = intrestContainer2.getChildAt(i); 
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					TextView tv = (TextView) arg0;
					if (tv != null) {
						setInterestText(tv.getText().toString());
					}
				}
			});
		}
		LinearLayout intrestContainer3 = (LinearLayout) findViewById(R.id.mySetIntrest_intrestContainer3);
		for (int i = 0; i < intrestContainer3.getChildCount(); i++) {
			View view = intrestContainer3.getChildAt(i);
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					TextView tv = (TextView) arg0;
					if (tv != null) {
						setInterestText(tv.getText().toString());
					}
				}
			});
		}

		showSelectedInterest();
	}

	private void setInterestText(String text) {
		if (selectedInteres.size() >= 5) {
			return;
		}

		if (!selectedInteres.contains(text)) {
			selectedInteres.add(text);

			showSelectedInterest();
		}
	}

	private void removeInterestText(String text) {
		if (selectedInteres.contains(text)) {
			selectedInteres.remove(text);

			showSelectedInterest();
		}
	}

	private void showSelectedInterest() {
		my_interest_num1.setVisibility(View.INVISIBLE);
		my_interest_num2.setVisibility(View.INVISIBLE);
		my_interest_num3.setVisibility(View.INVISIBLE);
		my_interest_num4.setVisibility(View.INVISIBLE);
		my_interest_num5.setVisibility(View.INVISIBLE);

		((TextView) findViewById(R.id.mySetIntrest_txtSelectedCount)).setText(String.valueOf(selectedInteres.size()));

		for (int i = 0; i < selectedInteres.size(); i++) {
			switch (i) {
				case 0:
					my_interest_num1.setText(selectedInteres.get(0));
					my_interest_num1.setVisibility(View.VISIBLE);
					break;
				case 1:
					my_interest_num2.setText(selectedInteres.get(1));
					my_interest_num2.setVisibility(View.VISIBLE);
					break;
				case 2:
					my_interest_num3.setText(selectedInteres.get(2));
					my_interest_num3.setVisibility(View.VISIBLE);
					break;
				case 3:
					my_interest_num4.setText(selectedInteres.get(3));
					my_interest_num4.setVisibility(View.VISIBLE);
					break;
				case 4:
					my_interest_num5.setText(selectedInteres.get(4));
					my_interest_num5.setVisibility(View.VISIBLE);
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.my_document_setIntrest_back:
				Intent intentBack = new Intent(My_InterestActivity.this, DocumentActivity.class);
				startActivity(intentBack);
				break;
			case R.id.my_btn_setIntrest_complete:
				new Thread(new Runnable() {

					@Override
					public void run() {
						showWaitingDialog();

						if (!UserManager.uid.equals("")) {
							
							String sendInterestString = "";
//							sendInterestString = my_interest_num1.getText().toString()+","+
//												my_interest_num2.getText().toString()+","+
//												my_interest_num3.getText().toString()+","+
//												my_interest_num4.getText().toString()+","+
//												my_interest_num5.getText().toString();
							
							for (int i = 0; i < selectedInteres.size(); i++) {
								 
								sendInterestString+=selectedInteres.get(i)+",";
							}
							
							System.out.println("将要设置的的兴趣是："+sendInterestString);
							System.out.println("数组里边的值为"+selectedInteres.toString());
							
							rrInterest = ConnectProvider.setIntrest(UserManager.uid,selectedInteres.toString());
							
						}
						if (rrInterest.status.equals("0")) {
//							hideWaitingDialog();
//							My_InterestActivity.this.runOnUiThread(new Runnable() {
//								
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									Toast.makeText(getApplicationContext(), "恭喜您，设定成功",Toast.LENGTH_SHORT).show();  
//									Intent intent= new Intent();
//									intent.putExtra("444", my_edit_setFriendsDeclaration.getText().toString());
//									setResult(444, intent);
//									finish();
//								}
//							});
							
//							if (rrSetPersonalAddress.info.equals("ok")) {
//								Toast.makeText(SetPersonalAddressActivity.this, "个性域名设定成功",Toast.LENGTH_SHORT).show();  
//								Intent intent= new Intent(SetPersonalAddressActivity.this,DocumentActivity.class);
//								startActivity(intent);
//							}
						}
						
						
						hideWaitingDialog();

					}
				}).start();

				break;

//			case R.id.my_interest_num1:
//				my_interest_num1.setText("");
//				my_interest_num1.setVisibility(View.INVISIBLE);
//				break;

			default:
				break;
		}
	}

}
