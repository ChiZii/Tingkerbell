package kt.atoz.econovation.tingkerbell.main;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

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
												+KEY_TITLE+" text,"
												+KEY_TEXT+" text not null," 
												+KEY_TIME+" text not null,"
												+KEY_ETC+" text);";

	SQLiteDatabase mDb;
	Context mCtx;
	DBHelper mDbHelper;

	double year=Calendar.getInstance().get(Calendar.YEAR);
	double day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	double dayOf= year*1000+day;
	
	public DBAdapter(Context context) {
		this.mCtx = context;
	}

	public DBAdapter open() throws SQLException {
		mDbHelper = new DBHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
//		Toast.makeText(mCtx, ""+fetchAllTasks().getCount(), Toast.LENGTH_SHORT).show();
		setGroup();
		changeYester();
		return this;
	}

	
	public void close() {
		mDbHelper.close();
	}

	//Initial set values and change KEY_TIME 
	public void setGroup(){
		if(fetchAllTasks().getCount()==0){
			createTask("GROUP","TODAY", "",""+0, "ETC");
			createTask("GROUP","TOMORROW", "",""+(dayOf+1), "ETC");
			createTask("GROUP","in WEEK", "",""+(dayOf+2), "ETC");
			createTask("GROUP","in MONTH", "",""+(dayOf+10), "ETC");
			createTask("TAG","Y asdfa", "",""+(dayOf-1), "ETC");
			createTask("TAG","Y asdfa", "",""+(dayOf-1), "ETC");
			createTask("TAG","To asdf ", "",""+(dayOf), "ETC");
			createTask("TAG","To a wasdf", "",""+(dayOf), "ETC");
			createTask("TAG","To asrhat", "",""+(dayOf), "ETC");
			createTask("TAG","Tm asdf a", "",""+(dayOf+1), "ETC");
			createTask("TAG","Tm asgr a", "",""+(dayOf+1), "ETC");
			createTask("TAG","Tm as as", "",""+(dayOf+1), "ETC");
			createTask("TAG","We dg asg", "",""+(dayOf+2), "ETC");
			createTask("TAG","We athath ", "",""+(dayOf+3), "ETC");
			createTask("TAG","Mo ag ", "",""+(dayOf+10), "ETC");
			createTask("TAG","Mo aht ", "",""+(dayOf+20), "ETC");
		}else {
			updateTask(1, "GROUP", "TODAY", "", ""+0, "ETC");
			updateTask(2, "GROUP", "TOMORROW", "", ""+(dayOf+1), "ETC");
			updateTask(3, "GROUP", "in WEEK", "", ""+(dayOf+3), "ETC");
			updateTask(4, "GROUP", "in MONTH", "", ""+(dayOf+10), "ETC");
		}
	}
	
	public void changeYester(){
//		Cursor c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROW_ID, KEY_TAG,KEY_TITLE,KEY_TEXT,KEY_TEXT,KEY_TIME,KEY_ETC}, KEY_TIME + " < " +dayOf, null,null, null, null);
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TAG, "YESTERDAY");
		mDb.update(DATABASE_TABLE, initialValues, KEY_TIME + " < "+ dayOf+" AND "+KEY_TIME + " > "+ 0, null);
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
//		return mDb.update(DATABASE_TABLE, initialValues, null, null) > 0;
	}
	
	public boolean updateTask(long id, String tag,String title,String text) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TAG, tag);
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_TEXT, text);
		return mDb.update(DATABASE_TABLE, initialValues, KEY_ROW_ID + " = "+ id, null) > 0;
	}
	
	public boolean completeTask(long id,String tag){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TAG, tag);
		initialValues.put(KEY_TIME, dayOf);
		return mDb.update(DATABASE_TABLE, initialValues, KEY_ROW_ID + " = "+ id, null) > 0;
	}

	public boolean tomorrowTask(long id){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TIME, dayOf+1);
		return mDb.update(DATABASE_TABLE, initialValues, KEY_ROW_ID + " = "+ id, null) > 0;
	}
	
	public boolean laterTask(long id,double time){
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TIME, time);
		return mDb.update(DATABASE_TABLE, initialValues, KEY_ROW_ID + " = "+ id, null) > 0;
	}
	
	public Cursor fetchAllTasks() {
		return mDb.query(DATABASE_TABLE,new String[]{ KEY_ROW_ID, KEY_TAG,KEY_TITLE,KEY_TEXT,KEY_TEXT,KEY_TIME,KEY_ETC },null, null, null, null, KEY_TIME/*+" DESC"*/);
	}
	
	public Cursor mainTask(double day) {
//		return mDb.query(DATABASE_TABLE,new String[]{ KEY_ROW_ID, KEY_TAG,KEY_TITLE,KEY_TEXT,KEY_TEXT,KEY_TIME,KEY_ETC },KEY_TIME+" > "+(day-1), null, null, null, KEY_TIME);
//		return mDb.query(DATABASE_TABLE,new String[]{ KEY_ROW_ID, KEY_TAG,KEY_TITLE,KEY_TEXT,KEY_TEXT,KEY_TIME,KEY_ETC },KEY_TAG+" != YESTERDAY "+" & "+ KEY_TAG+" != COMPLETE", null, null, null, KEY_TIME);
		//"NOT " "like '%string'"
		return mDb.query(DATABASE_TABLE,new String[]{ KEY_ROW_ID, KEY_TAG,KEY_TITLE,KEY_TEXT,KEY_TEXT,KEY_TIME,KEY_ETC },
						"("+KEY_TIME+" = 0 OR "+KEY_TIME+" >= "+dayOf+") OR ("+KEY_TIME+" = "+dayOf+" AND "+KEY_TAG+" LIKE '%COMPLETE')"+
						" OR ("+KEY_TIME+" < "+dayOf+" AND "+KEY_TAG+" LIKE '%YESTERDAY')"
						, null, null, null, KEY_TIME);
	}
	
	public Cursor pastList() {
		return mDb.query(DATABASE_TABLE,new String[]{ KEY_ROW_ID, KEY_TAG,KEY_TITLE,KEY_TEXT,KEY_TEXT,KEY_TIME,KEY_ETC },"NOT "+KEY_TAG+" LIKE '%GROUP'", null, null, null, KEY_TIME/*+" DESC"*/);
	}

	
	
	public Cursor fetchTask(long id) {
		Cursor c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROW_ID, KEY_TAG,KEY_TITLE,KEY_TEXT,KEY_TEXT,KEY_TIME,KEY_ETC}, KEY_ROW_ID + " = " + id, null,null, null, null);
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
