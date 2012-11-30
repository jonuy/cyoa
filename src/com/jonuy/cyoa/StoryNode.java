package com.jonuy.cyoa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StoryNode implements Serializable {
	
	private static final long serialVersionUID = Constants.SerialVersionUID.STORY_NODE;
	private int choiceTime;
	private String header;
	private String[] images;
	private String pageId;
	private int numChoices;
	private Constants.PageType pageType;
	private String text;
	private Map<String,UserChoice> userChoices;
	
	public StoryNode() {}
	
	public StoryNode(String[] csvLine) {
		pageId = csvLine[0];
		pageType = getPageType(csvLine[1]);
		header = csvLine[2];
		text = csvLine[3];
		images = separateBySemicolons(csvLine[4]);
		
		numChoices = Integer.parseInt(csvLine[5]);
		String[] choicesTexts = separateBySemicolons(csvLine[6]);
		String[] choiceDestinations = separateBySemicolons(csvLine[7]); 
		userChoices = new HashMap<String,UserChoice>();
		for (int i = 0; i < numChoices; i++) {
			UserChoice uc = new UserChoice(choiceDestinations[i], choicesTexts[i]);
			userChoices.put(choiceDestinations[i], uc);
		}
		
		choiceTime = csvLine[8].length() > 0 ? Integer.parseInt(csvLine[8]) : 0;
	}
	
	public int getChoiceTime() {
		return choiceTime;
	}
	
	public String getHeader() {
		return header;
	}
	
	public String[] getImages() {
		return images;
	}
	
	public String getPageId() {
		return pageId;
	}
	
	public Constants.PageType getPageType(String val) {
		if (val.compareTo("standard-image") == 0) {
			return Constants.PageType.STANDARD_IMAGE;
		}
		else if (val.compareTo("choice") == 0) {
			return Constants.PageType.CHOICE;
		}
		else if (val.compareTo("choice-image") == 0) {
			return Constants.PageType.CHOICE_IMAGE;
		}
		else if (val.compareTo("choice-time") == 0) {
			return Constants.PageType.CHOICE_TIME;
		}
		else if (val.compareTo("end") == 0) {
			return Constants.PageType.END;
		}
		else {
			return Constants.PageType.STANDARD;
		}
	}
	
	public String getText() {
		return text;
	}
	
	public Map<String,UserChoice> getUserChoices() {
		return userChoices;
	}
	
	private String[] separateBySemicolons(String val) {
		String[] tokens = val.split(";");
		return tokens;
	}
	
	@Override
	public String toString() {
		String imagesString = "";
		for (int i = 0; i < images.length; i++) {
			imagesString += images[i] + ";";
		}
		
		String choicesString = "";
		for (Map.Entry<String, UserChoice> entry : userChoices.entrySet()) {
			choicesString += entry.getKey() + ":" + entry.getValue().toString() + ";";
		}
		
		return "pageId = " + pageId + "\n"
				+ " - type = " + pageType + "\n"
				+ " - header = " + header + "\n"
				+ " - text = " + text + "\n"
				+ " - images = " + imagesString + "\n"
				+ " - numChoices = " + numChoices + "\n"
				+ " - choices = " + choicesString + "\n"
				+ " - choiceTime = " + choiceTime + "\n";
	}
	
	private class UserChoice implements Serializable {
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
		
		public String getNodeId() {
			return this.pageId;
		}
		
		public String getText() {
			return this.pageId;
		}
		
		public StoryNode getNode() {
			return this.node;
		}
		
		@Override
		public String toString() {
			return "["+this.pageId+","+this.text+"]";
		}
	}
}
