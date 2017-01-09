package net.usrlib.libre.view;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.usrlib.libre.BuildConfig;
import net.usrlib.libre.R;
import net.usrlib.libre.model.Book;
import net.usrlib.libre.model.BookItem;
import net.usrlib.libre.presenter.Presenter;
import net.usrlib.libre.util.Logger;
import net.usrlib.libre.util.UiUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

		// Register for Events
		EventBus.getDefault().register(this);

		initRecyclerViewAndAdapter();
	}

	@Override
	public void onStop() {
		// Unregister for notifications
		EventBus.getDefault().unregister(this);
		super.onStop();
	}

	private void initRecyclerViewAndAdapter() {
		// Reusing adapter causes bugs in data set. Commenting out.
//		if (mRecyclerAdapter != null) {
//			mRecyclerAdapter.changeCursor(cursor);
//			return;
//		}

		final Cursor cursor = Presenter.getBookItemsFromDb(getContext(), mBookId);

		if (cursor == null) {
			return;
		}

		if (cursor.getCount() > 0) {
			mEmptyBookListMessage.setVisibility(View.GONE);
		}

		mRecyclerAdapter = new BookDetailAdapter(getContext(), cursor);

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mRecyclerView.setAdapter(mRecyclerAdapter);
	}

	private void startBookChapterActivity(int position) {
		final BookItem bookItem = mRecyclerAdapter.getItem(position);
		final Intent intent = new Intent(getContext(), BookChapterActivity_.class);

		intent.putExtra(BookItem.BOOK_ID, bookItem.getBookId());
		intent.putExtra("position", position);

		intent.putExtra(BookItem.ITEM_KEY, bookItem.getItemKey());
		intent.putExtra(BookItem.TITLE, bookItem.getTitle());

		getContext().startActivity(intent);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onBookItemClickedEvent(Presenter.BookItemClickedEvent event) {
		if (BuildConfig.DEBUG) {
			Logger.i(TAG, "onBookItemClickedEvent position: " + event.position);
		}

		startBookChapterActivity(event.position);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMarkAsUnreadEvent(Presenter.MarkAsUnreadEvent event) {
		UiUtil.showAlertDialog(getContext(), "Confirm", "Mark this item as Unread?", confirm -> {
			if (confirm) {
				Presenter.markedItemAsRead(
						getContext(),
						event.itemKey,
						false,
						success -> {
							Logger.i(TAG, "onMarkAsUnreadEvent id: " + event.itemKey);
							initRecyclerViewAndAdapter();
						}
				);
			}
		});
	}
}
