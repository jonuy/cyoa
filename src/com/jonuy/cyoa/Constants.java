package com.jonuy.cyoa;

public final class Constants {
	
	public static enum PageType {
		STANDARD,
		STANDARD_IMAGE,
		CHOICE,
		CHOICE_IMAGE,
		CHOICE_TIME,
		END
	};

	private Constants() {
		throw new AssertionError();
	}
}
