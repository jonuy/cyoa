package com.jonuy.cyoa;

public final class Constants {
	
	public static String STORY_DATA_FOLDER = "story_data/";
	public static String CYOA_PREFERENCES = "cyoa_preferences";
	
	public static enum PageType {
		STANDARD,
		STANDARD_IMAGE,
		CHOICE,
		CHOICE_IMAGE,
		CHOICE_TIME,
		END
	};
	
	public static class BundleId {
		public static String STORY = "STORY";
		public static String PAGE_ID = "PAGE_ID";
	}
	
	public static class SerialVersionUID {
		public static long STORY = 1L;
		public static long STORY_NODE = 2L;
		public static long USER_CHOICE = 3L;
		public static long HISTORY_ITEM = 4L;
	}

	private Constants() {
		throw new AssertionError();
	}
}
