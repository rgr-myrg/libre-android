package net.usrlib.libre.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import net.usrlib.libre.R;
import net.usrlib.libre.model.BookItem;
import net.usrlib.libre.util.Logger;

/**
 * Created by rgr-myrg on 1/4/17.
 */

public class BookChapterFragment extends Fragment {
	private View mRootView;
	private BookItem mBookItem = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);

//		if (savedInstanceState != null) {
//			mData = savedInstanceState.getParcelable(MvpModel.NAME);
//			Log.d("FRAGMENT",mData.getTitle() + " isFavorite: " + mData.isFavorite());
//		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Logger.i("FRAGMENT", "VIEW");
		mRootView = inflater.inflate(R.layout.book_chapter_item, container, false);

		setWebView(mBookItem.getContentEN());
		return mRootView;
	}

	public BookChapterFragment() {}

	public static final BookChapterFragment newInstance(final BookItem bookItem) {
		final BookChapterFragment fragment = new BookChapterFragment();
		fragment.mBookItem = bookItem;

		return fragment;
	}

	private void setWebView(final String url) {
		final WebView webView = (WebView) mRootView.findViewById(R.id.content_web_view);
		webView.loadUrl(url);
	}

	public BookChapterActivity_ getBaseActivity() {
		return (BookChapterActivity_) getActivity();
	}
}
