package de.maikmerten.chainreaction.ai;

import de.maikmerten.chainreaction.Game;

public interface AI {

	public void doMove();

	public void setGame(Game game);

	public String getName();
	
}
