package net.usrlib.libre.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import net.usrlib.libre.R;
import net.usrlib.libre.presenter.Presenter;
import net.usrlib.libre.service.FetchDataBroadcastReceiver;
import net.usrlib.libre.util.Preferences;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.WindowFeature;

/**
 * Created by rgr-myrg on 12/29/16.
 */

@Fullscreen
@EActivity(R.layout.splash_screen_activity)
@WindowFeature(Window.FEATURE_NO_TITLE)

public class SplashScreenActivity extends AppCompatActivity {
	@AfterViews
	@Background
	protected void onAfterViews() {
		// Schedule service for data updates
		if (!Preferences.hasDataInstall(getApplicationContext())){
			FetchDataBroadcastReceiver.scheduleService(this);
		}

		// Install data and move to BookListActivity
		Presenter.performDataInstall(getApplicationContext(), success -> {
			startBookListActivity();
		});
	}

	@UiThread
	protected void startBookListActivity() {
		startActivity(
				new Intent(getBaseContext(), BookListActivity_.class)
		);

		finish();
	}
}
