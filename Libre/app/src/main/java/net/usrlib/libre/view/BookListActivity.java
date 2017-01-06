package net.usrlib.libre.view;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.ads.AdView;

import net.usrlib.libre.R;
import net.usrlib.libre.util.AdRequestUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rgr-myrg on 12/8/16.
 */

@EActivity(R.layout.book_list_activity)
@OptionsMenu(R.menu.menu_main)

public class BookListActivity extends AppCompatActivity {
	@ViewById(R.id.toolbar)
	protected Toolbar mToolbar;

	@ViewById(R.id.adView)
	protected AdView mAdView;

	@AfterViews
	protected void setSupportActionBar() {
		setSupportActionBar(mToolbar);
	}

	@AfterViews
	protected void requestBannerAd() {
		AdRequestUtil.makeAdRequest(mAdView);
	}

	@OptionsItem(R.id.action_settings)
	protected void onMenuSettingsSelected() {
		Log.d("BOOK", "onMenuSettingsSelected");
	}
}
