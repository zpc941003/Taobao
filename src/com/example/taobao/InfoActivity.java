package com.example.taobao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class InfoActivity extends Activity implements OnClickListener {

	private TextView title, price, extraprice, postage, sellcount, from,
			shopname;
	private Button btnBuy, btnStore, btnShowStore;
	private NetworkImageView imageview,shoplogo;
	private static String item_id, url,logourl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		init();
		Intent intent = getIntent();
		item_id = intent.getStringExtra("item_id");
		System.out.println(item_id);
		volley_Get(item_id);
	}

	private void init() {
		// TODO Auto-generated method stub
		title = (TextView) findViewById(R.id.info_title);
		price = (TextView) findViewById(R.id.info_price);
		extraprice = (TextView) findViewById(R.id.info_extraprice);
		postage = (TextView) findViewById(R.id.info_postage);
		sellcount = (TextView) findViewById(R.id.info_sellcount);
		from = (TextView) findViewById(R.id.info_from);
		shopname = (TextView) findViewById(R.id.info_shopname);
		btnBuy = (Button) findViewById(R.id.btnbuy);
		btnStore = (Button) findViewById(R.id.btnstore);
		btnShowStore = (Button) findViewById(R.id.btnshowstore);
		imageview = (NetworkImageView) findViewById(R.id.imageView1);
		shoplogo = (NetworkImageView) findViewById(R.id.info_shoplogo);
		btnBuy.setOnClickListener(this);
		btnStore.setOnClickListener(this);
		btnShowStore.setOnClickListener(this);
	}

	private void volley_Get(String item_id) {// 获取数据
		// TODO Auto-generated method stub
		String url = "http://api.m.taobao.com/h5/mtop.taobao.detail.getdetail/6.0/?appKey=12574478&type=jsonp&dataType=jsonp&data=%7B%22itemNumId%22%3A%22{0}%22";
		String tmpUrl = url.replace("{0}", item_id);
		VolleyRequest.RequestGet(this, tmpUrl, "abcGet",
				new VolleyInterface(this, VolleyInterface.mListener,
						VolleyInterface.mErrorListener) {

					@Override
					public void onMySuccess(String result) {
						// TODO Auto-generated method stub
						try {
							JSONObject obj = new JSONObject(result);
							JSONObject data = obj.getJSONObject("data");
							JSONArray apiStack = data.getJSONArray("apiStack");
							JSONObject obj1 = apiStack.getJSONObject(0);
							String value = obj1.getString("value").replaceAll(
									"\\\\", "");// 去掉反斜杠
							JSONObject value1 = new JSONObject(value);
							JSONObject delivery = value1
									.getJSONObject("delivery");
							String fromstr = delivery.getString("from");
							String postagestr = delivery.getString("postage");
							from.setText(fromstr);
							postage.setText(postagestr);
							JSONObject itemobj = data.getJSONObject("item");
							String titlestr = itemobj.getString("title");
							title.setText(titlestr);
							JSONArray imagearr = itemobj.getJSONArray("images");
							InfoActivity.url = imagearr.getString(0);
							JSONObject seller = data.getJSONObject("seller");
							String shopnamestr = seller.getString("shopName");
							InfoActivity.logourl=seller.getString("shopIcon");
							shopname.setText(shopnamestr);
							JSONObject item = value1.getJSONObject("item");
							String sellcountstr = item.getString("sellCount");
							sellcount.setText(sellcountstr);
							System.out.println(sellcountstr);
							JSONObject obj2 = value1.getJSONObject("price");
							System.out.println(obj2);
							JSONObject priceobj = obj2.getJSONObject("price");
							String pricestr = priceobj.getString("priceText");
							price.setText(pricestr);
							JSONArray extrapriceobj = obj2
									.getJSONArray("extraPrices");
							JSONObject obj3 = extrapriceobj.getJSONObject(0);
							String extrapricestr = obj3.getString("priceText");
							System.out
									.println(pricestr + "   " + extrapricestr);
							extraprice.setText(extrapricestr);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						ImageLoader loader = new ImageLoader(MyApplication
								.getHttpQueues(), new BitmapCache());
						imageview.setImageUrl(InfoActivity.url, loader);
						shoplogo.setImageUrl(InfoActivity.logourl, loader);
					}

					@Override
					public void onMyError(VolleyError error) {
						// TODO Auto-generated method stub
						Toast.makeText(InfoActivity.this, error.toString(),
								3000).show();
					}
				});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btnbuy:
			String url = "https://h5.m.taobao.com/awp/core/detail.htm?ft=t&id={0}&sid=2ec013fcc5ce2e5372024d9f1ba3ec01&rn=d8811de3a46628b207e4dc8384a8735c&abtest=8";
			Uri uri = Uri.parse(url.replace("{0}", item_id));
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
			break;
		case R.id.btnstore:
			dataStore();//添加到收藏夹
			break;
		case R.id.btnshowstore:
			Intent intent = new Intent(InfoActivity.this, StoreActivity.class);
			startActivity(intent);
			break;
		}
	}

	private void dataStore() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = openOrCreateDatabase("goods.db", MODE_PRIVATE, null);
		db.execSQL("create table if not exists goods (_id integer primary key autoincrement, title text not null , price text not null, sellcount text not null , imgurl text not null, item_id text not null  )");
		Cursor c = db.rawQuery("select * from goods where item_id='"
				+ InfoActivity.item_id + "'", null);
		System.out.println(c.getCount());
		if (c.getCount() == 0) {
			db.execSQL("insert into goods(title,price,sellcount,imgurl,item_id) values('"
					+ title.getText().toString()
					+ "','"
					+ price.getText().toString()
					+ "','"
					+ sellcount.getText().toString()
					+ "','"
					+ InfoActivity.url
					+ "','" + InfoActivity.item_id + "')");
			Toast.makeText(InfoActivity.this, "收藏成功", 3000).show();
		} else {
			Toast.makeText(InfoActivity.this, "该商品已被收藏，不能重复收藏", 3000).show();
		}
		c.close();
		db.close();
	}

}
