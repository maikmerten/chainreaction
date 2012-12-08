package de.maikmerten.chainreaction.swing;

import java.awt.Graphics2D;
import java.awt.Image;


/**
 * @author jonny
 *
 */
public abstract class AbstractUIAnimation implements UIDrawable {
	private Image[] images;
	private static final int DELAY = 50;
	private long lastAnim;
	private int animCounter = 0;

	public AbstractUIAnimation(final String fileName, final int animCount) {
		images = initAnimImages(fileName, animCount);
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.drawImage(getImage(), 0, 0, null);
	}
	
	private Image getImage() {
		final long currentTimeMillis = System.currentTimeMillis();
		final long diff = currentTimeMillis - lastAnim;
		if(diff > DELAY) {
			lastAnim = currentTimeMillis;
			animCounter = (animCounter + ((int)(diff/DELAY)))%images.length;
		}
		return images[animCounter];
	}

	protected abstract Image[] initAnimImages(final String fileName, final int animCount);
}
