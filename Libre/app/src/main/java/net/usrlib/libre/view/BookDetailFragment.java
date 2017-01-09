package net.usrlib.libre.view;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.usrlib.libre.R;
import net.usrlib.libre.model.Book;
import net.usrlib.libre.model.BookItem;
import net.usrlib.libre.presenter.Presenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rgr-myrg on 12/8/16.
 */

@EFragment(R.layout.book_detail_fragment_main)

public class BookDetailFragment extends Fragment {
	public static final String TAG = BookDetailFragment.class.getSimpleName();

	@ViewById(R.id.book_detail_recycler_view)
	protected RecyclerView mRecyclerView;

	@ViewById(R.id.book_detail_recycler_empty)
	protected TextView mEmptyBookListMessage;

	protected BookDetailAdapter mRecyclerAdapter = null;

	@InstanceState
	protected int mBookId;

	@AfterViews
	protected void loadBookItems() {
		final Intent intent = getActivity().getIntent();
		mBookId = intent.getIntExtra(Book.BOOK_ID, 0);
	}

	@Override
	public void onStart() {
		super.onStart();

		// Get a fresh cursor on start.
		final Cursor cursor = Presenter.getBookItemsFromDb(getContext(), mBookId);

		if (cursor == null) {
			return;
		}

		if (cursor.getCount() > 0) {
			mEmptyBookListMessage.setVisibility(View.GONE);
		}

		initRecyclerViewAndAdapter(cursor);
	}

	private void initRecyclerViewAndAdapter(final Cursor cursor) {
		// Reusing adapter causes bugs in data set. Commenting out.
//		if (mRecyclerAdapter != null) {
//			mRecyclerAdapter.changeCursor(cursor);
//			return;
//		}

		mRecyclerAdapter = new BookDetailAdapter(getContext(), cursor, position -> {
			startBookChapterActivity(position);
		});

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setAdapter(mRecyclerAdapter);
	}

	private void startBookChapterActivity(int position) {
		final BookItem bookItem = mRecyclerAdapter.getItem(position);
		final Intent intent = new Intent(getContext(), BookChapterActivity_.class);

		intent.putExtra(BookItem.BOOK_ID, bookItem.getBookId());
		intent.putExtra("position", position);

		intent.putExtra(BookItem.ITEM_ID, bookItem.getItemId());
		intent.putExtra(BookItem.TITLE_EN, bookItem.getTitleEN());
		intent.putExtra(BookItem.TITLE_SP, bookItem.getTitleSP());

		getContext().startActivity(intent);
	}
}
