package de.maikmerten.chainreaction;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author maik
 */
public class Game implements MoveListener {
	
	private Field field;
	private byte player = 1;
	private Set<Byte> moved = new HashSet<Byte>();
	private int round = 1;

	public Game(int width, int height) {
		field = new Field(width, height);
	}
	
	public Field getField() {
		return field;
	}

	public byte getCurrentPlayer() {
		return player;
	}
	
	public int getRound() {
		return round;
	}
	
	public void onMoveSelected(int x, int y) {
		
		// no further moves if we have a winner
		if(getWinner() != 0) {
			return;
		}
		
		byte owner = field.getOwner(x, y);
	
		// only allow moves into fields that are either unowend
		// or belong to the current player
		if(owner != 0 && owner != player) {
			return;
		}
		
		field.putAtom(player, x, y);
		field.react();
		
		moved.add(player);
		++round;
		// next player
		player = player == 1 ? (byte) 2 : (byte) 1;
	}
	
	public byte getWinner() {
		byte winner = 0;
		if(moved.contains((byte)1) && field.getPlayerAtoms((byte)1) == 0) {
			winner = 2;
		} else if (moved.contains((byte)2) && field.getPlayerAtoms((byte)2) == 0) {
			winner = 1;
		}
		return winner;
	}

}
