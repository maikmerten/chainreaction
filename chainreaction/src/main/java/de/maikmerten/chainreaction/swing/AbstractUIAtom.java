package de.maikmerten.chainreaction.swing;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Properties;


public abstract class AbstractUIAtom implements UIDrawable {
	
	public enum Mode {
		ENTER("enter", false),
		LEAVE("leave", false),
		IDLE("idle", true);
		
		private final String name;
		private final boolean cyclic;
		
		private Mode(final String name, final boolean cyclic) {
			this.name = name;
			this.cyclic = cyclic;
		}
		
		public String getAnimName() {
			return this.name + ".anim";
		}
		
		public String getCountName() {
			return this.name + ".count";
		}
		
		public boolean isCyclic() {
			return cyclic;
		}

		@Override
		public String toString() {
			return name;
		}
	}
	
	private Mode mode = Mode.IDLE;
	private UIDrawable[] modes = new UIDrawable[Mode.values().length];

	public AbstractUIAtom(final String propertyFile) {
		final Properties props = new Properties();
		try {
			props.load(this.getClass().getResourceAsStream(propertyFile));
		}
		catch (IOException e) {
			throw new IllegalStateException("could not load properties for '" + this.getClass().getSimpleName() + "'", e);
		}
		for(final Mode mode : Mode.values()) {
			final String animFN = props.getProperty(mode.getAnimName());
			final int count = Integer.parseInt(props.getProperty(mode.getCountName()));
			modes[mode.ordinal()] = mode.isCyclic() ? 
					new UIAnimCycle(animFN, count) :
						new UIAnimation(animFN, count);
		}
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		modes[mode.ordinal()].draw(g2d);
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
}
