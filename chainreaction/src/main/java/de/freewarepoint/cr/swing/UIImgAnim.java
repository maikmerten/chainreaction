package de.freewarepoint.cr.swing;

import java.awt.Image;


public class UIImgAnim extends AbstractUIImgAnim {

	public UIImgAnim(final String fileName, final int animCount, final String bgFN) {
		super(fileName, animCount, bgFN);
	}

	@Override
	protected Image[] initAnimImages(String fileName, int animCount) {
		Image[] images = new Image[animCount <= 0 ? 0 : animCount];
		if(animCount <= 0) {
			return images;
		}
		for(int i = 0; i < animCount; i++) {
			images[i] = UIImageCache.loadImage(String.format(fileName, i));
		}
		return images;
	}
}
