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
					+ "%s TEXT NOT NULL,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s TEXT,"
					+ "%s DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL"
					+ ")",
			TABLE_NAME,
			BookItem.ITEM_ID,
			BookItem.BOOK_ID,
			BookItem.TITLE,
			BookItem.SPANISH,
			BookItem.ENGLISH,
			BookItem.IMAGE_URL,
			TIMESTAMP
	);

	public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
}
