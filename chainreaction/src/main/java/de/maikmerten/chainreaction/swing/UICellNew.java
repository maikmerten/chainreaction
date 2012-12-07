package de.maikmerten.chainreaction.swing;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import static de.maikmerten.chainreaction.swing.UIFieldNew.CELL_SIZE;


/**
 * @author jonny
 *
 */
public class UICellNew implements UIDrawable {
	private AbstractUIMouse[] mice;
	private int count;
	
	private byte player;
	
	private final int x;
	private final int y;
	
	public UICellNew(int x, int y) {
		this.x = x;
		this.y = y;
		this.count = 0;
		this.player = 0;
		mice = new AbstractUIMouse[4];
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
		putAtomInternal(count++);
	}
	
	public void moveTo(final UICellNew otherCell) {
		// TODO do the move animation instead!
		mice[--count] = null;
		if(otherCell.isEmpty()) {
			otherCell.setOwner(player);
		}
		otherCell.addAdtom();
	}

	@Override
	public void draw(Graphics2D g2d) {
		final AffineTransform transform = g2d.getTransform();
		g2d.translate((double)x, (double)y);
		
		if(count > 0) {
			mice[0].draw(g2d);
		}
		g2d.translate((double)CELL_SIZE, 0d);
		if(count > 1) {
			mice[1].draw(g2d);
		}
		
		g2d.translate((double)-CELL_SIZE, (double)CELL_SIZE);
		if(count > 2) {
			mice[2].draw(g2d);
		}
		g2d.translate((double)CELL_SIZE, 0d);
		if(count > 3) {
			mice[3].draw(g2d);
		}
		g2d.setTransform(transform);
	}
	
	private void putAtomInternal(int cellIndex) {
		if(player == 1) {
			mice[cellIndex] = new UIGoodMouse();
		}
		else if(player == 2) {
			mice[cellIndex] = new UIEvilMouse();
		}
		else {
			throw new IllegalStateException("neither player 1 or 2, but: " + player);
		}
	}
}
