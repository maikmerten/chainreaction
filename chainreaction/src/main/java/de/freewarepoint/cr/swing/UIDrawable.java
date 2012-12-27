package de.freewarepoint.cr.swing;

import java.awt.Graphics2D;


/**
 * @author jonny
 */
public interface UIDrawable {
	/**
	 * Draws itself.
	 * 
	 * @param g2d
	 * 		the graphics object to draw itself on.
	 */
	public void draw(final Graphics2D g2d);
}
