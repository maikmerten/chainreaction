package de.freewarepoint.cr.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.Transient;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.freewarepoint.cr.Player;
import de.freewarepoint.cr.PlayerStatus;


/**
 * @author jonny
 *
 */
public class UIPlayerStatus extends AbstractUIStatus implements Runnable {

	private static final long serialVersionUID = 8437395162237408047L;
	private final BufferedImage human, computer;
	private final static int MAX_WIDTH = 192;
	
	public UIPlayerStatus() {
		super();
		computer = loadImg("/computer.png");
		human = loadImg("/human.png");
		setMinimumSize(new Dimension(MAX_WIDTH, FONT_SIZE));
	}

	private BufferedImage loadImg(String fn) {
		try {
			final BufferedImage img = ImageIO.read(this.getClass().getResourceAsStream(fn));
			final BufferedImage transpImg =  new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
			transpImg.getGraphics().drawImage(img, 0, 0, null);
			
			for(int x = 0; x < transpImg.getWidth(); ++x) {
				for(int y = 0; y < transpImg.getHeight(); ++y) {
					// punch all black pixels transparent
					if(transpImg.getRGB(x, y) == (0xFF << 24)) {
						transpImg.setRGB(x, y, 0);
					}
				}
			}
			return transpImg;
		}
		catch (IOException e) {
			throw new RuntimeException("cannot read '" + fn + "' image", e);
		}
	}

	// This runnable should be used in the SwingEventQueue, only!
	@Override
	public void run() {
		if(game != null) {
			final Color color;
			final PlayerStatus status;
			if(game.getWinner() != Player.NONE) {
				color = UIPlayer.getPlayer(game.getWinner()).getForeground();
				status = game.getPlayerStatus(game.getWinner());
			}
			else {
				color = UIPlayer.getPlayer(game.getCurrentPlayer()).getForeground();
				status = game.getPlayerStatus(game.getCurrentPlayer());
			}
			final BufferedImage base;
			if(status.isAIPlayer()) {
				base = computer;
			}
			else {
				base = human;
			}

			final BufferedImage coloredImg = new BufferedImage(base.getWidth(), base.getHeight(), base.getType());
			base.copyData(coloredImg.getRaster());
			for(int x = 0; x < coloredImg.getWidth(); ++x) {
				for(int y = 0; y < coloredImg.getHeight(); ++y) {
					// colorize white pixels
					if(coloredImg.getRGB(x, y) == 0xFFFFFFFF) {
						coloredImg.setRGB(x, y, color.getRGB());
					}
				}
			}
			
			final Image playerImg = coloredImg.getScaledInstance(coloredImg.getWidth()*7, coloredImg.getHeight()*7, Image.SCALE_REPLICATE);
			// TODO support player names for human players
			final String name = status.isAIPlayer() ? status.getAI().getName() : "Human";
			final String text;
			if((name.length()-1)*FONT_SIZE > MAX_WIDTH) {
				text = name.substring(0, (MAX_WIDTH/FONT_SIZE)-3) + "...";
			}
			else {
				 text = name;
			}
			final BufferedImage textImg	= retroFont.getRetroString(text, color, FONT_SIZE); 
			
			final BufferedImage targetImg = new BufferedImage(
					Math.max(textImg.getWidth(), playerImg.getWidth(null)), 
					playerImg.getHeight(null)+(FONT_SIZE+2), coloredImg.getType());
			final Graphics2D graphics = targetImg.createGraphics();
			
			graphics.drawImage(playerImg, 
					0, 
					0, null);
			
			final int fontX = playerImg.getWidth(null) > textImg.getWidth() ? ((playerImg.getWidth(null) - textImg.getWidth()) / 2) : 0;
			graphics.drawImage(textImg, 
					fontX, 
					playerImg.getHeight(null) + 2, null);
			
			statusImg = targetImg;
			
			UIPlayerStatus.this.revalidate();
			UIPlayerStatus.this.repaint();
		}
	}

	@Override
	@Transient
	public Dimension getPreferredSize() {
		final Dimension preferredSize = super.getPreferredSize();
		if(preferredSize.getWidth() < getMinimumSize().getWidth()) {
			preferredSize.setSize(getMinimumSize().getWidth(), preferredSize.getHeight());
		}
		return preferredSize;
	}
	
	
}
