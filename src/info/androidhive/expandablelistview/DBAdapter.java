package info.androidhive.expandablelistview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
	private static final String DATABASE_TABLE = "Todo_table";
	public static final String KEY_ROW_ID = "_id";
	public static final String KEY_TAG = "tag";
	public static final String KEY_TITLE = "title";
	public static final String KEY_TEXT = "text";
	public static final String KEY_TIME = "time";
	public static final String KEY_ETC = "etc";
	private static final String DATABASE_NAME = "todo";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = 
			"CREATE TABLE "+DATABASE_TABLE+"("+KEY_ROW_ID+" integer primary key autoincrement,"
												+KEY_TAG+" text not null,"
												+KEY_TITLE+" text not null,"
												+KEY_TEXT+"," 
												+KEY_TIME+" text not null,"
												+KEY_ETC+");";

	SQLiteDatabase mDb;
	Context mCtx;
	DBHelper mDbHelper;

	public DBAdapter(Context context) {
		this.mCtx = context;
	}

	public DBAdapter open() throws SQLException {
		mDbHelper = new DBHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createTask(String tag,String title,String text,String time,String etc) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TAG, tag);
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_TEXT, text);
		initialValues.put(KEY_TIME, time);
		initialValues.put(KEY_ETC, etc);
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean deleteTask(long id) {
		return mDb.delete(DATABASE_TABLE, KEY_ROW_ID + " = " + id, null) > 0;
//		return mDb.delete(DATABASE_TABLE, null, null) > 0;
	}

	public boolean updateTask(long id, String tag,String title,String text,String time,String etc) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TAG, tag);
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_TEXT, text);
		initialValues.put(KEY_TIME, time);
		initialValues.put(KEY_ETC, etc);
		return mDb.update(DATABASE_TABLE, initialValues, KEY_ROW_ID + " = "+ id, null) > 0;
	}

	public Cursor fetchAllTasks() {
		return mDb.query(DATABASE_TABLE,new String[]{ KEY_ROW_ID, KEY_TAG,KEY_TITLE,KEY_TEXT,KEY_TIME,KEY_ETC },null, null, null, null, KEY_TIME+" DESC");
	}

	public Cursor fetchTask(long id) {
		Cursor c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROW_ID, KEY_TAG,KEY_TITLE,KEY_TEXT,KEY_TIME,KEY_ETC}, KEY_ROW_ID + " = " + id, null,null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	
	public class DBHelper extends SQLiteOpenHelper {
		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
			onCreate(db);
		}
	}
}
