package de.maikmerten.chainreaction.swing;

import java.awt.Image;


public class UIImgAnim extends AbstractUIImgAnim {

	public UIImgAnim(final String fileName, final int animCount) {
		super(fileName, animCount);
	}

	@Override
	protected Image[] initAnimImages(String fileName, int animCount) {
		Image[] images = new Image[animCount <= 0 ? 0 : (2*animCount)-1];
		if(animCount <= 0) {
			return images;
		}
		for(int i = 0; i < animCount; i++) {
			images[i] = UIImageCache.loadImage(String.format(fileName, i));
		}
		int j = animCount-1;
		for(int i = animCount; i < (2*animCount)-1; i++) {
			images[i] = images[j--];
		}
		return images;
	}
}
