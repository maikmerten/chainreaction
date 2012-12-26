package de.maikmerten.chainreaction.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.beans.Transient;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import de.maikmerten.chainreaction.Game;
import de.maikmerten.chainreaction.Player;
import de.maikmerten.chainreaction.retrofont.RetroFont;


/**
 * @author jonny
 *
 */
public class UIStatus extends JPanel implements Runnable {

	private static final int FONT_SIZE = 14;
	private static final long serialVersionUID = 8437395162237408047L;
	private Game game;
	private Image statusImg;
	private RetroFont retroFont;
	
	public UIStatus() {
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
			g2d.drawImage(statusImg, 0, 0, null);
		}
		
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	// This runnable should be used in the SwingEventQueue, only!
	@Override
	public void run() {
		if(game != null) {
			final StringBuilder sb = new StringBuilder();
	
			if (game.getWinner() != Player.NONE) {
				sb.append("Player ").append(game.getWinner()).append(" won in round ").append(game.getRound());
			}
			else {
				sb.append("Round ").append(game.getRound()).append(" | Active player: ").append(game.getCurrentPlayer());
				sb.append(" | Current Score: ").append(game.getField().getTotalNumberOfAtomsForPlayer(Player.FIRST));
				sb.append(":").append(game.getField().getTotalNumberOfAtomsForPlayer(Player.SECOND));
			}
			
			final Color foreground;
			switch(game.getCurrentPlayer()) {
				case FIRST:
				case SECOND:
					foreground = UIPlayer.getPlayer(game.getCurrentPlayer()).getForeground();
					break;
				case NONE:
					foreground = Color.WHITE;
					break;
				default:
					throw new IllegalStateException("Should never happen");
			}
			
			statusImg = retroFont.getRetroString(sb.toString(), foreground, FONT_SIZE);
			UIStatus.this.repaint();
		}
	}

	@Override
	@Transient
	public Dimension getPreferredSize() {
		if(statusImg != null) {
			return new Dimension(statusImg.getWidth(null), statusImg.getHeight(null));
		}
		return new Dimension(0, FONT_SIZE);
	}
}
