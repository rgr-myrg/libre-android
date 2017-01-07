package net.usrlib.libre.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import net.usrlib.libre.R;
import net.usrlib.libre.presenter.Presenter;
import net.usrlib.libre.view.SplashScreenActivity_;

/**
 * Created by rgr-myrg on 1/6/17.
 */

public class FetchDataIntentService extends IntentService {
	public static final int NOTIFICATION = 001;
	public static final int REQUEST_CODE = 0;
	public static final int REQUEST_FLAG = 0;

	public FetchDataIntentService(String name) {
		super(name);
	}

	public FetchDataIntentService() {
		super(FetchDataIntentService.class.getName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Presenter.performDataInstall(getApplicationContext(), hasNewDataInsert -> {
			if (hasNewDataInsert) {
				sendNotification();
			}
		});
	}

	protected void sendNotification() {
		final Context context = getApplicationContext();
		final PendingIntent pendingIntent = PendingIntent.getActivity(
				context,
				REQUEST_CODE,
				new Intent(context, SplashScreenActivity_.class),
				REQUEST_FLAG
		);

		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(
				NOTIFICATION,
				new Notification.Builder(context)
						.setContentIntent(pendingIntent)
						.setContentTitle(getString(R.string.app_title))
						.setContentText(getString(R.string.notification_text))
						.setSmallIcon(R.drawable.common_ic_googleplayservices)
						.setAutoCancel(true)
						.build()
		);
	}
}
