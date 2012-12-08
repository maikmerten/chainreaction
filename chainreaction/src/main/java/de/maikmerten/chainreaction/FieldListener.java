package de.maikmerten.chainreaction;

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
	 * An atom has been moved.
	 * 
	 * @param x1
	 * 		old horizontal coordinate of the cell.
	 * @param y1
	 * 		old vertical coordinate of the cell.
	 * @param x2
	 * 		new horizontal coordinate of the cell.
	 * @param y2
	 * 		new vertical coordinate of the cell.
	 */
	public void onAtomMoved(int x1, int y1, int x2, int y2);
	
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
