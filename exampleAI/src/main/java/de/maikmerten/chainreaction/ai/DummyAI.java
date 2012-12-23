package de.maikmerten.chainreaction.ai;

import de.maikmerten.chainreaction.Field;
import de.maikmerten.chainreaction.Game;

import java.util.Random;

public class DummyAI implements AI {

	private Game game;
	private Random random;

	@Override
	public void doMove() {
		Random random = new Random();
		findNextCoordinates();
	}

	private int[] findNextCoordinates() {
		Field field = game.getField();

		int counter = 1000;

		while (counter > 0) {
			int randomX = random.nextInt(field.getWidth()+1);
			int randomY = random.nextInt(field.getHeight()+1);

			if (field.getOwnerOfCellAtPosition(randomX, randomY) != game.getCurrentPlayer()) {
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
