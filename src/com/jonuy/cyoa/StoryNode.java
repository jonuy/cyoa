package com.jonuy.cyoa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
	private List<UserChoice> userChoices;
	
	public StoryNode() {}
	
	public StoryNode(String[] csvLine) {
		pageId = csvLine[0];
		pageType = translatePageType(csvLine[1]);
		header = csvLine[2];
		text = csvLine[3];
		images = separateBySemicolons(csvLine[4]);
		
		numChoices = Integer.parseInt(csvLine[5]);
		String[] choicesTexts = separateBySemicolons(csvLine[6]);
		String[] choiceDestinations = separateBySemicolons(csvLine[7]); 
		userChoices = new ArrayList<UserChoice>();
		for (int i = 0; i < numChoices; i++) {
			UserChoice uc = new UserChoice(choiceDestinations[i], choicesTexts[i]);
			userChoices.add(uc);
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
	
	public Constants.PageType getPageType() {
		return pageType;
	}
	
	public Constants.PageType translatePageType(String val) {
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
	
	public int getNumChoices() {
		return numChoices;
	}
	
	public UserChoice getUserChoiceById(String _pageId) {
		if (!_pageId.isEmpty()) {
			for (int i = 0; i < userChoices.size(); i++) {
				UserChoice uc = userChoices.get(i);
				if (_pageId.compareTo(uc.getPageId()) == 0) {
					return uc;
				}
			}
		}
		
		return null;
	}
	
	public UserChoice getUserChoiceByIndex(int index) {
		try {
			return userChoices.get(index);
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
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
		Iterator<UserChoice> ucIter = userChoices.iterator();
		while (ucIter.hasNext()) {
			UserChoice uc = ucIter.next();
			choicesString += uc.toString() + ";";
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
}
