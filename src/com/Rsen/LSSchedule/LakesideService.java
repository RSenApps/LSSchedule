package com.Rsen.LSSchedule;

import java.sql.Date;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;

public class LakesideService extends Service {
	NotificationManager nm;
	SharedPreferences prefs;
	Boolean appCalled;

	@Override
	public void onCreate() {

		super.onCreate();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		

	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		appCalled = intent.getBooleanExtra("appCalled", false);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		updateNotification();
		if (!prefs.getBoolean("Enable_Notifications", true)) {
			nm.cancel(R.string.app_name);
			
			//stopSelf();
		}
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	/*
	 * private void addBroadcastReceiver() {
	 * 
	 * // TODO Auto-generated method stub BroadcastReceiver timeChanged = new
	 * TimeChanged(); IntentFilter intentFilter = new
	 * IntentFilter(Intent.ACTION_TIME_TICK);
	 * 
	 * this.registerReceiver(timeChanged, intentFilter);
	 * 
	 * 
	 * 
	 * 
	 * 
	 * }
	 */
	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	public void updateNotification() {

		DictionaryOpenHelper dh = new DictionaryOpenHelper(this);
		int alarmRegular = 98123;
		int alarmWakeup = 98124;
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_YEAR, 1);
		// today.add(Calendar.HOUR_OF_DAY, 20);
		 //today.add(Calendar.MINUTE, 35);
		int DayofWeek = today.get(Calendar.DAY_OF_WEEK) - 1;
		Date daytoDisplayDate = new Date(today.get(Calendar.YEAR),
				today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
		Cursor switchForToday = dh.getSwitchforToday(daytoDisplayDate);
		final AlarmManager alarm = (AlarmManager) this
				.getSystemService(Context.ALARM_SERVICE);
		final Intent intentService = new Intent(this, LakesideService.class);
		final PendingIntent pendingServiceRegular = PendingIntent.getService(
				this, alarmRegular, intentService, 0);
		final PendingIntent pendingServiceWakeup = PendingIntent.getService(
				this, alarmWakeup, intentService, 0);
		alarm.cancel(pendingServiceRegular);
		alarm.cancel(pendingServiceWakeup);
		if (switchForToday.getCount() > 0) {
			switchForToday.moveToFirst();
			DayofWeek = switchForToday.getInt(1);
		}
		
		if (DayofWeek == 6) {
			DayofWeek = 8;
		}
		int nowMin = today.get(Calendar.HOUR_OF_DAY) * 60
				+ today.get(Calendar.MINUTE);

		Object[] lastCurrentClass = dh.getLastCurrentClass(nowMin, DayofWeek);
		if (lastCurrentClass == null) {
			lastCurrentClass = new Object[2];
			lastCurrentClass[0] = -1;
			lastCurrentClass[1] = "None";
		}
		Object[] nextClass = dh.getNextClass(nowMin, DayofWeek);
		if (nextClass == null) {
			nextClass = new Object[3];
			if ((Integer) lastCurrentClass[0] > nowMin) {
				nextClass[0] = (Integer) lastCurrentClass[0] - nowMin;
				nextClass[1] = "End of School";
				nextClass[2] = lastCurrentClass[0];
			}
			else
			{
				nm.cancel(R.string.app_name);
				long alarmTime = today.getTimeInMillis()
						+ ((23 - today.get(Calendar.HOUR_OF_DAY)) * 60 * 60 * 1000)
						+ ((60 - today.get(Calendar.MINUTE)) * 60 * 1000);
					alarm.set(AlarmManager.RTC, alarmTime, pendingServiceRegular);
				
				nextClass[0] = -1;
				nextClass[1] = "None";
				nextClass[2] = -1;
			}
		}

		int minUntilNextClass = (Integer) nextClass[0];
		String nextClassName = (String) nextClass[1];
		int nextClassEnd = (Integer) nextClass[2];
		int lastCurrentClassEnd = (Integer) lastCurrentClass[0];
		String lastCurrentClassName = (String) lastCurrentClass[1];
		int notificationShowTime = 5;
		int minUntilAlarm;

		if (prefs.getString("Notification_show_time", "") != "") {
			notificationShowTime = Integer.parseInt(prefs.getString(
					"Notification_show_time", ""));
		}
		if (!prefs.getBoolean("Enable_Notifications", true)) {
			notificationShowTime=0;
		}
		// show no notification
		if ((minUntilNextClass > notificationShowTime)
				&& !(prefs.getBoolean("Notification_show_frees", true)
						&& (lastCurrentClassName.matches("Free") || (lastCurrentClassName
								.matches("Lunch"))) && nowMin < lastCurrentClassEnd)) { // !(prefs.getBoolean("Notification_vibrate",
																						// false)
																						// &&
																						// nowMin<=lastCurrentClassEnd)
			nm.cancel(R.string.app_name);
			minUntilAlarm = minUntilNextClass - notificationShowTime;
		} else {
			minUntilAlarm = 1;
			PendingIntent contentIntent = PendingIntent.getActivity(this,
					1282134,

					new Intent(this, LakesideScheduleActivity.class), 0);
			if (!nextClassName.matches("None") && prefs.getBoolean("Enable_Notifications", true)) {
				Notification notification = new Notification(
						R.drawable.statusbaricon, "Next Class: "
								+ nextClassName + " in " + minUntilNextClass
								+ " min", today.getTimeInMillis());
				notification.flags |= Notification.FLAG_ONGOING_EVENT;
				notification.setLatestEventInfo(this, "Next Class: "
						+ nextClassName, "in " + minUntilNextClass + " min",
						contentIntent);
				nm.notify(R.string.app_name, notification);
			}
			if ((nowMin == lastCurrentClassEnd) && !(appCalled)){
				if (prefs.getBoolean("Notification_vibrate", false))
				{
					Vibrator vibrator = (Vibrator) this
							.getSystemService(VIBRATOR_SERVICE);
					vibrator.vibrate(new long[] { 0, 200, 200, 200, 500, 200, 200,
							200 }, -1);
				}
				String ringVolumeClassEnd = prefs.getString("Ring_Volume_Class_End", "Keep Unchanged");
				if (!ringVolumeClassEnd.matches("Keep Unchanged"))
				{
					AudioManager audioM = (AudioManager) this.getSystemService(AUDIO_SERVICE);
					if (ringVolumeClassEnd.matches("Silent"))
					{
						audioM.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					}
					if (ringVolumeClassEnd.matches("Vibrate"))
					{
						audioM.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
					}
					if (ringVolumeClassEnd.matches("Lowest Volume"))
					{
						audioM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						audioM.setStreamVolume(AudioManager.STREAM_RING, 1, 0);
					}
					if (ringVolumeClassEnd.matches("Medium Volume"))
					{
						audioM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						audioM.setStreamVolume(AudioManager.STREAM_RING, audioM.getStreamMaxVolume(AudioManager.STREAM_RING)/2, 0);
					}
					if (ringVolumeClassEnd.matches("Highest Volume"))
					{
						audioM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						audioM.setStreamVolume(AudioManager.STREAM_RING, audioM.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
					}
				}
				//DEBUG
				/*
				PendingIntent contentIntentClassEnd = PendingIntent.getActivity(this,
						1282134,
						new Intent(this, LakesideScheduleActivity.class), 0);
				Notification notification = new Notification(
						R.drawable.statusbaricon, "Class Over: "
								+ lastCurrentClassName, today.getTimeInMillis());
				notification.setLatestEventInfo(this, "Class Over: "
						+ lastCurrentClassName,"",
						contentIntentClassEnd);
				nm.notify(R.string.app_name + new Random().nextInt(999999999), notification);
				*/
				//DEBUG
			}
			if ((minUntilNextClass == 1) && !(appCalled)){
				String ringVolumeClassStart = prefs.getString("Ring_Volume_Class_Start", "Keep Unchanged");
				if (!ringVolumeClassStart.matches("Keep Unchanged"))
				{
					AudioManager audioM = (AudioManager) this.getSystemService(AUDIO_SERVICE);
					if (ringVolumeClassStart.matches("Silent"))
					{
						audioM.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					}
					if (ringVolumeClassStart.matches("Vibrate"))
					{
						audioM.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
					}
					if (ringVolumeClassStart.matches("Lowest Volume"))
					{
						audioM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						audioM.setStreamVolume(AudioManager.STREAM_RING, 1, 0);
					}
					if (ringVolumeClassStart.matches("Medium Volume"))
					{
						audioM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						audioM.setStreamVolume(AudioManager.STREAM_RING, audioM.getStreamMaxVolume(AudioManager.STREAM_RING)/2, 0);
					}
					if (ringVolumeClassStart.matches("Highest Volume"))
					{
						audioM.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						audioM.setStreamVolume(AudioManager.STREAM_RING, audioM.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
					}
				}
			}
		}

		long alarmTimeRegular = today.getTimeInMillis()
				- today.get(Calendar.SECOND) * 1000 + minUntilAlarm * 60 * 1000 - 2 *24*60*60*1000; //DEBUG
		int minUntilWakeupAlarm = 0;
		if (lastCurrentClassEnd > nowMin) {
			minUntilWakeupAlarm = lastCurrentClassEnd - nowMin;
		} else if (nextClassEnd > nowMin) {
			minUntilWakeupAlarm = nextClassEnd - nowMin;
		}
		long alarmTimeWakeup = today.getTimeInMillis()
				- today.get(Calendar.SECOND) * 1000 + (minUntilWakeupAlarm)
				* 60 * 1000;
		if (!nextClassName.matches("None")) {
			alarm.set(AlarmManager.RTC, alarmTimeRegular, pendingServiceRegular);
		}
		if (!lastCurrentClassName.matches("None")
				&& prefs.getBoolean("Notification_vibrate", false)
				&& (lastCurrentClassEnd > nowMin || nextClassEnd > nowMin)) {
			alarm.set(AlarmManager.RTC_WAKEUP, alarmTimeWakeup,
					pendingServiceWakeup);
		}

		dh.close();

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}