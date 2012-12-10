package com.jonuy.cyoa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pig.impl.util.ObjectSerializer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserStoryHistory {

	private static final String STORY_HISTORY = "user_story_history";
	
	private Context context;
	private List<HistoryItem> history;
	
	public UserStoryHistory(Context _context) {
		context = _context;
		
		// Load user's history if found in SharedPreferences
		SharedPreferences sharedPrefs = context.getSharedPreferences(Constants.CYOA_PREFERENCES, Context.MODE_PRIVATE);
		String storedHistory = sharedPrefs.getString(STORY_HISTORY, null);
		if (storedHistory != null) {
			try {
				history = (ArrayList<HistoryItem>)ObjectSerializer.deserialize(storedHistory);
			}
			catch (IOException e) {
				history = new ArrayList<HistoryItem>();
			}
		}
		else {
			history = new ArrayList<HistoryItem>();
		}
	}
	
	public void addHistory(int pageId, int userChoiceIndex) {
		int prevPageId = -1;
		
		if (history.size() > 0) {
			int lastItemIdx = history.size() - 1;
			HistoryItem lastItem = history.get(lastItemIdx);
			prevPageId = lastItem.getPageId();
			lastItem.setNextPageId(pageId);
		}
		
		HistoryItem item = new HistoryItem(pageId, userChoiceIndex, prevPageId);
		history.add(item);
		
		try {
			SharedPreferences sharedPrefs = context.getSharedPreferences(Constants.CYOA_PREFERENCES, Context.MODE_PRIVATE);
			Editor editor = sharedPrefs.edit();
			editor.putString(STORY_HISTORY, ObjectSerializer.serialize((ArrayList<HistoryItem>)history));
			editor.commit();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
