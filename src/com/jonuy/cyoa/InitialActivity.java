package com.jonuy.cyoa;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class InitialActivity extends Activity {
	
	Story selectedStory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initial_page);
		
		try {
			AssetManager am = getAssets();
			InputStream is = am.open("story_data/test-story.csv");
			
			selectedStory = new Story();
			selectedStory.loadStory(is);
			
			//am.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		// TODO: save to Shared Preferences cache
		// TODO: load story from cache if it's there
	}
	
	public void beginStory(View v) {
		Log.v("story", "begin story");
		startActivity(BasePage.getNewIntent(this, selectedStory, selectedStory.getFirstPageId()));
	}
}
