package net.usrlib.libre.view;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import net.usrlib.libre.R;
import net.usrlib.libre.model.BookItem;

/**
 * Created by rgr-myrg on 12/8/16.
 */

public class BookDetailAdapter extends RecyclerView.Adapter {
	protected ViewBinderHelper mBinderHelper = new ViewBinderHelper();
	protected LayoutInflater mInflater = null;
	protected Context mContext = null;
	protected Cursor mCursor = null;
	protected OnItemClick mOnItemClick = null;

	public BookDetailAdapter(Context context, Cursor cursor, OnItemClick callback) {
		Log.i("ADAPTER", "BookDetailAdapter");
		this.mInflater = LayoutInflater.from(context);
		this.mContext  = context;
		this.mCursor   = cursor;
		this.mOnItemClick = callback;

		// Allow Swipe to reveal one row at a time
		mBinderHelper.setOpenOnlyOne(true);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		Log.i("ADAPTER", "onCreateViewHolder");
		return new ViewHolder(
				mInflater.inflate(
						R.layout.book_item_swipe_view,
						parent,
						false
				)
		);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		Log.i("ADAPTER", "onBindViewHolder");
		final ViewHolder viewHolder = (ViewHolder) holder;
		final BookItem data = getItem(position);

		if (data != null && position >= 0 && position < mCursor.getCount()) {
			viewHolder.bindData(data);

			// BinderHelper requires a unique key. Use title.
			mBinderHelper.bind(viewHolder.swipeLayout, data.getTitleEN());
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

	public void changeCursor(Cursor cursor) {
		Cursor swappedCursor = swapCursor(cursor);

		if (swappedCursor != null) {
			swappedCursor.close();
		}
	}

	public Cursor swapCursor(Cursor cursor) {
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

	public class ViewHolder extends RecyclerView.ViewHolder {
		public SwipeRevealLayout swipeLayout;
		public CardView bookCardView;
		public ImageView bookCover;
		public TextView bookTitle;
		public TextView bookAuthor;

		public ViewHolder(View view) {
			super(view);
			swipeLayout  = (SwipeRevealLayout) view.findViewById(R.id.book_item_swipe_layout);
			bookCardView = (CardView) view.findViewById(R.id.book_card_view);
			bookCover  = (ImageView) view.findViewById(R.id.book_cover_image);
			bookTitle  = (TextView) view.findViewById(R.id.book_title);
			bookAuthor = (TextView) view.findViewById(R.id.book_author);
		}

		public void bindData(final BookItem data) {
			Glide.with(mContext).load(data.getImageUrl()).centerCrop().into(bookCover);
			bookTitle.setText(data.getTitleEN());

			bookCardView.setOnClickListener((View view) -> {
				mOnItemClick.run(getAdapterPosition());
			});
		}
	}

	public interface OnItemClick {
		void run(int position);
	}
}
