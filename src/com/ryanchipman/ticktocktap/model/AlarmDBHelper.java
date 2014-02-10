package com.ryanchipman.ticktocktap.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.ryanchipman.ticktocktap.model.AlarmContract.Alarm;

public class AlarmDBHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "alarmclock.db";

	private static final String SQL_CREATE_ALARM = 
		"CREATE TABLE " + Alarm.TABLE_NAME + " (" +
		Alarm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		Alarm.COLUMN_NAME_ALARM_NAME + " TEXT," +
		Alarm.COLUMN_NAME_ALARM_TIME_HOUR + " INTEGER," +
		Alarm.COLUMN_NAME_ALARM_TIME_MINUTE + " INTEGER," +
		Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS + " TEXT," +
		Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY + " BOOLEAN," +
		Alarm.COLUMN_NAME_ALARM_TONE + " TEXT," +
		Alarm.COLUMN_NAME_ALARM_ENABLED + " BOOLEAN" + " )";

	private static final String SQL_DELETE_ALARM =
		    "DROP TABLE IF EXISTS " + Alarm.TABLE_NAME;

	public AlarmDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ALARM);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ALARM);
        onCreate(db);
	}

	private AlarmModel populateModel(Cursor c) {
		AlarmModel model = new AlarmModel();
		model.setID(c.getLong(c.getColumnIndex(Alarm._ID)));
		model.setName(c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_NAME)));
		model.setTimeHour(c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TIME_HOUR)));
		model.setTimeMinute(c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TIME_MINUTE)));
		model.setRepeatWeekly(c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY)) == 0 ? false : true);
		model.setAlarmTone(Uri.parse(c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TONE))));
		model.setEnabled(c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_ENABLED)) == 0 ? false : true);

		String[] repeatingDays = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS)).split(",");
		for (int i = AlarmModel.SUNDAY; i < repeatingDays.length; ++i) {
			model.setRepeatingDay(i, repeatingDays[i].equals("false") ? false : true);
		}

		return model;
	}

	private ContentValues populateContent(AlarmModel model) {
		ContentValues values = new ContentValues();
		values.put(Alarm.COLUMN_NAME_ALARM_NAME, model.getName());
		values.put(Alarm.COLUMN_NAME_ALARM_TIME_HOUR, model.getTimeHour());
		values.put(Alarm.COLUMN_NAME_ALARM_TIME_MINUTE, model.getTimeMinute());
		values.put(Alarm.COLUMN_NAME_ALARM_REPEAT_WEEKLY, model.repeatsWeekly());
		values.put(Alarm.COLUMN_NAME_ALARM_TONE, model.getAlarmTone().toString());

		String repeatingDays = "";
		for (int i = AlarmModel.SUNDAY; i <= AlarmModel.SATURDAY; ++i) {
			repeatingDays += model.getRepeatingDay(i) + ",";
		}
		values.put(Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS, repeatingDays);

		return values;
	}
	
	/**
	 * Create a new alarm based on the given model
	 * @param model the AlarmModel containing the representation of an alarm
	 * @return the primary key of the newly created row, which is also assigned to model.id
	 */
	public long createAlarm(AlarmModel model) {
		ContentValues values = populateContent(model);
		return getWritableDatabase().insert(Alarm.TABLE_NAME, null, values);
	}

	public AlarmModel getAlarm(long id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String select = "SELECT * FROM " + Alarm.TABLE_NAME + " WHERE " + Alarm._ID + " = " + id;

		Cursor c = db.rawQuery(select, null);

		if (c.moveToNext()) {
			return populateModel(c);
		}

		return null;
	}
	
	public long updateAlarm(AlarmModel model) {
		ContentValues values = populateContent(model);
		return getWritableDatabase().update(Alarm.TABLE_NAME, values, Alarm._ID + " = ?", new String[] { String.valueOf(model.getID()) });
	}

	public int deleteAlarm(long id) {
		return getWritableDatabase().delete(Alarm.TABLE_NAME, Alarm._ID + " = ?", new String[] { String.valueOf(id) });
	}
	
	public List<AlarmModel> getAlarms() {
		SQLiteDatabase db = this.getReadableDatabase();

		String select = "SELECT * FROM " + Alarm.TABLE_NAME;

		Cursor c = db.rawQuery(select, null);

		List<AlarmModel> alarmList = new ArrayList<AlarmModel>();

		while (c.moveToNext()) {
			alarmList.add(populateModel(c));
		}

		if (!alarmList.isEmpty()) {
			return alarmList;
		}

		return null;
	}
}
