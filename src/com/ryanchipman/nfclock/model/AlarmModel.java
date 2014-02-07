package com.ryanchipman.nfclock.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class that represents the alarm in terms of its fields
 * @author rchipman
 */
public class AlarmModel implements Parcelable{
	//Day constants
	public static final int SUNDAY = 0;
	public static final int MONDAY = 1;
	public static final int TUESDAY = 2;
	public static final int WEDNESDAY = 3;
	public static final int THURSDAY = 4;
	public static final int FRDIAY = 5;
	public static final int SATURDAY = 6;

	//alarm id TODO: figure out what this does and where it gets set
	public long id;
	public int timeHour; //TODO: look into representing this as just a date or timemillis object
	public int timeMinute;
	private boolean repeatingDays[];//which days the alarm repeats
	public boolean repeatWeekly;//does it repeat weekly?
	public Uri alarmTone; //ringtone TODO: add functionality for choosing music or other tones
	public String name; //name of the alarm
	public boolean isEnabled; //if it is enabled

	/**
	 * Basic AlarmModel constructor
	 * @param id the id to give it (i think this is always -1...)
	 */
	public AlarmModel(long id) {
		this.id = id;
		repeatingDays = new boolean[7];
	}
	
	/**
	 * Construct an AlarmModel from a parceled AlarmModel
	 * precondition: all the fields of the parceled AlarmModel have been set properly
	 * @param pc
	 */
	public AlarmModel(Parcel pc) {
		//don't change the order of these! must be same as when parceled
		id = pc.readLong();
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
	}

	public void setRepeatingDay(int dayOfWeek, boolean value) {
		repeatingDays[dayOfWeek] = value;
	}

	public boolean getRepeatingDay(int dayOfWeek) {
		return repeatingDays[dayOfWeek];
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
		dest.writeInt(timeHour);
		dest.writeInt(timeMinute);
		dest.writeBooleanArray(repeatingDays);
		dest.writeValue(repeatWeekly);
		dest.writeValue(alarmTone);
		dest.writeString(name);
		dest.writeValue(isEnabled);
	}
}
