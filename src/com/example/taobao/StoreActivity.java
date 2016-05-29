package com.example.taobao;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import bean.ItemBean;

public class StoreActivity extends Activity {

	private ListView mListView;
	private List<ItemBean> mData;
	private MyAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
		mListView=(ListView) findViewById(R.id.listView1);
		mData=getData();
		mAdapter = new MyAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		AdapterView.OnItemClickListener listViewListener = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mData.get(position).getItem_id();
				Intent intent = new Intent(StoreActivity.this,
						InfoActivity.class);
				intent.putExtra("item_id", mData.get(position).getItem_id());
				startActivity(intent);
			}
		};
		mListView.setOnItemClickListener(listViewListener);
	}
	
	private List<ItemBean> getData() {
		// TODO Auto-generated method stub
		List<ItemBean> list=new ArrayList<ItemBean>(); 
		SQLiteDatabase db = openOrCreateDatabase("goods.db", MODE_PRIVATE, null);
		 Cursor c = db.rawQuery("select * from goods", null);
		 	if (c!=null) {
		 		while (c.moveToNext()) {
		 			ItemBean item=new ItemBean();
		 			item.setTitle(c.getString(c.getColumnIndex("title")));
		 			item.setPrice(c.getString(c.getColumnIndex("price")));
		 			item.setSold(c.getString(c.getColumnIndex("sellcount")));
		 			item.setImgurl(c.getString(c.getColumnIndex("imgurl")));
		 			item.setItem_id(c.getString(c.getColumnIndex("item_id")));
		 			list.add(item);
				}
		 		c.close();
		 	}
		 	db.close();
		return list;
	}

}
