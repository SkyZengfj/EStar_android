package com.mcds.app.android.estar.ui.work;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.Document;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.ConnectProviderTC;
import com.mcds.app.android.estar.ui.BaseActivity;

/**
 * 文档
 * 
 * @author TangChao
 */
public class DocumentListActivity extends BaseActivity
{
	private String theme_id;
	/**
	 * 请求文档列表响应
	 */
	private ReturnResult<Document> rrDocument;
	/**
	 * 删除文档响应
	 */
	private ReturnResult<String> rrDeleteDocument;
	private DocumentAdapter adapter0;
	private ListView listView0;
	/**
	 * 下载按钮
	 */
	private ImageView downloadBtn;
	/**
	 * 删除按钮
	 */
	private ImageView deleteBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.work_home_document_list);
		/* ------------------------------------------------------------ */
		listView0 = (ListView) findViewById(R.id.work_home_document_listView);
		adapter0 = new DocumentAdapter();
		listView0.setAdapter(adapter0);
		/* ------------------------------------------------------------ */
		/**
		 * 长按事件
		 */
		listView0.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				arg0.findViewById(R.id.work_home_document_adapter_item_delete_icon).setVisibility(View.VISIBLE);
				return true;
			}
		});
		/* ---------------------------------------------------- */
		/**
		 * 取消
		 */
		findViewById(R.id.work_home_document_cancel).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		theme_id = getIntent().getStringExtra("theme_id");
		Log.i("theme_id", theme_id);
		getData(theme_id);
	}
	/**
	 * 请求文档列表
	 */
	private void getData(final String theme_id)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				showWaitingDialog();
				if (!UserManager.uid.equals(""))
				{
					rrDocument = ConnectProviderTC.getDocumentList(UserManager.uid, theme_id);
					doUI();
				}
				hideWaitingDialog();
			}
		}).start();
	}
	/**
	 * 删除文档
	 */
	private void deleteDocument(final String document_id)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (!UserManager.uid.equals(""))
				{
					rrDeleteDocument = ConnectProviderTC.deleteDocument(UserManager.uid, document_id);
					doUI();
				}
			}
		}).start();
	}
	/**
	 * 刷新界面
	 */
	private void doUI()
	{
		this.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if (rrDocument.getDatas().size() == 0)
				{
					Toast.makeText(getApplicationContext(), "暂无文档记录", Toast.LENGTH_SHORT).show();
					finish();
				}
				if (rrDocument.status.equals("0"))
				{
					adapter0.setItems(rrDocument.getDatas());
					adapter0.notifyDataSetChanged();
				}
				// if (rrDeleteDocument.getDatas().get(0) != null)
				// {
				// if (rrDeleteDocument.getDatas().size() == 0)
				// {
				// getData(theme_id);
				// }
				// }
			}
		});
	}
	
	/**
	 * 文档列表适配器
	 * 
	 * @author TangChao
	 */
	private class DocumentAdapter extends BaseListAdapter<Document>
	{
		ViewHolder holder = null;
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			final Document item = this.getItem(position);
			if (convertView == null)
			{
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.work_home_document_list_adapter, null);
				holder.name = (TextView) convertView.findViewById(R.id.work_home_document_adapter_item_file_name);
				holder.time = (TextView) convertView.findViewById(R.id.work_home_document_adapter_item_mission_time);
				holder.size = (TextView) convertView.findViewById(R.id.work_home_document_adapter_item_mission_num);
				holder.deleteBtn = (ImageView) convertView.findViewById(R.id.work_home_document_adapter_item_delete_icon);
				holder.downloadBtn = (ImageView) convertView.findViewById(R.id.work_home_document_adapter_item_delete_icon);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}
			/**
			 * 删除按钮事件
			 */
			holder.deleteBtn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					deleteDocument(item.document_id);
				}
			});
			/**
			 * 下载按钮事件
			 */
			holder.downloadBtn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					// downloadDocument();
				}
			});
			if (holder != null)
			{
				holder.name.setText(item.name);
				holder.time.setText(item.time);
				holder.size.setText(item.size);
			}
			return convertView;
		}
	}
	
	public class ViewHolder
	{
		/**
		 * 下載的文件名
		 */
		TextView name;
		/**
		 * 文档上传时间
		 */
		TextView time;
		/**
		 * 文档大小
		 */
		TextView size;
		/**
		 * 删除按钮
		 */
		ImageView deleteBtn;
		/**
		 * 下载按钮
		 */
		ImageView downloadBtn;
	}
}
