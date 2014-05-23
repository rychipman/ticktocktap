package com.ryanchipman.ticktocktap.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class that represents the alarm in terms of its fields
 * @author rchipman
 */
public class AlarmModel implements Parcelable{
	//Day constants
	public static final int SUNDAY = 1;
	public static final int MONDAY = 2;
	public static final int TUESDAY = 3;
	public static final int WEDNESDAY = 4;
	public static final int THURSDAY = 5;
	public static final int FRDIAY = 6;
	public static final int SATURDAY = 7;

	private long id;
	private int[] hashes;
	private int timeHour;
	private int timeMinute;
	private boolean repeatingDays[];//which days the alarm repeats
	private boolean repeatWeekly;//does it repeat weekly?
	private Uri alarmTone; //ringtone TODO: add functionality for choosing music or other tones
	private String name; //name of the alarm
	private boolean isEnabled; //if it is enabled

	/**
	 * Basic AlarmModel constructor
	 * @param id the id to give it (i think this is always -1...)
	 */
	public AlarmModel(long id) {
		this.id = id;
		repeatingDays = new boolean[7];
		hashes = new int[7];
	}
	
	/**
	 * Construct an AlarmModel from a parceled AlarmModel
	 * precondition: all the fields of the parceled AlarmModel have been set properly
	 * @param pc
	 */
	public AlarmModel(Parcel pc) {
		//don't change the order of these! must be same as when parceled
		id = pc.readLong();
		hashes = new int[7];
		pc.readIntArray(hashes);
		timeHour = pc.readInt();
		timeMinute = pc.readInt();
		repeatingDays = new boolean[7];
		pc.readBooleanArray(repeatingDays);
		repeatWeekly = (Boolean) pc.readValue(null);
		alarmTone = (Uri) pc.readValue(null);
		name = pc.readString();
		isEnabled = (Boolean) pc.readValue(null);
	}
	
	/**
	 * Most basic constructor
	 */
	public AlarmModel() {
		id = -1;
		repeatingDays = new boolean[7];
		hashes = new int[7];
	}

	public void setRepeatingDay(int dayOfWeek, boolean value) {
		repeatingDays[dayOfWeek-1] = value;
	}

	public boolean getRepeatingDay(int dayOfWeek) {
		return repeatingDays[dayOfWeek-1];
	}
	
	public void setID(long id) {
		this.id = id;
		createHashes();
	}
	
	public long getID() {
		return id;
	}
	
	public void setTimeHour(int hour) {
		timeHour = hour;
	}
	
	public int getTimeHour() {
		return timeHour;
	}
	
	public void setTimeMinute(int minute) {
		timeMinute = minute;
	}
	
	public int getTimeMinute() {
		return timeMinute;
	}
	
	public void setRepeatWeekly(boolean repeat) {
		repeatWeekly = repeat;
	}
	
	public boolean repeatsWeekly() {
		return repeatWeekly;
	}
	
	public void setAlarmTone(Uri tone) {
		this.alarmTone = tone;
	}
	
	public Uri getAlarmTone() {
		return alarmTone;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public int getAlarmHash(int dayOfWeek) {
		return hashes[dayOfWeek-1];
		//bacause sunday is 1 and saturday is 8
	}
	
	private void createHashes() {
		for(int i=0; i<7; i++) {
			hashes[i] = 7*((Long) id).intValue() + i;
		}
	}
	
	/**
	 * Parcelable creator method, used for automatic parceling
	 */
	public static final Parcelable.Creator<AlarmModel> CREATOR = new Parcelable.Creator<AlarmModel>() {
		@Override
		public AlarmModel createFromParcel(Parcel source) {
			return new AlarmModel(source);
		}

		@Override
		public AlarmModel[] newArray(int size) {
			return new AlarmModel[size];
		}
	};

	/**
	 * left at defaults...don't really know what this is for, and looking
	 * it up is not high on the agenda right now
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * write all instance fields to a parcel
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		//Do not change order...must be same as unparceling order
		dest.writeLong(id);
		dest.writeIntArray(hashes);
		dest.writeInt(timeHour);
		dest.writeInt(timeMinute);
		dest.writeBooleanArray(repeatingDays);
		dest.writeValue(repeatWeekly);
		dest.writeValue(alarmTone);
		dest.writeString(name);
		dest.writeValue(isEnabled);
	}
}
