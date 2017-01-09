package net.usrlib.libre.view;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.usrlib.libre.R;
import net.usrlib.libre.model.Book;

/**
 * Created by rgr-myrg on 12/8/16.
 */

public class BookListAdapter extends RecyclerView.Adapter {
	protected LayoutInflater mInflater = null;
	protected Context mContext = null;
	protected Cursor mCursor = null;
	protected OnItemClick mOnItemClick = null;

	public BookListAdapter(Context context, Cursor cursor, OnItemClick callback) {
		this.mInflater = LayoutInflater.from(context);
		this.mContext  = context;
		this.mCursor   = cursor;
		this.mOnItemClick = callback;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(
				mInflater.inflate(
						R.layout.book_list_card_view,
						parent,
						false
				)
		);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		final ViewHolder viewHolder = (ViewHolder) holder;
		final Book data = getItem(position);

		if (data != null && position >= 0 && position < mCursor.getCount()) {
			viewHolder.bindData(data);
		}
	}

	@Override
	public int getItemCount() {
		return mCursor == null ? 0 : mCursor.getCount();
	}

	public Book getItem(final int position) {
		mCursor.moveToPosition(position);

		return Book.fromDbCursor(mCursor);
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
		public CardView bookCardView;
		public ImageView bookCover;
		public TextView bookTitle;
		public TextView bookCaption;
		public TextView bookAuthor;

		public ViewHolder(View view) {
			super(view);

			bookCardView = (CardView) view.findViewById(R.id.book_list_card_view);
			bookCover    = (ImageView) view.findViewById(R.id.book_cover_image);
			bookTitle    = (TextView) view.findViewById(R.id.book_title);
			bookCaption  = (TextView) view.findViewById(R.id.book_caption);
			bookAuthor   = (TextView) view.findViewById(R.id.book_author);
		}

		public void bindData(final Book data) {
			Glide.with(mContext).load(data.getImageUrl()).centerCrop().into(bookCover);

			bookTitle.setText(data.getTitle());
			bookCaption.setText(data.getCaption());
			bookAuthor.setText(data.getAuthor());

			bookCardView.setOnClickListener((View view) -> {
				mOnItemClick.run(getAdapterPosition());
			});
		}
	}

	public interface OnItemClick {
		void run(int position);
	}
}
