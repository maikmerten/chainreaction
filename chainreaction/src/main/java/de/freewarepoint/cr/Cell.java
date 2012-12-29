package de.freewarepoint.cr;

class Cell {

	private byte numberOfAtoms;
	private Player owningPlayer;

	public Cell() {
		numberOfAtoms = 0;
		owningPlayer = Player.NONE;
	}

	public Cell(byte numberOfAtoms, Player owningPlayer) {
		if (owningPlayer == null) {
			throw new IllegalArgumentException("owningPlayer must not be null");
		}

		this.numberOfAtoms = numberOfAtoms;
		this.owningPlayer = owningPlayer;
	}

	public byte getNumberOfAtoms() {
		return numberOfAtoms;
	}

	/**
	 * Sets the number of atoms for this cell to the given count. If the count
	 * is larger than 4, it will be set to 4.
	 *
	 * @param numberOfAtoms The new number of atoms for this cell
	 */
	private boolean setNumberOfAtoms(byte numberOfAtoms) {
		if (numberOfAtoms > 4) {
			this.numberOfAtoms = 4;
			return false;
		}
		else {
			this.numberOfAtoms = numberOfAtoms;
			return true;
		}
	}

	/**
	 * Increases the number of atoms for this cell by one, if it is lower
	 * than the maximum capacity.
	 */
	public boolean increaseNumberOfAtoms() {
		return setNumberOfAtoms((byte)(getNumberOfAtoms() + 1));
	}

	/**
	 * @return the {@link Player} who owns this cell.
	 * May be {@link Player#NONE} but never {@code null}.
	 */
	public Player getOwningPlayer() {
		return owningPlayer;
	}

	/**
	 * Sets the owning player for this cell.
	 *
	 * @param owningPlayer
	 *  The {@link Player}, must not but {@code null}.
	 */
	public void setOwningPlayer(Player owningPlayer) {
		if (owningPlayer == null) {
			throw new IllegalArgumentException("owningPlayer must not be null!");
		}

		this.owningPlayer = owningPlayer;
	}

	/**
	 * Clears all atoms.
	 */
	public void clearAtoms() {
		numberOfAtoms = 0;
	}
}
