package net.usrlib.libre.model;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rgr-myrg on 1/2/17.
 */

public class BookItem {
	public static final String ITEM_ID = "itemId";
	public static final String BOOK_ID = "bookId";
	public static final String TITLE = "title";
	public static final String SPANISH = "spanish";
	public static final String ENGLISH = "english";
	public static final String IMAGE_URL = "imageUrl";

	@SerializedName("itemId")
	private int itemId;

	@SerializedName("bookId")
	private int bookId;

	@SerializedName("title")
	private String title;

	@SerializedName("spanish")
	private String spanish;

	@SerializedName("english")
	private String english;

	@SerializedName("image")
	private String imageUrl;

	public int getItemId() {
		return itemId;
	}

	public int getBookId() {
		return bookId;
	}

	public String getTitle() {
		return title;
	}

	public String getSpanish() {
		return spanish;
	}

	public String getEnglish() {
		return english;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public ContentValues toContentValues() {
		final ContentValues values = new ContentValues();

		values.put(ITEM_ID, itemId);
		values.put(BOOK_ID, bookId);
		values.put(TITLE, title);
		values.put(SPANISH, spanish);
		values.put(ENGLISH, english);
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
}
