package com.ryanchipman.ticktocktap.alarm;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.ryanchipman.ticktocktap.AlarmsActivity;
import com.ryanchipman.ticktocktap.model.AlarmDBHelper;
import com.ryanchipman.ticktocktap.model.AlarmModel;

public class AlarmBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			//TODO: Set the alarms(s)
			AlarmDBHelper dbHelper = new AlarmDBHelper(context);
			List<AlarmModel> alarms = dbHelper.getAlarms();
			for(AlarmModel alarm : alarms) {
				if(alarm.isEnabled()) {
					Intent i = new Intent(context, AlarmService.class);
					i.setAction(AlarmService.ACTION_CREATE);
					i.putExtra(AlarmsActivity.EXTRA_MODEL, alarm);
					context.startService(i);
				}
			}
		}
	}
	
	public static void setBootReceiverEnabled(Context ctx, boolean enabled) {
		ComponentName receiver = new ComponentName(ctx, AlarmBootReceiver.class);
		PackageManager pm = ctx.getPackageManager();
		if(enabled) {
			pm.setComponentEnabledSetting(receiver,
			        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
			        PackageManager.DONT_KILL_APP);
		} else {
			pm.setComponentEnabledSetting(receiver,
			        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
			        PackageManager.DONT_KILL_APP);
		}
	}

}
