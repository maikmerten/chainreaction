package de.freewarepoint.cr.swing;

import java.awt.*;
import java.awt.image.BufferedImage;


public abstract class AbstractUIAlphaAnim implements UIAnimation {
	private static final int DELAY = 25;
	private final UIAnimation anim;
	private long lastAnim = System.currentTimeMillis();
	final static int MAX_COUNTER = 20;
	
	AbstractUIAlphaAnim(UIAnimation anim) {
		this.anim = anim;
	}

	@Override
	public void draw(Graphics2D g2d) {
		final long currentTimeMillis = System.currentTimeMillis();
		final long diff = currentTimeMillis - lastAnim;
		int countOfAnims = 0;
		if(diff > DELAY) {
			lastAnim = currentTimeMillis;
			countOfAnims = ((int)(diff/DELAY));
		}
		int	currCounter = nextCounter(countOfAnims);

		final BufferedImage offlineScreen = new BufferedImage(UIField.CELL_SIZE, UIField.CELL_SIZE,
				BufferedImage.TYPE_INT_ARGB);
		final Graphics2D gbi = offlineScreen.createGraphics();

		final AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ((float)currCounter)/MAX_COUNTER);
		gbi.setComposite(ac);
		anim.draw(gbi);

		g2d.drawImage(offlineScreen, 0, 0, null);
	}
	
	protected abstract int nextCounter(int countOfAnims);

	@Override
	public boolean isFinished() {
		return anim.isFinished();
	}

}
