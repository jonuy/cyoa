package com.jonuy.cyoa;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jonuy.util.CustomTypefaceSpan;

public class BasePage extends Activity {
	
	private Story story;
	private StoryNode currentPage;
	
	private Typeface fontDroidMono;
	private Typeface fontHVD;
	private Typeface fontRoboto;
	private CustomTypefaceSpan fontDroidMonoSpan;
	private CustomTypefaceSpan fontRobotoSpan;
	
	private UserStoryHistory userHistory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.error_page);
		
		fontDroidMono = Typeface.createFromAsset(getAssets(), "fonts/DroidSansMono-webfont.ttf");
		fontHVD = Typeface.createFromAsset(getAssets(), "fonts/HVD_Comic_Serif_Pro.otf");
		fontRoboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
		fontDroidMonoSpan = new CustomTypefaceSpan("monospace", fontDroidMono);
		fontRobotoSpan = new CustomTypefaceSpan("sans-serif", fontRoboto);
		
		userHistory = new UserStoryHistory(this);
		
		int currentPageId = 0;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			story = (Story)extras.get(Constants.BundleId.STORY);
			currentPageId = extras.getInt(Constants.BundleId.PAGE_ID, 1);
		}
		
		if (story != null && currentPageId > 0) {
			currentPage = story.getPage(currentPageId);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		setPageContent();
	}
	
	@Override
	public void onBackPressed() {
		// TODO eventually want to use goPreviousPage() and override animations
		super.onBackPressed();
	}
	
	protected void setPageContent() {
		
		Constants.PageType pageType = currentPage.getPageType();
		
		if (pageType == Constants.PageType.STANDARD
			|| pageType == Constants.PageType.CHOICE) {
			setContentView(R.layout.standard_page);
			
			setHeaderText();
			setBodyText();
			createUserChoiceButtons();
		}
		else if (pageType == Constants.PageType.STANDARD_IMAGE
			|| pageType == Constants.PageType.CHOICE_IMAGE) {
			setContentView(R.layout.image_page);
			
			setHeaderText();
			setBodyText();
			setImage();
			createUserChoiceButtons();
		}
		else if (pageType == Constants.PageType.CHOICE_TIME) {
			setContentView(R.layout.time_limit_page);
			
			setHeaderText();
			setBodyText();
			setImage();
			createUserChoiceButtons();
			
			long timerLength = currentPage.getChoiceTime() * 1000;
			TextView tvTimer = (TextView)findViewById(R.id.timer_text);
			new ChoiceTimer(timerLength, tvTimer).start();
		}
		else if (pageType == Constants.PageType.END) {
			setContentView(R.layout.end_page);
			
			setHeaderText();
			setBodyText();
			setImage();

			Button endButton = (Button)findViewById(R.id.endButton);
			endButton.setTypeface(fontHVD);
			endButton.setOnClickListener(new OnEndClickListener());
		}
	}
	
	private void setBodyText() {
		String bodyText = unescape(currentPage.getText());
		List<SpanTextRange> spanRanges = new ArrayList<SpanTextRange>(); 
		
		int currentIdx = 0;
		boolean finished = false;
		// Parse through text and get ranges of characters and decide what font it should use
		while (finished == false) {
			int monoMarkupIdx = bodyText.indexOf("<mono>", currentIdx); 
			
			// Add range for the mono markup  
			if (monoMarkupIdx == currentIdx) {
				// find matching </mono> and use mono font
				int endMonoMarkupIdx = bodyText.indexOf("</mono>", currentIdx);
				bodyText = bodyText.substring(0, monoMarkupIdx) 
						+ bodyText.substring(monoMarkupIdx + "<mono>".length(), endMonoMarkupIdx)
						+ bodyText.substring(endMonoMarkupIdx + "</mono>".length(), bodyText.length());
				
				int newEndMono = endMonoMarkupIdx - "<mono>".length();
				spanRanges.add(new SpanTextRange(currentIdx, newEndMono, "monospace")); 
				
				currentIdx = endMonoMarkupIdx;
			}
			// Still a body of text before the mono markup
			else if (monoMarkupIdx > 0) {
				// set text with normal font
				spanRanges.add(new SpanTextRange(currentIdx, monoMarkupIdx, "sans-serif"));
				currentIdx = monoMarkupIdx;
			}
			// indexOf will return -1 if no match is found
			else {
				spanRanges.add(new SpanTextRange(currentIdx, bodyText.length(), "sans-serif"));
				finished = true;
			}
		}
		
		// Using ranges found, set font depending on font family each span is set to.           
		Spannable spanText = new SpannableString(bodyText);
		for(Iterator<SpanTextRange> iter = spanRanges.iterator(); iter.hasNext();) {
			SpanTextRange range = iter.next();
			CustomTypefaceSpan font = fontRobotoSpan;
			if (range.getFontFamily().compareTo("monospace") == 0) {
				font = fontDroidMonoSpan;
			}
			spanText.setSpan(font, range.getStartIndex(), range.getEndIndex(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		
		TextView tvText = (TextView)findViewById(R.id.text);
		tvText.setText(spanText);
	}
	
	private class SpanTextRange {
		private int startIndex;
		private int endIndex;
		private String fontFamily;
		
		public SpanTextRange(int _start, int _end, String _font) {
			startIndex = _start;
			endIndex = _end;
			fontFamily = _font;
		}
		
		public int getStartIndex() {
			return startIndex;
		}
		
		public int getEndIndex() {
			return endIndex;
		}
		
		public String getFontFamily() {
			return fontFamily;
		}
	}
	
	private void setHeaderText() {
		TextView tvHeader = (TextView)findViewById(R.id.header);
		tvHeader.setText(currentPage.getHeader());
		tvHeader.setTypeface(fontHVD);
	}
	
	private void setImage() {
		if (currentPage.getImage().isEmpty()) {
			return;
		}
		
		ImageView ivImage = (ImageView)findViewById(R.id.image);
		ivImage.setContentDescription(currentPage.getImageDescription());
		try {
			String imgFilename = Constants.STORY_DATA_FOLDER + story.getName() + "/" + currentPage.getImage();
			InputStream is = getAssets().open(imgFilename);
			Bitmap bmp = BitmapFactory.decodeStream(is);
			ivImage.setImageBitmap(bmp);
		}
		catch (IOException e) {
			ivImage.setVisibility(View.GONE);
		}
	}
	
	private void createUserChoiceButtons() {
		int previousChoiceIndex = -1;
		HistoryItem historyItem = userHistory.findItemForPageId(currentPage.getPageId());
		if (historyItem != null) {
			previousChoiceIndex = historyItem.getUserChoiceIndex();
		}
		
		LinearLayout llChoices = (LinearLayout)findViewById(R.id.choice_container);
		UserChoiceClickListener ucClickListener = new UserChoiceClickListener();
		for (int i = 0; i < currentPage.getNumChoices(); i++) {
			UserChoice uc = currentPage.getUserChoiceByIndex(i);
			
			Button button = new Button(this);
			button.setTextAppearance(this, R.style.UserChoiceButton);
			button.setText(uc.getText());
			button.setTypeface(fontHVD);
			button.setId(i);
			button.setOnClickListener(ucClickListener);
			
			if (i == previousChoiceIndex) {
				button.setTextColor(Color.MAGENTA);
			}
			
			llChoices.addView(button);
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
	
	private class ChoiceTimer extends CountDownTimer {
		private static final long tickInterval = 1000;
		private static final int warningTime = 6;
		
		private boolean toggledWarning = false;
		private TextView tvTimer;
		
		public ChoiceTimer(long _timerLength, TextView _tvTimer) {
			super(_timerLength, tickInterval);
			tvTimer = _tvTimer;
			tvTimer.setTypeface(fontHVD);
		}
		
		@Override
		public void onTick(long millisUntilFinished) {
			long secondsUntilFinished = millisUntilFinished / 1000;
			tvTimer.setText(getString(R.string.choice_page_timer, Long.toString(secondsUntilFinished)));
			
			if (secondsUntilFinished < warningTime && !toggledWarning) {
				tvTimer.setTextColor(Color.RED);
				toggledWarning = true;
			}
		}
		
		@Override
		public void onFinish() {
			// TODO randomly choose an option?
			tvTimer.setText("done!");
		}
	}
	
	private class UserChoiceClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			int ucIndex = view.getId();
			UserChoice uc = currentPage.getUserChoiceByIndex(ucIndex);
			int nextPageId = uc.getPageId();
			
			userHistory.addHistory(currentPage.getPageId(), ucIndex);
			goNextPage(nextPageId);
		}
	}
	
	private class OnEndClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			startActivity(new Intent(view.getContext(), InitialActivity.class));
		}
	}
}
