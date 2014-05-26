package com.ryanchipman.ticktocktap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.ryanchipman.ticktocktap.alarm.AlarmService;
import com.ryanchipman.ticktocktap.model.AlarmDBHelper;
import com.ryanchipman.ticktocktap.model.AlarmModel;
import com.ryanchipman.ticktocktap.ui.AlarmListAdapter;

public class AlarmsActivity extends ListActivity {
	
	private AlarmDBHelper dbHelper;
	private AlarmListAdapter mAdapter;
	
	public static final String EXTRA_MODEL = "alarm model extra";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		setContentView(R.layout.activity_alarms);
		dbHelper = new AlarmDBHelper(this);
		mAdapter = new AlarmListAdapter(this, dbHelper.getAlarms());
		setListAdapter(mAdapter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		finish();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
//		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarms, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_add_alarm:
				//id of -1 for creating new alarm
				startAlarmDetailActivity(-1);
				break;
			case R.id.action_settings:
				Intent i = new Intent(this, SettingsActivity.class);
				startActivity(i);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			mAdapter.setAlarms(dbHelper.getAlarms());
			mAdapter.notifyDataSetChanged();
		} 
	}
	
	public void setAlarmEnabled(long id, boolean isEnabled) {
		AlarmModel model = dbHelper.getAlarm(id);
		model.setEnabled(isEnabled);
		dbHelper.updateAlarm(model);
		Intent i = new Intent(this, AlarmService.class);
		if(isEnabled)
			i.setAction(AlarmService.ACTION_CREATE);
		else
			i.setAction(AlarmService.ACTION_CANCEL);
		i.putExtra(AlarmsActivity.EXTRA_MODEL, model);
		startService(i);
	}
	
	public void startAlarmDetailActivity(long id) {
		Intent intent = new Intent(this, AlarmDetailActivity.class);
		intent.putExtra("id", id);
		startActivityForResult(intent, 0);
	}
}
