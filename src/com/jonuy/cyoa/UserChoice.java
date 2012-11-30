package com.jonuy.cyoa;

import java.io.Serializable;

public class UserChoice implements Serializable {
	private static final long serialVersionUID = Constants.SerialVersionUID.USER_CHOICE;
	private int pageId;
	private String text;
	private StoryNode node;
	
	public UserChoice(int _pageId, String _text) {
		pageId = _pageId;
		text = _text;
	}
	
	public void setNode(StoryNode _node) {
		node = _node;
	}
	
	public int getPageId() {
		return pageId;
	}
	
	public String getText() {
		return text;
	}
	
	public StoryNode getNode() {
		return node;
	}
	
	@Override
	public String toString() {
		return "["+pageId+","+text+"]";
	}
}