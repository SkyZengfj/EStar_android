package com.mcds.app.android.estar.ui.action;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.adapter.BaseListAdapter;
import com.mcds.app.android.estar.bean.ActionQuestionairParam;
import com.mcds.app.android.estar.bean.ActivitAnswer;
import com.mcds.app.android.estar.bean.ActivitTestQues;
import com.mcds.app.android.estar.bean.ActivityTest;
import com.mcds.app.android.estar.bean.ReturnResult;
import com.mcds.app.android.estar.manager.QuestionairManager;
import com.mcds.app.android.estar.manager.UserManager;
import com.mcds.app.android.estar.provider.AsyncBitmapLoader;
import com.mcds.app.android.estar.provider.ConnectProviderZX;
import com.mcds.app.android.estar.ui.BaseActivity;
import com.mcds.app.android.estar.ui.news.HeartTestQA;

public class QuestionairActivity extends BaseActivity {

	private ListView qalist;
	private TextView qaQuestion;
	private TextView qaQuestion_index;
	private QAAdapter adapter;
	private EditText editQa;

	private ReturnResult<ActivityTest> rrActionTestList;
	private ActivityTest acTest;
	private ActivitTestQues ntq;
	private List<ActivitAnswer> ntalist;

	private String curTestType;
	private String curQuesType;

	private int curQaIndex = 0;
	private int totalQa;
	private String curActionID;
	private String curAnswerID;

