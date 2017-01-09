package net.usrlib.libre.view;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.usrlib.libre.R;
import net.usrlib.libre.model.BookItem;
import net.usrlib.libre.presenter.Presenter;

/**
 * Created by rgr-myrg on 12/8/16.
 */

public class BookDetailAdapter extends RecyclerView.Adapter {
	protected LayoutInflater mInflater = null;
	protected Context mContext = null;
	protected Cursor mCursor = null;

	public BookDetailAdapter(Context context, Cursor cursor) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext  = context;
		this.mCursor   = cursor;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(
				mInflater.inflate(
						R.layout.book_detail_card_view,
						parent,
						false
				)
		);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		final ViewHolder viewHolder = (ViewHolder) holder;
		final BookItem data = getItem(position);

		if (data != null && position >= 0 && position < mCursor.getCount()) {
			viewHolder.bindData(data);
		}
	}

	@Override
	public int getItemCount() {
		return mCursor == null ? 0 : mCursor.getCount();
	}

	public BookItem getItem(final int position) {
		mCursor.moveToPosition(position);

		return BookItem.fromDbCursor(mCursor);
	}

//	public void changeCursor(Cursor cursor) {
//		Cursor swappedCursor = swapCursor(cursor);
//
//		if (swappedCursor != null) {
//			swappedCursor.close();
//		}
//	}
//
//	public Cursor swapCursor(Cursor cursor) {
//		if (mCursor == cursor) {
//			return null;
//		}
//
//		final Cursor previousCursor = mCursor;
//
//		mCursor = cursor;
//
//		if (mCursor != null) {
//			this.notifyDataSetChanged();
//		}
//
//		return previousCursor;
//	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public TextView sectionIcon;
		public TextView sectionTitle;
		public ImageView iconStatus;

		public ViewHolder(View view) {
			super(view);

			sectionIcon  = (TextView) view.findViewById(R.id.section_icon);
			sectionTitle = (TextView) view.findViewById(R.id.section_title);
			iconStatus   = (ImageView) view.findViewById(R.id.section_status);
		}

		public void bindData(final BookItem data) {
			sectionTitle.setText(data.getTitle());

			sectionIcon.setOnClickListener(view -> {
				Presenter.notifyOnBookItemClicked(getAdapterPosition());
			});

			sectionTitle.setOnClickListener(view -> {
				Presenter.notifyOnBookItemClicked(getAdapterPosition());
			});

			if (data.isMarkedRead()) {
				iconStatus.setVisibility(View.VISIBLE);
				iconStatus.setOnClickListener(view -> {
					Presenter.notifyOnMarkAsUnreadEvent(data.getItemKey());
				});
			}
		}
	}
}
