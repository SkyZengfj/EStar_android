package com.mcds.app.android.estar.ui.work;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.AllContactList;
import com.mcds.app.android.estar.bean.GetContactsGroupChildsInfo;
import com.mcds.app.android.estar.bean.GetContactsGroupInfo;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader.ImageCallBack;
import com.mcds.app.android.estar.provider.ConnectProviderGC;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.ui.weibo.ContactActivity;
import com.mcds.app.android.estar.ui.weibo.HomeListDynamicEditActivity;
import com.mcds.app.android.estar.util.Utility;

public class WorkMemberListActivity extends BaseActivity
{
	private ListView all_contacts;
	private ExpandableListView attention_contacts;
	private LinearLayout avatar_list;
	private RelativeLayout avatar_list_relativeLayout;
	private TextView attention_contacts_textView;
	private TextView all_contacts_textView;
	private TextView compeleteSelect;
	private boolean contactTypeFlag = false;
	private AllContactListAdapter allContactAdapter;
	private ContactExpandableListAdapter attentionlContactAdapter;
	private ReturnResult<AllContactList> result_AllContactList;
	private ReturnResult<GetContactsGroupInfo> result_GetContactsGroupInfo;
	private ReturnResult<GetContactsGroupChildsInfo> result_GetContactsGroupChildsInfo;
	private List<GetContactsGroupInfo> gcgiList;
	private List<List<GetContactsGroupChildsInfo>> childList;
	private ArrayList<String> userID;
	private ArrayList<String> userName;
	private Map user_img_map;
	private final String PAGE_SIZE = "10";
	private int page_num = 0;
	private int usertype = 0;//0:负责人;1：表示成员
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_contact_list_layout);
		usertype = getIntent().getIntExtra("usertype", 0);
		gcgiList = new ArrayList<GetContactsGroupInfo>();
		childList = new ArrayList<List<GetContactsGroupChildsInfo>>();
		user_img_map = new HashMap<String, List<Object>>();
		userID = new ArrayList<String>();
		userName = new ArrayList<String>();
		avatar_list = (LinearLayout) findViewById(R.id.weiboHome_contact_selected_avatar_list);
		avatar_list_relativeLayout = (RelativeLayout) findViewById(R.id.weiboHome_contact_selected_avatar_list_relativeLayout);
		attention_contacts_textView = (TextView) findViewById(R.id.weiboHome_imageView_contact_attention);
		all_contacts_textView = (TextView) findViewById(R.id.weiboHome_imageView_contact_all);
		compeleteSelect = (TextView) findViewById(R.id.weiboHome_contact_select_complete_btn);
		all_contacts = (ListView) findViewById(R.id.weiboHome_all_contact_list);
		allContactAdapter = new AllContactListAdapter();
		all_contacts.setAdapter(allContactAdapter);
		attention_contacts = (ExpandableListView) findViewById(R.id.weiboHome_attention_contact_list);
		attention_contacts.setDivider(getResources().getDrawable(R.drawable.expandablelistview_divider));
		attention_contacts.setChildDivider(getResources().getDrawable(R.drawable.expandablelistview_divider));
		attention_contacts.setGroupIndicator(null);
		attentionlContactAdapter = new ContactExpandableListAdapter(this);
		attention_contacts.setAdapter(attentionlContactAdapter);
		attention_contacts.setOnGroupClickListener(new OnGroupClickListener()
		{
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
			{
				// TODO Auto-generated method stub
				return false;
			}
		});
		attention_contacts.setOnChildClickListener(new OnChildClickListener()
		{
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2, int arg3, long arg4)
			{
				// TODO Auto-generated method stub
				return false;
			}
		});
		all_contacts.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				// TODO Auto-generated method stub
			}
		});
		// add data when the slide in the end portion of list view
		all_contacts.setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				// TODO Auto-generated method stub
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE)
				{
					if (view.getLastVisiblePosition() == view.getCount() - 1)
					{
						page_num = page_num + 1;
						getAllContactData();
						allContactAdapter.notifyDataSetChanged();
					}
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				// TODO Auto-generated method stub
			}
		});
		compeleteSelect.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(WorkMemberListActivity.this, AddMatterActivity.class);
				intent.putStringArrayListExtra("atUserID", userID);
				intent.putStringArrayListExtra("userName", userName);
				if (usertype == 0) {//负责人
					if (user_img_map.size()!=1) {
						Toast.makeText(WorkMemberListActivity.this, "请选择一个负责人", Toast.LENGTH_SHORT).show();
					}else {
						setResult(101, intent);
						finish();
					}
				}else if (usertype == 1) {//成员
					setResult(201, intent);
					finish();
				}
				
			}
		});
		attention_contacts_textView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				if (contactTypeFlag)
				{
					attention_contacts_textView.setBackgroundResource(R.drawable.contact_select_left_1);
					all_contacts_textView.setBackgroundResource(R.drawable.contact_select_right_2);
					all_contacts.setVisibility(LinearLayout.GONE);
					attention_contacts.setVisibility(LinearLayout.VISIBLE);
					contactTypeFlag = false;
				}
			}
		});
		all_contacts_textView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (!contactTypeFlag)
				{
					attention_contacts_textView.setBackgroundResource(R.drawable.contact_select_left_2);
					all_contacts_textView.setBackgroundResource(R.drawable.contact_select_right_1);
					attention_contacts.setVisibility(LinearLayout.GONE);
					all_contacts.setVisibility(LinearLayout.VISIBLE);
					contactTypeFlag = true;
				}
			}
		});
		getAttentionContactData();
		getAllContactData();
	}
	private void getAllContactData()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!UserManager.uid.equals(""))
				{
					result_AllContactList = ConnectProviderGC.getAllWeiboersList(UserManager.uid, String.valueOf(page_num),
							PAGE_SIZE);
				}
				doAttentionContactListViewUI();
				doAllContactListViewUI();
				hideWaitingDialog();
			}
		}).start();
	}
	private void getAttentionContactData()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!UserManager.uid.equals(""))
				{
					result_GetContactsGroupInfo = ConnectProviderGC.getMyGroupList(UserManager.uid);
					gcgiList = result_GetContactsGroupInfo.getDatas();
					if (!gcgiList.isEmpty())
					{
						for (int i = 0; i < gcgiList.size(); i++)
						{
							getChildInfoData();
						}
					}
				}
				hideWaitingDialog();
			}
		}).start();
	}
	private void getChildInfoData()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				for (int i = 0; i < gcgiList.size(); i++)
				{
					result_GetContactsGroupChildsInfo = ConnectProviderGC.getGroupMembers(UserManager.uid,
							gcgiList.get(i).group_id);
					List<GetContactsGroupChildsInfo> gcgciList = new ArrayList<GetContactsGroupChildsInfo>();
					gcgciList = result_GetContactsGroupChildsInfo.getDatas();
					childList.add(gcgciList);
				}
				hideWaitingDialog();
			}
		}).start();
	}
	private void doAllContactListViewUI()
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if (result_AllContactList.status.equals("0"))
				{
					allContactAdapter.addItems(result_AllContactList.getDatas());
					allContactAdapter.notifyDataSetChanged();
				}
			}
		});
	}
	private void doAttentionContactListViewUI()
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				getAttentionContactData();
			}
		});
	}
	
	private class AllContactListAdapter extends BaseListAdapter<AllContactList>
	{
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder = null;
			if (convertView == null)
			{
				convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.weibo_contact_list_item_layout, null);
				holder = new ViewHolder();
				holder.contact_select = (ImageView) convertView.findViewById(R.id.contact_list_imagebutton_select_mark);
				holder.user_img = (ImageView) convertView.findViewById(R.id.contact_list_imageview_avatar_pic);
				holder.userName = (TextView) convertView.findViewById(R.id.weiboHome_contact_name);
				holder.userPost = (TextView) convertView.findViewById(R.id.weiboHome_contact_post);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			if (holder != null)
			{
				AllContactList item = this.getItem(position);
				// set user name value
				holder.userName.setText(item.user_name);
				// set send work value
				holder.userPost.setText(item.user_work);
				// set contact selected icon
				if (item.isSelected.equals("0"))
				{
					holder.contact_select.setBackgroundResource(R.drawable.contact_image_button_unselected_mark_icon);
				}
				else
				{
					holder.contact_select.setBackgroundResource(R.drawable.contact_image_button_selected_mark_icon);
				}
				// SyncLoader user's avatar_pic
				Bitmap bmp = asyncLoader.loadBitmap(holder.user_img, item.user_img,
						Utility.dip2px(WorkMemberListActivity.this, (float) 60.0),
						Utility.dip2px(WorkMemberListActivity.this, (float) 60.0), new ImageCallBack()
						{
							@Override
							public void imageLoad(ImageView imageView, Bitmap bitmap)
							{
								if (bitmap == null)
								{
									bitmap = BitmapFactory.decodeResource(getResources(),
											R.drawable.main_frame_listview_default_avatar);
								}
								imageView.setImageBitmap(bitmap);
							}
						});
				if (bmp != null)
				{
					holder.user_img.setImageBitmap(bmp);
				}
				holder.contact_select.setTag(R.id.all_contact_select_tag_0, item);
				holder.contact_select.setTag(R.id.all_contact_select_tag_1, holder);
				holder.contact_select.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						// TODO Auto-generated method stub
						AllContactList acl = (AllContactList) view.getTag(R.id.all_contact_select_tag_0);
						ViewHolder holder = (ViewHolder) view.getTag(R.id.all_contact_select_tag_1);
						ImageView iv = new ImageView(WorkMemberListActivity.this);
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						lp.leftMargin = 15;
						lp.topMargin = 15;
						lp.bottomMargin = 15;
						lp.gravity = Gravity.CENTER_VERTICAL;
						iv.setLayoutParams(lp);
						iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
						// SyncLoader user's avatar_pic
						Bitmap bmp = asyncLoader.loadBitmap(iv, acl.user_img,
								Utility.dip2px(WorkMemberListActivity.this, (float) 60.0),
								Utility.dip2px(WorkMemberListActivity.this, (float) 60.0), new ImageCallBack()
								{
									@Override
									public void imageLoad(ImageView imageView, Bitmap bitmap)
									{
										if (bitmap == null)
										{
											bitmap = BitmapFactory.decodeResource(getResources(),
													R.drawable.main_frame_listview_default_avatar);
										}
										imageView.setImageBitmap(bitmap);
									}
								});
						if (bmp != null)
						{
							iv.setImageBitmap(bmp);
						}
						
						if (acl.isSelected.equals("0"))//已选中
						{
							holder.contact_select.setBackgroundResource(R.drawable.contact_image_button_selected_mark_icon);
							List<Object> atInfo = new ArrayList<Object>();
							atInfo.add(acl.user_name);
							atInfo.add(iv);
							user_img_map.put(acl.uid, atInfo);
							acl.isSelected = "1";
						}
						else
						{
							holder.contact_select.setBackgroundResource(R.drawable.contact_image_button_unselected_mark_icon);
							user_img_map.remove(acl.uid);
							acl.isSelected = "0";
						}
						// avatar_list.addView(iv);
						if (user_img_map.isEmpty())
						{
							avatar_list_relativeLayout.setVisibility(LinearLayout.GONE);
						}
						else
						{
							avatar_list.removeAllViews();
							userID.clear();
							userName.clear();
							Iterator it = user_img_map.entrySet().iterator();
							while (it.hasNext())
							{
								Map.Entry entry = (Map.Entry) it.next();
								userID.add((String) entry.getKey());
								List<Object> ui = (List<Object>) entry.getValue();
								userName.add((String) ui.get(0));
								avatar_list.addView((View) ui.get(1));
								compeleteSelect.setText("完成(" + user_img_map.size() + ")");
							}
							avatar_list_relativeLayout.setVisibility(LinearLayout.VISIBLE);
						}
					}
				});
			}
			return convertView;
		}
	}
	
	private class ContactExpandableListAdapter extends BaseExpandableListAdapter
	{
		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();
		private Context ctx;
		
		public ContactExpandableListAdapter(Context ctx)
		{
			super();
			this.ctx = ctx;
		}
		@Override
		public Object getChild(int groupPosition, int childPosition)
		{
			// TODO Auto-generated method stub
			return childList.get(groupPosition).get(childPosition);
		}
		@Override
		public long getChildId(int groupPosition, int childPosition)
		{
			// TODO Auto-generated method stub
			return childPosition;
		}
		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null)
			{
				holder = new ViewHolder();
				LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.weibo_contact_list_item_layout, null);
				holder.userName = (TextView) convertView.findViewById(R.id.weiboHome_contact_name);
				holder.userPost = (TextView) convertView.findViewById(R.id.weiboHome_contact_post);
				holder.user_img = (ImageView) convertView.findViewById(R.id.contact_list_imageview_avatar_pic);
				holder.contact_select = (ImageView) convertView.findViewById(R.id.contact_list_imagebutton_select_mark);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			if (holder != null)
			{
				holder.userName.setText(childList.get(groupPosition).get(childPosition).user_name);
				holder.userPost.setText(childList.get(groupPosition).get(childPosition).user_work);
				holder.contact_select.setBackgroundResource(R.drawable.contact_image_button_unselected_mark_icon);
				Bitmap bmp = asyncLoader.loadBitmap(holder.user_img, childList.get(groupPosition).get(childPosition).user_img,
						Utility.dip2px(WorkMemberListActivity.this, (float) 40.0),
						Utility.dip2px(WorkMemberListActivity.this, (float) 40.0), new ImageCallBack()
						{
							@Override
							public void imageLoad(ImageView imageView, Bitmap bitmap)
							{
								if (bitmap == null)
								{
									bitmap = BitmapFactory.decodeResource(getResources(),
											R.drawable.main_frame_listview_default_avatar);
								}
								imageView.setImageBitmap(bitmap);
							}
						});
				if (bmp != null)
				{
					holder.user_img.setImageBitmap(bmp);
				}
			}
			return convertView;
		}
		@Override
		public int getChildrenCount(int groupPosition)
		{
			// TODO Auto-generated method stub
			return childList.get(groupPosition).size();
		}
		@Override
		public Object getGroup(int groupPosition)
		{
			// TODO Auto-generated method stub
			return gcgiList.get(groupPosition);
		}
		@Override
		public int getGroupCount()
		{
			// TODO Auto-generated method stub
			return gcgiList.size();
		}
		@Override
		public long getGroupId(int groupPosition)
		{
			// TODO Auto-generated method stub
			return groupPosition;
		}
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null)
			{
				holder = new ViewHolder();
				LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.weibo_contact_group_title_layout, null);
				holder.groupName = (TextView) convertView.findViewById(R.id.weiboHome_contact_select_list_header);
				holder.indicator_img = (ImageView) convertView
						.findViewById(R.id.weiboHome_contact_expandable_listview_indicator_icon);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			if (holder != null)
			{
				holder.groupName.setText(gcgiList.get(groupPosition).group_name);
				if (isExpanded)
				{
					holder.indicator_img.setBackgroundResource(R.drawable.weibo_expandable_indicator_icon_2);
				}
				else
				{
					holder.indicator_img.setBackgroundResource(R.drawable.weibo_expandable_indicator_icon_1);
				}
			}
			return convertView;
		}
		@Override
		public boolean hasStableIds()
		{
			// TODO Auto-generated method stub
			return true;
		}
		@Override
		public boolean isChildSelectable(int arg0, int arg1)
		{
			// TODO Auto-generated method stub
			return true;
		}
	}
	
	private class ViewHolder
	{
		ImageView contact_select;
		ImageView user_img;
		ImageView indicator_img;
		TextView userName;
		TextView userPost;
		TextView groupName;
	}
}
