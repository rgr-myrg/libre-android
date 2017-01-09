package net.usrlib.libre.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rgr-myrg on 1/2/17.
 */

public class BookItem {
	public static final String ITEM_KEY    = "itemKey";
	public static final String BOOK_ID     = "bookId";
	public static final String TITLE       = "title";
	public static final String CONTENT     = "content";
	public static final String BOOKMARKED  = "bookmarked";
	public static final String MARKED_READ = "markedItemAsRead";
	public static final String HTML_CACHE  = "htmlCache";

	@SerializedName("itemKey")
	private String itemKey;

	@SerializedName("bookId")
	private int bookId;

	@SerializedName("title")
	private String title;

	@SerializedName("content")
	private String content;

	private boolean bookmarked;
	private boolean markedRead;
	private String htmlCache;

	public String getItemKey() {
		return itemKey;
	}

	public int getBookId() {
		return bookId;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public boolean isBookmarked() {
		return bookmarked;
	}

	public boolean isMarkedRead() {
		return markedRead;
	}

	public String getHtmlCache() {
		return htmlCache;
	}

	public void setHtmlCache(String htmlCache) {
		this.htmlCache = htmlCache;
	}

	public boolean hasHtmlCache() {
		// Handle sqlite default 0 Text value. Meep.
		return !(htmlCache == null || htmlCache.equals("0"));
	}

	public ContentValues toContentValues() {
		final ContentValues values = new ContentValues();

		values.put(ITEM_KEY, itemKey);
		values.put(BOOK_ID, bookId);
		values.put(TITLE, title);
		values.put(CONTENT, content);

		return values;
	}

	public static final ContentValues[] fromListAsContentValues(final List<BookItem> list) {
		final ContentValues[] contentValues = new ContentValues[list.size()];

		for (int i = 0; i < list.size(); i++) {
			contentValues[i] = list.get(i).toContentValues();
		}

		return contentValues;
	}

	public static BookItem fromDbCursor(final Cursor cursor) {
		if (cursor == null) {
			return null;
		}

		final BookItem bookItem = new BookItem();

		bookItem.itemKey    = cursor.getString(cursor.getColumnIndex(BookItem.ITEM_KEY));
		bookItem.bookId     = cursor.getInt(cursor.getColumnIndex(BookItem.BOOK_ID));
		bookItem.title      = cursor.getString(cursor.getColumnIndex(BookItem.TITLE));
		bookItem.content    = cursor.getString(cursor.getColumnIndex(BookItem.CONTENT));
		bookItem.bookmarked = cursor.getInt(cursor.getColumnIndex(BookItem.BOOKMARKED)) == 1;
		bookItem.markedRead = cursor.getInt(cursor.getColumnIndex(BookItem.MARKED_READ)) == 1;
		bookItem.htmlCache  = cursor.getString(cursor.getColumnIndex(BookItem.HTML_CACHE));

		return bookItem;
	}
}
