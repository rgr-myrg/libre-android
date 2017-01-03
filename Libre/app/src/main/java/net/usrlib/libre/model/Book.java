package net.usrlib.libre.model;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rgr-myrg on 1/2/17.
 */

public class Book {
	public static final String BOOK_ID = "bookId";
	public static final String TITLE = "title";
	public static final String AUTHOR = "author";
	public static final String IMAGE_URL = "imageUrl";
	public static final String WEBSITE_URL = "websiteUrl";
	public static final String DATA_FILE = "dataFile";
	public static final String ITEM_COUNT = "itemCount";

	@SerializedName("bookId")
	private int bookId;

	@SerializedName("title")
	private String title;

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

	public int getBookId() {
		return bookId;
	}

	public String getTitle() {
		return title;
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

	public ContentValues toContentValues() {
		final ContentValues values = new ContentValues();

		values.put(BOOK_ID, bookId);
		values.put(TITLE, title);
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
}
