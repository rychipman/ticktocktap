package com.ryanchipman.nfclock.alarm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * Acquires a partial WakeLock, allows TaskButtlerService to keep the CPU alive
 * until the work is done.
 * @author Dhimitraq Jorgji
 *
 */
public class WakefulIntentService extends IntentService {
	public static final String LOCK_NAME_STATIC="com.ryanchipman.nfclock.alarm.TaskButlerService.Static";
	public static final String LOCK_NAME_LOCAL="com.ryanchipman.neclock.alarm.TaskButlerService.Local";
	private static PowerManager.WakeLock lockStatic=null;
	private PowerManager.WakeLock lockLocal=null;
	
	public WakefulIntentService(String name) {
		super(name);
	}
	/**
     * Acquire a partial static WakeLock, you need too call this within the class
     * that calls startService()
     * @param context
     */

	public static void acquireStaticLock(Context context) {
		getLock(context).acquire();
		}

	synchronized private static PowerManager.WakeLock getLock(Context context) {
		if (lockStatic==null) {
			PowerManager mgr=(PowerManager)context.getSystemService(Context.POWER_SERVICE);
			lockStatic=mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,LOCK_NAME_STATIC);
			lockStatic.setReferenceCounted(true);
		}
		return(lockStatic);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		PowerManager mgr=(PowerManager)getSystemService(Context.POWER_SERVICE);
		lockLocal=mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,LOCK_NAME_LOCAL);
		lockLocal.setReferenceCounted(true);
	}
	
	@Override
	public void onStart(Intent intent, final int startId) {
		lockLocal.acquire();
		super.onStart(intent, startId);
		getLock(this).release();
		}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		lockLocal.release();
	}
}