package de.freewarepoint.cr.swing;

import java.awt.Color;

import de.freewarepoint.cr.Player;


/**
 * @author jonny
 *
 */
public class UIStatus extends AbstractUIStatus implements Runnable {

	private static final long serialVersionUID = 8437395162237408047L;
	
	public UIStatus() {
		super();
	}

	// This runnable should be used in the SwingEventQueue, only!
	@Override
	public void run() {
		if(game != null) {
			final StringBuilder sb = new StringBuilder();
			final Color foreground;
	
			if (game.getWinner() != Player.NONE) {
				sb.append("Player ").append(game.getWinner()).append(" won in round ").append(game.getRound());
				foreground = UIPlayer.getPlayer(game.getWinner()).getForeground();
			}
			else {
				sb.append("Round ").append(game.getRound());
				sb.append(" | Current Score: ").append(game.getField().getTotalNumberOfAtomsForPlayer(Player.FIRST));
				sb.append(":").append(game.getField().getTotalNumberOfAtomsForPlayer(Player.SECOND));
				foreground = UIPlayer.getPlayer(game.getCurrentPlayer()).getForeground();
			}
			sb.append(" Leave with 'q'.");
			
			statusImg = retroFont.getRetroString(sb.toString(), foreground, FONT_SIZE);
			UIStatus.this.repaint();
		}
	}
}
