package net.usrlib.libre.sql;

import net.usrlib.libre.model.Book;

/**
 * Created by rgr-myrg on 1/2/17.
 */

public class BookTable {
	public static final String TABLE_NAME = "Book";
	public static final String TIMESTAMP  = "timestamp";

	public static final String CREATE_TABLE = String.format(
			"CREATE TABLE IF NOT EXISTS %s ("
					+ "%s INTEGER PRIMARY KEY,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s INTEGER DEFAULT 0,"
					+ "%s DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL"
					+ ")",
			TABLE_NAME,
			Book.BOOK_ID,
			Book.TITLE_EN,
			Book.TITLE_SP,
			Book.AUTHOR,
			Book.IMAGE_URL,
			Book.WEBSITE_URL,
			Book.DATA_FILE,
			Book.ITEM_COUNT,
			TIMESTAMP
	);

	public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);

	public static final String SELECT_ITEM_COUNT_WITH_BOOK_ID = String.format(
			"SELECT %s FROM %s WHERE %s = ?", Book.ITEM_COUNT, TABLE_NAME, Book.BOOK_ID
	);

	public static final String SELECT_ALL = String.format("SELECT * FROM %s", TABLE_NAME);
}
