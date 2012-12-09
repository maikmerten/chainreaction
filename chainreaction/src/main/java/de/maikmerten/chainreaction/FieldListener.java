package de.maikmerten.chainreaction;

import java.util.List;

/**
 * A listener being informed of changes to the game field like adding an atom, moving an atom and 
 * changing the owner of a cell.
 *
 * @author maik
 * @author jonny
 */
public interface FieldListener {
	
	/**
	 * An atom has been added.
	 * 
	 * @param player
	 * 		the player who added the atom.
	 * @param x
	 * 		the horizontal coordinate of the cell the atom has been placed.
	 * @param y
	 * 		the vertical coordinate of the cell the atom has been placed.
	 */
	public void onAtomAdded(byte player, int x, int y);
	
	/**
	 * A list of atom moves occured.
	 * 
	 * @param moves
	 * 		list of atom moves.
	 */
	public void onAtomsMoved(List<Move> moves);
	
	/**
	 * The owner of a field has changed.
	 * 
	 * @param player
	 * 		the new player.
	 * @param x
	 * 		the horizontal coordinate of the cell.
	 * @param y
	 * 		the vertical coordinate of the cell.
	 */
	public void onOwnerChanged(byte player, int x, int y);
}
