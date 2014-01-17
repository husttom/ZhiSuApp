package zhisu.mt.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BillDatabase extends SQLiteOpenHelper{
	
	public BillDatabase(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

	}
	
	public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table tb_newbill(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"class varchar(50) not null, material varchar(50) not null, num integer(50) not null)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
}