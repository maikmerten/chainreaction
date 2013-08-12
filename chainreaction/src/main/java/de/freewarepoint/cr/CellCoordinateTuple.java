package de.freewarepoint.cr;


public class CellCoordinateTuple {
	public final int x;
	public final int y;
	
	public CellCoordinateTuple(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object other) {
		if (other == null || !(other instanceof CellCoordinateTuple)) {
			return false;
		} else {
			return ((CellCoordinateTuple) other).x == this.x && ((CellCoordinateTuple) other).y == this.y;
		}
	}
	
	public int hashCode() {
		int i = 17;
		i += x*19;
		i += y*31;
		return i;
	}
}