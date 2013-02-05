package de.freewarepoint.cr.swing;

import java.awt.*;
import java.io.IOException;
import java.util.Properties;

import de.freewarepoint.cr.Player;

public class UIAtom implements UIAnimation {
	
	private UIAnimation anim;
	private final int x, y, width, height, pos; 

	public UIAtom(final String propertyFile, int x, int y, int width, int height, int pos, long delay, Player player) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.pos = pos;
		final Properties props = new Properties();
		try {
			props.load(this.getClass().getResourceAsStream(propertyFile));
		}
		catch (IOException e) {
			throw new IllegalStateException("could not load properties for '" + this.getClass().getSimpleName() + "'", e);
		}
		final String animFN = props.getProperty("idle.anim" );
		final int count = Integer.parseInt(props.getProperty("idle.count"));
		final String bgFN = props.getProperty("bg." + player.ordinal());
		anim = new UIEnterAnim(new UIImgAnim(animFN, count, bgFN), delay);
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		anim.draw(g2d);
	}

	public void leave() {
		anim = new UILeaveAnim(anim, 0) ;
	}
	
	public void explode() {
		anim = new UIExplodeAnim(new UILeaveAnim(anim, 250), x, y, width, height, pos) ;
	}


	@Override
	public boolean isFinished() {
		return anim.isFinished();
	}
	
}
