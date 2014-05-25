package com.ryanchipman.ticktocktap.alarm;

import java.io.IOException;

import com.ryanchipman.ticktocktap.AlarmDismissActivity;
import com.ryanchipman.ticktocktap.AlarmsActivity;
import com.ryanchipman.ticktocktap.R;
import com.ryanchipman.ticktocktap.model.AlarmModel;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class RingerService extends Service {

	private MediaPlayer mp;
	public static final String ACTION_START = "START";
	public static final String ACTION_STOP = "STOP";

	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // No intent, tell the system not to restart us.
        if (intent == null) {
            stopSelf();
            return START_NOT_STICKY;
        }
        
        if(intent.getAction().equals(ACTION_STOP))
        	stopSelf();
        else {
	        final AlarmModel alarm = intent.getParcelableExtra(AlarmsActivity.EXTRA_MODEL);
	        Uri alarmTone = alarm.getAlarmTone();
	        play(alarmTone);
        }

        return START_STICKY;
    }
	
	private void play(Uri alarmTone) {
		
		//TODO: test dealing with stopping previous alarm if it is sounding
		mp = new MediaPlayer();
		mp.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                System.out.println("Audio Playbeck Error");
                mp.stop();
                mp.release();
                mp = null;
                return true;
            }
        });
		//TODO: lock volume at max!
		try {
			mp.setDataSource(this, alarmTone);
			mp.setAudioStreamType(AudioManager.STREAM_ALARM);
	        mp.setLooping(true);
			mp.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        mp.start();
	}
	
	private void stop() {
		if(mp != null) {
			mp.stop();
			mp.release();
			mp = null;
		}
	}
	
	@Override
	public void onDestroy() {
		stop();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
