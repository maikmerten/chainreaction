package de.freewarepoint.cr;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class Game {
	
	private final Field field;
	private final Settings settings;
	private final List<MoveListener> listeners = new ArrayList<MoveListener>();
	private Player player = Player.FIRST;
	private final Set<Player> moved = EnumSet.noneOf(Player.class);
	private int round = 1;

	public Game(int width, int height, Settings settings) {
		this.field = new Field(width, height);
		this.settings = settings;
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
	
	public void selectMove(int x, int y) {
		
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
		
		fireOnMove(player, x, y);
		
		field.react();
		
		moved.add(player);
		++round;
		// next player
		player = player == Player.FIRST ? Player.SECOND : Player.FIRST;
	}
	
	public Player getWinner() {
		Player winner;

		if (moved.contains(Player.FIRST) && field.getTotalNumberOfAtomsForPlayer(Player.FIRST) == 0) {
			winner = Player.SECOND;
		} else if (moved.contains(Player.SECOND) && field.getTotalNumberOfAtomsForPlayer(Player.SECOND) == 0) {
			winner = Player.FIRST;
		} else {
			winner = Player.NONE;
		}

		return winner;
	}

	public Settings getSettings() {
		return settings;
	}
	
	public void addMoveListener(final MoveListener l) {
		this.listeners.add(l);
	}
	
	private void fireOnMove(Player p, int x, int y) {
		for(final MoveListener l : listeners) {
			l.onMove(p, x, y);
		}
	}
}
