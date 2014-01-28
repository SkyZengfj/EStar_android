package com.mcds.app.android.estar.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.mcds.app.android.estar.R;
import com.mcds.app.android.estar.ui.BaseTabActivity;

public class HomeActivity extends BaseTabActivity implements OnClickListener{

	private ImageButton newsHomeNews, newsHomeCongrats, newsHomeCommemorate;
	private ImageButton newsHomeHeartLife, newsHomeHeartConstellation, newsHomeHeartMind, newsHomeHeartTest, newsHomeHeartEap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_home);
		initTabNavigate(R.id.newsHome_tabNavigate, TAB_INDEX_NEWS);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		newsHomeNews = (ImageButton)findViewById(R.id.news_home_news_imgbutton);
		newsHomeCongrats = (ImageButton)findViewById(R.id.news_home_congrats_imgbutton);
		newsHomeCommemorate = (ImageButton)findViewById(R.id.news_home_commemorate_imgbutton);
		newsHomeHeartLife = (ImageButton)findViewById(R.id.news_home_heart_life_imgbutton);
		newsHomeHeartConstellation = (ImageButton)findViewById(R.id.news_home_heart_constellation_imgbutton);
		newsHomeHeartMind = (ImageButton)findViewById(R.id.news_home_heart_mind_imgbutton);
		newsHomeHeartTest = (ImageButton)findViewById(R.id.news_home_heart_test_imgbutton);
		newsHomeHeartEap = (ImageButton)findViewById(R.id.news_home_heart_eap_imgbutton);
		
		newsHomeNews.setOnClickListener(this);
		newsHomeCongrats.setOnClickListener(this);
		newsHomeCommemorate.setOnClickListener(this);
		newsHomeHeartLife.setOnClickListener(this);
		newsHomeHeartConstellation.setOnClickListener(this);
		newsHomeHeartMind.setOnClickListener(this);
		newsHomeHeartTest.setOnClickListener(this);
		newsHomeHeartEap.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.news_home_news_imgbutton:
			//进入新闻资讯
			startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.NewsActivity.class));
			break;
		case R.id.news_home_congrats_imgbutton:
			//进入生日祝福
			startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.CongratsActivity.class));
			break;
		case R.id.news_home_commemorate_imgbutton:
			//进入荣誉表彰
			startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.CommemorateActivity.class));
			break;
		case R.id.news_home_heart_life_imgbutton:
			//进入心灵家园_生活
			startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HeartLifeActivity.class));
			break;
		case R.id.news_home_heart_constellation_imgbutton:
			//进入心灵家园_星座
			startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HeartConstellation.class));
			break;
		case R.id.news_home_heart_mind_imgbutton:
			//进入心灵家园_心灵
			startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HeartMindActivity.class));
			break;
		case R.id.news_home_heart_test_imgbutton:
			//进入心灵家园_测试
			startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HeartTestActivity.class));
			break;
		case R.id.news_home_heart_eap_imgbutton:
			//进入心灵家园_EAP阳光心态
			startActivity(new Intent(getApplicationContext(), com.mcds.app.android.estar.ui.news.HeartEAPActivity.class));
			break;
		}
	}

}
