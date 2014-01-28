package com.mcds.app.android.estar.ui.news;

import java.util.ArrayList;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.NewsTestAnswers;
import com.mcds.app.android.estar.bean.NewsTestQuestion;
import com.mcds.app.android.estar.manager.NewsTestManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.ui.BaseActivity;

public class HeartTestQA extends BaseActivity {

	private ListView qalist;
	private QAAdapter adapter;

	private List<NewsTestAnswers> ntalist;
	private NewsTestQuestion ntq;
	private int curQaIndex = 0;
	private int totalQa;
	private int scoreAll;

	private boolean isSingleTest = false;

	private TextView qaQuestion;
	private TextView qaQuestion_index;

	private int lastPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		switch (Integer
				.valueOf(NewsTestManager.test.questions.get(curQaIndex).test_type)) {
		case 0:
			isSingleTest = true;
			break;
		case 1:
			isSingleTest = false;
			break;
		}
		setContentView(R.layout.news_heart_ontest_qa);

		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		qalist = (ListView) findViewById(R.id.news_detail_ontest_qa_list);
		qaQuestion = (TextView) findViewById(R.id.news_detail_ontest_qa_question);
		qaQuestion_index = (TextView) findViewById(R.id.news_detail_ontest_qa_index);
		((TextView) this.findViewById(R.id.news_detail_ontest_qa_next_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO 下一题

						curQaIndex++;
						if (totalQa == curQaIndex) {
							// 跳到最终界面
							Intent intent = new Intent(
									HeartTestQA.this,
									com.mcds.app.android.estar.ui.news.HeartTestQAResultActivity.class);
							Bundle b = new Bundle();
							b.putInt("testscore", scoreAll);
							intent.putExtras(b);
							startActivity(intent);
						}else{
							doUIList();
						}
					}
				});
		ntq = new NewsTestQuestion();
		ntalist = new ArrayList<NewsTestAnswers>();

		adapter = new QAAdapter();
		qalist.setAdapter(adapter);

		doUIList();

	}

	private void doUIList() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (NewsTestManager.test != null) {
					ntq = NewsTestManager.test.questions.get(curQaIndex);
					ntalist = NewsTestManager.test.questions.get(curQaIndex).answers;
					totalQa = NewsTestManager.test.questions.size();

					if ((curQaIndex + 1) == totalQa) {
						((TextView) findViewById(R.id.news_detail_ontest_qa_next_btn))
								.setText("查看结果");
					}

					qaQuestion.setText(ntq.question_content);
					qaQuestion_index.setText("" + curQaIndex + 1);
					adapter.setItems(ntalist);
					adapter.notifyDataSetChanged();
				}

			}
		});
	}

	private class QAAdapter extends BaseListAdapter<NewsTestAnswers> {

		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (isSingleTest) {
				ViewHolder holder = null;
				if (convertView == null) {
					convertView = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.news_heart_ontest_qa_item, null);
					holder = new ViewHolder();
					holder.qaframe = (ImageView) convertView
							.findViewById(R.id.news_detail_ontest_qa_item_redframe);
					holder.qaanswer = (TextView) convertView
							.findViewById(R.id.news_detail_ontest_qa_item_answer);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				if (holder != null) {
					NewsTestAnswers item = this.getItem(position);
					holder.qaanswer.setText(item.answer_content);
					if (lastPosition == position) {
						holder.qaframe.setVisibility(View.VISIBLE);
					} else {
						holder.qaframe.setVisibility(View.INVISIBLE);
					}
					holder.lastSelectedPosition = position;
					holder.itemscore = Integer.valueOf(item.score);
					holder.qaanswer.setTag(holder);
					holder.qaanswer.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ViewHolder holder = (ViewHolder) v.getTag();
							lastPosition = holder.lastSelectedPosition;
							if (holder.qaframe.getVisibility() == View.INVISIBLE) {
								holder.qaframe.setVisibility(View.VISIBLE);
							} else {
								holder.qaframe.setVisibility(View.INVISIBLE);
							}
							scoreAll = holder.itemscore;
							doUIList();
						}
					});

				}
			} else {
				// 多选
				ViewHoldermuti mholder = null;
				if (convertView == null) {
					convertView = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.news_heart_ontest_qa_muti_item,
									null);
					mholder = new ViewHoldermuti();
					mholder.qaanswer = (TextView) convertView
							.findViewById(R.id.news_detail_ontest_qa_item_answer_muti);
					convertView.setTag(mholder);
				} else {
					mholder = (ViewHoldermuti) convertView.getTag();
				}

				if (mholder != null) {
					NewsTestAnswers item = this.getItem(position);
					mholder.qaanswer.setText(item.answer_content);
					mholder.itemscore = Integer.valueOf(item.score);

					Resources resources = getBaseContext().getResources();
					Drawable drawable = resources
							.getDrawable(R.drawable.action_detail_ontest_qa_answeritem_bg_unselected);
					mholder.qaanswer.setBackgroundDrawable(drawable);

					mholder.qaanswer.setTag(mholder);
					mholder.qaanswer.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ViewHoldermuti holder = (ViewHoldermuti) v.getTag();
							Resources resources = v.getResources();
							if (!holder.isSelected) {
								Drawable drawable = resources
										.getDrawable(R.drawable.action_detail_ontest_qa_answeritem_bg);
								holder.qaanswer.setBackgroundDrawable(drawable);
								holder.isSelected = true;
								scoreAll += holder.itemscore;
							} else {
								Drawable drawable = resources
										.getDrawable(R.drawable.action_detail_ontest_qa_answeritem_bg_unselected);
								holder.qaanswer.setBackgroundDrawable(drawable);
								holder.isSelected = false;
								scoreAll -= holder.itemscore;
							}
						}
					});

				}
			}

			return convertView;
		}

	}
	
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HeartTestStart.class);
		startActivity(intent);
		super.onBackPressed();
	}

	private class ViewHolder {
		ImageView qaframe;
		TextView qaanswer;
		int itemscore;
		int lastSelectedPosition;
	}

	private class ViewHoldermuti {
		TextView qaanswer;
		int itemscore;
		boolean isSelected;
	}
}
