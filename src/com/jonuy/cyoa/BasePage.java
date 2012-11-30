package com.jonuy.cyoa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class BasePage extends Activity {
	
	private Story story;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.error_page);
		
		story = (Story)getIntent().getExtras().get(Constants.BundleId.STORY);
	}
	
	protected void loadContentView(int pageType) {
		
	}
	
	protected void goNextPage() {
		// Determine what next page is based on user selection
	}
	
	protected void goPreviousPage() {
		// Refer to user's history and go back to previous page
	}
	
	public static Intent getNewIntent(Context ctx, Story story, String pageId) {
		Intent intent = new Intent(ctx, BasePage.class);
		intent.putExtra(Constants.BundleId.STORY, story);
		
		return intent;
	}
}
