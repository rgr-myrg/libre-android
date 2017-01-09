package net.usrlib.libre.view;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.usrlib.libre.R;
import net.usrlib.libre.model.Book;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

/**
 * Created by rgr-myrg on 12/8/16.
 */

@EActivity(R.layout.book_detail_activity)
@OptionsMenu(R.menu.menu_main)

public class BookDetailActivity extends AppCompatActivity {
	@ViewById(R.id.toolbar)
	protected Toolbar mToolbar;

	@ViewById(R.id.collapsing_toolbar)
	protected CollapsingToolbarLayout mCollapsingToolbar;

	@ViewById(R.id.book_cover_image)
	protected ImageView mBookCoverImage;

	@OptionsItem(android.R.id.home)
	void onHomeBackButtonClicked() {
		onBackPressed();
	}

	@AfterViews
	protected void setSupportActionBar() {
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@AfterViews
	protected void setToolbarTitle() {
		final Intent intent = getIntent();

		Glide.with(getApplicationContext())
				.load(intent.getStringExtra(Book.IMAGE_URL))
				.fitCenter()
				.into(mBookCoverImage);

		mCollapsingToolbar.setTitle(intent.getStringExtra(Book.TITLE));
	}
}
