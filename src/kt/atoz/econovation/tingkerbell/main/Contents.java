package kt.atoz.econovation.tingkerbell.main;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.tingkerbell.R;

public class Contents extends SwipeListViewActivity implements AnimationListener {

	FrameLayout mainFrame; // FrameLayout
	ListView mainLV; // ListView
	EditText edtTitle; // Create
	Button btnTitleCreate;
	LinearLayout updateLayout, updateButtonsLayout; // Update
	EditText edtText;
	Button btnTitleUpdate, btnTodaUpdate,btnTomoUpdate,btnTimeUpdate;
	LinearLayout settingLayout, buttonLayout; // Setting
	Button btnList, btnVoice, btnSetting;
	LinearLayout pastListLayout, settingPageLayout;
	ListView pastListView;

	// Animation
	Animation animSideDown, animSideUp,animMoveDown, animMoveUp;
	
	public Contents() {
		// TODO Auto-generated constructor stub
	}

	public void findViewByIds(){
		// Activity Contents match XML
		mainFrame = (FrameLayout) findViewById(R.id.MainFrame);
		updateLayout = (LinearLayout) findViewById(R.id.UpdateLayout);
		buttonLayout = (LinearLayout) findViewById(R.id.ButtonsLayout);
		updateButtonsLayout=(LinearLayout) findViewById(R.id.UpdateButtonsLayout);
		settingLayout = (LinearLayout) findViewById(R.id.SettingLayout);
		pastListLayout = (LinearLayout) findViewById(R.id.PastListLayout);
		settingPageLayout = (LinearLayout) findViewById(R.id.SettingPageLayout);

		edtTitle = (EditText) findViewById(R.id.edtTitle);
		edtText = (EditText) findViewById(R.id.edtText);
		mainLV=(ListView) findViewById(R.id.listView1);
		pastListView = (ListView) findViewById(R.id.pastListView);

		btnTodaUpdate=(Button) findViewById(R.id.btnTodaUpdate);
		btnTomoUpdate=(Button) findViewById(R.id.btnTomoUpdate);
		btnTimeUpdate=(Button) findViewById(R.id.btnTimeUpdate);
		btnTitleCreate = (Button) findViewById(R.id.btnTitleCreate);
		btnTitleUpdate = (Button) findViewById(R.id.btnTitleUpdate);
		btnList = (Button) findViewById(R.id.btnList);
		btnVoice = (Button) findViewById(R.id.btnVoice);
		btnSetting = (Button) findViewById(R.id.btnSetting);
	}
	
	public void loadAnimation(){
		//animation
		animSideDown=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
		animSideUp=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
		animMoveDown=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movedown);
		animMoveUp=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveup);
				
	}
	//
	
