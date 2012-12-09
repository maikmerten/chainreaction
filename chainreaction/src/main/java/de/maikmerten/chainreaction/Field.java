package de.maikmerten.chainreaction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
            System.arraycopy(atoms[x], 0, result[x], 0, height);
		}
		return result;
	}

	public byte[][] getOwnerField() {
		byte[][] result = new byte[width][height];
		for (int x = 0; x < width; ++x) {
            System.arraycopy(owner[x], 0, result[x], 0, height);
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
		putAtomInternal(player, x, y);
		fireOnAtomAdded(player, x, y);
	}

	private void putAtomInternal(byte player, int x, int y) {
		byte cnt = getAtoms(x, y);
		++cnt;
		setAtoms(player, cnt, x, y);
	}

	public void setAtoms(byte player, byte count, int x, int y) {
		if(count > 4) {
			// TODO logging?
			System.err.println("Count greater than 4: " + count);
			count = 4;
		}
		atoms[x][y] = count;
		owner[x][y] = player;
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
		// clear cell
		setAtoms((byte) 0, (byte) 0, x, y);
		List<Move> moves = new LinkedList<Move>();
		// move left
		if (x > 0) {
			moveAtom(player, x, y, x - 1, y, moves);
		}
		// move right
		if (x < getWidth() - 1) {
			moveAtom(player, x, y, x + 1, y, moves);
		}
		// move up
		if (y > 0) {
			moveAtom(player, x, y, x, y - 1, moves);
		}
		// move down
		if (y < getHeight() - 1) {
			moveAtom(player, x, y, x, y + 1, moves);
		}
		fireOnAtomsMoved(moves);
	}

	private void moveAtom(byte player, int x1, int y1, int x2, int y2, List<Move> moves) {
		if(getOwner(x2, y2) != player) {
			fireOnOwnerChange(player, x2, y2);
		}
		putAtomInternal(player, x2, y2);
		moves.add(new Move(x1, y1, x2, y2));
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
	
	private void fireOnAtomAdded(byte player, int x, int y) {
		for(FieldListener l : listeners) {
			l.onAtomAdded(player, x, y);
		}
	}
	
	private void fireOnAtomsMoved(List<Move> moves) {
		for(FieldListener l : listeners) {
			l.onAtomsMoved(moves);
		}
	}
	
	private void fireOnOwnerChange(byte player, int x, int y) {
		for(FieldListener l : listeners) {
			l.onOwnerChanged(player, x, y);
		}
	}
}
