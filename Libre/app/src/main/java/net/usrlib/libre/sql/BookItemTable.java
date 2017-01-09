package net.usrlib.libre.sql;

import net.usrlib.libre.model.BookItem;

/**
 * Created by rgr-myrg on 1/2/17.
 */

public class BookItemTable {
	public static final String TABLE_NAME = "BookItem";
	public static final String TIMESTAMP  = "timestamp";

	public static final String CREATE_TABLE = String.format(
			"CREATE TABLE IF NOT EXISTS %s ("
					+ "%s INTEGER PRIMARY KEY,"
					+ "%s INTEGER NOT NULL,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s INTEGER NOT NULL DEFAULT 0,"
					+ "%s INTEGER NOT NULL DEFAULT 0,"
					+ "%s DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL"
					+ ")",
			TABLE_NAME,
			BookItem.ITEM_ID,
			BookItem.BOOK_ID,
			BookItem.TITLE_EN,
			BookItem.TITLE_SP,
			BookItem.CONTENT_SP,
			BookItem.CONTENT_EN,
			BookItem.IMAGE_URL,
			BookItem.HTML_CACHE,
			BookItem.BOOKMARKED,
			BookItem.MARKED_READ,
			TIMESTAMP
	);

	public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);

	public static final String SELECT_ALL_WITH_BOOK_ID = String.format(
			"SELECT * FROM %s WHERE %s = ?", TABLE_NAME, BookItem.BOOK_ID
	);

	public static final String WHERE_ITEM_ID = String.format(
			"%s = ?", BookItem.ITEM_ID
	);
}
