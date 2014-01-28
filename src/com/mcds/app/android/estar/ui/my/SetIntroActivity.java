package com.mcds.app.android.estar.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.bean.My_FriendsDeclaration;
import com.mcds.app.android.estar.bean.My_SetIntro;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProvider;
import com.mcds.app.android.estar.provider.ConnectProviderCL;
import com.mcds.app.android.estar.ui.BaseTabActivity;

/**
 * 设置我的心情
 * @author chenliang
 *
 */
public class SetIntroActivity extends BaseTabActivity implements OnClickListener{
	private ImageButton my_setIntro_complete;
	private ImageButton my_setIntro_back;
	private ImageView my_document_cancel;
	private EditText my_edit_setIntro;
	private TextView my_txt_setIntroNum;
	private ReturnResult<My_SetIntro> rrFriendsDeclaration; 

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_setintro);
		my_setIntro_back = (ImageButton) findViewById(R.id.my_setIntro_back);
		my_setIntro_back.setOnClickListener(this);
		my_setIntro_complete = (ImageButton) findViewById(R.id.my_setIntro_complete);
		my_setIntro_complete.setOnClickListener(this);
		my_edit_setIntro = (EditText) findViewById(R.id.my_edit_setIntro);
		my_txt_setIntroNum = (TextView) findViewById(R.id.my_txt_setIntroNum);
		my_edit_setIntro.addTextChangedListener(mTextWatcher);
		
		
//		Intent getIntent =getIntent();
//		String friendString = getIntent.getStringExtra("friendString");
//		System.out.println(friendString);
//		my_edit_setIntro.setHint(friendString);
	}

    
    TextWatcher mTextWatcher = new TextWatcher() {  
        private CharSequence temp;  
        private int editStart ;  
        private int editEnd ;  
        @Override  
        public void onTextChanged(CharSequence s, int start, int before, int count) {  
            // TODO Auto-generated method stub  
             temp = s;  
             
        }  
          
        @Override  
        public void beforeTextChanged(CharSequence s, int start, int count,  
                int after) {  
            // TODO Auto-generated method stub  
//          my_txt_friendsDeclarationNum.setText(s);//将输入的内容实时显示  
        }  
          
        public void afterTextChanged(Editable s) {  
            // TODO Auto-generated method stub  
            editStart = my_edit_setIntro.getSelectionStart();  
            editEnd = my_edit_setIntro.getSelectionEnd();  
            my_txt_setIntroNum.setText("您输入了" + temp.length() + "个字符");  
            if (temp.length() > 120) {  
                Toast.makeText(SetIntroActivity.this,  
                        "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)  
                        .show();  
                s.delete(editStart-1, editEnd);  
                int tempSelection = editStart;  
                my_edit_setIntro.setText(s);  
                my_edit_setIntro.setSelection(tempSelection);  
            }  
        }

	 
    };  
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_setIntro_complete:
				new Thread(new Runnable() {
					 
					@Override 
					public void run() {
						showWaitingDialog();

						if (!UserManager.uid.equals("")) {
							rrFriendsDeclaration= ConnectProviderCL.setIntro(UserManager.uid, my_edit_setIntro.getText().toString());
						}
						
						if (rrFriendsDeclaration.status.equals("0")) {
							System.out.println("关注返回结果："+rrFriendsDeclaration.info);
							hideWaitingDialog();
							SetIntroActivity.this.runOnUiThread( new Runnable() {
								public void run() {
									Toast.makeText(getApplicationContext(), "恭喜您，设定成功",Toast.LENGTH_SHORT).show();  
									Intent intent= new Intent();
									intent.putExtra("123", my_edit_setIntro.getText().toString());
									setResult(111, intent);
									finish();
								}
							});
								
						}else {
							Toast.makeText(getApplicationContext(), "设置失败，请检查网络是否正常连接",Toast.LENGTH_SHORT).show();
						}
						hideWaitingDialog();
					
					}
				}).start();
				
			break;
		
		case R.id.my_setIntro_back:
			Intent intent= new Intent(SetIntroActivity.this,HomeActivity.class);
			onDestroy();
			startActivity(intent);
			break;
		
		default:
			break;
		}

	}
}

