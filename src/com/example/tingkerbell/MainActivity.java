package com.example.tingkerbell;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends SwipeListViewActivity implements TextToSpeech.OnInitListener{
	//DataBase
	DBAdapter mDbAdapter;
	Cursor c, d;
	Long mRowId;
	SimpleCursorAdapter adapter, todayAD;
	
	// Today Of Year
	double dayOf;

	// Activity Contents
	private FrameLayout mainFrame; //FrameLayout
	private ListView mainLV; // ListView
	private EditText EditTitle; // Create
	private Button btnAdd;
	private LinearLayout upLLO; // Update
	private EditText upET;
	private Button btnUp;
	private LinearLayout setLLO,setB; //Setting
	private Button btnList,btnVoice,btnSett;
	private LinearLayout setPageList,setPageSetting;
	private ListView pastListView;
	
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

		// Activity Contents match XML
		mainFrame=(FrameLayout) findViewById(R.id.mainFrame);
		EditTitle = (EditText) findViewById(R.id.EditTitle);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		upLLO = (LinearLayout) findViewById(R.id.updateLayout);
		upET=(EditText) findViewById(R.id.EditTextUp);
		btnUp=(Button) findViewById(R.id.btnUpdate);
		setB=(LinearLayout) findViewById(R.id.setBtnS);
		setLLO=(LinearLayout) findViewById(R.id.setttingLayout);
		btnList=(Button) findViewById(R.id.btnList);
		btnVoice=(Button) findViewById(R.id.btnVoice);
		btnSett=(Button) findViewById(R.id.btnSetting);
		setPageList=(LinearLayout) findViewById(R.id.setPageList);
		setPageSetting=(LinearLayout) findViewById(R.id.setPageSetting);
		pastListView = (ListView) findViewById(R.id.pastListView);
		
		// mRowId = (savedInstanceState == null) ? null: (Long)
		// savedInstanceState.getSerializable(DBAdapter.KEY_ROW_ID);

		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!EditTitle.getText().toString().equals(""))
					createTask();
			}
		});
		fillData();
		
		//Call TextToSpeech
		tts = new TextToSpeech(this, this);
		
		//Setting Button Clicked
		btnList.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setPageList.setVisibility(View.VISIBLE);
				setPageSetting.setVisibility(View.GONE);
				upLLO.setVisibility(View.GONE);
				c = mDbAdapter.mainTask(dayOf);
				// startManagingCursor(c);
				String[] from = new String[] { DBAdapter.KEY_TITLE, DBAdapter.KEY_TIME };
				int[] to = new int[] { R.id.row };

				adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.row, c, from, to);
				pastListView.setAdapter(adapter);
				
			}
		});
		btnSett.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setPageList.setVisibility(View.GONE);
				setPageSetting.setVisibility(View.VISIBLE);
				upLLO.setVisibility(View.GONE);
			}
		});
		btnVoice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setPageList.setVisibility(View.GONE);
				setPageSetting.setVisibility(View.GONE);
				upLLO.setVisibility(View.GONE);
				speakOut();
			}
		});
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
//			Toast.makeText(getApplicationContext(), "WoW"+upLLO.getVisibility(), Toast.LENGTH_SHORT).show();
			if(setLLO.getVisibility()==View.GONE && upLLO.getVisibility()==View.GONE){
				return super.onKeyDown(keyCode, event);
			}else if(setLLO.getVisibility()==View.VISIBLE){
				setLLO.setVisibility(View.GONE);
				return true;
			}else if(upLLO.getVisibility()==View.VISIBLE){
				upLLO.setVisibility(View.GONE);
				btnAdd.setVisibility(View.VISIBLE);
				btnUp.setVisibility(View.GONE);
				return true;
			}
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
				} else if (cursor.getString(cursor.getColumnIndex("tag")).equals("COMPLITE")) {
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
		mDbAdapter.createTask("TAG", EditTitle.getText().toString(), "", ""+ dayOf, "ETC");
		fillData();
		EditTitle.setText("");
	}

	public void updateTask(long id,String tag, String title, String text) {
		// EditText.getText().toString()
		mDbAdapter.updateTask(id, tag, title, text);
		fillData();
		upLLO.setVisibility(View.GONE);
		EditTitle.setText("");
		btnAdd.setVisibility(View.VISIBLE);
		btnUp.setVisibility(View.GONE);
		
	}
	
	//GESTURE
	//create ListView
	@Override
	public ListView getListView() {
		return mainLV;
	}

	@Override
	public void getSwipeItem(boolean isRight, int position) {
		if (adapter.getItemId(position) > 4) {
			if (isRight) {
				mDbAdapter.completeTask(adapter.getItemId(position), "COMPLITE");
			} else {
				mDbAdapter.completeTask(adapter.getItemId(position), "TAG");
			}
			fillData();
		}
	}
	
	@Override
	public void getSwipeItem(boolean isDown) {
		if (isDown) {
			setLLO.setVisibility(View.GONE);
			setPageList.setVisibility(View.GONE);
			setPageSetting.setVisibility(View.GONE);
			pastListView.setAdapter(null);
		} else {
			setLLO.setVisibility(View.VISIBLE);
			upLLO.setVisibility(View.GONE);
			btnAdd.setVisibility(View.VISIBLE);
			btnUp.setVisibility(View.GONE);
			EditTitle.setText("");
			
		}
	}

	// Item Update to New Activity
	@Override
	public void onItemClickListener(final ListAdapter adapter, final int position) {
//		Toast.makeText(getApplicationContext(), mDbAdapter.fetchTask(adapter.getItemId(position)).getString(mDbAdapter.fetchTask(adapter.getItemId(position)).getColumnIndexOrThrow(DBAdapter.KEY_TIME)), Toast.LENGTH_SHORT).show();
		if (adapter.getItemId(position) > 4) {
			upLLO.setVisibility(View.VISIBLE);
			upLLO.bringToFront();
			EditTitle.setText(mDbAdapter.fetchTask(adapter.getItemId(position)).getString(mDbAdapter.fetchTask(adapter.getItemId(position)).getColumnIndexOrThrow(DBAdapter.KEY_TITLE)));
			upET.setText(mDbAdapter.fetchTask(adapter.getItemId(position)).getString(mDbAdapter.fetchTask(adapter.getItemId(position)).getColumnIndexOrThrow(DBAdapter.KEY_TEXT))
						);
			btnAdd.setVisibility(View.GONE);
			btnUp.setVisibility(View.VISIBLE);
			
			
			btnUp.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					updateTask(adapter.getItemId(position), "TAG", EditTitle.getText().toString(), upET.getText().toString());
				}
			});
		}else{
			upLLO.setVisibility(View.GONE);
			btnAdd.setVisibility(View.VISIBLE);
			btnUp.setVisibility(View.GONE);
		}
	}
	
	// Item Delete
	@Override
	public void onItemLongClickListener(ListAdapter adapter, int position) {
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);

		// Setting Dialog Title
		alertDialog2.setTitle("Confirm Delete...");

		// Setting Dialog Message
		alertDialog2.setMessage("Are you sure you want delete this file?");

		// Setting Icon to Dialog