//		updateLayout
//			edtText
//			updateButtonsLayout
//			btnTodaUpdate
//			btnTomoUpdate
//			btnTimeUpdate
//		settingLayout
//			buttonLayout
//				btnList
//				btnVoice
//				btnSetting
//			pastListLayout
//				pastListView
//			settingPageLayout
	public void home(){
		
		if(updateLayout.getVisibility()==View.VISIBLE){
			
			updateLayout.startAnimation(animSideUp);
				edtText.startAnimation(animSideUp);
				btnTitleCreate.setVisibility(View.VISIBLE);
				btnTitleUpdate.setVisibility(View.GONE);
				updateButtonsLayout.setVisibility(View.GONE);
					btnTodaUpdate.setVisibility(View.GONE);
					btnTomoUpdate.setVisibility(View.GONE);
					btnTimeUpdate.setVisibility(View.GONE);
			settingLayout.setVisibility(View.GONE);
				buttonLayout.setVisibility(View.GONE);
					btnList.setVisibility(View.GONE);
					btnVoice.setVisibility(View.GONE);
					btnSetting.setVisibility(View.GONE);
				pastListLayout.setVisibility(View.GONE);
					pastListView.setVisibility(View.GONE);
				settingPageLayout.setVisibility(View.GONE);
			
			edtTitle.setText("");
			updateLayout.setVisibility(View.GONE);
			edtText.setVisibility(View.GONE);
			Handler mHandler = new Handler();
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					updateLayout.clearAnimation();
					edtText.clearAnimation();
				}
			}, 350);
			
		}else if(settingLayout.getVisibility()==View.VISIBLE){
			
			updateLayout.setVisibility(View.GONE);
				edtText.setVisibility(View.GONE);
				btnTitleCreate.setVisibility(View.VISIBLE);
				btnTitleUpdate.setVisibility(View.GONE);
				updateButtonsLayout.setVisibility(View.GONE);
					btnTodaUpdate.setVisibility(View.GONE);
					btnTomoUpdate.setVisibility(View.GONE);
					btnTimeUpdate.setVisibility(View.GONE);
			settingLayout.startAnimation(animMoveDown);
				buttonLayout.startAnimation(animMoveDown);
					btnList.startAnimation(animMoveDown);
					btnVoice.startAnimation(animMoveDown);
					btnSetting.startAnimation(animMoveDown);
				pastListLayout.setVisibility(View.GONE);
					pastListView.setVisibility(View.GONE);
				settingPageLayout.setVisibility(View.GONE);
			
			edtTitle.setText("");
			settingLayout.setVisibility(View.GONE);
				buttonLayout.setVisibility(View.GONE);
					btnList.setVisibility(View.GONE);
					btnVoice.setVisibility(View.GONE);
					btnSetting.setVisibility(View.GONE);
			pastListLayout.setVisibility(View.GONE);
			Handler mHandler = new Handler();
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					settingLayout.clearAnimation();
					buttonLayout.clearAnimation();
					btnList.clearAnimation();
					btnVoice.clearAnimation();
					btnSetting.clearAnimation();
				}
			}, 400);
		}else{
				btnTitleCreate.setVisibility(View.VISIBLE);
			updateLayout.setVisibility(View.GONE);
				btnTitleUpdate.setVisibility(View.GONE);
				edtText.setVisibility(View.GONE);
				updateButtonsLayout.setVisibility(View.GONE);
					btnTodaUpdate.setVisibility(View.GONE);
					btnTomoUpdate.setVisibility(View.GONE);
					btnTimeUpdate.setVisibility(View.GONE);
			settingLayout.setVisibility(View.GONE);
				buttonLayout.setVisibility(View.GONE);
					btnList.setVisibility(View.GONE);
					btnVoice.setVisibility(View.GONE);
					btnSetting.setVisibility(View.GONE);
				pastListLayout.setVisibility(View.GONE);
					pastListView.setVisibility(View.GONE);
				settingPageLayout.setVisibility(View.GONE);
		}
	}
	public void update(){
		home();
			btnTitleCreate.setVisibility(View.GONE);
		updateLayout.setVisibility(View.VISIBLE);
			btnTitleUpdate.setVisibility(View.VISIBLE);
			edtText.setVisibility(View.VISIBLE);
			updateButtonsLayout.setVisibility(View.VISIBLE);
			btnTodaUpdate.setVisibility(View.VISIBLE);
			btnTomoUpdate.setVisibility(View.VISIBLE);
			btnTimeUpdate.setVisibility(View.VISIBLE);
		
		updateLayout.bringToFront();
		updateLayout.startAnimation(animSideDown);
		edtText.startAnimation(animSideDown);
	}
	
	public void setbtns(){
		home();
		settingLayout.setVisibility(View.VISIBLE);
			buttonLayout.setVisibility(View.VISIBLE);
				btnList.setVisibility(View.VISIBLE);
				btnVoice.setVisibility(View.VISIBLE);
				btnSetting.setVisibility(View.VISIBLE);
		settingLayout.startAnimation(animMoveUp);
	}
	public void pastList(){
		home();
		settingLayout.setVisibility(View.VISIBLE);
			buttonLayout.setVisibility(View.VISIBLE);
				btnList.setVisibility(View.VISIBLE);
				btnVoice.setVisibility(View.VISIBLE);
				btnSetting.setVisibility(View.VISIBLE);
		pastListLayout.setVisibility(View.VISIBLE);
			pastListView.setVisibility(View.VISIBLE);
		settingPageLayout.setVisibility(View.GONE);
		pastListLayout.startAnimation(animMoveUp);
	}
	public void settingPage(){
		home();
		settingLayout.setVisibility(View.VISIBLE);
			buttonLayout.setVisibility(View.VISIBLE);
				btnList.setVisibility(View.VISIBLE);
				btnVoice.setVisibility(View.VISIBLE);
				btnSetting.setVisibility(View.VISIBLE);
		pastListLayout.setVisibility(View.GONE);
			pastListView.setVisibility(View.GONE);
		settingPageLayout.setVisibility(View.VISIBLE);
		pastListView.setAdapter(null);
	}
	
	
	
	
	
	
	
	
	@Override
	public void onAnimationEnd(android.view.animation.Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(android.view.animation.Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(android.view.animation.Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public ListView getListView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getSwipeItem(boolean isRight, int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getSwipeItem(boolean isUp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClickListener(ListAdapter adapter, int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemLongClickListener(ListAdapter adapter, int position) {
		// TODO Auto-generated method stub
		
	}
}
