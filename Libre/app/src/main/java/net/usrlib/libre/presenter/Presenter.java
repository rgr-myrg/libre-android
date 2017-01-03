package net.usrlib.libre.presenter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import net.usrlib.libre.BuildConfig;
import net.usrlib.libre.model.Book;
import net.usrlib.libre.model.BookItem;
import net.usrlib.libre.rest.BookLoader;
import net.usrlib.libre.sql.BookItemTable;
import net.usrlib.libre.sql.BookTable;
import net.usrlib.libre.util.DbHelper;
import net.usrlib.libre.util.Preferences;

/**
 * Created by rgr-myrg on 1/2/17.
 */

public class Presenter {
	public static final String TAG = Presenter.class.getSimpleName();
	public static final boolean DEBUG = BuildConfig.DEBUG;

	private static boolean sHasBookDataInsert = false;

	public static final void performDataInstall(
			final Context context,
			final OnSqlTransactionComplete callback) {

		final DbHelper dbHelper = DbHelper.getInstance(context);

		new BookLoader()
				.onBookListLoaded(bookList -> {
					final int rows = dbHelper.bulkInsert(
							BookTable.TABLE_NAME,
							Book.fromListAsContentValues(bookList)
					);

					if (DEBUG) Log.i(TAG, "onBookListLoaded inserted rows: " + rows);

					sHasBookDataInsert = rows > 0;
				})
				.onBeforeEachBook(book -> {
					final int currentItemCount = getItemCountFromDb(context, book.getBookId());

					if (DEBUG) {
						Log.i(TAG, "onBeforeEachBook "
								+ currentItemCount + ":" + book.getItemCount());
					}

					// Check if this is the first time installing data.
					// Otherwise compare the item count.
					return !Preferences.hasDataInstall(context)
							|| currentItemCount < book.getItemCount();
				})
				.forEachBook(bookItemList -> {
					final int rows = dbHelper.bulkInsert(
							BookItemTable.TABLE_NAME,
							BookItem.fromListAsContentValues(bookItemList)
					);

					if (DEBUG) Log.i(TAG, "forEachBook inserted rows: " + rows);
				})
				.onFinished(() -> {
					if (DEBUG) Log.i(TAG, "onFinished " + sHasBookDataInsert);

					Preferences.setHasDataInstall(context, sHasBookDataInsert);
					callback.run(sHasBookDataInsert);
				})
				.onFailure((Throwable t ) -> {
					if (DEBUG) Log.i(TAG, t.getMessage());

					callback.run(false);
				})
				.start();
	}

	public static final int getItemCountFromDb(final Context context, final int bookId) {
		int result = 0;

		final Cursor cursor = DbHelper
				.getInstance(context)
				.getDbCursorWithSql(
						BookTable.SELECT_ITEM_COUNT_WITH_BOOK_ID
								.replaceFirst("\\?", String.valueOf(bookId))
				);

		if (cursor == null) {
			return result;
		}

		cursor.moveToFirst();
		result = cursor.getInt(cursor.getColumnIndex(Book.ITEM_COUNT));
		cursor.close();

		return result;
	}

	public interface OnSqlTransactionComplete {
		void run(boolean success);
	}
}
