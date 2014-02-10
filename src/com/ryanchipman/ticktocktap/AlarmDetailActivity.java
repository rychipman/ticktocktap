package com.ryanchipman.ticktocktap;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ryanchipman.ticktocktap.R;
import com.ryanchipman.ticktocktap.alarm.AlarmService;
import com.ryanchipman.ticktocktap.model.AlarmDBHelper;
import com.ryanchipman.ticktocktap.model.AlarmModel;
import com.ryanchipman.ticktocktap.ui.CustomToggleButton;

public class AlarmDetailActivity extends Activity {
	

	AlarmDBHelper dbHelper;
	private AlarmModel alarmDetails;
	
	private TimePicker timePicker;
	private EditText edtName;
	private CustomToggleButton chkWeekly;
	private CustomToggleButton chkSunday;
	private CustomToggleButton chkMonday;
	private CustomToggleButton chkTuesday;
	private CustomToggleButton chkWednesday;
	private CustomToggleButton chkThursday;
	private CustomToggleButton chkFriday;
	private CustomToggleButton chkSaturday;
	private TextView txtToneSelection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_detail);
		dbHelper = new AlarmDBHelper(this);
		alarmDetails = new AlarmModel();
		// Show the Up button in the action bar.
		setupActionBar();
		
		timePicker = (TimePicker) findViewById(R.id.alarm_details_time_picker);
		edtName = (EditText) findViewById(R.id.alarm_details_name);
		chkWeekly = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_weekly);
		chkSunday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_sunday);
		chkMonday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_monday);
		chkTuesday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_tuesday);
		chkWednesday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_wednesday);
		chkThursday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_thursday);
		chkFriday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_friday);
		chkSaturday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_saturday);
		txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
		
		final View ringToneContainer = findViewById(R.id.alarm_ringtone_container);
		ringToneContainer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
				startActivityForResult(intent , 1);
			}
		});
		
		long id = getIntent().getExtras().getLong("id");
		
		//an id of -1 tells us this is a new alarm
		if (id == -1) {
			alarmDetails = new AlarmModel(id);
		} else {
			alarmDetails = dbHelper.getAlarm(id);

			timePicker.setCurrentMinute(alarmDetails.getTimeMinute());
			timePicker.setCurrentHour(alarmDetails.getTimeHour());

			edtName.setText(alarmDetails.getName());

			chkWeekly.setChecked(alarmDetails.repeatsWeekly());
			chkSunday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.SUNDAY));
			chkMonday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.MONDAY));
			chkTuesday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.TUESDAY));
			chkWednesday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.WEDNESDAY));
			chkThursday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.THURSDAY));
			chkFriday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.FRDIAY));
			chkSaturday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.SATURDAY));

			txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmDetails.getAlarmTone()).getTitle(this));
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setTitle("Create New Alarm");
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_save_alarm_details:
			if(verifyUserInput()) {
				updateModelFromLayout();
				if (alarmDetails.getID() < 0) {
					long key = dbHelper.createAlarm(alarmDetails);
					alarmDetails.setID(key);
					Intent i = new Intent(this, AlarmService.class);
					i.setAction(AlarmService.ACTION_CREATE);
					i.putExtra(AlarmsActivity.EXTRA_MODEL, alarmDetails);
					startService(i);
				} else {
					dbHelper.updateAlarm(alarmDetails);
					Intent i = new Intent(this, AlarmService.class);
					i.setAction(AlarmService.ACTION_UPDATE);
					i.putExtra(AlarmsActivity.EXTRA_MODEL, alarmDetails);
					startService(i);
				}
				setResult(RESULT_OK);
	            finish();
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
	        switch (requestCode) {
		        case 1: 
		        	alarmDetails.setAlarmTone((Uri) data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI));
		        	TextView txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
		        	txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmDetails.getAlarmTone()).getTitle(this));
		            break;
		        default:
		            break;
	        }
	    }
	}
	
	private boolean verifyUserInput() {
		if(alarmDetails.getAlarmTone() == null) {
			Toast t = Toast.makeText(this, R.string.details_missing_ringtone, Toast.LENGTH_SHORT);
			t.show();
			return false;
		}
		return true;
	}
	
	private void updateModelFromLayout() {
		TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_details_time_picker);
		alarmDetails.setTimeMinute(timePicker.getCurrentMinute().intValue());
		alarmDetails.setTimeHour(timePicker.getCurrentHour().intValue());

		EditText edtName = (EditText) findViewById(R.id.alarm_details_name);
		alarmDetails.setName(edtName.getText().toString());

		CustomToggleButton chkWeekly = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_weekly);
		alarmDetails.setRepeatWeekly(chkWeekly.isChecked());

		CustomToggleButton chkSunday = (CustomToggleButton) findViewById(R.id.alarm_details_repeat_sunday);
		alarmDetails.setRepeatingDay(AlarmModel.SUNDAY, chkSunday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.MONDAY, chkMonday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.TUESDAY, chkTuesday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.WEDNESDAY, chkWednesday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.THURSDAY, chkThursday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.FRDIAY, chkFriday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.SATURDAY, chkSaturday.isChecked());
		alarmDetails.setEnabled(true);
	}
}
