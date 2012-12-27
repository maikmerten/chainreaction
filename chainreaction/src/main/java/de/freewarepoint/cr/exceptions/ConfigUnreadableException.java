package de.freewarepoint.cr.exceptions;

import java.io.IOException;

public class ConfigUnreadableException extends RuntimeException {

	public ConfigUnreadableException() {
		super();
	}

	public ConfigUnreadableException(IOException e) {
		super(e);
	}
}
