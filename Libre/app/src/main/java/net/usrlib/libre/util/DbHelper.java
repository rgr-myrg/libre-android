package net.usrlib.libre.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import net.usrlib.libre.BuildConfig;
import net.usrlib.libre.sql.BookItemTable;
import net.usrlib.libre.sql.BookTable;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE;

/**
 * Created by rgr-myrg on 12/6/16.
 */

public class DbHelper extends SQLiteOpenHelper {
	public static final String TAG = DbHelper.class.getSimpleName();
	public static final String DB_NAME = "net.usrlib.libre.data";
	public static final int DB_VERSION = 1;

	private static DbHelper sInstance = null;

	private DbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (BuildConfig.DEBUG) Logger.i(TAG, "onCreate starts up");

		db.execSQL(BookTable.CREATE_TABLE);
		db.execSQL(BookItemTable.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(BookTable.DROP_TABLE);
		db.execSQL(BookItemTable.DROP_TABLE);
		onCreate(db);
	}

	public long insert(@NonNull final String tableName, @NonNull final ContentValues item) {
		final SQLiteDatabase db = getWritableDatabase();
		long newRecordId = -1;

		db.beginTransaction();

		try {
			newRecordId = db.insertWithOnConflict(tableName, null, item, CONFLICT_REPLACE);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return newRecordId;
	}

	public int update(
			@NonNull final String tableName,
			@NonNull final ContentValues item,
			@NonNull final String where,
			@NonNull final String[] whereArgs) {

		final SQLiteDatabase db = getWritableDatabase();
		int numOfRows = -1;

		db.beginTransaction();

		try {
			numOfRows = db.update(tableName, item, where, whereArgs);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return numOfRows;
	}

	public int delete(
			@NonNull final String tableName,
			@NonNull final String where,
			@NonNull final String[] whereArgs) {

		final SQLiteDatabase db = getWritableDatabase();
		int numOfRows = -1;

		db.beginTransaction();

		try {
			numOfRows = db.delete(tableName, where, whereArgs);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return numOfRows;
	}

	public Cursor getDbCursorWithSql(final String sql) {
		if (BuildConfig.DEBUG) Logger.i(TAG, sql);
		final SQLiteDatabase db = getWritableDatabase();

		if (db == null) {
			return null;
		}

		final Cursor cursor = db.rawQuery(
				sql,
				null
		);

		return cursor;
	}

	public int bulkInsert(final String tableName, final ContentValues[] values) {
		int rowsInserted = 0;
		final SQLiteDatabase db = getWritableDatabase();

		db.beginTransaction();

		try {
			for (ContentValues item : values) {
				if (BuildConfig.DEBUG) {
					Logger.i(TAG, item.toString());
				}

				long newID = db.insertWithOnConflict(tableName, null, item, CONFLICT_REPLACE);

				if (newID <= 0) {
					throw new SQLException("Failed to insert row into " + tableName);
				}
			}

			db.setTransactionSuccessful();
			rowsInserted = values.length;

		} finally {
			db.endTransaction();
		}

		return rowsInserted;
	}

	public static synchronized DbHelper getInstance(@NonNull final Context context) {
		// Use application context, to prevent accidentally leaking an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (sInstance == null && context != null) {
			sInstance = new DbHelper(context.getApplicationContext());
		}

		return sInstance;
	}
}
