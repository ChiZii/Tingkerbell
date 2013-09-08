package info.androidhive.expandablelistview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	//Expan Variable
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	
	//DB Variable
	DBAdapter mDbAdapter;
	SimpleCursorAdapter simAdapter;
	int dayOf=Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	EditText EditTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.lvExp);

		// preparing list data
		prepareListData();
		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);
		
		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				Toast.makeText(getApplicationContext(),
						listDataHeader.get(groupPosition) + " Expanded",
						Toast.LENGTH_SHORT).show();
			}
		});
		
		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition) {
				Toast.makeText(getApplicationContext(),
						listDataHeader.get(groupPosition) + " Collapsed",
						Toast.LENGTH_SHORT).show();

			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
				Toast.makeText(
						getApplicationContext(),
						listDataHeader.get(groupPosition)+ " : "+ listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),
						Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});
		
		//Button click listener
		EditTitle=(EditText) findViewById(R.id.EditTitle);
		Button btnAdd=(Button) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!EditTitle.getText().toString().equals("")){
					createTask();
					prepareListData();
					expListView.setAdapter(listAdapter);
				}
			}
		});
		
	}

	//Create Task
	public void createTask(){
		//EditText.getText().toString()
		if(!EditTitle.getText().equals("")){
			mDbAdapter.createTask("TAG", EditTitle.getText().toString(), "TEXT",Integer.toString(dayOf), "ETC");
			EditTitle.setText("");
		}
	}
	
	/*
	 * Preparing the list data
	 */
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		//DB Connect
		mDbAdapter=new DBAdapter(this);
		mDbAdapter.open();
		Cursor c=mDbAdapter.fetchAllTasks();
		List<String[]> columnArray = new ArrayList<String[]>();
		String[]  getStr=new String[c.getColumnCount()];
		
		List<String> today = new ArrayList<String>();
		List<String> tomorrow = new ArrayList<String>();
		List<String> week = new ArrayList<String>();
		List<String> month = new ArrayList<String>();
		
		//DB Read
		for(c.moveToFirst(); c.moveToNext(); c.isAfterLast()) {
			getStr=new String[]{c.getString(0),c.getString(2)};
			columnArray.add(getStr);
			
			if(c.getInt(4)==dayOf){
				today.add(c.getString(2));
			}else if(c.getInt(4)==(1+dayOf)){
				tomorrow.add(c.getString(2));
			}else if(c.getInt(4)>=(2+dayOf) && c.getInt(4)<=(7+dayOf)){
				week.add(c.getString(2));
			}else if(c.getInt(4)>=(8+dayOf) && c.getInt(4)<=(30+dayOf)){
				month.add(c.getString(2));
			}
		}
		Toast.makeText(this,
				columnArray.size()+"",
				Toast.LENGTH_SHORT).show();
		
		// Adding child data
		listDataHeader.add("Today");
		listDataHeader.add("Tomorrow");
		listDataHeader.add("in Week");
		listDataHeader.add("in Month");

		listDataChild.put(listDataHeader.get(0), today); // Header, Child data
		listDataChild.put(listDataHeader.get(1), tomorrow);
		listDataChild.put(listDataHeader.get(2), week);
		listDataChild.put(listDataHeader.get(3), month);

		listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

	}
}
