package com.example.tingkerbell;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends SwipeListViewActivity {
	DBAdapter mDbAdapter;
	Cursor c, d;
	Long mRowId;
	EditText EditTitle;
	double dayOf = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	SimpleCursorAdapter adapter,todayAD;
	
	// ListView Contents
	private ListView mainLV;

	public static final int DELETE_ID = Menu.FIRST + 1;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    //HIDE ACTIONBAR
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		mDbAdapter=new DBAdapter(this);
		mDbAdapter.open();

		EditTitle=(EditText) findViewById(R.id.EditTitle);
		Button btnAdd=(Button) findViewById(R.id.btnAdd);

		mRowId = (savedInstanceState == null) ? null: (Long) savedInstanceState.getSerializable(DBAdapter.KEY_ROW_ID);
		
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!EditTitle.getText().toString().equals(""))
				createTask();
			}
		});
		
        fillData();
	}
		
	@SuppressWarnings("deprecation")
	public void fillData(){
//		c=mDbAdapter.fetchAllTasks();
		c=mDbAdapter.mainTask(dayOf);
//		startManagingCursor(c);
		String[] from=new String[]{DBAdapter.KEY_TITLE,DBAdapter.KEY_TIME};
		int[] to=new int[]{R.id.row};

		adapter=new SimpleCursorAdapter(this, R.layout.row, c, from, to);
		
		//ListView item STYLE set
		adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				// TODO Auto-generated method stub
				if(cursor.getString(cursor.getColumnIndex("tag")).equals("GROUP")){
					((TextView)view).setTextColor(Color.RED);
					((TextView)view).setBackgroundColor(Color.WHITE);
				}
				if(cursor.getString(cursor.getColumnIndex("tag")).equals("TAG")){
					((TextView)view).setTextColor(Color.BLACK);
				}
				if(cursor.getString(cursor.getColumnIndex("tag")).equals("COMPLITE")){
					((TextView)view).setTextColor(Color.BLACK);
					((TextView)view).setBackgroundColor(Color.GRAY);
				}
				return false;
			}
		});
		
		mainLV = (ListView) findViewById(R.id.listView1);
		mainLV.setAdapter(adapter);
	}
	
	public void createTask(){
		//EditText.getText().toString()
		mDbAdapter.createTask("TAG", EditTitle.getText().toString(), "",""+dayOf, "ETC");
		fillData();
		EditTitle.setText("");
	}

	
	@Override
	public ListView getListView() {
		return mainLV;
	}

	@Override
	public void getSwipeItem(boolean isRight, int position) {
//		Toast.makeText(this,"Swipe to " + (isRight ? "right" : "left") + " direction"+position,Toast.LENGTH_SHORT).show();
//		Toast.makeText(this,"Swipe to "+adapter.getItemId(position)+" "
//										+adapter.getItem(0)
//										,Toast.LENGTH_SHORT).show();
//		mDbAdapter.updateTask(adapter.getItemId(position), "", title, text, time, etc)
		if(adapter.getItemId(position)>4){
			if(isRight){
				mDbAdapter.completeTask(adapter.getItemId(position), "COMPLITE");
			}else{
				mDbAdapter.completeTask(adapter.getItemId(position), "TAG");
			}
			fillData();
		}
	}

	//Item Update to New Activity
	@Override
	public void onItemClickListener(ListAdapter adapter, int position) {
		if(adapter.getItemId(position)>4){
			Intent i = new Intent(this, UpdateActivity.class);
			i.putExtra(DBAdapter.KEY_ROW_ID,adapter.getItemId(position));
			startActivityForResult(i, 1);
		}
//		Toast.makeText(this, "Single tap on item position " + position+"\nIn DB: "+adapter.getItemId(position),Toast.LENGTH_SHORT).show();
//		Toast.makeText(this," "+adapter.getItemId(position)+" "+mDbAdapter.fetchTask(adapter.getItemId(position)).getString(1),Toast.LENGTH_SHORT).show();
//		mainLV.getSelectedView().setBackgroundColor(getResources().getColor(Color.RED));
//		CheckBox cb=(CheckBox) findViewById(R.id.rowCB);
	}
	
	//Item Delete
	@Override
	public void onItemLongClickListener(ListAdapter adapter, int position) {
		if(adapter.getItemId(position)>4){
			mDbAdapter.deleteTask(adapter.getItemId(position));
			fillData();
		}
	}
	
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case DELETE_ID:
//			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//			mDbAdapter.deleteTask(info.id);
//			fillData();
//			return true;
//		}
//		return onContextItemSelected(item);
//	}
//	
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
//		onCreateContextMenu(menu, v, menuInfo);
//		menu.add(0, DELETE_ID, 0, R.string.add);
//	}
}
