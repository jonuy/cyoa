package com.jonuy.cyoa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class BasePage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.error_page);
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
		
		return intent;
	}
}
