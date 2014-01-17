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

	// �ؼ�
	private Button warehouse_newbill_button_back = null;
	private Button warehouse_newbill_footbar_ok = null;
	private Button warehouse_newbill_footbar_commit = null;
	private Spinner classSpinner = null;
	private Spinner materialSpinner = null;
	private EditText edittext_num = null;
	private ListView warehouse_newbill_list = null;

	// ListItem ��������
	public TextView m_bill_item_class_name;
	public TextView m_bill_item_material_name;
	public TextView m_bill_item_material_num;
	public String m_item_class_name = "";
	public String m_item_material_name = "";
	public int m_item_material_num = 0;

	// ����
	private String[] classname = new String[] { "ũ��Ʒ", "���Ʒ", "ˮ��Ʒ", "����ʳƷ" };
	private String[][] materialname = new String[][] {
			{ "����", "С��", "����", "����" }, { "��", "��", "ţ", "��" },
			{ "��", "Ϻ", "з", "��" }, { "����", "��", "ֲ����", "������" } };

	// ������
	ArrayAdapter<String> classAdapter = null;
	ArrayAdapter<String> materialAdapter = null;
	static int classPosition = 0;
	static int materialPosition = 0;

	// ���ݿ� ��ر���
	public static String db_name = "newbill.db";
	public static String db_table_name = "tb_newbill";
	public static final int db_Version = 1;
	public String[] m_columns = { "_id", "class", "material", "num" };

	public String m_class_name = "";
	public String m_material_name = "";
	public int m_material_num = 0;

	// ���ݿ� ����
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

		// ��ȡ Helperʵ��
		dbNewbill = new BillDatabase(WarehouseNewBillActivity.this, db_name,
				null, db_Version);
		// �õ� ���ݱ�
		m_db = getdb();

		// �õ��α�
		m_cursor = db_query();

		// list�б���ʾ
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
					Log.i(TAG, "���" + m_class_name + m_material_name
							+ m_material_num);

					db_insert(m_class_name, m_material_name, m_material_num);
				}
			} else {
				toastInfo("��Ӵ���");
			}
			
			 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
			  //�õ�InputMethodManager��ʵ�� 
			 if (imm.isActive()) { 
			 //������� 
			 imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS); 
			 //�ر�����̣�����������ͬ������������л�������ر�״̬�� 
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
					// ��ʾѡ��ı��ʱ�򴥷��˷�������Ҫʵ�ְ취����̬�ı�ؼ��������İ�ֵ
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

	// Item �����¼�������
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

			// ����
			final AlertDialog.Builder Item_dialog = new AlertDialog.Builder(
					WarehouseNewBillActivity.this);
			Item_dialog.setTitle("�༭��ý���ַ��");
			Item_dialog.setPositiveButton("ɾ��",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Log.i("֪ͨ------", "�༭" + m_item_class_name + m_item_material_name);

							db_delete(m_item_class_name, m_item_material_name);
						}
					});
			Item_dialog.setNegativeButton("ȡ��",
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
		// �õ� SQLiteDatabase ���ݿ������
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

			Log.i(TAG, "��ѯ��" + nid + nclass + nmaterial + nnum);
		}
	}

	// ��List���������
	@SuppressWarnings("deprecation")
	public void fillListData() {
		if (m_cursor != null && m_cursor.getCount() >= 0) {
			m_adapter = new SimpleCursorAdapter(this,
					R.layout.newbill_listitem, m_cursor, new String[] {
							"class", "material", "num" }, new int[] {
							R.id.newbill_class, R.id.newbill_material,
							R.id.newbill_num });

			// list�� Adapter
			warehouse_newbill_list.setAdapter(m_adapter);
		}
	}

	// У������
	public boolean dataIsValid(String mclass, String mmaterial, int mnum) {
		if (null == mclass || null == mmaterial || mnum < 0) {
			return false;
		}

		return true;
	}

	// ���ݿ����
	// ����
	public void db_insert(String classstring, String materialstring, int num) {
		ContentValues cv = new ContentValues();
		cv.put("class", classstring);
		cv.put("material", materialstring);
		cv.put("num", num);
		m_db.insert(db_table_name, null, cv);

		updateList();
	}

	// ɾ��
	public void db_delete(String classstring, String materialstring) {
		String where = "class=? and material=?";
		String[] wherevalues = { classstring, materialstring };
		m_db.delete(db_table_name, where, wherevalues);

		updateList();
	}

	// ��ѯ
	public Cursor db_query() {
		Cursor cursor = m_db.query(db_table_name, m_columns, null, null, null,
				null, null);
		return cursor;
	}

	public void updateList() {
		// ��̬ ˢ������ ��ʾ
		
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

			// �ڴ�ҳ�� �� back��ʱ�ͷ� �α꣬ �ͷ����ݿ�
			m_cursor.close();
			m_db.close();
			WarehouseNewBillActivity.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}