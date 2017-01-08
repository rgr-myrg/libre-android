package net.usrlib.libre.presenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import net.usrlib.libre.BuildConfig;
import net.usrlib.libre.model.Book;
import net.usrlib.libre.model.BookItem;
import net.usrlib.libre.rest.BookLoader;
import net.usrlib.libre.sql.BookItemTable;
import net.usrlib.libre.sql.BookTable;
import net.usrlib.libre.util.DbHelper;
import net.usrlib.libre.util.Logger;
import net.usrlib.libre.util.Preferences;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by rgr-myrg on 1/2/17.
 */

public class Presenter {
	public static final String TAG = Presenter.class.getSimpleName();
	public static final boolean DEBUG = BuildConfig.DEBUG;

	private static List<Book> sBookListFromDb = null;
	private static boolean sHasBookDataInsert = false;
	private static boolean sHasAtLeastOneNewItem = false;

	public static final void performDataInstall(
			final Context context,
			final OnSqlTransactionComplete callback) {

		new BookLoader()
				.onBookListLoaded(bookList -> {
					insertBookList(context, bookList);
				})
				.onBeforeEachBook(
						book -> shouldFetchBookItems(context, book)
				)
				.forEachBook(bookItemList -> {
					final int rows = DbHelper.getInstance(context).bulkInsert(
							BookItemTable.TABLE_NAME,
							BookItem.fromListAsContentValues(bookItemList)
					);

					// Track if at least one new item was inserted
					if (!sHasAtLeastOneNewItem) {
						sHasAtLeastOneNewItem = rows > 0;
					}

					if (DEBUG) Logger.i(TAG, "forEachBook inserted rows: " + rows);
				})
				.onFinished(() -> {
					if (DEBUG) Logger.i(TAG, "onFinished "
							+ sHasBookDataInsert + ":" + sHasAtLeastOneNewItem);

					Preferences.setHasDataInstall(context, sHasBookDataInsert);

					// Let the caller know at least one new item was inserted.
					if (callback != null) {
						callback.run(sHasAtLeastOneNewItem);
					}
				})
				.onFailure((Throwable t) -> {
					if (DEBUG) Logger.i(TAG, t.toString());

					if (callback != null) {
						callback.run(false);
					}
				})
				.start();
	}

	public static final void insertBookList(final Context context, final List<Book> bookList) {
		final Cursor cursor = getBookListFromDb(context);

		// Retrieve current state of book list from db
		if (cursor != null && cursor.getCount() > 0) {
			sBookListFromDb = Book.fromDbCursorAsList(cursor);
		}

		final int rows = DbHelper.getInstance(context).bulkInsert(
				BookTable.TABLE_NAME,
				Book.fromListAsContentValues(bookList)
		);

		if (DEBUG) Logger.i(TAG, "insertBookList inserted rows: " + rows);

		sHasBookDataInsert = rows > 0;
	}

	public static final boolean shouldFetchBookItems(final Context context, final Book book) {
		// No previous data found. Return true to retrieve new book list.
		if (sBookListFromDb == null
				|| sBookListFromDb.isEmpty()
				|| !Preferences.hasDataInstall(context)) {
			return true;
		}

		final Book prevBook = sBookListFromDb.get(book.getBookId() - 1);
		int prevItemCount = 0;

		// Get previous item count for this item
		if (prevBook != null) {
			prevItemCount = prevBook.getItemCount();
		}

		if (DEBUG) Logger.i(TAG, "shouldFetchBookItems compare counts: "
				+ prevItemCount + " vs " + book.getItemCount());

		return prevItemCount < book.getItemCount();
	}

	public static final Cursor getBookListFromDb(final Context context) {
		return DbHelper
				.getInstance(context)
				.getDbCursorWithSql(
						BookTable.SELECT_ALL
				);
	}

	public static final Cursor getBookItemsFromDb(final Context context, final int bookId) {
		return DbHelper
				.getInstance(context)
				.getDbCursorWithSql(
						BookItemTable.SELECT_ALL_WITH_BOOK_ID
								.replaceFirst("\\?", String.valueOf(bookId))
				);
	}

	public static final void saveHtmlCache(
			final Context context,
			final int itemId,
			final String html,
			final OnSqlTransactionComplete callback) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(BookItem.HTML_CACHE, html);

		final int rows = DbHelper.getInstance(context).update(
				BookItemTable.TABLE_NAME,
				contentValues,
				BookItemTable.WHERE_ITEM_ID,
				new String[]{String.valueOf(itemId)}
		);

		if (callback != null) {
			callback.run(rows > 0);
		}
	}

	public static final void addBookmark(
			final Context context,
			final int itemId,
			final OnSqlTransactionComplete callback) {

		ContentValues contentValues = new ContentValues();
		contentValues.put(BookItem.BOOKMARKED, 1);

		final int rows = DbHelper.getInstance(context).update(
				BookItemTable.TABLE_NAME,
				contentValues,
				BookItemTable.WHERE_ITEM_ID,
				new String[]{String.valueOf(itemId)}
		);

		if (callback != null) {
			callback.run(rows > 0);
		}
	}

	public static final void notifyOnFontSizeChanged(final Context context, final int fontSize) {
		Preferences.setFontSize(context, fontSize);
		EventBus.getDefault().post(new Presenter.FontSizeChangedEvent(fontSize));
	}

//	public static final void notifyOnAddBookmarkEvent(final int itemId) {
//
//	}

	public interface OnSqlTransactionComplete {
		void run(boolean success);
	}

	public static class FontSizeChangedEvent {
		public int fontSize;

		public FontSizeChangedEvent(int fontSize) {
			this.fontSize = fontSize;
		}
	}

//	public static class AddBookmarkEvent {
//		public int itemId;
//
//		public AddBookmarkEvent(int itemId) {
//			this.itemId = itemId;
//		}
//	}
}
