package com.ryanchipman.ticktocktap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
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
	
	public static final String PREF_VALUE_THEME_DARK = "Dark";
	public static final String PREF_VALUE_THEME_LIGHT = "Light";
	public static final String PREF_VALUE_THEME_LIGHT_DARK_AB = "Light with Dark Action Bar";
	public static final String PREF_VALUE_THEME_CUSTOM = "Custom";
	
	private static String[] integerPrefs = new String[]{PREF_KEY_SILENCE_AFTER, PREF_KEY_FADE_IN, PREF_KEY_NUM_SNOOZES, PREF_KEY_SNOOZE_LENGTH, PREF_KEY_ALARM_VOLUME};
	private static String[] booleanPrefs = new String[]{PREF_KEY_SHOW_ALARM_ICON, PREF_KEY_USE_DEFAULT_VOLUME};
	private static String[] stringPrefs = new String[]{PREF_KEY_COLOR_SCHEME};	
	
	private SettingsFragment settingsFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingsFragment = new SettingsFragment();
		getFragmentManager().beginTransaction()
	        .replace(android.R.id.content, settingsFragment)
	        .commit();
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    // Set up a listener whenever a key changes
	    settingsFragment.getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	    // Unregister the listener whenever a key changes
	    settingsFragment.getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Preference pref = settingsFragment.findPreference(key);
		String summary = "";
		if(key.equals(PREF_KEY_COLOR_SCHEME)) {
			summary = sharedPreferences.getString(key, "");
		} else if(key.equals(PREF_KEY_SILENCE_AFTER)) {
			int val = getIntegerSetting(this, key);
			if(val > 0) {
				val /= 60000;
				summary = val + " minutes";
			} else {
				summary = "Never";
			}
		} else if(key.equals(PREF_KEY_FADE_IN)) {
			int val = getIntegerSetting(this, key);
			if(val == 0) {
				summary = "None";
			} else if(val < 60000) {
				val /= 1000;
				summary = val + " seconds";
			} else if(val == 60000) {
				summary = "1 minute";
			} else {
				val /= 60000;
				summary = val + " minutes";
			}
		} else if(key.equals(PREF_KEY_NUM_SNOOZES)) {
			int val = getIntegerSetting(this, key);
			summary = val + " snoozes";
		} else if(key.equals(PREF_KEY_SNOOZE_LENGTH)) {
			int val = getIntegerSetting(this, key);
			if(val > 0) {
				val /= 60000;
				summary = val + " minutes";
			} else {
				summary = "Never";
			}
		} else if(key.equals(PREF_KEY_USE_DEFAULT_VOLUME)) {
			if(getBooleanSetting(this, key))
				summary = "Use the system's setting for alarm volume";
			else
				summary = "Use a custom alarm volume";
		} else if(key.equals(PREF_KEY_ALARM_VOLUME)) {
			int val = getIntegerSetting(this, key);
			summary = val + " / 9";
		} else if(key.equals(PREF_KEY_SHOW_ALARM_ICON)) {
			if(getBooleanSetting(this, key))
				summary = "Show icon when alarm is set";
			else
				summary = "Don't show icons for alarms";
		}
		pref.setSummary(summary);
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
		return sharedPref.getBoolean(key, false);
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
