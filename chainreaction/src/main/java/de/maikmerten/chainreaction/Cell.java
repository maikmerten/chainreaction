package de.maikmerten.chainreaction;

public class Cell {

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
	public void setNumberOfAtoms(byte numberOfAtoms) {
		if (numberOfAtoms > 4) {
			this.numberOfAtoms = 4;
		}
		else {
			this.numberOfAtoms = numberOfAtoms;
		}
	}

	public Player getOwningPlayer() {
		return owningPlayer;
	}

	public void setOwningPlayer(Player owningPlayer) {
		this.owningPlayer = owningPlayer;
	}

	public void clear() {
		numberOfAtoms = 0;
		owningPlayer = Player.NONE;
	}
}
