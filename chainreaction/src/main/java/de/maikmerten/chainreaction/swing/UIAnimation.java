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
	private static final int DELAY = 50;
	private long lastAnim;
	private int animCounter = 0;

	public UIAnimation(final String fileName, final int animCount) {
		initAnimImages(fileName, animCount);
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

	private void initAnimImages(final String fileName, final int animCount) {
		this.images = new Image[animCount <= 0 ? 0 : (2*animCount)-1];
		if(animCount <= 0) {
			return;
		}
		for(int i = 0; i < animCount; i++) {
			final ImageIcon curr = 
					new ImageIcon(this.getClass().getResource(String.format(fileName, i)));
			images[i] = curr.getImage();
		}
		int j = animCount-1;
		for(int i = animCount; i < (2*animCount)-1; i++) {
			images[i] = images[j--];
		}
	}
}
