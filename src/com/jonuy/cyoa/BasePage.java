package com.jonuy.cyoa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BasePage extends Activity {
	
	private Story story;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.error_page);
		
		int pageId = 1;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			story = (Story)extras.get(Constants.BundleId.STORY);
			pageId = extras.getInt(Constants.BundleId.PAGE_ID, 1);
		}
		
		if (story != null) {
			setPageContent(pageId);
		}
	}
	
	protected void setPageContent(int _pageId) {
		StoryNode page = story.getPage(_pageId);
		Constants.PageType pageType = page.getPageType();
		
		if (pageType == Constants.PageType.STANDARD) {
			setContentView(R.layout.standard_page);
			
			TextView tvHeader = (TextView)findViewById(R.id.header);
			tvHeader.setText(page.getHeader());
			
			TextView tvText = (TextView)findViewById(R.id.text);
			tvText.setText(page.getText());
			
			LinearLayout llChoices = (LinearLayout)findViewById(R.id.choice_container);
			UserChoiceClickListener ucClickListener = new UserChoiceClickListener();
			for (int i = 0; i < page.getNumChoices(); i++) {
				UserChoice uc = page.getUserChoiceByIndex(i);
				
				Button button = new Button(this);
				button.setText(uc.getText());
				button.setId(uc.getPageId());
				button.setOnClickListener(ucClickListener);
				llChoices.addView(button);
			}
		}
	}
	
	protected void goNextPage(int nextPageId) {
		startActivity(BasePage.getNewIntent(this, story, nextPageId));
		// TODO: end old activities
	}
	
	protected void goPreviousPage() {
		// Refer to user's history and go back to previous page
	}
	
	public static Intent getNewIntent(Context _ctx, Story _story, int _pageId) {
		Intent intent = new Intent(_ctx, BasePage.class);
		intent.putExtra(Constants.BundleId.STORY, _story);
		intent.putExtra(Constants.BundleId.PAGE_ID, _pageId);
		
		return intent;
	}
	
	private class UserChoiceClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			int nextPageId = view.getId();
			goNextPage(nextPageId);
		}
	}
}
