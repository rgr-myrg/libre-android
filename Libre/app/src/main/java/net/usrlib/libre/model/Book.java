package net.usrlib.libre.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rgr-myrg on 1/2/17.
 */

public class Book {
	public static final String BOOK_ID = "bookId";
	public static final String TITLE   = "title";
	public static final String CAPTION = "caption";
	public static final String AUTHOR  = "author";
	public static final String IMAGE_URL   = "imageUrl";
	public static final String WEBSITE_URL = "websiteUrl";
	public static final String DATA_FILE   = "dataFile";
	public static final String ITEM_COUNT  = "itemCount";

	@SerializedName("bookId")
	private int bookId;

	@SerializedName("title")
	private String title;

	@SerializedName("caption")
	private String caption;

	@SerializedName("author")
	private String author;

	@SerializedName("image")
	private String imageUrl;

	@SerializedName("url")
	private String websiteUrl;

	@SerializedName("data")
	private String dataFile;

	@SerializedName("items")
	private int itemCount;

	public ContentValues toContentValues() {
		final ContentValues values = new ContentValues();

		values.put(BOOK_ID, bookId);
		values.put(TITLE, title);
		values.put(CAPTION, caption);
		values.put(AUTHOR, author);
		values.put(IMAGE_URL, imageUrl);
		values.put(WEBSITE_URL, websiteUrl);
		values.put(DATA_FILE, dataFile);
		values.put(ITEM_COUNT, itemCount);

		return values;
	}

	public static final ContentValues[] fromListAsContentValues(final List<Book> list) {
		final ContentValues[] contentValues = new ContentValues[list.size()];

		for (int i = 0; i < list.size(); i++) {
			contentValues[i] = list.get(i).toContentValues();
		}

		return contentValues;
	}

	public static Book fromDbCursor(final Cursor cursor) {
		if (cursor == null) {
			return null;
		}

		final Book book = new Book();
		book.bookId     = cursor.getInt(cursor.getColumnIndex(Book.BOOK_ID));
		book.title      = cursor.getString(cursor.getColumnIndex(Book.TITLE));
		book.caption    = cursor.getString(cursor.getColumnIndex(Book.CAPTION));
		book.author     = cursor.getString(cursor.getColumnIndex(Book.AUTHOR));
		book.imageUrl   = cursor.getString(cursor.getColumnIndex(Book.IMAGE_URL));
		book.websiteUrl = cursor.getString(cursor.getColumnIndex(Book.WEBSITE_URL));
		book.dataFile   = cursor.getString(cursor.getColumnIndex(Book.DATA_FILE));
		book.itemCount  = cursor.getInt(cursor.getColumnIndex(Book.ITEM_COUNT));

		return book;
	}

	public static List<Book> fromDbCursorAsList(final Cursor cursor) {
		if (cursor == null) {
			return null;
		}

		final List<Book> list = new ArrayList<>();

		while (cursor.moveToNext()) {
			list.add(fromDbCursor(cursor));
		}

		return list;
	}

	public int getBookId() {
		return bookId;
	}

	public String getTitle() {
		return title;
	}

	public String getCaption() {
		return caption;
	}

	public String getAuthor() {
		return author;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public String getDataFile() {
		return dataFile;
	}

	public int getItemCount() {
		return itemCount;
	}
}
