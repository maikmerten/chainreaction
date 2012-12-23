package de.maikmerten.chainreaction.ai;

import de.maikmerten.chainreaction.Field;
import de.maikmerten.chainreaction.Game;
import de.maikmerten.chainreaction.Player;

import java.util.Random;

public class DummyAI implements AI {

	private Game game;
	private Random random = new Random();

	@Override
	public void doMove() {
		Random random = new Random();
		int[] coords = findNextCoordinates();
		game.onMoveSelected(coords[0], coords[1]);
	}

	private int[] findNextCoordinates() {
		Field field = game.getField();

		int counter = 1000;

		while (counter > 0) {
			int randomX = random.nextInt(field.getWidth());
			int randomY = random.nextInt(field.getHeight());
			Player currentOwner = field.getOwnerOfCellAtPosition(randomX, randomY);

			if (currentOwner == game.getCurrentPlayer() || currentOwner == Player.NONE) {
				int[] coords = new int[2];
				coords[0] = randomX;
				coords[1] = randomY;
				return coords;
			}

			counter--;
		}

		throw new IllegalStateException("Unable to make a move!");
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
