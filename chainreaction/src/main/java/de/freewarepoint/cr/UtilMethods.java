package de.freewarepoint.cr;

/**
 * This class serves as adapter for jABC strategies as well as util class for methods that interact with the game or
 * need access to them.
 * 
 * @author kuehn
 */
public class UtilMethods {
	
	/**
	 * Iterates over every {@link Cell} of a {@link Field} and adds up the amount of cells that are critical.
	 * 
	 * @see #isCriticalCell(Field, int, int)
	 * 
	 * @param field The field containing the cells that will be checked. 
	 * @return A value determining the amount of critical cells.
	 */
	public static int countAllCriticalFields(Field field) {
		int result = 0;
		for(int x = 0; x < field.getWidth(); ++x) {
			for(int y = 0; y < field.getHeight(); ++y) {
				if(isCriticalCell(field, x, y)) {
					++result;
				}
			}
		}
		return result;
	}
	
	/**
	 * Iterates over every {@link Cell} of a {@link Field} that are owned by a given player and adds up the amount of cells that are critical.
	 * 
	 * @see #isCriticalCell(Field, int, int)
	 * 
	 * @param field The field containing the cells that will be checked. 
	 * @param player The player for whom the cells will be counted.
	 * @return A value determining the amount of critical cells owned by the player.
	 */
	public static int countCriticalFieldsForPlayer(Field field, Player player) {
		int result = 0;
		for(int x = 0; x < field.getWidth(); ++x) {
			for(int y = 0; y < field.getHeight(); ++y) {
				if(belongsToPlayer(field, x, y, player) && isCriticalCell(field, x, y)) {
					++result;
				}
			}
		}
		return result;
	}

	/**
	 * Iterates over every {@link Cell} of a {@link Field} that is owned by a given player and adds up the amount of 
	 * cells that are endangered.
	 * 
	 * @see #isEndangered(Field, int, int, Player)
	 * 
	 * @param field The field containing the cells that will be checked. 
	 * @param player The current owner for whom the cells will be checked.
	 * @return A value determining the amount of cells for the player, that are endangered.
	 */
	public static int countEndangeredFields(Field field, Player player) {
		int endangered = 0;
		for(int x = 0; x < field.getWidth(); ++x) {
			for(int y = 0; y < field.getHeight(); ++y) {
				if(isEndangered(field, x, y, player)) {
					++endangered;
				}
			}
		}
		return endangered;
	}
	
	/**
	 * Checks if a cell belongs to a given player and has at least one neighbour cell that is a critical cell of the opposing player.
	 * 
	 * @see #isCriticalEnemyCell(Field, int, int, Player)
	 * 
	 * @param field The {@link Field} containing the {@link Cell} for which the condition will be checked. 
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 * @param player The owner of the cell.
	 * @return <code>true</code> if the cell belongs to the parameterized player and has a critical neighbour cell owned by the opposing player, <code>false</code> otherwise.
	 */
	public static boolean isEndangered(Field field, int x, int y, Player player) {
		return belongsToPlayer(field, x, y, player) && (computeDangerForCell(field, x, y, player) > 0);
	}
	
	/**
	 * Evaluates if a cell is critical and not owned by a given player.
	 * 
	 * @param field The {@link Field} containing the {@link Cell} for which the condition will be checked. 
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 * @param player The owner of the cell.
	 * @return <code>true</code> if the cell is critical and not owned by the parameterized player, <code>false</code> otherwise.
	 */
	public static boolean isCriticalEnemyCell(Field field, int x, int y, Player player) {
		return (!(belongsToPlayer(field, x, y, player)) && isCriticalCell(field, x, y));
	}
	
	/**
	 * Counts the amount of neighbour cells that are critical but not owned by the parameterized player.
	 * 
	 * @param field The {@link Field} containing the {@link Cell} for which the value will be computed. 
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 * @param player The owner of the cell.
	 * @return A value between <code>0</code> and the amount of neighbours of the cell, representing the amount of critical neighbour cells owned by the opposing player.
	 */
	public static int computeDangerForCell(Field field, int x, int y, Player player) {
		int danger = 0;
		if (x > 0 && isCriticalEnemyCell(field, x-1, y, player)) {
			++danger;
		}
		if( x < (field.getWidth()) - 1 && isCriticalEnemyCell(field, x+1, y, player)) {
			++danger;
		}
		if (y > 0 && isCriticalEnemyCell(field, x, y-1, player)) {
			++danger;
		}
		if( y < (field.getHeight() - 1) && isCriticalEnemyCell(field, x, y+1, player)) {
			++danger;
		}
		return danger;
	}
	