	private int lastPosition = -1;
	private int lastPage = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_detail_ontest_qa);
		curActionID = QuestionairManager.activity_id;
		//
		init();
		getData();
	}

	private void init() {
		// TODO Auto-generated method stub
		qalist = (ListView) findViewById(R.id.action_detail_ontest_qa_list);
		qaQuestion = (TextView) findViewById(R.id.action_detail_ontest_qa_question);
		qaQuestion_index = (TextView) findViewById(R.id.action_detail_ontest_qa_index);
		editQa = (EditText) findViewById(R.id.action_detail_ontest_qa_edit);

		((TextView) this.findViewById(R.id.action_detail_ontest_qa_next_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO 下一题
						// 记录当前问题，用户所做的答案
						ActionQuestionairParam aqp = new ActionQuestionairParam();
						aqp.question_id = ntq.problem_id;
						aqp.question_type = ntq.problem_type;
						aqp.answer_id = curAnswerID;
						aqp.answer_content = editQa.getText().toString();
						QuestionairManager.question_result = new ArrayList<ActionQuestionairParam>();
						QuestionairManager.question_result.add(aqp);

						curQaIndex++;
						if (totalQa == curQaIndex) {
							// 跳到最终界面
							curQaIndex = 0;
							Intent intent = new Intent(
									QuestionairActivity.this,
									com.mcds.app.android.estar.ui.action.QuestionairResultActivity.class);
							startActivity(intent);
						} else {
							doListViewUI();
						}
					}
				});

		adapter = new QAAdapter();
		qalist.setAdapter(adapter);
	}

	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showWaitingDialog();
				if (!curActionID.equals("")) {
					rrActionTestList = ConnectProviderZX.getActivityTest(
							UserManager.uid, curActionID);

				}

				hideWaitingDialog();
				doListViewUI();
			}
		}).start();
	}

	private void doListViewUI() {

		this.runOnUiThread(new Runnable() {

			private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

			@Override
			public void run() {
				if (rrActionTestList.status.equals("0")) {
					acTest = rrActionTestList.getDatas().get(0);
					for (int i = 0; i < acTest.test_question.size(); i++) {
						if (acTest.test_question.get(i).problem_type
								.equals("5")) {
							onBackPressed();
						}
					}

					ntq = acTest.test_question.get(curQaIndex);
					curQuesType = acTest.test_question.get(curQaIndex).problem_type;
					ntalist = acTest.test_question.get(curQaIndex).answers;
					curTestType = acTest.test_type;
					totalQa = acTest.test_question.size();

					if ((curQaIndex + 1) == totalQa) {
						((TextView) findViewById(R.id.action_detail_ontest_qa_next_btn))
								.setText("查看结果");
					}
					qaQuestion.setText(ntq.question_content);
					int index = curQaIndex + 1;
					qaQuestion_index.setText("" + index);

					if (lastPage != curQaIndex) {
						lastPosition = -1;
						lastPage = curQaIndex;
					}

					adapter.setItems(ntalist);
					adapter.notifyDataSetChanged();

				}else{
					onBackPressed();
				}

			}
		});
	}

	private class QAAdapter extends BaseListAdapter<ActivitAnswer> {

		private AsyncBitmapLoader asyncLoader = new AsyncBitmapLoader();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if ((Integer.valueOf(curQuesType) == 1)
					|| (Integer.valueOf(curQuesType) == 3)) {
				// 单选
				qalist.setVisibility(View.VISIBLE);
				editQa.setVisibility(View.GONE);
				ViewHolder holder = null;
				if (convertView == null) {
					convertView = LayoutInflater.from(getApplicationContext())
							.inflate(R.layout.action_detail_ontest_qa_item,
									null);
					holder = new ViewHolder();
					holder.qaframe = (ImageView) convertView
							.findViewById(R.id.action_detail_ontest_qa_item_redframe);
					holder.qaanswer = (TextView) convertView
							.findViewById(R.id.action_detail_ontest_qa_item_answer);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				if (holder != null) {
					ActivitAnswer item = this.getItem(position);
					holder.qaanswer.setText(item.answer_content);
					if (lastPosition == position) {
						holder.qaframe.setVisibility(View.VISIBLE);
					} else {
						holder.qaframe.setVisibility(View.INVISIBLE);
					}
					holder.lastSelectedPosition = position;
					holder.itemscore = Integer.valueOf(item.score);
					holder.activitAnswer = item;
					holder.qaanswer.setTag(holder);
					holder.qaanswer.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ViewHolder holder = (ViewHolder) v.getTag();
							lastPosition = holder.lastSelectedPosition;
							curAnswerID = holder.activitAnswer.answer_id;
							if (holder.qaframe.getVisibility() == View.INVISIBLE) {
								holder.qaframe.setVisibility(View.VISIBLE);
							} else {
								holder.qaframe.setVisibility(View.INVISIBLE);
							}
							doListViewUI();
						}
					});

				}
			} else if (Integer.valueOf(curQuesType) == 2) {
				qalist.setVisibility(View.VISIBLE);
				editQa.setVisibility(View.GONE);
				ViewHoldermuti mholder = null;
				if (convertView == null) {
					convertView = LayoutInflater
							.from(getApplicationContext())
							.inflate(
									R.layout.action_detail_ontest_qa_multi_item,
									null);
					mholder = new ViewHoldermuti();
					mholder.qaanswer = (TextView) convertView
							.findViewById(R.id.action_detail_ontest_qa_item_answer_muti);
					convertView.setTag(mholder);
				} else {
					mholder = (ViewHoldermuti) convertView.getTag();
				}

				if (mholder != null) {
					ActivitAnswer item = this.getItem(position);
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
								// scoreAll += holder.itemscore;
							} else {
								Drawable drawable = resources
										.getDrawable(R.drawable.action_detail_ontest_qa_answeritem_bg_unselected);
								holder.qaanswer.setBackgroundDrawable(drawable);
								holder.isSelected = false;
								// scoreAll -= holder.itemscore;
							}
						}
					});

				}
			} else if (Integer.valueOf(curQuesType) == 4) {
				qalist.setVisibility(View.GONE);
				editQa.setVisibility(View.VISIBLE);

			}

			return convertView;
		}

	}

	private class ViewHolder {
		ImageView qaframe;
		TextView qaanswer;
		int itemscore;
		int lastSelectedPosition;
		ActivitAnswer activitAnswer;
	}

	private class ViewHoldermuti {
		TextView qaanswer;
		int itemscore;
		boolean isSelected;
	}

	public void onBackPressed() {
		super.onBackPressed();
	}

}
