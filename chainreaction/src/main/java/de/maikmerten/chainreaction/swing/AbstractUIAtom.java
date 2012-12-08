package de.maikmerten.chainreaction.swing;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.Properties;


public abstract class AbstractUIAtom implements UIDrawable {
	
	public enum Mode {
		ENTER("enter"),
		LEAVE("leave"),
		IDLE("idle");
		
		private String name;
		
		private Mode(String name) {
			this.name = name;
		}
		
		public String getAnimName() {
			return this.name + ".anim";
		}
		
		public String getCountName() {
			return this.name + ".count";
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
			modes[mode.ordinal()] = new UIAnimation(
					props.getProperty(mode.getAnimName()), 
					Integer.parseInt(props.getProperty(mode.getCountName())));
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
