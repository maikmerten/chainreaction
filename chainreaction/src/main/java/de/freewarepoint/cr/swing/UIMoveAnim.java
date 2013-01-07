package de.freewarepoint.cr.swing;

import de.freewarepoint.cr.Player;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static de.freewarepoint.cr.swing.UIField.CELL_SIZE;

public class UIMoveAnim implements UIAnimation {
	private static final int DELAY = 25;
	private final int animCount = 25, brightness = 125, fadeCount = 20;

	private int animCounter = 0, fadeCounter = 0;
	private long lastAnim = System.currentTimeMillis();
	private boolean raise = true;
	private Player player;
	private Player oldPlayer;
	private final int x, y;

	public UIMoveAnim(final int x, final int y, final Player player) {
		this.player = player;
		this.x = x;
		this.y = y;
	}

	@Override
	public void draw(Graphics2D g2d) {
		final AffineTransform transform = g2d.getTransform();
		g2d.translate((x * CELL_SIZE * 2), (y * CELL_SIZE * 2));
		final long now = System.currentTimeMillis();
		Color color;
		if(oldPlayer != null && fadeCounter == fadeCount) {
			oldPlayer = null;
		}
		if(oldPlayer != null) {
			final Color oldColor = UIPlayer.getPlayer(oldPlayer).getBackground();
			final Color newColor = UIPlayer.getPlayer(player).getBackground();
			color = new Color(
					calcCurrFadeColor(oldColor.getRed(), newColor.getRed()),
					calcCurrFadeColor(oldColor.getGreen(), newColor.getGreen()),
					calcCurrFadeColor(oldColor.getBlue(), newColor.getBlue()));
		}
		else {
			color = UIPlayer.getPlayer(player).getBackground();
		}

		
		if((now - lastAnim) > DELAY) {
			final int anims = (int)(now - lastAnim)/DELAY;
			if(oldPlayer != null) {
				fadeCounter = (fadeCounter + anims);
				if(fadeCounter > fadeCount) {
					fadeCounter = fadeCount;
				}
			}
			animCounter = (animCounter + (raise ? anims : - anims));
			if(animCounter > animCount) {
				animCounter = animCount;
			}
			lastAnim = now;
		}
		final Color oldColor = g2d.getColor();

		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
		hsb[2] += (animCounter*((float)brightness/animCount)/255);
		g2d.setColor(new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])));
		
		g2d.setStroke(new BasicStroke(8));
		
		g2d.drawLine((CELL_SIZE/4), 0, (int)(CELL_SIZE*1.75d), 0);
		g2d.drawLine((CELL_SIZE/4), 2*CELL_SIZE, (int)(CELL_SIZE*1.75d), 2*CELL_SIZE);
		g2d.drawLine(0, (CELL_SIZE/4), 0, (int)(CELL_SIZE*1.75d));
		g2d.drawLine(2*CELL_SIZE, (CELL_SIZE/4), 2*CELL_SIZE, (int)(CELL_SIZE*1.75d));
		
		g2d.setColor(oldColor);
		g2d.setTransform(transform);
	}
	
	private int calcCurrFadeColor(final int oldCanal, final int newCanal) {
		return oldCanal + (fadeCounter * (newCanal - oldCanal)) / (fadeCount-1);
		
	}

	@Override
	public boolean isFinished() {
		return animCounter == animCount;
	}
}
