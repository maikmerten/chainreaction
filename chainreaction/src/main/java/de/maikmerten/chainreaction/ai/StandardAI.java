package de.maikmerten.chainreaction.ai;

import de.maikmerten.chainreaction.Field;
import de.maikmerten.chainreaction.Game;
import de.maikmerten.chainreaction.Player;

import java.util.Random;

/**
 *
 * @author maik
 */
public class StandardAI implements AI {

	private final Game game;
	
	public StandardAI(Game game) {
		this.game = game;
	}
	
	private int countCritical(Field f, Player player) {
		int result = 0;
		for(int x = 0; x < f.getWidth(); ++x) {
			for(int y = 0; y < f.getHeight(); ++y) {
				Player owner = f.getOwnerOfCellAtPosition(x, y);
				if(owner == player && f.isCritical(x, y)) {
					++result;
				}
			}
		}
		
		return result;
	}
	
	
	private int computeDanger(Field f, Player player, int x, int y) {
		int danger = 0;
		
		if (x > 0 && f.getOwnerOfCellAtPosition(x - 1, y) != player && f.isCritical(x - 1, y)) {
			++danger;
		}
		if( x < (f.getWidth()) - 1 && f.getOwnerOfCellAtPosition(x + 1, y) != player && f.isCritical(x + 1, y)) {
			++danger;
		}

		if (y > 0 && f.getOwnerOfCellAtPosition(x, y - 1) != player && f.isCritical(x, y - 1)) {
			++danger;
		}
		if( y < (f.getHeight() - 1) && f.getOwnerOfCellAtPosition(x, y + 1) != player && f.isCritical(x, y + 1)) {
			++danger;
		}

		return danger;
	}
	
	
	private int countEndangeredFields(Field f, Player player) {
		int endangered = 0;
		
		for(int x = 0; x < f.getWidth(); ++x) {
			for(int y = 0; y < f.getHeight(); ++y) {
				Player owner = f.getOwnerOfCellAtPosition(x, y);
				if(owner == player && computeDanger(f, player, x, y) > 0) {
					++endangered;
				}
			}
		}
		
		return endangered;
	}
	
	
	private int[] think(Field f, Player playerAI, Player playerOpposing) {
		Random r = new Random();
		int opposingAtoms = f.getTotalNumberOfAtomsForPlayer(playerOpposing);
		int score = Integer.MIN_VALUE;
		int[] coords = new int[2];
		for(int x = 0; x < f.getWidth(); ++x) {
			for(int y = 0; y < f.getHeight(); ++y) {
				Player owner = f.getOwnerOfCellAtPosition(x, y);
				if(owner == Player.NONE || owner == playerAI) {
					Field fieldAI = f.getCopy();
					fieldAI.putAtom(playerAI, x, y);
					fieldAI.react();
					int tmp = fieldAI.getPlayerFields(playerAI);
					tmp += fieldAI.getTotalNumberOfAtomsForPlayer(playerAI);
					tmp += opposingAtoms - fieldAI.getTotalNumberOfAtomsForPlayer(playerOpposing);
					tmp += ((x == 0 || x == fieldAI.getWidth() - 1) && (y == 0 || y == fieldAI.getHeight() - 1)) ? 1 : 0;
					tmp += countCritical(fieldAI, playerAI) * 2;
					tmp -= computeDanger(fieldAI, playerAI, x, y) * 4;
					tmp -= countEndangeredFields(fieldAI, playerAI);
					if(tmp > score || (r.nextBoolean() && tmp >= score)) {
						score = tmp;
						coords[0] = x;
						coords[1] = y;
					}
				}
			}
		}
		
		return coords;
	}

	public void doMove() {
		Field field = game.getField();
		Player playerAI = game.getCurrentPlayer();
		Player playerOpposing = playerAI == Player.SECOND ? Player.FIRST : Player.SECOND;
		
		int[] coords = think(field, playerAI, playerOpposing);
		game.onMoveSelected(coords[0], coords[1]);
	}

	@Override
	public String getName() {
		return "Standard AI";
	}


}
