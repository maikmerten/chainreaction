package de.maikmerten.chainreaction.test;

import de.maikmerten.chainreaction.Game;
import de.maikmerten.chainreaction.Player;
import de.maikmerten.chainreaction.Settings;
import junit.framework.Assert;
import org.junit.Test;

public class GameTest {

	@Test
	public void testCreateGame() {
		Game game = new Game(7, 8, new Settings());
		Assert.assertEquals(game.getField().getWidth(), 7);
		Assert.assertEquals(game.getField().getHeight(), 8);
		Assert.assertEquals(game.getCurrentPlayer(), Player.FIRST);
		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.FIRST), 0);
		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.SECOND), 0);
	}

	@Test
	public void testOneMove() {
		Game game = new Game(6, 6, new Settings());
		game.onMoveSelected(3, 3);
		Assert.assertEquals(game.getCurrentPlayer(), Player.SECOND);
		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.FIRST), 1);
		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.SECOND), 0);
	}

	@Test
	public void testMoveToInvalidPosition() {
		Game game = new Game(6, 6, new Settings());
		game.onMoveSelected(3, 3);
		Assert.assertEquals(game.getCurrentPlayer(), Player.SECOND);
		game.onMoveSelected(3, 3);
		Assert.assertEquals(game.getCurrentPlayer(), Player.SECOND);
	}

	@Test
	public void testMultipleMoves() {
		Game game = new Game(6, 6, new Settings());

		Assert.assertEquals(game.getCurrentPlayer(), Player.FIRST);
		game.onMoveSelected(2, 2);

		Assert.assertEquals(game.getCurrentPlayer(), Player.SECOND);
		game.onMoveSelected(3, 2);

		Assert.assertEquals(game.getCurrentPlayer(), Player.FIRST);
		game.onMoveSelected(2, 2);

		Assert.assertEquals(game.getCurrentPlayer(), Player.SECOND);
		game.onMoveSelected(5, 5);

		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.FIRST), 2);
		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.SECOND), 2);

		Assert.assertEquals(game.getCurrentPlayer(), Player.FIRST);
		game.onMoveSelected(2, 2);

		Assert.assertEquals(game.getCurrentPlayer(), Player.SECOND);
		game.onMoveSelected(3, 2);

		Assert.assertEquals(game.getCurrentPlayer(), Player.FIRST);
		game.onMoveSelected(2, 2);

		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.FIRST), 6);
		Assert.assertEquals(game.getField().getTotalNumberOfAtomsForPlayer(Player.SECOND), 1);
	}


}