//		alertDialog2.setIcon(R.drawable.delete);

		// Setting Positive "Yes" Btn
		alertDialog2.setPositiveButton("YES",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                // Write your code here to execute after dialog
		                Toast.makeText(getApplicationContext(),
		                        "You clicked on YES", Toast.LENGTH_SHORT)
		                        .show();
		            }
		        });
		// Setting Negative "NO" Btn
		alertDialog2.setNegativeButton("NO",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                // Write your code here to execute after dialog
		                Toast.makeText(getApplicationContext(),
		                        "You clicked on NO", Toast.LENGTH_SHORT)
		                        .show();
		                dialog.cancel();
		            }
		        });

		// Showing Alert Dialog
		alertDialog2.show();

//		if (adapter.getItemId(position) > 4) {
//			mDbAdapter.deleteTask(adapter.getItemId(position));
//			fillData();
//		}
	}

	//Text To Speech Method
	private TextToSpeech tts;
	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub

		if (arg0 == TextToSpeech.SUCCESS) {
			int result = tts.setLanguage(Locale.KOREAN);
			 tts.setPitch(5); // set pitch level
			 tts.setSpeechRate(2); // set speech speed rate
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "Language is not supported");
			} else {
				btnVoice.setEnabled(true);
			}

		} else {
			Log.e("TTS", "Initilization Failed");
		}
		
	}
	
	private void speakOut() {
//		String text = txtText.getText().toString();
		tts.speak("wow wow wow wow wow wow wow wow", TextToSpeech.QUEUE_FLUSH, null);
	}
}
