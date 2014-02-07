package com.ryanchipman.nfclock.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class AlarmModel implements Parcelable{
	public static final int SUNDAY = 0;
	public static final int MONDAY = 1;
	public static final int TUESDAY = 2;
	public static final int WEDNESDAY = 3;
	public static final int THURSDAY = 4;
	public static final int FRDIAY = 5;
	public static final int SATURDAY = 6;

	public long id;
	public int timeHour;
	public int timeMinute;
	private boolean repeatingDays[];
	public boolean repeatWeekly;
	public Uri alarmTone;
	public String name;
	public boolean isEnabled;

	public AlarmModel(long id) {
		this.id = id;
		repeatingDays = new boolean[7];
	}
	
	public AlarmModel(Parcel pc) {
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
	
	public AlarmModel() {
		repeatingDays = new boolean[7];
	}

	public void setRepeatingDay(int dayOfWeek, boolean value) {
		repeatingDays[dayOfWeek] = value;
	}

	public boolean getRepeatingDay(int dayOfWeek) {
		return repeatingDays[dayOfWeek];
	}
	
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
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
