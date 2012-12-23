package de.maikmerten.chainreaction;

public class Settings {

	private final int reactionDely;

	public Settings() {
		this.reactionDely = 25;
	}

	public Settings(int reactionDely) {
		this.reactionDely = reactionDely;
	}

	public int getReactionDely() {
		return reactionDely;
	}
}
