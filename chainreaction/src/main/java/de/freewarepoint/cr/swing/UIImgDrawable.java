package de.freewarepoint.cr.swing;

import java.awt.Graphics2D;
import java.awt.Image;


public class UIImgDrawable implements UIAnimation {
	private final Image image;
	private final int width, height;
	public UIImgDrawable(final Image image, final int width, final int height) {
		this.image = image;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Graphics2D g2d) {
		final int 
				x = (width/2) - (image.getWidth(null)/2),
				y = (height/2) - (image.getHeight(null)/2);
		
		g2d.drawImage(image, x, y, null);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

}
