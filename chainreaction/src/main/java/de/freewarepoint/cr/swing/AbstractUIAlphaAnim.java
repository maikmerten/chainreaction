package de.freewarepoint.cr.swing;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;


public abstract class AbstractUIAlphaAnim implements UIAnimation {
	private static final int DELAY = 25;
	private final UIAnimation anim;
	private long lastAnim;
	final static int MAX_COUNTER = 20;
	
	AbstractUIAlphaAnim(UIAnimation anim, long delay) {
		this.anim = anim;
		lastAnim = System.currentTimeMillis() + delay;
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

		final AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ((float)currCounter)/MAX_COUNTER);
		final Graphics2D gbi = (Graphics2D)g2d.create();
		gbi.setComposite(ac);
		anim.draw(gbi);
	}
	
	protected abstract int nextCounter(int countOfAnims);

	@Override
	public boolean isFinished() {
		return anim.isFinished();
	}

}
