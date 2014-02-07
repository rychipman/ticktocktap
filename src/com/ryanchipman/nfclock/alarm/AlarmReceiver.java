package com.ryanchipman.nfclock.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;

import com.ryanchipman.nfclock.AlarmsActivity;
import com.ryanchipman.nfclock.R;
import com.ryanchipman.nfclock.model.AlarmModel;

public class AlarmReceiver extends BroadcastReceiver {
 
    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	AlarmModel alarm = intent.getParcelableExtra(AlarmsActivity.EXTRA_MODEL);
    	
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = new Notification.Builder(context)
//    	.setOngoing(true)
//    	.setAutoCancel(false)
    	.setPriority(Notification.PRIORITY_MAX)
    	.setContentTitle("Alarm Title Here")
    	.setContentText("Wake up! This is an alarm!")
    	.setSmallIcon(R.drawable.ic_launcher)
    	.setSound(alarm.alarmTone, AudioManager.STREAM_ALARM)
    	.build();    
         
        nm.notify(1212, notif);
    }
 
}