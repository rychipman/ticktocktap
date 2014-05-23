package com.ryanchipman.ticktocktap.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;

import com.ryanchipman.ticktocktap.AlarmDismissActivity;
import com.ryanchipman.ticktocktap.AlarmsActivity;
import com.ryanchipman.ticktocktap.R;
import com.ryanchipman.ticktocktap.model.AlarmModel;

public class AlarmReceiver extends BroadcastReceiver {
	
	private NotificationManager nm;
	public static final int NOTIFICATION_ID = 11132011;
 
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
    	.setDefaults(Notification.DEFAULT_LIGHTS)
    	
    	.setContentTitle("Alarm Title Here")
    	.setContentText("Wake up! This is an alarm!")
    	.setSmallIcon(R.drawable.ic_launcher)
    	.setContentIntent(delIntent)
    	.build();
        nm.notify(NOTIFICATION_ID, notif);
        
        Intent i = new Intent(context, RingerService.class);
		i.setAction(RingerService.ACTION_START);
		i.putExtra(AlarmsActivity.EXTRA_MODEL, alarm);
		context.startService(i);
    }
    
    //TODO: prevent closing app from stopping alarm
}