package com.jonuy.cyoa;

import java.io.Serializable;

public class HistoryItem implements Serializable {
	private static final long serialVersionUID = Constants.SerialVersionUID.HISTORY_ITEM;
	
	private int pageId;
	private int previousPageId;
	private int nextPageId;
	private int userChoiceIndex;
	
	public HistoryItem(int _pageId, int _userChoiceIndex, int _previousPageId) {
		pageId = _pageId;
		userChoiceIndex = _userChoiceIndex;
		previousPageId = _previousPageId;
	}
	
	public int getPageId() {
		return pageId;
	}
	
	public int getPreviousPageId() {
		return previousPageId;
	}
	
	public int getNextPageId() {
		return nextPageId;
	}
	
	public int getUserChoiceIndex() {
		return userChoiceIndex;
	}
	
	public void setNextPageId(int _nextPageId) {
		nextPageId = _nextPageId;
	}
}