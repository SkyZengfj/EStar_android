package com.mcds.app.android.estar.ui.weibo;

import java.util.ArrayList;
import java.util.List;

import com.mcds.app.android.estar.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.bean.WeiboGetMatters;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderGC;
import com.mcds.app.android.estar.ui.BaseActivity;

public class DynamicMatterActivity extends BaseActivity {
	private ExpandableListView  elv;
	
	private List<String> groupTitle;
	private List<List<String>> childList;
	private List<WeiboGetMatters> wgm;
	
	private int matterType;
	
	private MatterListAdapter adapter;
	
	private ReturnResult<WeiboGetMatters> result_WeiboGetMatters;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weibo_home_matter_list);
		
		setMatterType();
		
		groupTitle = new ArrayList<String>();
		childList = new ArrayList<List<String>>();
		
		elv = (ExpandableListView) findViewById(R.id.weiboHome_matter_select_list);
		elv.setDivider(getResources().getDrawable(R.drawable.expandablelistview_divider));
		elv.setChildDivider(getResources().getDrawable(R.drawable.expandablelistview_divider));
		elv.setGroupIndicator(null);
//		if (matterType == 4) {
//			elv.setGroupIndicator(getResources().getDrawable(R.drawable.weibo_expandable_list_view_icon_0));
//		}else{
//			elv.setGroupIndicator(getResources().getDrawable(R.drawable.weibo_expandable_indicator_icon_1));
//		}
		adapter = new MatterListAdapter(this, groupTitle, childList);	
		elv.setAdapter(adapter);
		elv.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				if (childList.get(groupPosition).isEmpty()) {
					Intent intent = new Intent(DynamicMatterActivity.this, HomeListDynamicEditActivity.class);
					intent.putExtra("groupMatter", groupTitle.get(groupPosition));
					intent.putExtra("childMatter", "");
					setResult(3, intent);
					finish();
                    return true;  
                } else {
                    return false;  
                }
			}
		});
		
		elv.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DynamicMatterActivity.this, HomeListDynamicEditActivity.class);
				intent.putExtra("childMatter", childList.get(groupPosition).get(childPosition));
				intent.putExtra("groupMatter", "");
				setResult(3, intent);
				finish();
				return true;
			}
		});
		
		getData();
	}
	
	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();

				if (!UserManager.uid.equals("")) {
					result_WeiboGetMatters = ConnectProviderGC.getWeiboItems(UserManager.uid, String.valueOf(matterType));
				}
				
				doExpandableListViewUI();
				
				hideWaitingDialog();
			}
		}).start();
	}
	
	private void setMatterType() {
		switch (getIntent().getIntExtra("messageType", 0)) {
		case 1:
			matterType = 0;
			break;
			
		case 2:
			matterType = 1;
			break;
			
		case 3:
			matterType = 2;
			break;
			
		case 4:
			matterType = 4;
			break;
			
		case 5:
			matterType = 3;
			break;

		default:
			matterType = 0;
			break;
		}
	}
	
	private void doExpandableListViewUI() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (result_WeiboGetMatters.status.equals("0")) {
					wgm = result_WeiboGetMatters.getDatas();
					for (int i = 0; i < wgm.size(); i++) {
						groupTitle.add(wgm.get(i).item_title);
						childList.add(wgm.get(i).items);
					}
				}
			}
		});
	}
	
	private class MatterListAdapter extends BaseExpandableListAdapter {
		private Context ctx;
	    private List<String> listDataHeader;
	    private List<List<String>> listDataChild;


	    public MatterListAdapter(Context ctx, List<String> listDataHeader, List<List<String>> listDataChild) {
	        super();
	        this.ctx = ctx;
	        this.listDataHeader = listDataHeader;
	        this.listDataChild = listDataChild;
	    }

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childList.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			String childText = (String) getChild(groupPosition, childPosition);
			if(convertView==null){
	            LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = layoutInflater.inflate(R.layout.weibo_home_matter_select_item_layout, null);
	        }
			
			TextView childTextView = (TextView) convertView.findViewById(R.id.weiboHome_matter_select_list_item);
	        childTextView.setText(childText);
	        
			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return childList.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groupTitle.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groupTitle.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			String groupText = groupTitle.get(groupPosition);
	        if(convertView==null){
	            LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView=layoutInflater.inflate(R.layout.weibo_home_matter_select_group_layout, null);
	        }
	        ImageView indicator = (ImageView) convertView.findViewById(R.id.weiboHome_expandable_listview_indicator_icon);
	        TextView groupTextView = (TextView) convertView.findViewById(R.id.weiboHome_matter_select_list_header);
//	        groupTextView.setTypeface(null, Typeface.BOLD);
	        groupTextView.setText(groupText);
	        
	        if (matterType != 4) {
		        if(isExpanded){
		        	indicator.setBackgroundResource(R.drawable.weibo_expandable_indicator_icon_2);
	            }else{
	            	indicator.setBackgroundResource(R.drawable.weibo_expandable_indicator_icon_1);
	            }
			}
	        return convertView;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}

}
