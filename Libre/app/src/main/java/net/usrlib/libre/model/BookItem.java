package net.usrlib.libre.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rgr-myrg on 1/2/17.
 */

public class BookItem {
	public static final String ITEM_ID    = "itemId";
	public static final String BOOK_ID    = "bookId";
	public static final String TITLE_EN   = "titleEN";
	public static final String TITLE_SP   = "titleSP";
	public static final String CONTENT_SP = "contentSP";
	public static final String CONTENT_EN = "contentEN";
	public static final String IMAGE_URL  = "imageUrl";
	public static final String BOOKMARKED = "bookmarked";
	public static final String HTML_CACHE = "htmlCache";

	@SerializedName("itemId")
	private int itemId;

	@SerializedName("bookId")
	private int bookId;

	@SerializedName("titleEN")
	private String titleEN;

	@SerializedName("titleSP")
	private String titleSP;

	@SerializedName("spanish")
	private String contentSP;

	@SerializedName("english")
	private String contentEN;

	@SerializedName("image")
	private String imageUrl;

	private boolean bookmarked;
	private String htmlCache;

	public int getItemId() {
		return itemId;
	}

	public int getBookId() {
		return bookId;
	}

	public String getTitleEN() {
		return titleEN;
	}

	public String getTitleSP() {
		return titleSP;
	}

	public String getContentSP() {
		return contentSP;
	}

	public String getContentEN() {
		return contentEN;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public boolean isBookmarked() {
		return bookmarked;
	}

	public String getHtmlCache() {
		return htmlCache;
	}

	public void setHtmlCache(String htmlCache) {
		this.htmlCache = htmlCache;
	}

	public boolean hasHtmlCache() {
		// Handle sqlite default null Text value. Meep.
		return !htmlCache.equals("0");
	}

	public ContentValues toContentValues() {
		final ContentValues values = new ContentValues();

		values.put(ITEM_ID, itemId);
		values.put(BOOK_ID, bookId);
		values.put(TITLE_EN, titleEN);
		values.put(TITLE_SP, titleSP);
		values.put(CONTENT_SP, contentSP);
		values.put(CONTENT_EN, contentEN);
		values.put(IMAGE_URL, imageUrl);

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

		bookItem.itemId     = cursor.getInt(cursor.getColumnIndex(BookItem.ITEM_ID));
		bookItem.bookId     = cursor.getInt(cursor.getColumnIndex(BookItem.BOOK_ID));
		bookItem.titleEN    = cursor.getString(cursor.getColumnIndex(BookItem.TITLE_EN));
		bookItem.titleSP    = cursor.getString(cursor.getColumnIndex(BookItem.TITLE_SP));
		bookItem.contentSP  = cursor.getString(cursor.getColumnIndex(BookItem.CONTENT_SP));
		bookItem.contentEN  = cursor.getString(cursor.getColumnIndex(BookItem.CONTENT_EN));
		bookItem.imageUrl   = cursor.getString(cursor.getColumnIndex(BookItem.IMAGE_URL));
		bookItem.bookmarked = cursor.getInt(cursor.getColumnIndex(BookItem.BOOKMARKED)) == 1;
		bookItem.htmlCache  = cursor.getString(cursor.getColumnIndex(BookItem.HTML_CACHE));

		return bookItem;
	}
}
