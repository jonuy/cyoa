package com.jonuy.cyoa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StoryNode implements Serializable {
	
	private static final long serialVersionUID = Constants.SerialVersionUID.STORY_NODE;
	// Time to make a choice in seconds
	private int choiceTime;
	// Header at the top of the page
	private String header;
	// Filename of image to display
	private String image;
	// Image description primarily used for accessibility
	private String imageDescription;
	// Unique page identifier
	private int pageId;
	// Number of user choices available for this page
	private int numChoices;
	// Denotes page's type
	private Constants.PageType pageType;
	// Main body text
	private String text;
	// List of choice objects defining text and destination for user choices
	private List<UserChoice> userChoices;
	
	public StoryNode() {}
	
	public StoryNode(String[] csvLine) {
		pageId = Integer.parseInt(csvLine[0]);
		pageType = translatePageType(csvLine[1]);
		header = csvLine[2];
		text = csvLine[3];
		image = csvLine[4];
		imageDescription = csvLine[5];
		
		numChoices = Integer.parseInt(csvLine[6]);
		String[] choicesTexts = separateBySemicolons(csvLine[7]);
		String[] choiceDestinations = separateBySemicolons(csvLine[8]); 
		userChoices = new ArrayList<UserChoice>();
		for (int i = 0; i < numChoices; i++) {
			UserChoice uc = new UserChoice(Integer.parseInt(choiceDestinations[i]), choicesTexts[i]);
			userChoices.add(uc);
		}
		
		choiceTime = csvLine[9].length() > 0 ? Integer.parseInt(csvLine[9]) : 0;
	}
	
	public int getChoiceTime() {
		return choiceTime;
	}
	
	public String getHeader() {
		return header;
	}
	
	public String getImage() {
		return image;
	}
	
	public String getImageDescription() {
		return imageDescription;
	}
	
	public int getPageId() {
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
	
	public UserChoice getUserChoiceById(int _pageId) {
		for (int i = 0; i < userChoices.size(); i++) {
			UserChoice uc = userChoices.get(i);
			if (_pageId == uc.getPageId()) {
				return uc;
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
				+ " - images = " + image + "\n"
				+ " - numChoices = " + numChoices + "\n"
				+ " - choices = " + choicesString + "\n"
				+ " - choiceTime = " + choiceTime + "\n";
	}
}
