package com.jonuy.cyoa;

import java.io.Serializable;

public class UserChoice implements Serializable {
	private static final long serialVersionUID = -6305653994169433601L;
	private String pageId;
	private String text;
	private StoryNode node;
	
	public UserChoice(String _pageId, String _text) {
		this.pageId = _pageId;
		this.text = _text;
	}
	
	public void setNode(StoryNode _node) {
		node = _node;
	}
	
	public String getPageId() {
		return this.pageId;
	}
	
	public String getText() {
		return this.text;
	}
	
	public StoryNode getNode() {
		return this.node;
	}
	
	@Override
	public String toString() {
		return "["+this.pageId+","+this.text+"]";
	}
}