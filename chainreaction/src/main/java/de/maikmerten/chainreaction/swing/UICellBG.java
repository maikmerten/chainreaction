package de.maikmerten.chainreaction.swing;

import static de.maikmerten.chainreaction.swing.UIField.CELL_SIZE;

import java.awt.Color;
import java.awt.Graphics2D;


/**
 * @author jonny
 *
 */
public class UICellBG implements UIDrawable {
	private static final int DELAY = 50;
	private final int animCount = 50;
	private final int brightness = 75;
	
	private int animCounter = 0;
	private long lastAnim = System.currentTimeMillis();
	private boolean raise = true;
	private final byte player;

	public UICellBG(byte player) {
		this.player = player;
	}

	@Override
	public void draw(Graphics2D g2d) {
		final long now = System.currentTimeMillis();
		final Color oldColor = g2d.getColor();
		Color color = UIPlayer.getPlayer(player).getBackground();
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
		if((now - lastAnim) > DELAY) {
			int anims = (int)(now - lastAnim)/DELAY;
			animCounter = animCounter + (raise ? (int)anims : -(int)anims) % animCount;
			animCounter = animCounter < 0 ? 0 : animCounter;  
			if(raise ? animCounter == animCount : animCounter == 0) {
				raise = !raise;
			}
			lastAnim = now;
		}
		hsb[2] += (animCounter*((float)brightness/animCount)/255);
		g2d.setColor(new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2])));
		g2d.fillRect(2, 2, (CELL_SIZE*2)-3, (CELL_SIZE*2)-3);
		g2d.setColor(oldColor);
	}
}
