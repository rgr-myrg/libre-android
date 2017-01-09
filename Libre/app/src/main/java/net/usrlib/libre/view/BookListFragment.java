package net.usrlib.libre.view;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.usrlib.libre.R;
import net.usrlib.libre.model.Book;
import net.usrlib.libre.presenter.Presenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rgr-myrg on 12/8/16.
 */

@EFragment(R.layout.book_list_fragment_main)

public class BookListFragment extends Fragment {
	public static final String TAG = BookListFragment.class.getSimpleName();

	@ViewById(R.id.book_list_recycler_view)
	protected RecyclerView mRecyclerView;

	@ViewById(R.id.book_list_recycler_empty)
	protected TextView mEmptyBookListMessage;

	protected BookListAdapter mRecyclerAdapter = null;

	@AfterViews
	protected void onAfterViews() {
		final Cursor cursor = Presenter.getBookListFromDb(getContext());

		if (cursor == null) {
			return;
		}

		if (cursor.getCount() > 0) {
			mEmptyBookListMessage.setVisibility(View.GONE);
		}

		initRecyclerViewAndAdapter(cursor);
	}

	private void initRecyclerViewAndAdapter(final Cursor cursor) {
		if (mRecyclerAdapter != null) {
			mRecyclerAdapter.changeCursor(cursor);
			return;
		}

		mRecyclerAdapter = new BookListAdapter(getContext(), cursor, position -> {
			Log.i(TAG, "item clicked position: " + position);
			startBookDetailActivity(position);
		});

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setAdapter(mRecyclerAdapter);
	}

	private void startBookDetailActivity(int position) {
		final Book bookItem = mRecyclerAdapter.getItem(position);
		final Intent intent = new Intent(getContext(), BookDetailActivity_.class);

		intent.putExtra(Book.BOOK_ID, bookItem.getBookId());
		intent.putExtra(Book.TITLE, bookItem.getTitle());
		intent.putExtra(Book.IMAGE_URL, bookItem.getImageUrl());

		getContext().startActivity(intent);
	}
}
