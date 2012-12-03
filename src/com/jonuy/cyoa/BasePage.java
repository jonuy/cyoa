package com.jonuy.cyoa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BasePage extends Activity {
	
	private Story story;
	private StoryNode currentPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.error_page);
		
		int currentPageId = 0;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			story = (Story)extras.get(Constants.BundleId.STORY);
			currentPageId = extras.getInt(Constants.BundleId.PAGE_ID, 1);
		}
		
		if (story != null && currentPageId > 0) {
			currentPage = story.getPage(currentPageId);
			setPageContent();
		}
	}
	
	protected void setPageContent() {
		Typeface fontHVD = Typeface.createFromAsset(getAssets(), "fonts/HVD_Comic_Serif_Pro.otf");
		Typeface fontRoboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		
		Constants.PageType pageType = currentPage.getPageType();
		
		if (pageType == Constants.PageType.STANDARD
			|| pageType == Constants.PageType.CHOICE) {
			setContentView(R.layout.standard_page);
			
			TextView tvHeader = (TextView)findViewById(R.id.header);
			tvHeader.setText(currentPage.getHeader());
			tvHeader.setTypeface(fontHVD);
			
			TextView tvText = (TextView)findViewById(R.id.text);
			tvText.setText(unescape(currentPage.getText()));
			tvText.setTypeface(fontRoboto);
			
			
			LinearLayout llChoices = (LinearLayout)findViewById(R.id.choice_container);
			UserChoiceClickListener ucClickListener = new UserChoiceClickListener();
			for (int i = 0; i < currentPage.getNumChoices(); i++) {
				UserChoice uc = currentPage.getUserChoiceByIndex(i);
				
				Button button = new Button(this);
				button.setTextAppearance(this, R.style.UserChoiceButton);
				button.setText(uc.getText());
				button.setTypeface(fontHVD);
				button.setId(uc.getPageId());
				button.setOnClickListener(ucClickListener);
				llChoices.addView(button);
			}
		}
	}
	
	protected void goNextPage(int nextPageId) {
		startActivity(BasePage.getNewIntent(this, story, nextPageId));
		
		overrideAnimation(currentPage.getPageType());
		// TODO: end old activities
	}
	
	private void overrideAnimation(Constants.PageType pageType) {
		if (pageType == Constants.PageType.STANDARD
			|| pageType == Constants.PageType.STANDARD_IMAGE) {
			
			overridePendingTransition(R.anim.slide_in_left_overshoot, R.anim.slide_out_left_overshoot);
		}
		else if (pageType == Constants.PageType.CHOICE
				|| pageType == Constants.PageType.CHOICE_IMAGE
				|| pageType == Constants.PageType.CHOICE_TIME) {
			
			overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
		}
	}
	
	protected void goPreviousPage() {
		// Refer to user's history and go back to previous page
	}
	
	private String unescape(String val) {
		// Unescape new line characters being pulled from resource files
		return val.replaceAll("\\\\n", "\\\n");
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
