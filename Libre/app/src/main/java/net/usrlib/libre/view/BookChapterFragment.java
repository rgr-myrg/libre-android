package net.usrlib.libre.view;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import net.usrlib.libre.BuildConfig;
import net.usrlib.libre.R;
import net.usrlib.libre.model.BookItem;
import net.usrlib.libre.presenter.Presenter;
import net.usrlib.libre.rest.HtmlLoader;
import net.usrlib.libre.util.Logger;
import net.usrlib.libre.util.Preferences;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by rgr-myrg on 1/4/17.
 */

@EFragment(R.layout.book_chapter_item)
public class BookChapterFragment extends Fragment {
	public static final String TAG = BookChapterFragment.class.getSimpleName();
	public static final String MIME_TYPE = "text/html; charset=utf-8";
	public static final String ENCODING  = "utf-8";

	protected BookItem mBookItem = null;

	@ViewById(R.id.progress_bar)
	protected ProgressBar mProgressBar;

	@ViewById(R.id.content_web_view)
	protected WebView mWebView;

	protected Handler mHandler = new Handler();
	protected final Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			if (mWebView.getContentHeight() > 0 && mWebView.getProgress() == 100) {
				mProgressBar.setVisibility(View.GONE);
				mHandler.removeCallbacks(this);
			} else {
				mHandler.postDelayed(this, 100);
			}
		}
	};

	@AfterViews
	protected void loadWebView() {
		setRetainInstance(true);

		// Check if WebView is loaded
		//mHandler = new Handler();
		mHandler.postDelayed(mRunnable, 100);

		//mWebView.getSettings().setDefaultFontSize(Preferences.getFontSize(getContext()));
		// Save this WebView font size as a starting point. Override default.
		if (Preferences.getFontSize(getContext()) == Preferences.DEFAULT_FONT_SIZE) {
			Preferences.setFontSize(getContext(), mWebView.getSettings().getTextZoom());
		}

		setWebViewFontSizeFromPreferences();

		// Use cached html preferably
		if (mBookItem.hasHtmlCache()) {
			if (BuildConfig.DEBUG) {
				Logger.i(TAG, "Loading html from cache");
			}

			mWebView.loadData(mBookItem.getHtmlCache(), MIME_TYPE, ENCODING);
			return;
		}

		// Otherwise, retrieve html and save it to the db
		new HtmlLoader()
				.fetchWithUrl(mBookItem.getContentEN())
				.onRequestComplete(html -> {
					if (BuildConfig.DEBUG) {
						Logger.i(TAG, "Loaded html from server");
					}

					mWebView.loadData(html, MIME_TYPE, ENCODING);
					saveHtmlCache(html);
				})
				.start();
	}

	@Background
	protected void saveHtmlCache(final String html) {
		mBookItem.setHtmlCache(html);
		Presenter.saveHtmlCache(getContext(), mBookItem.getItemId(), html, success -> {
			if (BuildConfig.DEBUG) {
				Logger.i(TAG, "saveHtmlCache returns " + success);
			}
		});
	}

	public void setWebViewFontSizeFromPreferences() {
		if (mWebView == null) {
			return;
		}

		mWebView.getSettings().setTextZoom(Preferences.getFontSize(getContext()));
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onFontSizeChangedEvent(Presenter.FontSizeChangedEvent event) {
		if (BuildConfig.DEBUG) {
			Logger.i(TAG, "onFontSizeChangedEvent " + event.fontSize);
		}

		mWebView.getSettings().setTextZoom(event.fontSize);
	}

	@Override
	public void onStart() {
		super.onStart();
		// Register for Events
		EventBus.getDefault().register(this);
	}

	@Override
	public void onStop() {
		// Unregister for notifications
		EventBus.getDefault().unregister(this);
		super.onStop();
	}

	public BookChapterFragment() {}

	public static final BookChapterFragment newInstance(final BookItem bookItem) {
		final BookChapterFragment fragment = new BookChapterFragment_();
		fragment.mBookItem = bookItem;

		return fragment;
	}
}
