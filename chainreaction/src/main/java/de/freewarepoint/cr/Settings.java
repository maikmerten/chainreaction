package de.freewarepoint.cr;

public class Settings {

	private final int reactionDelay;

	public Settings() {
		this.reactionDelay = 100;
	}

	public Settings(int reactionDelay) {
		this.reactionDelay = reactionDelay;
	}

	public int getReactionDelay() {
		return reactionDelay;
	}
}
