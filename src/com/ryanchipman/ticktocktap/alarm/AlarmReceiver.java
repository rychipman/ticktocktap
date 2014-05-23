package com.ryanchipman.ticktocktap.alarm;

import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;

import com.ryanchipman.ticktocktap.AlarmDismissActivity;
import com.ryanchipman.ticktocktap.AlarmsActivity;
import com.ryanchipman.ticktocktap.R;
import com.ryanchipman.ticktocktap.model.AlarmModel;

public class AlarmReceiver extends BroadcastReceiver {
	
	private static final MediaPlayer mp = new MediaPlayer();
	private static NotificationManager nm; //TODO: some bug with this being null when I go to dismiss notification...
	private static final int NOTIFICATION_ID = 11132011;
 
    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	AlarmModel alarm = intent.getParcelableExtra(AlarmsActivity.EXTRA_MODEL);
    	
    	PendingIntent delIntent = PendingIntent.getActivity(context, 0, new Intent(context, AlarmDismissActivity.class), 0);
    	System.out.println("in ONRECEIVE!");
    	nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = new NotificationCompat.Builder(context)
    	.setOngoing(true)
    	.setAutoCancel(false)
    	.setPriority(Notification.PRIORITY_MAX)
    	.setContentTitle("Alarm Title Here")
    	.setContentText("Wake up! This is an alarm!")
    	.setSmallIcon(R.drawable.ic_launcher)
    	.setContentIntent(delIntent)
    	.build();
        nm.notify(NOTIFICATION_ID, notif);
        
        //TODO: test dealing with stopping previous alarm if it is sounding
        try {
        	if(mp.isPlaying()) {
        		mp.stop();
        		mp.reset();
        	}
	        mp.setAudioStreamType(AudioManager.STREAM_ALARM);
	        mp.setDataSource(context, alarm.getAlarmTone());
	        System.out.println("After SETDATASOURCE");
	        mp.prepare();
	        System.out.println("After PREPARE");
	        mp.start(); 
        } catch(IOException ioe) { 
        	ioe.printStackTrace();//TODO: get some better error handling
        }
    }
    
    public static void dismissAlarms() {
    	if(mp.isPlaying()) {
    		mp.stop();
    		mp.reset();
    	}
    	nm.cancel(NOTIFICATION_ID);
    }
    
    //TODO: prevent closing app from stopping alarm
}