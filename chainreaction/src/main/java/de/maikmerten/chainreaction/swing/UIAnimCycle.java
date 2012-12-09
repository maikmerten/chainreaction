package de.maikmerten.chainreaction.swing;

import java.awt.Image;

import javax.swing.ImageIcon;


public class UIAnimCycle extends AbstractUIAnimation {

	public UIAnimCycle(final String fileName, final int animCount) {
		super(fileName, animCount);
	}

	@Override
	protected Image[] initAnimImages(String fileName, int animCount) {
		Image[] images = new Image[animCount <= 0 ? 0 : (2*animCount)-1];
		if(animCount <= 0) {
			return images;
		}
		for(int i = 0; i < animCount; i++) {
			final String fileFN = String.format(fileName, i);
			System.out.println("i: " + i + ", fileFN: " + fileFN);
			final ImageIcon curr = 
					new ImageIcon(this.getClass().getResource(fileFN));
			images[i] = curr.getImage();
		}
		int j = animCount-1;
		for(int i = animCount; i < (2*animCount)-1; i++) {
			images[i] = images[j--];
		}
		return images;
	}
	
	

}
