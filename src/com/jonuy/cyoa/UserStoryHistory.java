package com.jonuy.cyoa;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.pig.impl.util.ObjectSerializer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class UserStoryHistory {

	private static final String STORY_HISTORY = "user_story_history";
	
	private Context context;
	private List<HistoryItem> history;
	private SharedPreferences sharedPrefs;
	
	public UserStoryHistory(Context _context) {
		context = _context;
		
		// Load user's history if found in SharedPreferences
		sharedPrefs = context.getSharedPreferences(Constants.CYOA_PREFERENCES, Context.MODE_PRIVATE);
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
		
		// Set nextPageId property of last saved HistoryItem
		if (history.size() > 0) {
			int lastItemIdx = history.size() - 1;
			HistoryItem lastItem = history.get(lastItemIdx);
			prevPageId = lastItem.getPageId();
			lastItem.setNextPageId(pageId);
		}
		
		// Then add the new item to the array
		HistoryItem item = new HistoryItem(pageId, userChoiceIndex, prevPageId);
		history.add(item);
		
		try {
			Editor editor = sharedPrefs.edit();
			editor.putString(STORY_HISTORY, ObjectSerializer.serialize((ArrayList<HistoryItem>)history));
			editor.commit();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public HistoryItem findItemForPageId(int pageId) {
		for (Iterator<HistoryItem> iter = history.iterator(); iter.hasNext();) {
			HistoryItem item = iter.next();
			if (pageId == item.getPageId()) {
				return item;
			}
		}
		
		return null;
	}
	
	public void clearHistory() {
		Editor editor = sharedPrefs.edit();
		editor.clear();
		editor.commit();
	}
}
