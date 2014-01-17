package zhisu.mt.zhisuapp;

import zhisu.mt.utils.BillDatabase;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class DetailBillActivity extends Activity {

	private static String TAG = "DetailBillActivity";

	private Button detailbill_backbtn = null;
	private ListView detailbill_list = null;

	// 数据库 相关变量
	public static String db_name = "newbill.db";
	public static String db_table_name = "tb_newbill";
	public static final int db_Version = 1;
	public String[] m_columns = { "_id", "class", "material", "num" };

	// 数据库 变量
	private SQLiteDatabase m_db = null;
	private Cursor m_cursor = null;
	private SimpleCursorAdapter m_adapter = null;
	private BillDatabase dbNewbill;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailbill_layout);

		detailbill_backbtn = (Button) findViewById(R.id.detailbill_button_back);
		detailbill_list = (ListView) findViewById(R.id.detailbill_list);

		// 获取 Helper实例
		dbNewbill = new BillDatabase(DetailBillActivity.this, db_name, null,
				db_Version);
		// 得到 数据表
		m_db = getdb();

		// 得到游标
		m_cursor = db_query();

		// list列表显示
		fillListData();

		detailbill_backbtn.setOnClickListener(backbtn_Listener);
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
			detailbill_list.setAdapter(m_adapter);
		}
	}

	public SQLiteDatabase getdb() {
		SQLiteDatabase db;
		// 得到 SQLiteDatabase 数据库访问类
		db = dbNewbill.getWritableDatabase();

		return db;
	}

	// 查询
	public Cursor db_query() {
		Cursor cursor = m_db.query(db_table_name, m_columns, null, null, null,
				null, null);
		return cursor;
	}

	private void toastInfo(String string) {
		Toast.makeText(DetailBillActivity.this, string, Toast.LENGTH_SHORT)
				.show();
	}

}
