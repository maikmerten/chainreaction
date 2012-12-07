package de.maikmerten.chainreaction;

/**
 *
 * @author maik
 * @author jonny
 */
public interface FieldListener {
	
	public void onFieldChange(Field f);
	
	public void onAtomAdded(byte player, int x, int y);
	
	public void onAtomMoved(int x1, int y1, int x2, int y2);
	
	public void onOwnerChanged(byte player, int x, int y);
}
