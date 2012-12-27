package de.freewarepoint.cr.ai;

import de.freewarepoint.cr.Game;

public interface AI {

	public void doMove();

	public void setGame(Game game);

	public String getName();
	
}
