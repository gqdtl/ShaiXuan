package com.yimei.shaixuan;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class MainActivity extends Activity implements OnClickListener,
		OnDismissListener {
	private LinearLayout ll_quyu, ll_jiage, ll_huxing, lv1_layout;
	private ListView lv1, lv2;
	private TextView quyu, huxing, jiage;
	private ImageView icon1, icon2, icon3;
	private int screenWidth;
	private int screenHeight;
	private MyAdapter adapter;
	private int idx;
	private SubAdapter subAdapter;
	private String cities[][];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		cities = new String[][] { null,
				this.getResources().getStringArray(R.array.cuiping), null,
				this.getResources().getStringArray(R.array.nanxi), null, null,
				null, null, null, null };
		initScreenWidth();
		findViews();
	}

	private void findViews() {
		ll_quyu = (LinearLayout) findViewById(R.id.ll_quyu);
		ll_jiage = (LinearLayout) findViewById(R.id.ll_jiage);
		ll_huxing = (LinearLayout) findViewById(R.id.ll_huxing);
		quyu = (TextView) findViewById(R.id.quyu);
		huxing = (TextView) findViewById(R.id.huxing);
		jiage = (TextView) findViewById(R.id.jiage);
		icon1 = (ImageView) findViewById(R.id.icon1);
		icon2 = (ImageView) findViewById(R.id.icon2);
		icon3 = (ImageView) findViewById(R.id.icon3);
		ll_quyu.setOnClickListener(this);
		ll_jiage.setOnClickListener(this);
		ll_huxing.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_quyu:
			idx = 1;
			icon1.setImageResource(R.drawable.icon_43343434);
			showPopupWindow(findViewById(R.id.ll_layout), 1);
			break;
		case R.id.ll_jiage:
			idx = 2;
			icon2.setImageResource(R.drawable.icon_43343434);
			showPopupWindow(findViewById(R.id.ll_layout), 2);
			break;
		case R.id.ll_huxing:
			idx = 3;
			icon3.setImageResource(R.drawable.icon_43343434);
			showPopupWindow(findViewById(R.id.ll_layout), 3);
			break;
		}
	}

	/**
	 * 
	 * @Title: showPopupWindow
	 * @Description: PopupWindow
	 * @author yimei
	 * @return void 返回类型
	 */
	public void showPopupWindow(View anchor, int flag) {
		final PopupWindow popupWindow = new PopupWindow(MainActivity.this);
		View contentView = LayoutInflater.from(MainActivity.this).inflate(
				R.layout.windows_popupwindow, null);
		lv1 = (ListView) contentView.findViewById(R.id.lv1);
		lv2 = (ListView) contentView.findViewById(R.id.lv2);
		lv1_layout = (LinearLayout) contentView.findViewById(R.id.lv_layout);
		switch (flag) {
		case 1:
			adapter = new MyAdapter(MainActivity.this,
					initArrayData(R.array.quyu));
			break;
		case 2:
			adapter = new MyAdapter(MainActivity.this,
					initArrayData(R.array.zongjia));
			break;
		case 3:
			adapter = new MyAdapter(MainActivity.this,
					initArrayData(R.array.huxing));
			break;
		}
		lv1.setAdapter(adapter);
		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (parent.getAdapter() instanceof MyAdapter) {
					adapter.setSelectItem(position);
					adapter.notifyDataSetChanged();
					lv2.setVisibility(View.INVISIBLE);
					if (lv2.getVisibility() == View.INVISIBLE) {
						lv2.setVisibility(View.VISIBLE);
						switch (idx) {
						case 1:
							lv1_layout.getLayoutParams().width = 0;
							if (cities[position] != null) {
								subAdapter = new SubAdapter(
										getApplicationContext(),
										cities[position]);
							} else {
								subAdapter = null;
							}
							break;
						case 2:
							lv1_layout.getLayoutParams().width = LayoutParams.MATCH_PARENT;

							break;
						case 3:
							lv1_layout.getLayoutParams().width = LayoutParams.MATCH_PARENT;
							break;
						}
						if (subAdapter != null) {
							lv2.setAdapter(subAdapter);
							subAdapter.notifyDataSetChanged();
							lv2.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									String name = (String) parent.getAdapter()
											.getItem(position);
									setHeadText(idx, name);
									popupWindow.dismiss();
									subAdapter = null;
								}
							});
						} else {
							// 当没有下级时直接将信息设置textview中
							String name = (String) parent.getAdapter().getItem(
									position);
							setHeadText(idx, name);
							popupWindow.dismiss();
						}

					}
				}
			}
		});
		popupWindow.setOnDismissListener(this);
		popupWindow.setWidth(screenWidth);
		popupWindow.setHeight(screenHeight);
		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new PaintDrawable());
		popupWindow.showAsDropDown(anchor);

	}

	/**
	 * @Title: setHeadText
	 * @Description: 点击之后设置在上边的TextView里
	 * @author yimei
	 * @return void 返回类型
	 */
	private void setHeadText(int idx, String text) {
		switch (idx) {
		case 1:
			quyu.setText(text);
			break;
		case 2:
			jiage.setText(text);
			break;
		case 3:
			huxing.setText(text);
			break;
		}

	}

	/**
	 * @Title: initScreenWidth
	 * @Description: 查看自身的宽高
	 * @author yimei
	 * @return void 返回类型
	 */
	private void initScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		screenHeight = dm.heightPixels;
		screenWidth = dm.widthPixels;
		Log.v("屏幕宽高", "宽度" + screenWidth + "高度" + screenHeight);
	}

	private List<String> initArrayData(int id) {
		List<String> list = new ArrayList<String>();
		String[] array = this.getResources().getStringArray(id);
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
		return list;
	}

	@Override
	public void onDismiss() {
		icon1.setImageResource(R.drawable.icon_435);
		icon2.setImageResource(R.drawable.icon_435);
		icon3.setImageResource(R.drawable.icon_435);
	}
}
