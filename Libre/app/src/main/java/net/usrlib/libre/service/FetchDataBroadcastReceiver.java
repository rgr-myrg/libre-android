package net.usrlib.libre.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import net.usrlib.libre.BuildConfig;
import net.usrlib.libre.util.Logger;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by rgr-myrg on 1/6/17.
 */

public class FetchDataBroadcastReceiver extends BroadcastReceiver {
	public static final String TAG = FetchDataBroadcastReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, FetchDataIntentService.class));
	}

	public static final void scheduleService(AppCompatActivity activity) {
		final Calendar updateTime = Calendar.getInstance();
		updateTime.setTimeZone(TimeZone.getDefault());
		updateTime.set(Calendar.HOUR_OF_DAY, 12);
		updateTime.set(Calendar.MINUTE, 30);

		final Intent intent = new Intent(
				activity.getBaseContext(), FetchDataBroadcastReceiver.class
		);

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				activity.getBaseContext(),
				FetchDataIntentService.REQUEST_CODE,
				intent,
				PendingIntent.FLAG_CANCEL_CURRENT
		);

		AlarmManager alarm = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

		alarm.setRepeating(
				AlarmManager.RTC_WAKEUP,
				updateTime.getTimeInMillis(),
				AlarmManager.INTERVAL_DAY,
				pendingIntent
		);

		if (BuildConfig.DEBUG) {
			Logger.i(TAG, "scheduleService on " + updateTime.getTime().toString());
		}
	}
}
