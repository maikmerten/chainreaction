package de.freewarepoint.cr.swing;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


public class UIExplodeAnim implements UIAnimation {
	private enum Direction {
		UP(0, -1),
		RIGHT(1, 0),
		LEFT(-1, 0),
		DOWN(0, 1),
		NONE(0, 0);
		
		private final int translateX, translateY;
		
		private Direction(final int translateX, final int translateY) {
			this.translateX = translateX;
			this.translateY = translateY;
		}
		
		public int getTranslateX() {
			return translateX;
		}

		public int getTranslateY() {
			return translateY;
		}
	}
	public static int DELAY = 25;
	public static float ACCELERATION = 0.8f; 
	
	private long lastAnim = System.currentTimeMillis();
	private long animCounter = 0;
	private final UIAnimation anim;
	private final Direction direction;
	
	public UIExplodeAnim(final UIAnimation anim, final int x, final int y, final int width, final int height, final int pos) {
		this.anim = anim;
		// top left
		if(x == 0 && y == 0) {
			switch(pos) {
				case 0:
					direction = Direction.DOWN;
					break;
				case 1:
					direction = Direction.values()[pos];
					break;
				default:
					direction = Direction.NONE;	
			}
		}
		// bottom left
		else if(x == 0 && y == (height-1)) {
			switch(pos) {
				case 0:
				case 1:
					direction = Direction.values()[pos];
					break;
				default:
					direction = Direction.NONE;
			}
		}
		// left
		else if(x == 0) {
			switch(pos) {
				case 0:
				case 1:
					direction = Direction.values()[pos];
					break;
				case 2:
					direction = Direction.DOWN;
					break;
				default:
					direction = Direction.NONE;
			}
		}
		// top right
		else if(x == (width-1) && y == 0) {
			switch(pos) {
				case 0:
					direction = Direction.LEFT;
					break;
				case 1:
					direction = Direction.DOWN;
					break;
				default:
					direction = Direction.NONE;
			}
		}
		// bottom right
		else if(x == (width-1) && y == (height-1)) {
			switch(pos) {
				case 0:
					direction = Direction.LEFT;
					break;
				case 1:
					direction = Direction.UP;
					break;
				default:
					direction = Direction.NONE;
			}
		}
		// right
		else if(x == (width-1)) {
			switch(pos) {
				case 0:
					direction = Direction.LEFT;
					break;
				case 1:
					direction = Direction.UP;
					break;
				case 2:
					direction = Direction.DOWN;
					break;
				default:
					direction = Direction.NONE;
			}
		}
		// top
		else if(y == 0) {
			switch(pos) {
				case 0:
					direction = Direction.LEFT;
					break;
				case 1:
					direction = Direction.RIGHT;
					break;
				case 2:
					direction = Direction.DOWN;
					break;
				default:
					direction = Direction.NONE;
			}
		}
		// bottom
		else if(y == (height-1)) {
			switch(pos) {
				case 0:
				case 1:
				case 2:
					direction = Direction.values()[pos];
					break;
				default:
					direction = Direction.NONE;
			}
		}
		// inner
		else {
			direction = Direction.values()[pos];
		}
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		final long currentTimeMillis = System.currentTimeMillis();
		final long diff = currentTimeMillis - lastAnim;
		if(diff > DELAY) {
			lastAnim = currentTimeMillis;
			final int countOfAnims = ((int)(diff/DELAY));
			animCounter += countOfAnims;
		}
		final double movement = (ACCELERATION/2)*Math.pow(animCounter, 2);
		final AffineTransform transform = g2d.getTransform();
		g2d.translate((double)(movement*direction.getTranslateX()), (double)movement*direction.getTranslateY());
		anim.draw(g2d);
		g2d.setTransform(transform);
	}



	@Override
	public boolean isFinished() {
		return anim.isFinished();
	}
}