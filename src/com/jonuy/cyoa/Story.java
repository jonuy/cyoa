package com.jonuy.cyoa;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class Story implements Serializable {
	
	private static final long serialVersionUID = Constants.SerialVersionUID.STORY;
	
	// TODO: eventually will want to store pages into a Tree-like structure
	private List<StoryNode> pages;
	
	public Story() {
		pages = new ArrayList<StoryNode>();
	}
	
	public void loadStory(InputStream is) {
		try {
			CSVReader reader = new CSVReader(new InputStreamReader(is));
			
			boolean readFirst = false;
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				if (!readFirst) {
					readFirst = true;
					continue;
				}
				
				StoryNode node = new StoryNode(nextLine);
				pages.add(node);
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public StoryNode getPage(int pageId) {
		for (int i = 0; i < pages.size(); i++) {
			StoryNode page = pages.get(i); 
			if (pageId == page.getPageId()) {
				return page;
			}
		}
		
		return null;
	}
	
	public int getFirstPageId() {
		return 1;
	}
	
	public StoryNode getFirstPage() {
		return getPage(getFirstPageId());
	}
}
