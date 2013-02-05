package de.freewarepoint.cr;

import de.freewarepoint.cr.ai.AI;


public class PlayerStatus {
	private final Player player;
	private AI ai = null;
	
	public PlayerStatus(final Player player) {
		this.player = player;
	}
	
	public void setAI(final AI ai) {
		this.ai = ai;
	}
	
	public AI getAI() {
		return ai;
	}

	public boolean isAIPlayer() {
		return ai != null;
	}
}
