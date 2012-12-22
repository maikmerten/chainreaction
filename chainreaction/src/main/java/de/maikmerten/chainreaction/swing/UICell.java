package de.maikmerten.chainreaction.swing;

import de.maikmerten.chainreaction.Player;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static de.maikmerten.chainreaction.swing.UIField.CELL_SIZE;

public class UICell implements UIDrawable {
	private static final int ATOMS_PER_CELL = 4;

	private final UIAtom[] atoms;
	private final List<List<UIAtom>> leavingAtoms;
	private UICellBG background = null;
	private int count;
	
	private Player player;
	
	private final int x;
	private final int y;
	
	public UICell(int x, int y) {
		this.x = x;
		this.y = y;
		this.count = 0;
		this.player = Player.NONE;
		atoms = new UIAtom[ATOMS_PER_CELL];
		leavingAtoms = new ArrayList<List<UIAtom>>(ATOMS_PER_CELL);
		for(int i = 0; i < ATOMS_PER_CELL; i++) {
			leavingAtoms.add(new LinkedList<UIAtom>());
		}
	}
	
	public boolean isEmpty() {
		return count == 0;
	}
	
	public void setOwner(Player player) {
		if(this.player == player) {
			return;
		}
		this.player = player;
		if(player != Player.NONE) {
			this.background = new UICellBG(player);
		}
		else {
			this.background = null;
		}
		// TODO do assimilation animation.
		for(int i = 0; i < count; i++) {
			putAtomInternal(i);
		}
	}
	
	public void clear() {
		while(count > 0 ) {
			removeAtom();
		}
	}
	
	public void addAdtom() {
		putAtomInternal(count++);
	}
	
	private void removeAtom() {
		leavingAtoms.get(--count).add(atoms[count].leave());
		atoms[count] = null;
	}
	
	public void moveTo(final UICell otherCell) {
		removeAtom();
		otherCell.addAdtom();
	}

	// draw the atoms on the cell.
	@Override
	public void draw(Graphics2D g2d) {
		final AffineTransform transform = g2d.getTransform();
		g2d.translate((double)x, (double)y);
		
		if(background != null) {
			background.draw(g2d);
		}
		
		if(count > 0) {
			atoms[0].draw(g2d);
		}
		drawLeavingAtoms(g2d, 0);
		g2d.translate((double)CELL_SIZE, 0d);
		if(count > 1) {
			atoms[1].draw(g2d);
		}
		drawLeavingAtoms(g2d, 1);
		g2d.translate((double)-CELL_SIZE, (double)CELL_SIZE);
		if(count > 2) {
			atoms[2].draw(g2d);
		}
		drawLeavingAtoms(g2d, 2);
		g2d.translate((double)CELL_SIZE, 0d);
		if(count > 3) {
			atoms[3].draw(g2d);
		}
		drawLeavingAtoms(g2d, 3);
		g2d.setTransform(transform);
	}

	private void drawLeavingAtoms(Graphics2D g2d, int cell) {
		final Iterator<UIAtom> iter = leavingAtoms.get(cell).iterator();
		while(iter.hasNext()) {
			final UIAtom leavingAtom = iter.next();
			if(leavingAtom.isFinished()) {
				iter.remove();
			}
			else {				
				leavingAtom.draw(g2d);
			}
		}
	}

	private void putAtomInternal(int cellIndex) {
		atoms[cellIndex] = UIPlayer.getPlayer(player).createAtom();
	}
}
