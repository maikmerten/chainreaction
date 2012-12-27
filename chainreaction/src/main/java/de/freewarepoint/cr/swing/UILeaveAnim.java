package de.freewarepoint.cr.swing;


public class UILeaveAnim extends AbstractUIAlphaAnim {
	private int currCounter = MAX_COUNTER;
	
	public UILeaveAnim(UIAnimation anim) {
		super(anim);
	}

	@Override
	public int nextCounter(int countOfAnims) {
		return currCounter = (currCounter - countOfAnims) >= 1 ? currCounter - countOfAnims : 1;
	}
	
	@Override
	public boolean isFinished() {
		return currCounter == 1;
	}
}