package com.ryanchipman.nfclock;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ryanchipman.nfclock.model.AlarmDBHelper;
import com.ryanchipman.nfclock.model.AlarmModel;
import com.ryanchipman.nfclock.ui.AlarmListAdapter;

public class AlarmsActivity extends ListActivity {
	
	private AlarmDBHelper dbHelper;
	private AlarmListAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarms);
		dbHelper = new AlarmDBHelper(this);
		mAdapter = new AlarmListAdapter(this, dbHelper.getAlarms());
		setListAdapter(mAdapter);
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
				startAlarmDetailActivity(-1);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			List<AlarmModel> thesealarms = dbHelper.getAlarms();
			mAdapter.setAlarms(dbHelper.getAlarms());
			mAdapter.notifyDataSetChanged();
		} 
	}
	
	public void setAlarmEnabled(long id, boolean isEnabled) {
		AlarmModel model = dbHelper.getAlarm(id);
		model.isEnabled = isEnabled;
		dbHelper.updateAlarm(model);
	}
	
	public void startAlarmDetailActivity(long id) {
		Intent intent = new Intent(this, AlarmDetailActivity.class);
		intent.putExtra("id", id);
		startActivityForResult(intent, 0);
	}
}
