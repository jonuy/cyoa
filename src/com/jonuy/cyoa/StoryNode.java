package com.jonuy.cyoa;

import java.util.HashMap;
import java.util.Map;

public class StoryNode {

	private int choiceTime;
	private String header;
	private String[] images;
	private String nodeId;
	private int numChoices;
	private Constants.PageType pageType;
	private String text;
	private Map<String,UserChoice> userChoices;
	
	public StoryNode() {}
	
	public StoryNode(String[] csvLine) {
		nodeId = csvLine[0];
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
	
	public String getNodeId() {
		return nodeId;
	}
	
	public Constants.PageType getPageType(String val) {
		if (val == "standard-image") {
			return Constants.PageType.STANDARD_IMAGE;
		}
		else if (val == "choice") {
			return Constants.PageType.CHOICE;
		}
		else if (val == "choice-image") {
			return Constants.PageType.CHOICE_IMAGE;
		}
		else if (val == "choice-time") {
			return Constants.PageType.CHOICE_TIME;
		}
		else if (val == "end") {
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
	
	public void setUserChoiceNode(String _nodeId, StoryNode _node) {
		
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
		
		return "nodeId = " + nodeId + "\n"
				+ " - type = " + pageType + "\n"
				+ " - header = " + header + "\n"
				+ " - text = " + text + "\n"
				+ " - images = " + imagesString + "\n"
				+ " - numChoices = " + numChoices + "\n"
				+ " - choices = " + choicesString + "\n"
				+ " - choiceTime = " + choiceTime + "\n";
	}
	
	private class UserChoice {
		private String nodeId;
		private String text;
		private StoryNode node;
		
		public UserChoice(String _nodeId, String _text) {
			this.nodeId = _nodeId;
			this.text = _text;
		}
		
		public void setNode(StoryNode _node) {
			node = _node;
		}
		
		public String getNodeId() {
			return this.nodeId;
		}
		
		public String getText() {
			return this.nodeId;
		}
		
		public StoryNode getNode() {
			return this.node;
		}
		
		@Override
		public String toString() {
			return "["+this.nodeId+","+this.text+"]";
		}
	}
}
