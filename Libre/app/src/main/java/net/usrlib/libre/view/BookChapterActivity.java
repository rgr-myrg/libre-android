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
import net.usrlib.libre.util.IntentUtil;
import net.usrlib.libre.util.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;

/**
 * Created by rgr-myrg on 1/4/17.
 */

@EActivity(R.layout.book_chapter_activity)

public class BookChapterActivity extends AppCompatActivity {
	public static final String TAG = BookChapterActivity.class.getSimpleName();

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
		mChapterTitle = intent.getStringExtra(BookItem.TITLE_EN);
		mItemPosition = intent.getIntExtra("position", 0);

		if (BuildConfig.DEBUG) Logger.i(TAG, "bookId: " + mBookId + " pos: " + mItemPosition);

		mCursor = Presenter.getBookItemsFromDb(getApplicationContext(), mBookId);

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

	public void onShareItClicked(View view) {
		Logger.i(TAG, "onShareItClicked " + mBookItem.getItemId());
		closeFloatingActionMenu(view);
		IntentUtil.startWithChooser(this, mBookItem.getTitleEN(), mBookItem.getContentEN());
	}

	public void onBookmarkItClicked(View view) {}

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
}
