package de.freewarepoint.cr;


public class EvalField {

	private final int width, height;
	private final int[][] fieldEvalues;

	public EvalField(int width, int height) {
		this.width = width;
		this.height = height;
		fieldEvalues = new int[width][height];
	}

	public EvalField getCopy() {
		EvalField copy = new EvalField(getWidth(), getHeight());
		for (int x = 0; x < getWidth(); ++x) {
			for (int y = 0; y < getHeight(); ++y) {
				int originalValue = getValueAt(x, y);
				copy.setValueAt(x, y, originalValue);
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
	
	public int getValueAt(int x, int y) {
		return fieldEvalues[x][y];
	}
	
	public int getValueAt(CellCoordinateTuple cellCoordinate) {
		return fieldEvalues[cellCoordinate.x][cellCoordinate.y];
	}
	
	public void setValueAt(int x, int y, int value) {
		fieldEvalues[x][y] = value;
	}
}
