package com.example.tingkerbell;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

public class UpdateActivity extends Activity {
	DBAdapter mDbAdapter;
	Long mRowId;
	EditText EditTitleUp, EditTextUp;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		mDbAdapter=new DBAdapter(this);
		mDbAdapter.open();
		
		EditTitleUp=(EditText) findViewById(R.id.EditTitleUp);
		EditTextUp=(EditText) findViewById(R.id.EditTextUp);
		
		Bundle extras = getIntent().getExtras();
		mRowId = extras != null ? extras.getLong(DBAdapter.KEY_ROW_ID) : null;
		
		Cursor c = mDbAdapter.fetchTask(mRowId);
		startManagingCursor(c);
//		Toast.makeText(this, mRowId+" "+c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_TITLE)), Toast.LENGTH_SHORT).show();
		EditTitleUp.setText(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_TITLE)));
		EditTextUp.setText(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_TEXT))+"\n"+
				c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_TAG))+"\n"+
				c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_ROW_ID))+"\n"+
				c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_TEXT))+"\n"+
				c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_TIME))+"\n"+
				c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_ETC))
				);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update, menu);
		return true;
	}

}
