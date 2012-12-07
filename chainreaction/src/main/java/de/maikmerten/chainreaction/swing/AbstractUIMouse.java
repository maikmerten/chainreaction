package de.maikmerten.chainreaction.swing;

import java.awt.Graphics2D;


public abstract class AbstractUIMouse implements UIDrawable {
	public enum Mode {
		RUN,
		IDLE
		// TODO further modes: ENTER, ASSIMILATE, DEFFECT
	}
	
	private Mode mode = Mode.IDLE;
	private UIDrawable runAnim;
	private UIDrawable idleAnim;

	public AbstractUIMouse(final String runFN, final int runCount, final String idleFN, final int idleCount, final long delay) {
		runAnim = new UIAnimation(runFN, runCount, delay);
		idleAnim = new UIAnimation(idleFN, idleCount, delay);
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		switch(mode) {
			case RUN:
				runAnim.draw(g2d);
				break;
			case IDLE:
				idleAnim.draw(g2d);
				break;
		}
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
}
