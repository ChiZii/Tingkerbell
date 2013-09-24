package kt.atoz.econovation.tingkerbell.main;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tingkerbell.R;

@SuppressLint("NewApi")
public class MainActivity extends Contents{
	//DataBase
	DBAdapter mDbAdapter;
	Cursor c;
	SimpleCursorAdapter adapter;
	
	// Today Of Year
	double dayOf;
	

	public static final int DELETE_ID = Menu.FIRST + 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// HIDE ACTIONBAR
		ActionBar actionBar = getActionBar();
		actionBar.hide();

		mDbAdapter = new DBAdapter(this);
		mDbAdapter.open();
		dayOf=mDbAdapter.dayOf;

		findViewByIds();
		loadAnimation();
		final TextToSpeechActivity tts=new TextToSpeechActivity();

		btnTitleCreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!edtTitle.getText().toString().equals(""))
					createTask();
			}
		});
		fillData();
		home();
		
		//Setting Button Clicked
		btnList.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				pastList();
				
				c = mDbAdapter.pastList();
				// startManagingCursor(c);
				String[] from = new String[] { DBAdapter.KEY_TITLE, DBAdapter.KEY_TIME };
				int[] to = new int[] { R.id.row};

				adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.row, c, from, to);
				pastListView.setAdapter(adapter);
			}
		});
		
		btnSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				settingPage();
			}
		});
		
		btnVoice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				home();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(settingLayout.getVisibility()==View.GONE && updateLayout.getVisibility()==View.GONE){
				return super.onKeyDown(keyCode, event);
			}else if(settingLayout.getVisibility()==View.VISIBLE){
				home();
				pastListView.setAdapter(null);
				return true;
			}else if(updateLayout.getVisibility()==View.VISIBLE){
				home();
				return true;
			}
		}else if(keyCode==KeyEvent.KEYCODE_MENU){
			setbtns();
			return true;
		}
		return true;
	}
	
	//DataBase data set in ListView
	@SuppressWarnings("deprecation")
	public void fillData() {
		c = mDbAdapter.mainTask(dayOf);
		// startManagingCursor(c);
		String[] from = new String[] { DBAdapter.KEY_TITLE, DBAdapter.KEY_TIME };
		int[] to = new int[] { R.id.row };

		adapter = new SimpleCursorAdapter(this, R.layout.row, c, from, to);

		// ListView item STYLE set
		adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor,int columnIndex) {
				// TODO Auto-generated method stub
				if (cursor.getString(cursor.getColumnIndex("tag")).equals("GROUP")) {
					((TextView) view).setTextColor(Color.rgb(255, 0, 0));
					((TextView) view).setBackgroundColor(Color.rgb(255, 255, 255));
				} else if (cursor.getString(cursor.getColumnIndex("tag")).equals("TAG")) {
					((TextView) view).setTextColor(Color.rgb(0, 0, 0));
					((TextView) view).setBackgroundColor(Color.rgb(255, 255, 255));
					((TextView) view).setPadding(20, 0, 0, 0);
				} else if (cursor.getString(cursor.getColumnIndex("tag")).equals("COMPLETE")) {
					((TextView) view).setTextColor(Color.rgb(0, 0, 0));
					((TextView) view).setBackgroundColor(Color.rgb(50, 50, 50));
					((TextView) view).setPadding(40, 0, 0, 0);
				}else if (cursor.getString(cursor.getColumnIndex("tag")).equals("YESTERDAY")){
					((TextView) view).setTextColor(Color.rgb(55, 55, 55));
					((TextView) view).setBackgroundColor(Color.rgb(100,100,100));
					((TextView) view).setPadding(40, 0, 0, 0);	
				}
				return false;
			}
		});

		mainLV = (ListView) findViewById(R.id.listView1);
		mainLV.setAdapter(adapter);
	}

	public void createTask() {
		// EditText.getText().toString()
		mDbAdapter.createTask("TAG", edtTitle.getText().toString(), "", ""+ dayOf, "ETC");
		fillData();
		edtTitle.setText("");
	}

	public void updateTask(long id,String tag, String title, String text) {
		// EditText.getText().toString()
		mDbAdapter.updateTask(id, tag, title, text);
		fillData();
		home();
	}
	
	//GESTURE
	//create ListView
	@Override
	public ListView getListView() {
		return mainLV;
	}

	@Override
	public void getSwipeItem(boolean isRight, final int position) {
//		Toast.makeText(getApplicationContext(), mDbAdapter.fetchTask(adapter.getItemId(position)).getString(mDbAdapter.fetchTask(adapter.getItemId(position)).getColumnIndexOrThrow(DBAdapter.KEY_TIME)), Toast.LENGTH_SHORT).show();
		if (adapter.getItemId(position) > 4) {
			if (isRight) {
				mDbAdapter.completeTask(adapter.getItemId(position), "COMPLETE");
				//mDbAdapter.fetchTask(adapter.getItemId(position)).getString(mDbAdapter.fetchTask(adapter.getItemId(position)).getColumnIndexOrThrow(DBAdapter.KEY_TAG)) == "TAG"
			} else {
				if(mDbAdapter.fetchTask(adapter.getItemId(position)).getString(mDbAdapter.fetchTask(adapter.getItemId(position)).getColumnIndexOrThrow(DBAdapter.KEY_TAG)).equals("TAG")){
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

					// Setting Dialog Title
					alertDialog.setTitle("Confirm Delete...");

					// Setting Dialog Message
					alertDialog.setMessage("Are you sure you want delete this file?");

					// Setting Icon to Dialog
//					alertDialog.setIcon(R.drawable.delete);

					// Setting Positive "Yes" Btn
					alertDialog.setPositiveButton("YES",
					        new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int which) {
					                // Write your code here to execute after dialog
					                Toast.makeText(getApplicationContext(),"You clicked on YES", Toast.LENGTH_SHORT).show();
					                mDbAdapter.tomorrowTask(adapter.getItemId(position));
					                fillData();
					            }
					        });
					// Setting Negative "NO" Btn
					alertDialog.setNegativeButton("NO",
					        new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int which) {
					                // Write your code here to execute after dialog
					                Toast.makeText(getApplicationContext(),"You clicked on NO", Toast.LENGTH_SHORT).show();
					                dialog.cancel();
					            }
					        });

					// Showing Alert Dialog
					alertDialog.show();
				}else{
					mDbAdapter.completeTask(adapter.getItemId(position), "TAG");
				}
			}
			fillData();
		}
	}
	
	@Override
	public void getSwipeItem(boolean isDown) {
		if (isDown) {
			home();
			pastListView.setAdapter(null);
		} else {
			if(updateLayout.getVisibility()==View.VISIBLE)
				home();
			else
				setbtns();
		}
	}

	// Item Update to New Activity
	@Override
	public void onItemClickListener(final ListAdapter adapter, final int position) {
		if (adapter.getItemId(position) > 4) {
			update();
			edtTitle.setText(mDbAdapter.fetchTask(adapter.getItemId(position)).getString(mDbAdapter.fetchTask(adapter.getItemId(position)).getColumnIndexOrThrow(DBAdapter.KEY_TITLE)));
			edtText.setText(
					mDbAdapter.fetchTask(adapter.getItemId(position)).getString(mDbAdapter.fetchTask(adapter.getItemId(position)).getColumnIndexOrThrow(DBAdapter.KEY_TEXT))
					);
			btnTitleUpdate.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					updateTask(adapter.getItemId(position), "TAG", edtTitle.getText().toString(), edtText.getText().toString());
				}
			});
			btnTodaUpdate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					mDbAdapter.laterTask(adapter.getItemId(position),dayOf);
					home();
					fillData();
				}
			});
			
			btnTomoUpdate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mDbAdapter.laterTask(adapter.getItemId(position),dayOf+1);
					home();
					fillData();
				}
			});
			
			btnTimeUpdate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mDbAdapter.laterTask(adapter.getItemId(position),dayOf+4);
					home();
					fillData();
				}
			});
		}else{
			home();
		}
	}
	
	// Item Delete
	@Override
	public void onItemLongClickListener(final ListAdapter adapter, final int position) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

		// Setting Dialog Title
		alertDialog.setTitle("Confirm Delete...");

		// Setting Dialog Message
		alertDialog.setMessage("Are you sure you want delete this file?");

		// Setting Icon to Dialog
//		alertDialog.setIcon(R.drawable.delete);

		// Setting Positive "Yes" Btn
		alertDialog.setPositiveButton("YES",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                // Write your code here to execute after dialog
		                Toast.makeText(getApplicationContext(),"You clicked on YES", Toast.LENGTH_SHORT).show();
		                mDbAdapter.deleteTask(adapter.getItemId(position));
		                fillData();
		            }
		        });
		// Setting Negative "NO" Btn
		alertDialog.setNegativeButton("NO",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                // Write your code here to execute after dialog
		                Toast.makeText(getApplicationContext(),"You clicked on NO", Toast.LENGTH_SHORT).show();
		                dialog.cancel();
		            }
		        });

		// Showing Alert Dialog
		alertDialog.show();
//		if (adapter.getItemId(position) > 4) {
//			mDbAdapter.deleteTask(adapter.getItemId(position));
//			fillData();
//		}
	}
}