	/**
	 * Checks if a cell belongs to a given player.
	 * 
	 * @param field The {@link Field} containing the {@link Cell} for which the condition will be checked. 
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 * @param player The owner of the cell.
	 * @return <code>true</code> if the cell belongs to the parameterized player, <code>false</code> otherwise.
	 */
	public static boolean belongsToPlayer(Field field, int x, int y, Player player) {
		return field.getOwnerOfCellAtPosition(x, y) == player;
	}
	
	/**
	 * Checks if a given player can place an atom on a given cell of a given field. This is possible if it belongs to
	 * the player or to nobody.
	 * 
	 * @param field The {@link Field} containing the {@link Cell} for which the condition will be checked.
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 * @param player The player to check the condition for.
	 * @return <code>true</code> if the placement of an atom is possible on the given field, <code>false</code> otherwise.
	 */
	public static boolean isPlacementPossible(Field field, int x, int y, Player player) {
		return belongsToPlayer(field, x, y, player) || field.getOwnerOfCellAtPosition(x, y) == Player.NONE;
	}
	
	/**
	 * Checks if a cell has exactly one atom less than it takes to make it react.
	 * 
	 * @param field The {@link Field} containing the {@link Cell} for which the condition will be checked. 
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 * @return <code>true</code> if the cell is critical, <code>false</code> otherwise.
	 */
	public static boolean isCriticalCell(Field field, int x, int y) {
		return field.isCritical(x, y);
	}

	/**
	 * Retrieves the amount of atoms on cells of a field that are owned by a given player.
	 * 
	 * @param field The {@link Field} for which the atoms will be counted.
	 * @param player The player whose atoms will be counted.
	 * @return The amount of atoms on the field owned by the player.
	 */
	public static int countOwnedAtoms(Field field, Player player) {
		return field.getTotalNumberOfAtomsForPlayer(player);
	}
	
	/**
	 * Creates a copy of a given {@link Field}.
	 * 
	 * @param field The field to be copied.
	 * @return A copy of the field.
	 */
	public static Field getCopyOfField(Field field) {
		return field.getCopy();
	}

	/**
	 * TODO: Javadoc - reacts after placement?
	 * 
	 * @param field
	 * @param x
	 * @param y
	 * @param player
	 */
	public static void placeAtom(Field field, int x, int y, Player player) {
		field.putAtom(player, x, y);
	}
	
	/**
	 * Causes a given {@link Field} to react, triggering every cell that reached its capacity to spread its atoms to
	 * neighbour fields.
	 * 
	 * @param field The field for which the reaction will be triggered.
	 */
	public static void reactField(Field field) {
		field.react();
	}
	
	/**
	 * Counts the amount of cells on a given {@link Field} that are owned by a given player.
	 * 
	 * @param field The field that contains the cells.
	 * @param player The owner for whom the cells will be counted.
	 * @return The amount of cells on the field that are owned by the player.
	 */
	public static int countPlayerCells(Field field, Player player) {
		return field.getPlayerFields(player);
	}
	
	/**
	 * Checks if the cell is located on an edge or in a corner of a given field.
	 * 
	 * @param field The {@link Field} containing the {@link Cell} for which the condition will be checked. 
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 * @return <code>true</code> if the cell is located on an edge or in a corner of the field, <code>false</code> otherwise.
	 */
	public static boolean isEdgeOrCornerCell(Field field, int x, int y) {
		return ((x == 0 || x == field.getWidth() - 1) || (y == 0 || y == field.getHeight() - 1));
	}
	
	/**
	 * Checks if the cell is located on an edge but not in a corner of a given field.
	 * 
	 * @param field The {@link Field} containing the {@link Cell} for which the condition will be checked. 
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 * @return <code>true</code> if the cell is located on an edge but not in a corner of the field, <code>false</code> otherwise.
	 */
	public static boolean isEdgeCell(Field field, int x, int y) {
		return isEdgeOrCornerCell(field, x, y) && !isCornerCell(field, x, y);
	}
	
	/**
	 * Checks if the cell is located in a corner of a given field.
	 * 
	 * @param field The {@link Field} containing the {@link Cell} for which the condition will be checked. 
	 * @param x The x-coordinate of the cell.
	 * @param y The y-coordinate of the cell.
	 * @return <code>true</code> if the cell is located on an edge or in the corner of the field, <code>false</code> otherwise.
	 */
	public static boolean isCornerCell(Field field, int x, int y) {
		return ((x == 0 || x == field.getWidth() - 1) && (y == 0 || y == field.getHeight() - 1));
	}
}
