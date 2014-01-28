//package com.mcds.app.android.estar.ui.work;
//
//import android.graphics.PixelFormat;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.ExpandableListView;
//import android.widget.ExpandableListView.OnChildClickListener;
//
//import com.mcds.app.android.estar.R;
//import com.mcds.app.android.estar.bean.MyAttentionMemberList;
//import com.mcds.app.android.estar.bean.ReturnResult;
//import com.mcds.app.android.estar.manager.UserManager;
//import com.mcds.app.android.estar.provider.ConnectProviderTC;
//import com.mcds.app.android.estar.ui.BaseActivity;
//
///**
// * 成员列表
// * 
// * @author TangChao
// */
//public class MatterMemberListActivity extends BaseActivity implements OnChildClickListener
//{
//	private ExpandableListView eListView;
//	private MyExpandableLVAdapyet eLVAdapter;
//	private ReturnResult<MyAttentionMemberList> rrMyAttentionMemberList;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		getWindow().setFormat(PixelFormat.RGBA_8888);
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//		setContentView(R.layout.work_home_matter_member_list);
//		eListView = (ExpandableListView) findViewById(R.id.eListView);
//		eLVAdapter = new MyExpandableLVAdapyet();
//		eListView.setAdapter(eLVAdapter);
//		eListView.setOnChildClickListener(this);
//	}
//	/**
//	 * 请求关注的成员列表
//	 */
//	private void getMyAttention_memberList()
//	{
//		new Thread(new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				showWaitingDialog();
//				if (!UserManager.uid.equals(""))
//				{
//					rrMyAttentionMemberList = ConnectProviderTC.getMyAttentionMemberList(UserManager.uid);
//				}
//				hideWaitingDialog();
//			}
//		}).start();
//	}
//	
//	/**
//	 * 二级列表适配器
//	 * 
//	 * @author TangChao
//	 */
//	private class MyExpandableLVAdapyet extends BaseExpandableListAdapter
//	{
//		@Override
//		public Object getChild(int groupPosition, int childPosition)
//		{
//			// TODO Auto-generated method stub
//			return null;
//		}
//		@Override
//		public long getChildId(int groupPosition, int childPosition)
//		{
//			// TODO Auto-generated method stub
//			return 0;
//		}
//		@Override
//		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
//		{
//			// TODO Auto-generated method stub
//			return null;
//		}
//		@Override
//		public int getChildrenCount(int groupPosition)
//		{
//			// TODO Auto-generated method stub
//			return 0;
//		}
//		@Override
//		public Object getGroup(int groupPosition)
//		{
//			// TODO Auto-generated method stub
//			return null;
//		}
//		@Override
//		public int getGroupCount()
//		{
//			// TODO Auto-generated method stub
//			return 0;
//		}
//		@Override
//		public long getGroupId(int groupPosition)
//		{
//			// TODO Auto-generated method stub
//			return 0;
//		}
//		@Override
//		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
//		{
//			// TODO Auto-generated method stub
//			return null;
//		}
//		@Override
//		public boolean hasStableIds()
//		{
//			// TODO Auto-generated method stub
//			return false;
//		}
//		@Override
//		public boolean isChildSelectable(int groupPosition, int childPosition)
//		{
//			// TODO Auto-generated method stub
//			return false;
//		}
//	}
//	
//	@Override
//	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
//	{
//		// TODO Auto-generated method stub
//		return false;
//	}
//}
