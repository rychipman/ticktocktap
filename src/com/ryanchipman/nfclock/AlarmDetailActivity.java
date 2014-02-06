package com.ryanchipman.nfclock;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
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

import com.ryanchipman.nfclock.model.AlarmDBHelper;
import com.ryanchipman.nfclock.model.AlarmModel;
import com.ryanchipman.nfclock.ui.CustomToggleButton;

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

		if (id == -1) {
			alarmDetails = new AlarmModel(id);
		} else {
			alarmDetails = dbHelper.getAlarm(id);

			timePicker.setCurrentMinute(alarmDetails.timeMinute);
			timePicker.setCurrentHour(alarmDetails.timeHour);

			edtName.setText(alarmDetails.name);

			chkWeekly.setChecked(alarmDetails.repeatWeekly);
			chkSunday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.SUNDAY));
			chkMonday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.MONDAY));
			chkTuesday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.TUESDAY));
			chkWednesday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.WEDNESDAY));
			chkThursday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.THURSDAY));
			chkFriday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.FRDIAY));
			chkSaturday.setChecked(alarmDetails.getRepeatingDay(AlarmModel.SATURDAY));

			txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmDetails.alarmTone).getTitle(this));
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
				if (alarmDetails.id < 0) {
					dbHelper.createAlarm(alarmDetails);
				} else {
					dbHelper.updateAlarm(alarmDetails);
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
		        	alarmDetails.alarmTone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
		        	TextView txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
		        	txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmDetails.alarmTone).getTitle(this));
		            break;
		        default:
		            break;
	        }
	    }
	}
	
	private boolean verifyUserInput() {
		if(alarmDetails.alarmTone == null) {
			Toast t = Toast.makeText(this, R.string.details_missing_ringtone, Toast.LENGTH_SHORT);
			t.show();
			return false;
		}
		return true;
	}
	
	private void updateModelFromLayout() {
		alarmDetails.timeMinute = timePicker.getCurrentMinute().intValue();
		alarmDetails.timeHour = timePicker.getCurrentHour().intValue();
		alarmDetails.name = edtName.getText().toString();
		alarmDetails.repeatWeekly = chkWeekly.isChecked();
		alarmDetails.setRepeatingDay(AlarmModel.SUNDAY, chkSunday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.MONDAY, chkMonday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.TUESDAY, chkTuesday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.WEDNESDAY, chkWednesday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.THURSDAY, chkThursday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.FRDIAY, chkFriday.isChecked());
		alarmDetails.setRepeatingDay(AlarmModel.SATURDAY, chkSaturday.isChecked());
		alarmDetails.isEnabled = true;
	}
}
