package de.freewarepoint.cr;

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
	public void onAtomAdded(Player player, int x, int y);
	
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
	 * 		the vertical coordinate of the cell that has been cleared.
	 */
	public void onOwnerChanged(Player player, int x, int y);
	
	/**
	 * A cell has been cleared.
	 * 
	 * @param x
	 * 		the horizontal coordinate of the cell that has been cleared.
	 * @param y
	 * 		the vertical coordinate of the cell that has been cleared.
	 */
	public void onCellCleared(int x, int y);
}
