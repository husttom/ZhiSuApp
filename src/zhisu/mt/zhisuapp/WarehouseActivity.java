package zhisu.mt.zhisuapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WarehouseActivity extends Activity {
	
	private static String TAG = "WarehouseActivity";

	private Button warehouse_backbtn = null;
	private Button warehouse_footbar_newbill = null;
	private ImageButton warehouse_newbtn = null;
	private ImageView warehouse_img = null;

	private ListView warehouse_list = null;
	private TextView warehouse_item_bill = null;
	private Button warehouse_item_detail = null;

	private List<Map<String, Object>> m_listData = null;
	private BaseAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.warehouse_layout);

		warehouse_backbtn = (Button) findViewById(R.id.warehouse_button_back);
		warehouse_footbar_newbill = (Button) findViewById(R.id.warehouse_footbar_newbill);
		warehouse_newbtn = (ImageButton) findViewById(R.id.warehouse_button_new);
		warehouse_img = (ImageView) findViewById(R.id.warehouse_imageView);
		warehouse_list = (ListView) findViewById(R.id.warehouse_list);

		warehouse_backbtn.setOnClickListener(backbtn_Listener);
		warehouse_newbtn.setOnClickListener(newbtn_Listener);
		warehouse_footbar_newbill.setOnClickListener(footbar_newbill);
		
		mAdapter = new WarehouesAdapter();
		m_listData = getData();
		warehouse_list.setAdapter(mAdapter);
		warehouse_list.setOnItemClickListener(m_itemlistenr);
	}

	public Button.OnClickListener backbtn_Listener = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MainSourceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};

	public Button.OnClickListener newbtn_Listener = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {

			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, 1);
		}
	};

	public Button.OnClickListener footbar_newbill = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					WarehouseNewBillActivity.class);
			startActivity(intent);
		}
	};
	
	OnItemClickListener m_itemlistenr = new AdapterView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), WarehouseScanActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}	
	};

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("img", R.drawable.bill_img);
		map.put("title", "表单一");
		map.put("detail", "详情");
		list.add(map);

		map = new HashMap<String, Object>();
		// map.put("img", R.drawable.bill_img);
		map.put("title", "表单二");
		map.put("detail", "详情");
		list.add(map);

		return list;
	}

	public final class ViewHolder {
		// public ImageView img;
		public TextView title;
		public Button detail;
	}

	public class WarehouesAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return m_listData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			LayoutInflater mInflater = LayoutInflater
					.from(WarehouseActivity.this);

			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();

				convertView = mInflater
						.inflate(R.layout.warehouse_listitem, null);
				// holder.state_img = (ImageView)
				// convertView.findViewById(R.id.pub_confmanage_layout_listItem_state_img);
				holder.title = (TextView) convertView
						.findViewById(R.id.warehouse_item_bill);

				holder.detail = (Button) convertView
						.findViewById(R.id.warehouse_item_detail);

				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			// holder.state_img.setBackgroundResource((Integer)m_listData.get(position).get("state_img"));
			holder.title
					.setText((String) m_listData.get(position).get("title"));

			holder.detail.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					toastInfo("show details");
					
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), DetailBillActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			});

			return convertView;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (resultCode == Activity.RESULT_OK) {

			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Log.i("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			String name = new DateFormat().format("yyyyMMdd_hhmmss",
					Calendar.getInstance(Locale.CHINA))
					+ ".jpg";
			Log.i(TAG, "======================================" + name);

			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

			FileOutputStream b = null;

			File file = new File("/sdcard/myImage/");
			file.mkdirs();// 创建文件夹
			String fileName = "/sdcard/myImage/" + name;
			Log.i(TAG, "======================================" + fileName);
			toastInfo(fileName);

			try {
				b = new FileOutputStream(fileName);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			// warehouse_img.setImageBitmap(bitmap);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void toastInfo(String string) {
		Toast.makeText(WarehouseActivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}

}