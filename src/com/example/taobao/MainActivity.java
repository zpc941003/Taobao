package com.example.taobao;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import bean.ItemBean;

import com.android.volley.VolleyError;

public class MainActivity extends Activity {

	private EditText et;
	private ImageButton btnSearch;
	private ImageButton btnStore;
	private ListView mListView;
	private List<ItemBean> mDate;
	private MyAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();// 初始化
		clickListen();// 点击事件
	}

	private void init() {
		// TODO Auto-generated method stub
		et = (EditText) findViewById(R.id.editText1);
		btnSearch = (ImageButton) findViewById(R.id.imageButton1);
		btnStore = (ImageButton) findViewById(R.id.imageButton2);
		mListView = (ListView) findViewById(R.id.listView1);
	}

	private void clickListen() {
		// TODO Auto-generated method stub
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				volley_Get();
			}
		});

		btnStore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						StoreActivity.class);
				startActivity(intent);
			}
		});

		AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mDate.get(position).getItem_id();
				Intent intent = new Intent(MainActivity.this,
						InfoActivity.class);
				intent.putExtra("item_id", mDate.get(position).getItem_id());
				startActivity(intent);
			}
		};
		mListView.setOnItemClickListener(listViewListener);
	}

	private void volley_Get() {// 获取数据
		// TODO Auto-generated method stub
		String url = "http://s.m.taobao.com/search?event_submit_do_new_search_auction=1&_input_charset=utf-8&searchfrom=1&action=home%3Aredirect_app_action&from=1&q={0}&sst=1&n=20&buying=buyitnow&m=api4h5&wlsort=10&page=1";
		String keyName = "";
		try {
			keyName = URLEncoder.encode(et.getText().toString(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String tmpUrl = url.replace("{0}", keyName);
		VolleyRequest.RequestGet(this, tmpUrl, "abcGet",
				new VolleyInterface(this, VolleyInterface.mListener,
						VolleyInterface.mErrorListener) {

					@Override
					public void onMySuccess(String result) {
						// TODO Auto-generated method stub
						mDate = pauseJson(result);
						mListView.setAdapter(mAdapter);
						// Toast.makeText(MainActivity.this, result,
						// 3000).show();
					}

					@Override
					public void onMyError(VolleyError error) {
						// TODO Auto-generated method stub
						Toast.makeText(MainActivity.this, error.toString(),
								3000).show();
					}
				});
	}

	protected List<ItemBean> pauseJson(String result) {// 解析数据，生成listview数据源
		// TODO Auto-generated method stub
		List<ItemBean> Date = new ArrayList<ItemBean>();
		try {
			JSONObject obj = new JSONObject(result);
			JSONArray itemDate = obj.getJSONArray("listItem");
			for (int i = 0; i < itemDate.length(); i++) {
				JSONObject item = itemDate.getJSONObject(i);
				String imgurl = item.getString("pic_path").replace("60x60",
						"100x100");
				String title = item.getString("title");
				String price = item.getString("price");
				String sold = item.getString("sold");
				String item_id = item.getString("item_id");
				ItemBean list = new ItemBean();
				list.setTitle(title);
				list.setPrice(price);
				list.setSold(sold);
				list.setItem_id(item_id);
				list.setImgurl(imgurl);
				Date.add(list);
				mAdapter = new MyAdapter(this, Date);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Date;

	}

}
