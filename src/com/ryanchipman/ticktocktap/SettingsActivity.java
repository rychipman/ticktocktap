package com.ryanchipman.ticktocktap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SettingsActivity extends Activity implements
		OnSharedPreferenceChangeListener {
	
	public static final String PREF_KEY_COLOR_SCHEME = "pref_key_colorScheme";
	public static final String PREF_KEY_SILENCE_AFTER = "pref_key_silenceAfter";
	public static final String PREF_KEY_FADE_IN = "pref_key_fadeIn";
	public static final String PREF_KEY_NUM_SNOOZES = "pref_key_numSnoozes";
	public static final String PREF_KEY_SNOOZE_LENGTH = "pref_key_snoozeLength";
	public static final String PREF_KEY_USE_DEFAULT_VOLUME = "pref_key_useDefaultVolume";
	public static final String PREF_KEY_ALARM_VOLUME = "pref_key_alarmVolume";
	public static final String PREF_KEY_SHOW_ALARM_ICON = "pref_key_showAlarmIcon";
	
	public static final String PREF_VALUE_THEME_DARK = "PREF_THEME_DARK";
	public static final String PREF_VALUE_THEME_LIGHT = "PREF_THEME_LIGHT";
	public static final String PREF_VALUE_THEME_CUSTOM = "PREF_THEME_CUSTOM";
	
	private static String[] integerPrefs = new String[]{PREF_KEY_SILENCE_AFTER, PREF_KEY_FADE_IN, PREF_KEY_NUM_SNOOZES, PREF_KEY_SNOOZE_LENGTH, PREF_KEY_ALARM_VOLUME};
	private static String[] booleanPrefs = new String[]{PREF_KEY_SHOW_ALARM_ICON, PREF_KEY_USE_DEFAULT_VOLUME};
	private static String[] stringPrefs = new String[]{PREF_KEY_COLOR_SCHEME};	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
	        .replace(android.R.id.content, new SettingsFragment())
	        .commit();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO: listen for summary changes
	}
	
	public static int getIntegerSetting(Context ctx, String key) {
		boolean correctType = false;
		for(String s : integerPrefs)
			if(s.equals(key))
				correctType = true;
		assert(correctType);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
		return Integer.parseInt(sharedPref.getString(key, "-1"));
	}
	
	public static boolean getBooleanSetting(Context ctx, String key) {
		boolean correctType = false;
		for(String s : booleanPrefs)
			if(s.equals(key))
				correctType = true;
		assert(correctType);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
		return Boolean.parseBoolean(sharedPref.getString(key, "false"));
	}
	
	public static String getStringSetting(Context ctx, String key) {
		boolean correctType = false;
		for(String s : stringPrefs)
			if(s.equals(key))
				correctType = true;
		assert(correctType);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
		return sharedPref.getString(key, "");
	}

}
