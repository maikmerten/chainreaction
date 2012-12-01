package de.maikmerten.chainreaction;

import java.util.ArrayList;

/**
 *
 * @author maik
 */
public class Field {

	private int width, height;
	private byte[][] atoms;
	private byte[][] owner;
	private ArrayList<FieldListener> listeners = new ArrayList<FieldListener>();

	public Field(int width, int height) {
		this.width = width;
		this.height = height;
		atoms = new byte[width][height];
		owner = new byte[width][height];
	}

	public Field getCopy() {
		Field copy = new Field(getWidth(), getHeight());
		for (int x = 0; x < getWidth(); ++x) {
			for (int y = 0; y < getHeight(); ++y) {
				copy.atoms[x][y] = this.atoms[x][y];
				copy.owner[x][y] = this.owner[x][y];
			}
		}
		return copy;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public byte getAtoms(int x, int y) {
		return atoms[x][y];
	}

	public byte getOwner(int x, int y) {
		return owner[x][y];
	}

	public byte[][] getAtomField() {
		byte[][] result = new byte[width][height];
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				result[x][y] = atoms[x][y];
			}
		}
		return result;
	}

	public byte[][] getOwnerField() {
		byte[][] result = new byte[width][height];
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				result[x][y] = owner[x][y];
			}
		}
		return result;
	}

	public int getPlayerAtoms(byte player) {
		int count = 0;
		for (int x = 0; x < getWidth(); ++x) {
			for (int y = 0; y < getHeight(); ++y) {
				if (getOwner(x, y) == player) {
					count += getAtoms(x, y);
				}
			}
		}

		return count;
	}
	
	public int getPlayerFields(byte player) {
		int count = 0;
		for (int x = 0; x < getWidth(); ++x) {
			for (int y = 0; y < getHeight(); ++y) {
				if (getOwner(x, y) == player) {
					++count;
				}
			}
		}

		return count;
	}
	

	public void putAtom(byte player, int x, int y) {
		byte cnt = getAtoms(x, y);
		++cnt;
		setAtoms(player, cnt, x, y);
	}

	public void setAtoms(byte player, byte count, int x, int y) {
		count = count > 4 ? 4 : count;
		atoms[x][y] = count;
		owner[x][y] = player;
		for(FieldListener l : listeners) {
			l.onFieldChange(this);
		}
	}

	public byte getCapacity(int x, int y) {
		byte cap = 3;
		if (x == 0 || x == (width - 1)) {
			--cap;
		}
		if (y == 0 || y == (height - 1)) {
			--cap;
		}
		return cap;
	}

	public boolean isCritical(int x, int y) {
		return getAtoms(x, y) == getCapacity(x, y);
	}

	private void spreadAtoms(int x, int y) {
		byte player = getOwner(x, y);
		if (x > 0) {
			putAtom(player, x - 1, y);
		}
		if (x < getWidth() - 1) {
			putAtom(player, x + 1, y);
		}
		if (y > 0) {
			putAtom(player, x, y - 1);
		}
		if (y < getHeight() - 1) {
			putAtom(player, x, y + 1);
		}
		setAtoms((byte) 0, (byte) 0, x, y);
	}

	public void react() {
		boolean stable = false;
		byte iter = 16;
		while (!stable && iter-- > 0) {
			stable = true;
			for (int x = 0; x < getWidth(); ++x) {
				for (int y = 0; y < getHeight(); ++y) {
					byte count = getAtoms(x, y);
					if (count > getCapacity(x, y)) {
						stable = false;
						spreadAtoms(x, y);
					}
				}
			}
		}
	}
	
	
	public void addFieldListener(FieldListener l) {
		this.listeners.add(l);
	}
	
}
