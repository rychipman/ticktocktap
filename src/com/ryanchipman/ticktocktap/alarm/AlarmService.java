package com.ryanchipman.ticktocktap.alarm;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.ryanchipman.ticktocktap.AlarmsActivity;
import com.ryanchipman.ticktocktap.model.AlarmModel;

public class AlarmService extends IntentService {
     
    public static final String ACTION_CREATE = "CREATE";
    public static final String ACTION_UPDATE = "UPDATE";
    public static final String ACTION_CANCEL = "CANCEL";
    private final int WEEK_IN_MILLIS = 604800000;
    private final int NO_REPEAT = 8;
     
    private IntentFilter matcher;
 
    public AlarmService() {
        super("AlarmServiceWorkerThread");
        matcher = new IntentFilter();
        matcher.addAction(ACTION_CREATE);
        matcher.addAction(ACTION_CANCEL);
        matcher.addAction(ACTION_UPDATE);
    }
 
    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        AlarmModel alarm = intent.getParcelableExtra(AlarmsActivity.EXTRA_MODEL);
        if (matcher.matchAction(action)) {          
            execute(action, alarm);
        }
    }
    
	private void execute(String action, AlarmModel alarm) {
        if (ACTION_CREATE.equals(action)) {
        	if(!alarm.repeatsWeekly())
        		setAlarm(alarm, NO_REPEAT);
        	else {
	        	for(int i=Calendar.SUNDAY; i<=Calendar.SATURDAY; i++) {
	        		if(alarm.getRepeatingDay(i))
	        			setAlarm(alarm, i);
	        	}
        	}
        } else if (ACTION_UPDATE.equals(action)) {
        	if(!alarm.repeatsWeekly()) {
        		setAlarm(alarm, NO_REPEAT);
        		for(int i=Calendar.SUNDAY; i<=Calendar.SATURDAY; i++)
        			deleteAlarm(alarm,i);
        	} else {
        		for(int i=Calendar.SUNDAY; i<=Calendar.SATURDAY; i++) {
	            	if(alarm.getRepeatingDay(i))
	            		setAlarm(alarm, i);
	            	else
	            		deleteAlarm(alarm, i);
	            }
        	}
        } else if (ACTION_CANCEL.equals(action)) {
        	for(int i=Calendar.SUNDAY; i<=Calendar.SATURDAY; i++) {
            	if(alarm.getRepeatingDay(i))
            		deleteAlarm(alarm, i);
            }
        	deleteAlarm(alarm, NO_REPEAT);
        }
    }
	
	@SuppressLint("NewApi")
	private void setAlarm(AlarmModel alarm, int dayOfWeek) {
		long startTime = 0;
		if(alarm.repeatsWeekly())
			startTime = alarm.getTimeInMillis(dayOfWeek);
		else
			startTime = alarm.getTimeInMillis();
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        i.putExtra(AlarmsActivity.EXTRA_MODEL, alarm);
        //pass in id as unique request code
        PendingIntent pi = PendingIntent.getBroadcast(this, alarm.getAlarmHash(dayOfWeek), i, PendingIntent.FLAG_UPDATE_CURRENT);
        if(alarm.repeatsWeekly()) {
    		am.setRepeating(AlarmManager.RTC_WAKEUP, startTime, WEEK_IN_MILLIS, pi);
    	} else {
        	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    			am.setExact(AlarmManager.RTC_WAKEUP, startTime, pi);
        	} else {
        		am.set(AlarmManager.RTC_WAKEUP, startTime, pi);
        	}
    	}
	}
	
	private void deleteAlarm(AlarmModel alarm, int dayOfWeek) {
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        i.putExtra(AlarmsActivity.EXTRA_MODEL, alarm);
        //pass in id as unique request code
        PendingIntent pi = PendingIntent.getBroadcast(this, alarm.getAlarmHash(dayOfWeek), i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.cancel(pi);
	}
      
}