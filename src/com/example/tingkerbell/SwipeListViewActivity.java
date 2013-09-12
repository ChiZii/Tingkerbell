package com.example.tingkerbell;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public abstract class SwipeListViewActivity extends Activity {

	private ListView list;
	private int REL_SWIPE_MIN_DISTANCE;
	private int REL_SWIPE_MAX_OFF_PATH;
	private int REL_SWIPE_THRESHOLD_VELOCITY;

	public abstract ListView getListView();
	public abstract void getSwipeItem(boolean isRight, int position);
	public abstract void getSwipeItem(boolean isUp);
	public abstract void onItemClickListener(ListAdapter adapter, int position);
	public abstract void onItemLongClickListener(ListAdapter adapter, int position);

	//NOT USE
//	public abstract void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo);
//	public abstract boolean onContextItemSelected(MenuItem item);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics dm = getResources().getDisplayMetrics();
		REL_SWIPE_MIN_DISTANCE = (int) (120.0f * dm.densityDpi / 160.0f + 0.5);
		REL_SWIPE_MAX_OFF_PATH = (int) (250.0f * dm.densityDpi / 160.0f + 0.5);
		REL_SWIPE_THRESHOLD_VELOCITY = (int) (200.0f * dm.densityDpi / 160.0f + 0.5);
	}

	@Override
	protected void onResume() {
		super.onResume();
		list = getListView();
		if (list == null) {
			new Throwable("Listview not set exception");
		}

		@SuppressWarnings("deprecation")
		final GestureDetector gestureDetector = new GestureDetector(new MyGestureDetector());

		View.OnTouchListener gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};
		list.setOnTouchListener(gestureListener);
	}

	private void myOnItemClick(int position) {
		if (position < 0) return;
		onItemClickListener(list.getAdapter(), position);
	}

	private void myOnItemLongClick(int position) {
		if (position < 0) return;
		onItemLongClickListener(list.getAdapter(), position);
	}
	
	class MyGestureDetector extends SimpleOnGestureListener {

		private int temp_position = -1;

		// Detect a single-click and call my own handler.
		@Override
		public boolean onSingleTapUp(MotionEvent e) {

			int pos = list.pointToPosition((int) e.getX(), (int) e.getY());
			myOnItemClick(pos);
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {

			temp_position = list
					.pointToPosition((int) e.getX(), (int) e.getY());
			return super.onDown(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (Math.abs(e1.getY() - e2.getY()) > REL_SWIPE_MAX_OFF_PATH) return false;
			
			if (e1.getX() - e2.getX() > REL_SWIPE_MIN_DISTANCE && Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {
				int pos = list.pointToPosition((int) e1.getX(), (int) e2.getY());
				if (pos >= 0 && temp_position == pos) 
					getSwipeItem(false, pos);
			} else if (e2.getX() - e1.getX() > REL_SWIPE_MIN_DISTANCE&& Math.abs(velocityX) > REL_SWIPE_THRESHOLD_VELOCITY) {
				int pos = list.pointToPosition((int) e1.getX(), (int) e2.getY());
				if (pos >= 0 && temp_position == pos)
					getSwipeItem(true, pos);
			}

			if (e1.getY() - e2.getY() > REL_SWIPE_MIN_DISTANCE && Math.abs(velocityY) > REL_SWIPE_THRESHOLD_VELOCITY) {
					getSwipeItem(false);
			} else if (e2.getY() - e1.getY() > REL_SWIPE_MIN_DISTANCE&& Math.abs(velocityY) > REL_SWIPE_THRESHOLD_VELOCITY) {
					getSwipeItem(true);
			}
			return false;
		}
		
		public void onLongPress (MotionEvent e){
			int pos = list.pointToPosition((int) e.getX(), (int) e.getY());
			myOnItemLongClick(pos);
		}

	}
}
