package net.usrlib.libre.rest;

import net.usrlib.libre.model.Book;
import net.usrlib.libre.model.BookItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by rgr-myrg on 1/2/17.
 */

public interface RetrofitApi {
	@GET("book-list.json")
	Call<List<Book>> getBookList();

	// Appends the path to RetrofitClient.BASE_URL
	@GET("{dataFile}")
	Call<List<BookItem>> getBookItems(@Path("dataFile") String dataFile);
}
