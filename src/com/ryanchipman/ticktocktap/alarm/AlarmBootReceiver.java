package com.ryanchipman.ticktocktap.alarm;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class AlarmBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			//TODO: Set the alarms(s)
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
