package de.freewarepoint.cr.ai;

import de.freewarepoint.cr.Game;

/**
 *
 * @author jonny
 */
public class NoneAI implements AI {

	@Override
	public void doMove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setGame(Game game) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return "None";
	}


}
