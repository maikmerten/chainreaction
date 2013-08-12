package de.freewarepoint.cr.swing;

import de.freewarepoint.cr.Player;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static de.freewarepoint.cr.swing.UIField.CELL_SIZE;

public class UIMoveAnim implements UIAnimation {
	private static final int DELAY = 25;
	private static final int ANIM_COUNT = 25;
	private static final int  BRIGHTNESS = 125;

	private int animCounter = 0;
	private long lastAnim = System.currentTimeMillis();
	private final Player player;
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
		final Color color = UIPlayer.getPlayer(player).getBackground();
		
		if((now - lastAnim) > DELAY) {
			final int anims = (int)(now - lastAnim)/DELAY;
			animCounter += anims;
			if(animCounter > ANIM_COUNT) {
				animCounter = ANIM_COUNT;
			}
			lastAnim = now;
		}
		final Color oldColor = g2d.getColor();

		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
		hsb[2] += (animCounter*((float) BRIGHTNESS / ANIM_COUNT)/255);
		g2d.setColor(new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])));
		
		g2d.setStroke(new BasicStroke(8));
		
		g2d.drawLine((CELL_SIZE/4), 0, (int)(CELL_SIZE*1.75d), 0);
		g2d.drawLine((CELL_SIZE/4), 2*CELL_SIZE, (int)(CELL_SIZE*1.75d), 2*CELL_SIZE);
		g2d.drawLine(0, (CELL_SIZE/4), 0, (int)(CELL_SIZE*1.75d));
		g2d.drawLine(2*CELL_SIZE, (CELL_SIZE/4), 2*CELL_SIZE, (int)(CELL_SIZE*1.75d));
		
		g2d.setColor(oldColor);
		g2d.setTransform(transform);
	}

	@Override
	public boolean isFinished() {
		return animCounter == ANIM_COUNT;
	}
}
