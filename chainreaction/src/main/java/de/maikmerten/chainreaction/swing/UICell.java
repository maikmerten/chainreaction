package de.maikmerten.chainreaction.swing;

import static de.maikmerten.chainreaction.swing.UIField.CELL_SIZE;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;



/**
 * @author jonny
 *
 */
public class UICell implements UIDrawable {
	private static final int ATOMS_PER_CELL = 4;
	private UIAtom[] atoms;
	private int count;
	
	private byte player;
	
	private final int x;
	private final int y;
	
	public UICell(int x, int y) {
		this.x = x;
		this.y = y;
		this.count = 0;
		this.player = 0;
		atoms = new UIAtom[ATOMS_PER_CELL];
	}
	
	public boolean isEmpty() {
		return count == 0;
	}
	
	public void setOwner(byte player) {
		if(this.player == player) {
			return;
		}
		this.player = player;
		// TODO do assimilation animation.
		for(int i = 0; i < count; i++) {
			putAtomInternal(i);
		}
	}
	
	public void addAdtom() {
		if(count < ATOMS_PER_CELL) {
			putAtomInternal(count++);
		}
	}
	
	public void moveTo(final UICell otherCell) {
		// TODO do the move animation instead (another leaving array...)!
		atoms[--count] = null;
		if(otherCell.isEmpty()) {
			otherCell.setOwner(player);
		}
		otherCell.addAdtom();
	}

	// draw the atoms on the cell.
	@Override
	public void draw(Graphics2D g2d) {
		final AffineTransform transform = g2d.getTransform();
		g2d.translate((double)x, (double)y);
		
		if(count > 0) {
			atoms[0].draw(g2d);
		}
		g2d.translate((double)CELL_SIZE, 0d);
		if(count > 1) {
			atoms[1].draw(g2d);
		}
		
		g2d.translate((double)-CELL_SIZE, (double)CELL_SIZE);
		if(count > 2) {
			atoms[2].draw(g2d);
		}
		g2d.translate((double)CELL_SIZE, 0d);
		if(count > 3) {
			atoms[3].draw(g2d);
		}
		g2d.setTransform(transform);
	}
	
	private void putAtomInternal(int cellIndex) {
		atoms[cellIndex] = UIPlayer.getPlayer(player).createAtom();
	}
}
