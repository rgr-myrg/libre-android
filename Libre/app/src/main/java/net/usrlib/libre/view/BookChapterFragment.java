package net.usrlib.libre.view;

import android.support.v4.app.Fragment;
import android.webkit.WebView;

import net.usrlib.libre.R;
import net.usrlib.libre.model.BookItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rgr-myrg on 1/4/17.
 */

@EFragment(R.layout.book_chapter_item)
public class BookChapterFragment extends Fragment {
	public BookItem mBookItem = null;

	@ViewById(R.id.content_web_view)
	protected WebView mWebView;

	@AfterViews
	protected void loadWebView() {
		setRetainInstance(true);
		mWebView.loadUrl(mBookItem.getContentEN());
	}

	public BookChapterFragment() {}

	public static final BookChapterFragment newInstance(final BookItem bookItem) {
		final BookChapterFragment fragment = new BookChapterFragment_();
		fragment.mBookItem = bookItem;

		return fragment;
	}

//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		setRetainInstance(true);
//
////		if (savedInstanceState != null) {
////			mData = savedInstanceState.getParcelable(MvpModel.NAME);
////			Log.d("FRAGMENT",mData.getTitle() + " isFavorite: " + mData.isFavorite());
////		}
//	}

//	@Nullable
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		Logger.i("FRAGMENT", "VIEW");
//		mRootView = inflater.inflate(R.layout.book_chapter_item, container, false);
//
//		setWebView(mBookItem.getContentEN());
//		return mRootView;
//	}

//	private void setWebView(final String url) {
//		final WebView webView = (WebView) mRootView.findViewById(R.id.content_web_view);
//		webView.loadUrl(url);
//	}
}
