package de.maikmerten.chainreaction;

import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author maik
 */
public class Game implements MoveListener {
	
	private Field field;
	private Player player = Player.FIRST;
	private Set<Player> moved = EnumSet.noneOf(Player.class);
	private int round = 1;

	public Game(int width, int height) {
		field = new Field(width, height);
	}
	
	public Field getField() {
		return field;
	}

	public Player getCurrentPlayer() {
		return player;
	}
	
	public int getRound() {
		return round;
	}
	
	public void onMoveSelected(int x, int y) {
		
		// no further moves if we have a winner
		if(getWinner() != Player.NONE) {
			return;
		}
		
		Player owner = field.getOwnerOfCellAtPosition(x, y);
	
		// only allow moves into fields that are either unowned
		// or belong to the current player
		if(owner != Player.NONE && owner != player) {
			return;
		}
		
		field.putAtom(player, x, y);
		field.react();
		
		moved.add(player);
		++round;
		// next player
		player = player == Player.FIRST ? Player.SECOND : Player.FIRST;
	}
	
	public Player getWinner() {
		Player winner = Player.NONE;
		if(moved.contains(Player.FIRST) && field.getPlayerAtoms(Player.FIRST) == 0) {
			winner = Player.SECOND;
		} else if (moved.contains(Player.SECOND) && field.getPlayerAtoms(Player.SECOND) == 0) {
			winner = Player.FIRST;
		}
		return winner;
	}

}
