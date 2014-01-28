package com.mcds.app.android.estar.ui.my;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.ui.weibo.ContactActivity;

/**
 * 查看社团信息
 * 
 * @author TangChao
 */
public class SocietiesInfoActivity extends BaseActivity implements OnClickListener
{
	/**
	 * 社团ID
	 */
	private String club_id;
	/**
	 * 取消
	 */
	private ImageButton cancelBtn;
	/**
	 * 公告
	 */
	private TextView noticeBtn;
	/**
	 * 相册
	 */
	// private TextView albumBtn;
	/**
	 * 共享
	 */
	// private TextView shareBtn;
	/**
	 * 群成员
	 */
	private TextView memberBtn;
	/**
	 * 邀请成员
	 */
	private TextView inviteMemberBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.my_societies_info);
		initWidget();
		club_id = getIntent().getStringExtra("club_id");
		Log.i("club_id", club_id);
	}
	private void initWidget()
	{
		cancelBtn = (ImageButton) findViewById(R.id.my_societies_info_cancel);
		noticeBtn = (TextView) findViewById(R.id.societies_info_notice);
		// albumBtn = (TextView) findViewById(R.id.societies_info_blbum);
		// shareBtn = (TextView) findViewById(R.id.societies_info_share);
		memberBtn = (TextView) findViewById(R.id.societies_info_member);
		inviteMemberBtn = (TextView) findViewById(R.id.societies_info_invite_member);
		cancelBtn.setOnClickListener(this);
		noticeBtn.setOnClickListener(this);
		// albumBtn.setOnClickListener(this);
		// shareBtn.setOnClickListener(this);
		memberBtn.setOnClickListener(this);
		inviteMemberBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.my_societies_info_cancel:// 取消
				finish();
				break;
			case R.id.societies_info_notice:// 公告
				Intent intentNotice = new Intent(this, SocitiesNoticeActivity.class);
				startActivity(intentNotice);
				break;
			// case R.id.societies_info_blbum:// 相册
			// Intent intentAlbum = new Intent(this,
			// SocitiesAlbumActivity.class);
			// startActivity(intentAlbum);
			// break;
			// case R.id.societies_info_share:// 共享
			// Intent intentShare = new Intent(this,
			// SocitiesShareActivity.class);
			// startActivity(intentShare);
			// break;
			case R.id.societies_info_member:// 社团成员
				Intent intentSocieties = new Intent(this, SocietiesMemberListActivity.class);
				intentSocieties.putExtra("club_id", club_id);
				startActivity(intentSocieties);
				break;
			case R.id.societies_info_invite_member:// 邀请成员
//				Intent intentInviteMember = new Intent(this, MatterMemberListActivity.class);
				Intent intentInviteMember = new Intent(this, ContactActivity.class);
				startActivity(intentInviteMember);
				break;
			default:
				break;
		}
	}
}
