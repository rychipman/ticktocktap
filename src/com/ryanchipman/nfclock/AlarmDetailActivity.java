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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ryanchipman.nfclock.model.AlarmModel;

public class AlarmDetailActivity extends Activity {
	
	private AlarmModel alarmDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_detail);
		alarmDetails = new AlarmModel();
		// Show the Up button in the action bar.
		setupActionBar();
		
		final LinearLayout ringToneContainer = (LinearLayout) findViewById(R.id.alarm_ringtone_container);
		ringToneContainer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
				startActivityForResult(intent , 1);
			}
		});
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
            updateModelFromLayout();
            finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
	        switch (requestCode) {
		        case 1: {
		        	alarmDetails.alarmTone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
		        	TextView txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
		        	txtToneSelection.setText(RingtoneManager.getRingtone(this, alarmDetails.alarmTone).getTitle(this));
		            break;
		        }
		        default: {
		            break;
		        }
	        }
	    }
	}
	
	private void updateModelFromLayout() {
		TimePicker timePicker = (TimePicker) findViewById(R.id.alarm_details_time_picker);
		alarmDetails.timeMinute = timePicker.getCurrentMinute().intValue();
		alarmDetails.timeHour = timePicker.getCurrentHour().intValue();

		EditText edtName = (EditText) findViewById(R.id.alarm_details_name);
		alarmDetails.name = edtName.getText().toString();

		CheckBox chkWeekly = (CheckBox) findViewById(R.id.alarm_details_repeat_weekly);
		alarmDetails.repeatWeekly = chkWeekly.isChecked();

		CheckBox chkSunday = (CheckBox) findViewById(R.id.alarm_details_repeat_sunday);
		alarmDetails.setRepeatingDay(AlarmModel.SUNDAY, chkSunday.isChecked());

		CheckBox chkMonday = (CheckBox) findViewById(R.id.alarm_details_repeat_monday);
		alarmDetails.setRepeatingDay(AlarmModel.MONDAY, chkMonday.isChecked());

		CheckBox chkTuesday = (CheckBox) findViewById(R.id.alarm_details_repeat_tuesday);
		alarmDetails.setRepeatingDay(AlarmModel.TUESDAY, chkTuesday.isChecked());

		CheckBox chkWednesday = (CheckBox) findViewById(R.id.alarm_details_repeat_wednesday);
		alarmDetails.setRepeatingDay(AlarmModel.WEDNESDAY, chkWednesday.isChecked());

		CheckBox chkThursday = (CheckBox) findViewById(R.id.alarm_details_repeat_thursday);
		alarmDetails.setRepeatingDay(AlarmModel.THURSDAY, chkThursday.isChecked());

		CheckBox chkFriday = (CheckBox) findViewById(R.id.alarm_details_repeat_friday);
		alarmDetails.setRepeatingDay(AlarmModel.FRDIAY, chkFriday.isChecked());

		CheckBox chkSaturday = (CheckBox) findViewById(R.id.alarm_details_repeat_saturday);
		alarmDetails.setRepeatingDay(AlarmModel.SATURDAY, chkSaturday.isChecked());

		alarmDetails.isEnabled = true;
	}
}
