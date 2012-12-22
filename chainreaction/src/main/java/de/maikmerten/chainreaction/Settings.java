package de.maikmerten.chainreaction;

public class Settings {

	private final int delay;

	public Settings() {
		this.delay = 25;
	}

	public Settings(int delay) {
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
	}
}
