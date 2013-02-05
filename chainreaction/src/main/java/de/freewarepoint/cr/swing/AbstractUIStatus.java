package de.freewarepoint.cr.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.beans.Transient;

import javax.swing.JPanel;

import de.freewarepoint.cr.Game;
import de.freewarepoint.retrofont.RetroFont;


/**
 * @author jonny
 *
 */
public abstract class AbstractUIStatus extends JPanel implements Runnable {

	static final int FONT_SIZE = 16;
	private static final long serialVersionUID = 8437395162237408047L;
	Game game;
	Image statusImg;
	final RetroFont retroFont;
	
	AbstractUIStatus() {
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		retroFont = new RetroFont();
	}
	
	public void setGame(final Game game) {
		this.game = game; 
	}
	
	// render all objects on the screen.
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		final Graphics2D g2d = (Graphics2D) g;
		
		RenderingHints rh =
				new RenderingHints(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setRenderingHints(rh);
		
		// draw status
		if(statusImg != null) {
			double xRoot = (getSize().getWidth() - getPreferredSize().getWidth()) / 2;
			double yRoot = (getSize().getHeight() - getPreferredSize().getHeight()) / 2;
			if (xRoot < 0) {
				xRoot = 0;
			}
			if (yRoot < 0) {
				yRoot = 0;
			}
			g2d.translate(xRoot, yRoot);
			g2d.drawImage(statusImg, 0, 0, null);
		}
		
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	// This runnable should be used in the SwingEventQueue, only!
	@Override
	public abstract void run();
	
	@Override
	@Transient
	public Dimension getPreferredSize() {
		if(statusImg != null) {
			return new Dimension(statusImg.getWidth(null), statusImg.getHeight(null));
		}
		return new Dimension(0, FONT_SIZE);
	}
}
