package net.usrlib.libre.view;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

import net.usrlib.libre.BuildConfig;
import net.usrlib.libre.R;
import net.usrlib.libre.model.BookItem;
import net.usrlib.libre.presenter.Presenter;
import net.usrlib.libre.util.Logger;
import net.usrlib.libre.util.Preferences;
import net.usrlib.libre.util.UiUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;

/**
 * Created by rgr-myrg on 1/4/17.
 */

@EActivity(R.layout.book_chapter_activity)

public class BookChapterActivity extends AppCompatActivity {
	public static final String TAG = BookChapterActivity.class.getSimpleName();
	public static final int FONT_SIZE_OFFSET = 10;

	@InstanceState
	protected int mItemPosition;

	@InstanceState
	protected int mBookId;

	@InstanceState
	protected String mChapterTitle;

	protected BookChapterAdapter mPagerAdapter = null;
	protected ViewPager mViewPager = null;
	protected BookItem mBookItem = null;
	protected Cursor mCursor = null;

	@OptionsItem(android.R.id.home)
	void onHomeBackButtonClicked() {
		onBackPressed();
	}

	@AfterViews
	protected void getBookItems() {
		final Intent intent = getIntent();

		mBookId = intent.getIntExtra(BookItem.BOOK_ID, 0);
		mChapterTitle = intent.getStringExtra(BookItem.TITLE);
		mItemPosition = intent.getIntExtra("position", 0);

		if (BuildConfig.DEBUG){
			Logger.i(TAG, "bookId: " + mBookId + " pos: " + mItemPosition);
		}

		mCursor = Presenter.getBookItemsFromDb(getApplicationContext(), mBookId);
		mBookItem = getItem(mItemPosition);

		initViewPager(mItemPosition, mCursor);
	}

	protected void initViewPager(final int position, final Cursor cursor) {
		if (mPagerAdapter != null && mViewPager != null) {
			mViewPager.setCurrentItem(position);
			mPagerAdapter.changeCursor(cursor);

			return;
		}

		mPagerAdapter = new BookChapterAdapter(getSupportFragmentManager(), cursor);

		mViewPager = (ViewPager) findViewById(R.id.book_chapter_viewpager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(position);

		mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				Logger.i(TAG, "onPageSelected " + position + " : " + mItemPosition);
				markPreviousItemAsRead(getItem(mItemPosition));
				mBookItem = getItem(position);
			}
		});
	}

	public BookItem getItem(final int position) {
		if (mCursor == null) {
			return null;
		}

		if (BuildConfig.DEBUG) Logger.i(TAG, "getItem position: " + position);

		mCursor.moveToPosition(position);
		mItemPosition = position;

		return BookItem.fromDbCursor(mCursor);
	}

	public void onFontSizeIncreaseClicked(View view) {
		int fontSize = Preferences.getFontSize(getApplicationContext()) + FONT_SIZE_OFFSET;

		notifyFontSizeChanged(fontSize);
		closeFloatingActionMenu(view);
	}

	public void onFontSizeDecreaseClicked(View view) {
		int fontSize = Preferences.getFontSize(getApplicationContext()) - FONT_SIZE_OFFSET;

		notifyFontSizeChanged(fontSize);
		closeFloatingActionMenu(view);
	}

	protected void notifyFontSizeChanged(final int fontSize) {
		if (BuildConfig.DEBUG) {
			Logger.i(TAG, "notifyFontSizeChanged " + fontSize);
		}

		Preferences.setFontSize(getApplicationContext(), fontSize);
		Presenter.notifyOnFontSizeChanged(fontSize);
	}

//	public void onShareItClicked(View view) {
//		Logger.i(TAG, "onShareItClicked " + mBookItem.getItemKey());
//		closeFloatingActionMenu(view);
//		IntentUtil.startWithChooser(this, mBookItem.getTitle(), mBookItem.getContent());
//	}

	public void onBookmarkItClicked(View view) {
		if (BuildConfig.DEBUG) {
			Logger.i(TAG, "onBookmarkItClicked " + mBookItem.getItemKey());
		}

		closeFloatingActionMenu(view);
		addBookmarkFor(mBookItem.getItemKey(), view);
	}

	public void onBackToChapterListClicked(View view) {
		closeFloatingActionMenu(view);
		onBackPressed();
	}

	public void closeFloatingActionMenu(View view) {
		if (view == null) {
			return;
		}

		final FloatingActionMenu menu = (FloatingActionMenu) view.getParent();

		if (menu == null) {
			return;
		}

		menu.close(true);
	}

	@Background
	protected void markPreviousItemAsRead(final BookItem bookItem) {
		Presenter.markedItemAsRead(
				getApplicationContext(),
				bookItem.getItemKey(),
				true,
				success -> {
					Logger.i(TAG, "markPreviousItemAsRead key: " + bookItem.getItemKey());
				}
		);
	}

	@Background
	protected void addBookmarkFor(final String itemKey, final View view) {
		Presenter.addBookmark(getApplicationContext(), itemKey, success -> {
			onBookmarkAdded(success, view);
		});
	}

	@UiThread
	protected void onBookmarkAdded(final boolean success, final View view) {
		UiUtil.makeSnackbar(
				view,
				getString(
						success ? R.string.bookmark_complete : R.string.error_message
				)
		);
	}
}
