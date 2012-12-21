package de.maikmerten.chainreaction.swing;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Properties;


public class UIAtom implements UIAnimation {
	
	private UIAnimation anim;

	public UIAtom(final String propertyFile) {
		final Properties props = new Properties();
		try {
			props.load(this.getClass().getResourceAsStream(propertyFile));
		}
		catch (IOException e) {
			throw new IllegalStateException("could not load properties for '" + this.getClass().getSimpleName() + "'", e);
		}
		final String animFN = props.getProperty("idle.anim" );
		final int count = Integer.parseInt(props.getProperty("idle.count"));
		anim = new UIEnterAnim(new UIAnimCycle(animFN, count));
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		anim.draw(g2d);
	}

	public UIAtom leave() {
		anim = new UILeaveAnim(anim);
		return this;
	}

	@Override
	public boolean isFinished() {
		return anim.isFinished();
	}
	
}
