package com.mcds.app.android.estar.ui.work;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.MyGridAdapter;
import com.mcds.app.android.estar.bean.MatterMemberList;
import com.mcds.app.android.estar.bean.OperateButton;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderTC;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 事项成员
 * 
 * @author TangChao
 */
public class MemberActivity extends BaseActivity
{
	private String theme_id;
	private GridView gridView;
	private ReturnResult<MatterMemberList> rrMember;
	private ReturnResult<String> rrAddThemMemberResult;
	private ReturnResult<String> rrDeleteThemeMemberResult;
	private ImageView deleteBtn;
	private List<MatterMemberList> listMember;
	private List<OperateButton> listOperate;
	/**
	 * 适配器数据源
	 */
	private ArrayList<Object> arrayList;
	private MyGridAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.work_home_matter_member);
		/*-------------------------------------------*/
		deleteBtn = (ImageView) findViewById(R.id.adapter_item_delete_btn);
		gridView = (GridView) findViewById(R.id.work_home_matter_member_list);
		theme_id = getIntent().getStringExtra("theme_id");
		/*-------------------------------------------------*/
		listOperate = new ArrayList<OperateButton>();
		OperateButton operateBtn = new OperateButton();
		operateBtn.addBtn = BitmapFactory.decodeResource(getResources(), R.drawable.add);
		operateBtn.deleteBtn = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
		listOperate.add(operateBtn);
		arrayList = new ArrayList<Object>();
		adapter = new MyGridAdapter(arrayList, MemberActivity.this);
		gridView.setAdapter(adapter);
		/*------------------------------------------------*/
		findViewById(R.id.work_home_matter_member_cancel).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		getDate(theme_id);
	}
	/**
	 * 获取成员列表
	 */
	private void getDate(final String theme_id)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!UserManager.uid.equals(""))
				{
					rrMember = ConnectProviderTC.getThemeMemberList(UserManager.uid, theme_id);
					listMember = rrMember.getDatas();
					doUI(listMember);
				}
				hideWaitingDialog();
			}
		}).start();
	}
	// /**
	// * 新增成员
	// */
	// private void addMember(final String theme_id, final String member_id)
	// {
	// new Thread(new Runnable()
	// {
	// @Override
	// public void run()
	// {
	// if (!UserManager.uid.equals(""))
	// {
	// rrAddThemMemberResult = ConnectProviderTC.addThemeMember(UserManager.uid,
	// theme_id, member_id);
	// // doUI();
	// }
	// }
	// }).start();
	// }
	// /**
	// * 删除成员
	// */
	// private void deleteMember(final String theme_id, final String member_id)
	// {
	// new Thread(new Runnable()
	// {
	// @Override
	// public void run()
	// {
	// showWaitingDialog();
	// if (!UserManager.uid.equals(""))
	// {
	// rrDeleteThemeMemberResult =
	// ConnectProviderTC.deleteThemeMember(UserManager.uid, theme_id,
	// member_id);
	// }
	// hideWaitingDialog();
	// }
	// }).start();
	// }
	/**
	 * 刷新界面
	 * 
	 * @author TangChao
	 */
	private void doUI(final List<?> list)
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				Log.i("sizeA---------->", list.size() + "");
				Log.i("sizeB---------->", listOperate.size() + "");
				arrayList.add(listOperate);
				arrayList.add(list);
				Log.i("sizeC---------->", arrayList.size() + "");
				adapter.notifyDataSetChanged();
			}
		});
	}
}
