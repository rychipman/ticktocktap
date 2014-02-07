package com.ryanchipman.nfclock.alarm;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.ryanchipman.nfclock.AlarmsActivity;
import com.ryanchipman.nfclock.model.AlarmModel;

public class AlarmService extends IntentService {
     
    public static final String ACTION_CREATE = "CREATE";
    public static final String ACTION_CANCEL = "CANCEL";
     
    private IntentFilter matcher;
 
    public AlarmService() {
        super("AlarmServiceWorkerThread");
        matcher = new IntentFilter();
        matcher.addAction(ACTION_CREATE);
        matcher.addAction(ACTION_CANCEL);
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
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(Calendar.HOUR_OF_DAY, alarm.timeHour);
    	calendar.set(Calendar.MINUTE, alarm.timeMinute);
    	long startTime = calendar.getTimeInMillis();
    	
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        i.putExtra(AlarmsActivity.EXTRA_MODEL, alarm);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        if (ACTION_CREATE.equals(action)) {
            am.set(AlarmManager.RTC_WAKEUP, startTime, pi);
            //TODO: use set exact for new apis
             
        } else if (ACTION_CANCEL.equals(action)) {
            am.cancel(pi);
        }
    }
      
}