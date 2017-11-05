package com.why.project.fragmenttabhostunderlinedemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.why.project.fragmenttabhostunderlinedemo.fragment.WebViewFragment;
import com.why.project.fragmenttabhostunderlinedemo.views.tab.MyFragmentTabHost;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private MyFragmentTabHost mTopUnderlineFTabHostLayout;
	//选项卡子类集合
	private ArrayList<TabItem> tabItemList = new ArrayList<TabItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initTabList();
		initFTabHostLayout();
		setFTabHostData();
		initEvents();
	}

	/**
	 * 初始化选项卡数据集合
	 */
	private void initTabList() {
		//底部选项卡对应的Fragment类使用的是同一个Fragment，那么需要考虑切换Fragment时避免重复加载UI的问题】
		tabItemList.add(new TabItem(this,"百度",WebViewFragment.class));
		tabItemList.add(new TabItem(this,"博客园",WebViewFragment.class));
		tabItemList.add(new TabItem(this,"CSDN",WebViewFragment.class));
	}

	/**
	 * 初始化FragmentTabHost
	 */
	private void initFTabHostLayout() {
		//实例化
		mTopUnderlineFTabHostLayout = (MyFragmentTabHost) findViewById(R.id.tab_top_underline_ftabhost_layout);
		mTopUnderlineFTabHostLayout.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);//最后一个参数是碎片切换区域的ID值
		// 去掉分割线
		mTopUnderlineFTabHostLayout.getTabWidget().setDividerDrawable(null);

	}

	/**
	 * 设置选项卡的内容
	 */
	private void setFTabHostData() {
		//Tab存在于TabWidget内，而TabWidget是存在于TabHost内。与此同时，在TabHost内无需在写一个TabWidget，系统已经内置了一个TabWidget
		for (int i = 0; i < tabItemList.size(); i++) {
			//实例化一个TabSpec,设置tab的名称和视图
			TabHost.TabSpec spec = mTopUnderlineFTabHostLayout.newTabSpec(tabItemList.get(i).getTabTitle()).setIndicator(tabItemList.get(i).getTabView());
			// 添加Fragment
			//初始化传参：http://bbs.csdn.net/topics/391059505
			Bundle bundle = new Bundle();
			if(i == 0 ){
				bundle.putString("param", "http://www.baidu.com");
			}else if(i == tabItemList.size() - 1){
				bundle.putString("param", "http://blog.csdn.net");
			}else{
				bundle.putString("param", "http://www.cnblogs.com");
			}

			mTopUnderlineFTabHostLayout.addTab(spec, tabItemList.get(i).getTabFragment(), bundle);
		}

		//默认选中第一项
		mTopUnderlineFTabHostLayout.setCurrentTab(0);
		tabItemList.get(0).setChecked(true);
	}

	private void initEvents() {
		//选项卡的切换事件监听
		mTopUnderlineFTabHostLayout.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				//重置Tab样式
				for (int i = 0; i < tabItemList.size(); i++) {
					TabItem tabitem = tabItemList.get(i);
					if (tabId.equals(tabitem.getTabTitle())) {
						tabitem.setChecked(true);
					} else {
						tabitem.setChecked(false);
					}
				}

				Toast.makeText(MainActivity.this, tabId, Toast.LENGTH_SHORT).show();

			}
		});
	}

	/**
	 * 选项卡子项类*/
	class TabItem{

		private Context mContext;

		private TextView top_title;
		private View top_underline;

		//底部选项卡对应的文字
		private String tabTitle;
		//底部选项卡对应的Fragment类
		private Class<? extends Fragment> tabFragment;

		public TabItem(Context mContext, String tabTitle, Class tabFragment){
			this.mContext = mContext;

			this.tabTitle = tabTitle;
			this.tabFragment = tabFragment;
		}

		public Class<? extends Fragment> getTabFragment() {
			return tabFragment;
		}

		public String getTabTitle() {
			return tabTitle;
		}

		/**
		 * 获取底部选项卡的布局实例并初始化设置*/
		private View getTabView() {
			//============引用选项卡的各个选项的布局文件=================
			View toptabitemView = View.inflate(mContext,R.layout.tab_top_underline_item, null);

			//===========设置选项卡的文字==========
			top_title = (TextView) toptabitemView.findViewById(R.id.top_title);
			//设置选项卡的文字
			top_title.setText(tabTitle);

			//===========设置选项卡控件的下划线==========
			top_underline = (View) toptabitemView.findViewById(R.id.top_underline);

			return toptabitemView;
		}

		/**
		 * 更新文字颜色
		 */
		public void setChecked(boolean isChecked) {
			if(tabTitle != null){
				if(isChecked){
					//修改文字颜色
					top_title.setTextColor(getResources().getColor(R.color.tab_text_selected_top));
					//修改下划线的颜色
					top_underline.setBackgroundColor(getResources().getColor(R.color.tab_underline_selected_top));
				}else{
					//修改文字颜色
					top_title.setTextColor(getResources().getColor(R.color.tab_text_normal_top));
					//修改下划线的颜色
					top_underline.setBackgroundColor(getResources().getColor(R.color.tab_underline_normal_top));
				}
			}
		}
	}
}
