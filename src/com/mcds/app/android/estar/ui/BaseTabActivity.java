package com.mcds.app.android.estar.ui;

import com.mcds.app.android.estar.R;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

public class BaseTabActivity extends BaseActivity {
	protected static final int TAB_INDEX_NONE = 0;
	protected static final int TAB_INDEX_HOME = 1;
	protected static final int TAB_INDEX_ACTION = 2;
	protected static final int TAB_INDEX_MY = 3;
	protected static final int TAB_INDEX_NEWS = 4;
	protected static final int TAB_INDEX_WORK = 5;
	protected LinearLayout tabNavigateContainer;
	protected boolean isTabNavigateClose = false;
	protected int currentTabNavigateIndex = 0;

	protected void initTabNavigate(int tabNavigateId, int selectedItemIndex) {
		tabNavigateContainer = (LinearLayout) findViewById(tabNavigateId);
		currentTabNavigateIndex = selectedItemIndex;
		setTabNavigateItem();

		tabNavigateContainer.findViewById(R.id.componentTabNavigate_btnHome).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentTabNavigateIndex != TAB_INDEX_HOME) {
					gotoHome();
					if (isTabNavigateClose) {
						finish();
					}
				}
			}
		});

		tabNavigateContainer.findViewById(R.id.componentTabNavigate_btnAction).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentTabNavigateIndex != TAB_INDEX_ACTION) {
					gotoAction();
					if (isTabNavigateClose) {
						finish();
					}
				}
			}
		});
		tabNavigateContainer.findViewById(R.id.componentTabNavigate_btnMy).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentTabNavigateIndex != TAB_INDEX_MY) {
					gotoMy();
					if (isTabNavigateClose) {
						finish();
					}
				}
			}
		});
		tabNavigateContainer.findViewById(R.id.componentTabNavigate_btnNews).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentTabNavigateIndex != TAB_INDEX_NEWS) {
					gotoNews();
					if (isTabNavigateClose) {
						finish();
					}
				}
			}
		});
		tabNavigateContainer.findViewById(R.id.componentTabNavigate_btnWork).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentTabNavigateIndex != TAB_INDEX_WORK) {
					gotoWork();
					if (isTabNavigateClose) {
						finish();
					}
				}
			}
		});
	}

	private void gotoHome() {
		startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.weibo.HomeActivity.class));
	}

	private void gotoAction() {
		startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.action.HomeActivity.class));
	}

	private void gotoMy() {
		startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.my.HomeActivity.class));
	}


	private void gotoNews() {
		startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HomeActivity.class));
	}


	private void gotoWork() {
		startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.work.HomeActivity.class));
	}

	private void setTabNavigateItem() {
		switch (currentTabNavigateIndex) {
			case TAB_INDEX_HOME:
				tabNavigateContainer.findViewById(R.id.componentTabNavigate_btnHome).setBackgroundResource(R.drawable.main_frame_tab_bg_selected);
				tabNavigateContainer.findViewById(R.id.componentTabNavigate_icHome).setBackgroundResource(R.drawable.main_frame_tab_item_ic_home_selected);
				break;
			case TAB_INDEX_ACTION:
				tabNavigateContainer.findViewById(R.id.componentTabNavigate_btnAction).setBackgroundResource(R.drawable.main_frame_tab_bg_selected);
				tabNavigateContainer.findViewById(R.id.componentTabNavigate_icAction).setBackgroundResource(R.drawable.main_frame_tab_item_ic_action_selected);
				break;
			case TAB_INDEX_MY:
				tabNavigateContainer.findViewById(R.id.componentTabNavigate_btnMy).setBackgroundResource(R.drawable.main_frame_tab_bg_selected);
				tabNavigateContainer.findViewById(R.id.componentTabNavigate_icMy).setBackgroundResource(R.drawable.main_frame_tab_item_ic_my_selected);
				break;
			case TAB_INDEX_NEWS:
				tabNavigateContainer.findViewById(R.id.componentTabNavigate_btnNews).setBackgroundResource(R.drawable.main_frame_tab_bg_selected);
				tabNavigateContainer.findViewById(R.id.componentTabNavigate_icNews).setBackgroundResource(R.drawable.main_frame_tab_item_ic_news_selected);
				break;
			case TAB_INDEX_WORK:
				tabNavigateContainer.findViewById(R.id.componentTabNavigate_btnWork).setBackgroundResource(R.drawable.main_frame_tab_bg_selected);
				tabNavigateContainer.findViewById(R.id.componentTabNavigate_icWork).setBackgroundResource(R.drawable.main_frame_tab_item_ic_work_selected);
				break;
			default:
				break;
		}
	}
}
