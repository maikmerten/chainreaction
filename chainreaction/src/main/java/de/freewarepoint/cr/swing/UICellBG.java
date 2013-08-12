package de.freewarepoint.cr.swing;

import de.freewarepoint.cr.Player;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static de.freewarepoint.cr.swing.UIField.CELL_SIZE;

public class UICellBG implements UIDrawable {
	private static final int DELAY = 40;
	private static final int ANIM_COUNT = 80;
	private static final int BRIGHTNESS = 75;
	private static final int FADE_COUNT = 40;

	private int animCounter = 0, fadeCounter = 0;
	private long lastAnim = System.currentTimeMillis();
	private boolean raise = true;
	private Player player;
	private Player oldPlayer;
	private final int x, y;

	public UICellBG(final int x, final int y, final Player player) {
		this.player = player;
		this.x = x;
		this.y = y;
	}
	
	public void changeOwner(final Player player) {
		if(this.player == player) {
			return;
		}
		oldPlayer = this.player;
		this.player = player;
		this.fadeCounter = 0;
	}

	@Override
	public void draw(Graphics2D g2d) {
		final AffineTransform transform = g2d.getTransform();
		g2d.translate((x * CELL_SIZE * 2), (y * CELL_SIZE * 2));
		final long now = System.currentTimeMillis();
		Color color;
		if(oldPlayer != null && fadeCounter == FADE_COUNT) {
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
				if(fadeCounter > FADE_COUNT) {
					fadeCounter = FADE_COUNT;
				}
			}
			animCounter = (animCounter + (raise ? anims : - anims));
			animCounter = animCounter < 0 ? 0 : animCounter;
			animCounter = animCounter > ANIM_COUNT ? ANIM_COUNT : animCounter;
			if(player.equals(Player.NONE)) {
				raise = false;
			}
			else if(raise ? animCounter >= (ANIM_COUNT -1) : animCounter == 0) {
				raise = !raise;
			}
			lastAnim = now;
		}
		final Color oldColor = g2d.getColor();

		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
		hsb[2] += ((animCounter*((float) BRIGHTNESS / ANIM_COUNT))/255);
		g2d.setColor(new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])));
		
		g2d.fillRect(0, 0, (CELL_SIZE*2), (CELL_SIZE*2));
		
		g2d.setColor(oldColor);
		g2d.setTransform(transform);
	}
	
	private int calcCurrFadeColor(final int oldCanal, final int newCanal) {
		return oldCanal + (fadeCounter * (newCanal - oldCanal)) / (FADE_COUNT -1);
	}
}
