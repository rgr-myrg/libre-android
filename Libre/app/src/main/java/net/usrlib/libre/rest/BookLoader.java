package net.usrlib.libre.rest;

import android.util.Log;

import net.usrlib.libre.BuildConfig;
import net.usrlib.libre.model.Book;
import net.usrlib.libre.model.BookItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rgr-myrg on 1/2/17.
 */

public class BookLoader {
	public static final String TAG = BookLoader.class.getSimpleName();
	public static final String BASE_URL = "http://rgr-myrg.github.io/www/app/libre/json/";

	private RetrofitApi mRetrofitApi = null;

	private OnBookListLoaded mOnBookListLoaded = null;
	private OnBookItemLoaded mOnBookItemLoaded = null;
	private OnBeforeEachBook mOnBeforeEachBook = null;

	private OnFinished mOnFinished = null;
	private OnFailure mOnFailure = null;
	private List<Book> mBookList = null;

	private int mCurrentBookIndex = 0;

	public BookLoader() {
		this.mRetrofitApi = RetrofitClient
				.getClientWithUrl(BASE_URL)
				.create(RetrofitApi.class);
	}

	public BookLoader onBookListLoaded(final OnBookListLoaded callback) {
		this.mOnBookListLoaded = callback;
		return this;
	}

	public BookLoader onBeforeEachBook(final OnBeforeEachBook callback) {
		this.mOnBeforeEachBook = callback;
		return this;
	}

	public BookLoader forEachBook(final OnBookItemLoaded callback) {
		this.mOnBookItemLoaded = callback;
		return this;
	}

	public BookLoader onFinished(final OnFinished callback) {
		this.mOnFinished = callback;
		return this;
	}

	public BookLoader onFailure(final OnFailure callback) {
		this.mOnFailure = callback;
		return this;
	}

	public BookLoader start() {
		fetchBookList();

		return this;
	}

	private void fetchBookList() {
		if (BuildConfig.DEBUG) Log.i(TAG, "Fetching book list");

		final Call<List<Book>> bookListCall = mRetrofitApi.getBookList();

		bookListCall.enqueue(new Callback<List<Book>>() {
			@Override
			public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
				mBookList = response.body();

				if (mOnBookListLoaded != null) {
					mOnBookListLoaded.run(mBookList);
				}

				fetchBookItemForList(mBookList);
			}

			@Override
			public void onFailure(Call<List<Book>> call, Throwable t) {
				if (mOnFailure != null) {
					mOnFailure.run(t);
				}
			}
		});
	}

	private void fetchBookItemForList(final List<Book> bookList) {
		if (BuildConfig.DEBUG) Log.i(TAG, "Fetching items for this book");

		for (int i = 0; i < bookList.size(); i++) {
			Book book = bookList.get(i);
			String dataFile = book.getDataFile();
			boolean shouldFetchBookItems = true;

			// Track the index to determine if onFinished callback should fire.
			mCurrentBookIndex = i;

			if (mOnBeforeEachBook != null) {
				shouldFetchBookItems = mOnBeforeEachBook.run(book);
			}

			// Only fetch items if callback returns true
			if (!shouldFetchBookItems) {
				if (BuildConfig.DEBUG) Log.i(TAG, "shouldFetchBookItems is false. Skip book items");
				return;
			}

			Call<List<BookItem>> bookItems = mRetrofitApi.getBookItems(dataFile);

			bookItems.enqueue(new Callback<List<BookItem>>() {
				@Override
				public void onResponse(Call<List<BookItem>> call, Response<List<BookItem>> response) {
					if (mOnBookItemLoaded != null) {
						mOnBookItemLoaded.run(response.body());
					}

					// End of book list reached. Trigger onFinished
					if (mCurrentBookIndex == bookList.size() - 1 && mOnFinished != null) {
						mOnFinished.run();
					}
				}

				@Override
				public void onFailure(Call<List<BookItem>> call, Throwable t) {
					if (mOnFailure != null) {
						mOnFailure.run(t);
					}
				}
			});
		}
	}

	public interface OnBookListLoaded {
		void run(List<Book> list);
	}

	public interface OnBookItemLoaded {
		void run(List<BookItem> list);
	}

	public interface OnBeforeEachBook {
		boolean run(Book book);
	}

	public interface OnFinished {
		void run();
	}

	public interface OnFailure {
		void run(Throwable t);
	}
}
