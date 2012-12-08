package de.maikmerten.chainreaction.swing;

import java.awt.Image;

import javax.swing.ImageIcon;


public class UIAnimation extends AbstractUIAnimation {

	public UIAnimation(final String fileName, final int animCount) {
		super(fileName, animCount);
	}

	@Override
	protected Image[] initAnimImages(String fileName, int animCount) {
		Image[] images = new Image[animCount];
		if(animCount <= 0) {
			return images;
		}
		for(int i = 0; i < animCount; i++) {
			final ImageIcon curr = 
					new ImageIcon(this.getClass().getResource(String.format(fileName, i)));
			images[i] = curr.getImage();
		}
		return images;
	}

}
