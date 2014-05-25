package com.ryanchipman.ticktocktap;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ryanchipman.ticktocktap.alarm.AlarmReceiver;
import com.ryanchipman.ticktocktap.alarm.RingerService;

public class AlarmDismissActivity extends Activity {
	//TODO: add some sort of NFC writing capability
	//TODO: some sort of encryption/private-key strategy on tag to prevent home duplication?
	//TODO: add some sort of emergency backup shutoff?
	//TODO: add some sort of text to/from system?
	//TODO: add warnings/alarms if battery low while alarm is set
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_dismiss);
		//TODO: make action bar, etc. invisible?
		Button b = (Button) findViewById(R.id.dismiss_button);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), RingerService.class);
				i.setAction(RingerService.ACTION_STOP);
				v.getContext().startService(i);
		    	((NotificationManager) v.getContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancel(AlarmReceiver.NOTIFICATION_ID);
				finish();
			}
		});
	}
	//TODO: clean up strings and naming conventions in this xml file

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarm_dismiss, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
