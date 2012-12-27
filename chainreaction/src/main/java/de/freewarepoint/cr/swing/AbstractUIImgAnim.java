package de.freewarepoint.cr.swing;

import java.awt.*;


/**
 * @author jonny
 *
 */
public abstract class AbstractUIImgAnim implements UIAnimation {
	private final Image[] images;
	private static final int DELAY = 50;
	private long lastAnim;
	private int animCounter = 0;
	private final int animCount;

	AbstractUIImgAnim(final String fileName, final int animCount) {
		images = initAnimImages(fileName, animCount);
		this.animCount = images.length;
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
			animCounter = (animCounter + ((int)(diff/DELAY))) % animCount;
		}
		return images[animCounter];
	}
	
	public boolean isFinished() {
		return false;
	}

	protected abstract Image[] initAnimImages(final String fileName, final int animCount);
}
