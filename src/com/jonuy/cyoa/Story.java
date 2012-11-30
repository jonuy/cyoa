package com.jonuy.cyoa;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class Story {
	
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
	
	public StoryNode getPage(String nodeId) {
		for (int i = 0; i < pages.size(); i++) {
			StoryNode page = pages.get(i); 
			if (page.getNodeId() == nodeId) {
				return page;
			}
		}
		
		return null;
	}
	
	public String getFirstPageId() {
		return "0";
	}
	
	public StoryNode getFirstPage() {
		return getPage(getFirstPageId());
	}
}
