package zhisu.mt.zhisuapp;

import zhisu.mt.utils.BillDatabase;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class WarehouseNewBillActivity extends Activity {

	private static String TAG = "WarehouseNewBillActivity";

	// 控件
	private Button warehouse_newbill_button_back = null;
	private Button warehouse_newbill_footbar_ok = null;
	private Button warehouse_newbill_footbar_commit = null;
	private Spinner classSpinner = null;
	private Spinner materialSpinner = null;
	private EditText edittext_num = null;
	private ListView warehouse_newbill_list = null;

	// ListItem 变量定义
	public TextView m_bill_item_class_name;
	public TextView m_bill_item_material_name;
	public TextView m_bill_item_material_num;
	public String m_item_class_name = "";
	public String m_item_material_name = "";
	public int m_item_material_num = 0;

	// 变量
	private String[] classname = new String[] { "农产品", "畜产品", "水产品", "其他食品" };
	private String[][] materialname = new String[][] {
			{ "稻米", "小麦", "玉米", "高粱" }, { "鸡", "猪", "牛", "羊" },
			{ "鱼", "虾", "蟹", "龟" }, { "鸡蛋", "大豆", "植物油", "防腐剂" } };

	// 适配器
	ArrayAdapter<String> classAdapter = null;
	ArrayAdapter<String> materialAdapter = null;
	static int classPosition = 0;
	static int materialPosition = 0;

	// 数据库 相关变量
	public static String db_name = "newbill.db";
	public static String db_table_name = "tb_newbill";
	public static final int db_Version = 1;
	public String[] m_columns = { "_id", "class", "material", "num" };

	public String m_class_name = "";
	public String m_material_name = "";
	public int m_material_num = 0;

	// 数据库 变量
	private SQLiteDatabase m_db = null;
	private Cursor m_cursor = null;
	private SimpleCursorAdapter m_adapter = null;
	private BillDatabase dbNewbill;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.warehouse_newbill_layout);

		warehouse_newbill_button_back = (Button) findViewById(R.id.warehouse_newbill_button_back);
		warehouse_newbill_footbar_ok = (Button) findViewById(R.id.warehouse_newbill_footbar_ok);
		warehouse_newbill_footbar_commit = (Button) findViewById(R.id.warehouse_newbill_footbar_commit);

		classSpinner = (Spinner) findViewById(R.id.warehouse_newbill_spinner_class);
		materialSpinner = (Spinner) findViewById(R.id.warehouse_newbill_spinner_material);
		edittext_num = (EditText) findViewById(R.id.warehouse_newbill_edittext_num);

		warehouse_newbill_list = (ListView) findViewById(R.id.warehouse_newbill_list);

		warehouse_newbill_button_back.setOnClickListener(backbtn_Listener);
		warehouse_newbill_footbar_commit.setOnClickListener(commit_Listener);
		warehouse_newbill_footbar_ok.setOnClickListener(ok_Listener);

		setSpinner();

		// 获取 Helper实例
		dbNewbill = new BillDatabase(WarehouseNewBillActivity.this, db_name,
				null, db_Version);
		// 得到 数据表
		m_db = getdb();

		// 得到游标
		m_cursor = db_query();

		// list列表显示
		fillListData();
		
		warehouse_newbill_list.setOnItemLongClickListener(m_itemLongListener);
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

	public Button.OnClickListener commit_Listener = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), WarehouseActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	};

	public Button.OnClickListener ok_Listener = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {

			m_class_name = classname[classPosition];
			m_material_name = materialname[classPosition][materialPosition];
			String tempString = edittext_num.getText().toString();
			if (null != tempString && tempString.length() > 0) {
				m_material_num = Integer.parseInt(tempString);
				Log.i(TAG, "--ok_Listener---" + m_class_name + m_material_name
						+ m_material_num);

				if (dataIsValid(m_class_name, m_material_name, m_material_num)) {
					Log.i(TAG, "添加" + m_class_name + m_material_name
							+ m_material_num);

					db_insert(m_class_name, m_material_name, m_material_num);
				}
			} else {
				toastInfo("添加错误！");
			}
			
			 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			  //得到InputMethodManager的实例 
			 if (imm.isActive()) { 
			 //如果开启 
			 imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS); 
			 //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的 
			 }
		}
	};

	private void setSpinner() {
		classAdapter = new ArrayAdapter<String>(WarehouseNewBillActivity.this,
				android.R.layout.simple_spinner_item, classname);
		classSpinner.setAdapter(classAdapter);
		classSpinner.setSelection(0, true);

		materialAdapter = new ArrayAdapter<String>(
				WarehouseNewBillActivity.this,
				android.R.layout.simple_spinner_item,
				materialname[classPosition]);
		materialSpinner.setAdapter(materialAdapter);
		materialSpinner.setSelection(0, true);

		classSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					// 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						materialAdapter = new ArrayAdapter<String>(
								WarehouseNewBillActivity.this,
								android.R.layout.simple_spinner_item,
								materialname[position]);
						materialSpinner.setAdapter(materialAdapter);
						classPosition = position;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}

				});

		materialSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub
						materialPosition = position;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

	}

	// Item 长按事件监听器
	OnItemLongClickListener m_itemLongListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View view,
				int position, long id) {

			m_bill_item_class_name = (TextView) view
					.findViewById(R.id.newbill_class);
			m_bill_item_material_name = (TextView) view
					.findViewById(R.id.newbill_material);
			m_bill_item_material_num = (TextView) view
					.findViewById(R.id.newbill_num);
			m_item_class_name = m_bill_item_class_name.getText().toString();
			m_item_material_name = m_bill_item_material_name.getText().toString();
			m_item_material_num = Integer.parseInt(m_bill_item_material_num.getText().toString());

			// 弹框
			final AlertDialog.Builder Item_dialog = new AlertDialog.Builder(
					WarehouseNewBillActivity.this);
			Item_dialog.setTitle("编辑流媒体地址：");
			Item_dialog.setPositiveButton("删除",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Log.i("通知------", "编辑" + m_item_class_name + m_item_material_name);

							db_delete(m_item_class_name, m_item_material_name);
						}
					});
			Item_dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					});
			Item_dialog.create().show();

			return true;
		}
	};

	public SQLiteDatabase getdb() {
		SQLiteDatabase db;
		// 得到 SQLiteDatabase 数据库访问类
		db = dbNewbill.getWritableDatabase();

		return db;
	}

	public void db_queryItem() {
		while (m_cursor.moveToNext()) {
			int nid = m_cursor.getInt(m_cursor.getColumnIndex("_id"));
			String nclass = m_cursor
					.getString(m_cursor.getColumnIndex("class"));
			String nmaterial = m_cursor.getString(m_cursor
					.getColumnIndex("material"));
			String nnum = m_cursor.getString(m_cursor.getColumnIndex("num"));

			Log.i(TAG, "查询！" + nid + nclass + nmaterial + nnum);
		}
	}

	// 往List里填充数据
	@SuppressWarnings("deprecation")
	public void fillListData() {
		if (m_cursor != null && m_cursor.getCount() >= 0) {
			m_adapter = new SimpleCursorAdapter(this,
					R.layout.newbill_listitem, m_cursor, new String[] {
							"class", "material", "num" }, new int[] {
							R.id.newbill_class, R.id.newbill_material,
							R.id.newbill_num });

			// list绑定 Adapter
			warehouse_newbill_list.setAdapter(m_adapter);
		}
	}

	// 校验数据
	public boolean dataIsValid(String mclass, String mmaterial, int mnum) {
		if (null == mclass || null == mmaterial || mnum < 0) {
			return false;
		}

		return true;
	}

	// 数据库操作
	// 增加
	public void db_insert(String classstring, String materialstring, int num) {
		ContentValues cv = new ContentValues();
		cv.put("class", classstring);
		cv.put("material", materialstring);
		cv.put("num", num);
		m_db.insert(db_table_name, null, cv);

		updateList();
	}

	// 删除
	public void db_delete(String classstring, String materialstring) {
		String where = "class=? and material=?";
		String[] wherevalues = { classstring, materialstring };
		m_db.delete(db_table_name, where, wherevalues);

		updateList();
	}

	// 查询
	public Cursor db_query() {
		Cursor cursor = m_db.query(db_table_name, m_columns, null, null, null,
				null, null);
		return cursor;
	}

	public void updateList() {
		// 动态 刷新数据 显示
		
		m_cursor.requery();
		warehouse_newbill_list.invalidateViews();
		//fillListData();
	}

	private void toastInfo(String string) {
		Toast.makeText(WarehouseNewBillActivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			// 在此页面 按 back键时释放 游标， 释放数据库
			m_cursor.close();
			m_db.close();
			WarehouseNewBillActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}