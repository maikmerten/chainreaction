package de.maikmerten.chainreaction.swing;

import java.awt.*;


public class UIEnterAnim extends AbstractUIAlphaAnim {
	private int currCounter = 0;
	private final UIAnimation anim;
	
	public UIEnterAnim(UIAnimation anim) {
		super(anim);
		this.anim = anim;
	}

	@Override
	public void draw(Graphics2D g2d) {
		if(currCounter == MAX_COUNTER) {
			anim.draw(g2d);
			return;
		}
		super.draw(g2d);
	}

	@Override
	public int nextCounter(int countOfAnims) {
		return currCounter = (currCounter + countOfAnims) <= MAX_COUNTER ? currCounter + countOfAnims : MAX_COUNTER;
	}
}
