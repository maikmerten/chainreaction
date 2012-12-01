package de.maikmerten.chainreaction.swing;

import de.maikmerten.chainreaction.Game;
import de.maikmerten.chainreaction.MoveListener;
import de.maikmerten.chainreaction.ai.AI;
import de.maikmerten.chainreaction.ai.StandardAI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;

/**
 *
 * @author maik
 */
public class UIGame implements MoveListener {

    private JLabel status;
	private Game game;
	private UIField uifield;
	private UISettings uisettings;
	private AI ai;

	public UIGame() {
		game = new Game(6, 5);
        JFrame frame = new JFrame("Chain reaction");
		frame.setMinimumSize(new Dimension(640, 480));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		uifield = new UIField(this, game);
		frame.add(uifield, BorderLayout.CENTER);
		status = new JLabel();
		frame.add(status, BorderLayout.SOUTH);
		uisettings = new UISettings();
		frame.add(uisettings, BorderLayout.NORTH);

		startNewGame();
		
		frame.setVisible(true);
	}

	private void startNewGame() {
		game = new Game(6,5);
		ai = new StandardAI(game);
		uifield.setGame(game);
		updateStatus();
	}
	

	public void onMoveSelected(int x, int y) {
	
		if(game.getWinner() != 0) {
			startNewGame();
			return;
		}
		
		game.onMoveSelected(x, y);
		updateStatus();
		
		if(game.getWinner() == 0 && game.getCurrentPlayer() == 2 && uisettings.againstAI()) {
			ai.doMove();
			updateStatus();
		}
	}
	
	
	private void updateStatus() {
        StringBuilder sb = new StringBuilder();

		if(game.getWinner() != 0) {
            sb.append("Player ").append(game.getWinner()).append(" won in round ").append(game.getRound());
		} else {
            sb.append("Round ").append(game.getRound()).append(" | Active player: ").append(game.getCurrentPlayer());
            sb.append(" | Current Score: ").append(game.getField().getPlayerAtoms((byte) 1));
            sb.append(":").append(game.getField().getPlayerAtoms((byte)2));
		}

        status.setText(sb.toString());
	}

	public static void main(String[] args) {
		UIGame g = new UIGame();
	}

}
