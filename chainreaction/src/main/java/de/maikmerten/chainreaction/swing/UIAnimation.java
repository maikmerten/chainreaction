package de.maikmerten.chainreaction.swing;

import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;


/**
 * @author jonny
 *
 */
public class UIAnimation implements UIDrawable {
	private Image[] images;
	private final long delay;
	private long lastAnim;
	private int animCounter = 0;

	/**
	 * 
	 */
	public UIAnimation(final String fileName, final int animCount, final long delay) {
		this.delay = delay;
		initAnimImages(fileName, animCount);
	}

	private void initAnimImages(final String fileName, final int animCount) {
		this.images = new Image[animCount];
		for(int i = 0; i < images.length; i++) {
			final ImageIcon curr = 
					new ImageIcon(this.getClass().getResource(String.format(fileName, i)));
			images[i] = curr.getImage();
		}
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		g2d.drawImage(getImage(), 0, 0, null);
	}
	
	private Image getImage() {
		final long currentTimeMillis = System.currentTimeMillis();
		final long diff = currentTimeMillis - lastAnim;
		if(diff > delay) {
			lastAnim = currentTimeMillis;
			animCounter = (animCounter + ((int)(diff/delay)))%images.length;
		}
		return images[animCounter];
	}
}
