package zhisu.mt.zhisuapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WarehouseScanActivity extends Activity {

	private Button warehouse_scan_backbtn = null;
	private Button warehouse_scan_footbar_commit = null;
	private ListView warehouse_scan_list = null;

	private List<Map<String, Object>> m_listData = null;
	private HashMap<Integer, Boolean> isChecked = null;
	private BaseAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.warehouse_scan_layout);

		warehouse_scan_backbtn = (Button) findViewById(R.id.warehouse_scan_button_back);
		warehouse_scan_footbar_commit = (Button) findViewById(R.id.warehouse_scan_footbar_commit);
		warehouse_scan_list = (ListView) findViewById(R.id.warehouse_scan_list);

		warehouse_scan_backbtn.setOnClickListener(backbtn_Listener);
		warehouse_scan_footbar_commit.setOnClickListener(footbar_commit);
		
		m_listData = getData();
		mAdapter = new Warehoues_scanAdapter(WarehouseScanActivity.this, m_listData);	
		warehouse_scan_list.setAdapter(mAdapter);
		warehouse_scan_list.setOnItemClickListener(m_itemlistenr);
	}

	public Button.OnClickListener backbtn_Listener = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), WarehouseActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};


	public Button.OnClickListener footbar_commit = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					WarehouseActivity.class);
			startActivity(intent);
		}
	};
	
	OnItemClickListener m_itemlistenr = new AdapterView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			
			toastInfo("Item" + position);
		}	
	};

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("img", R.drawable.bill_img);
		map.put("mclass", "农产品");
		map.put("material", "大豆");
		map.put("num", "45");
		list.add(map);

		map = new HashMap<String, Object>();
		// map.put("img", R.drawable.bill_img);
		map.put("mclass", "农产品");
		map.put("material", "玉米");
		map.put("num", "12");
		list.add(map);
		
		map = new HashMap<String, Object>();
		// map.put("img", R.drawable.bill_img);
		map.put("mclass", "水产品");
		map.put("material", "鱼");
		map.put("num", "10");
		list.add(map);
		
		map = new HashMap<String, Object>();
		// map.put("img", R.drawable.bill_img);
		map.put("mclass", "畜产品");
		map.put("material", "牛肉");
		map.put("num", "20");
		list.add(map);
		
		map = new HashMap<String, Object>();
		// map.put("img", R.drawable.bill_img);
		map.put("mclass", "其他");
		map.put("material", "西红柿");
		map.put("num", "15");
		list.add(map);

		return list;
	}

	public final class ViewHolder {
		// public ImageView img;
		public TextView mclass;
		public TextView material;
		public TextView num;
		public CheckBox checkBox;
	}

	public class Warehoues_scanAdapter extends BaseAdapter {
		
		Context mContext;
		List<Map<String, Object>> mData;
		LayoutInflater mInflater;
		
		public Warehoues_scanAdapter(Context context, List<Map<String, Object>> data) {
			this.mContext = context;
			this.mData = data;
			mInflater = (LayoutInflater) mContext
					.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

			isChecked = new HashMap<Integer, Boolean>();
			for (int i = 0; i < data.size(); i++) {
				isChecked.put(i, false);
			}
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
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
					.from(WarehouseScanActivity.this);

			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();

				convertView = mInflater
						.inflate(R.layout.warehouse_scan_listitem, null);
				holder.mclass = (TextView) convertView
						.findViewById(R.id.warehouse_scan_class);
				holder.material = (TextView) convertView
						.findViewById(R.id.warehouse_scan_material);
				holder.num = (TextView) convertView
						.findViewById(R.id.warehouse_scan_num);
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.warehouse_scan_checkbox);

				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			holder.mclass
					.setText((String) mData.get(position).get("mclass"));
			holder.material
			.setText((String) mData.get(position).get("material"));
			holder.num
			.setText((String) mData.get(position).get("num"));
			holder.checkBox.setChecked(isChecked.get(position));
			holder.checkBox.setOnClickListener(null);

			return convertView;
		}
	}
	
	private void toastInfo(String string) {
		Toast.makeText(WarehouseScanActivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}

}