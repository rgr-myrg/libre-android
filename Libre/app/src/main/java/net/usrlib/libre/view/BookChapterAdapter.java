package net.usrlib.libre.view;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.usrlib.libre.model.BookItem;
import net.usrlib.libre.presenter.Presenter;
import net.usrlib.libre.util.Logger;

/**
 * Created by rgr-myrg on 6/8/16.
 */
public class BookChapterAdapter extends FragmentStatePagerAdapter {
	private Cursor mCursor = null;

	public BookChapterAdapter(final FragmentManager fragmentManager, final Cursor cursor) {
		super(fragmentManager);
		this.mCursor = cursor;
	}

	@Override
	public Fragment getItem(int position) {
		mCursor.moveToPosition(position);

		return BookChapterFragment_.newInstance(BookItem.fromDbCursor(mCursor));
	}

	@Override
	public int getCount() {
		return mCursor == null ? 0 : mCursor.getCount();
	}

	public void changeCursor(Cursor cursor) {
		Cursor swappedCursor = swapCursor(cursor);

		if (swappedCursor != null) {
			swappedCursor.close();
		}
	}

	private Cursor swapCursor(Cursor cursor) {
		if (mCursor == cursor) {
			return null;
		}

		final Cursor previousCursor = mCursor;

		mCursor = cursor;

		if (cursor != null) {
			this.notifyDataSetChanged();
		}

		return previousCursor;
	}
}
