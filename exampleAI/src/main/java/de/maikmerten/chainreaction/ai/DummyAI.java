package de.maikmerten.chainreaction.ai;

import de.maikmerten.chainreaction.Game;

public class DummyAI implements AI {

	private Game game;

	@Override
	public void doMove() {
	}

	@Override
	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public String getName() {
		return "Dummy AI";
	}
}
