package de.maikmerten.chainreaction.swing;

import de.maikmerten.chainreaction.Game;
import de.maikmerten.chainreaction.MoveListener;
import de.maikmerten.chainreaction.ai.AI;
import de.maikmerten.chainreaction.ai.AIThread;
import de.maikmerten.chainreaction.ai.StandardAI;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private boolean blockMoves = false;

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
		uisettings = new UISettings(this);
		frame.add(uisettings, BorderLayout.NORTH);

		startNewGame();

		frame.setVisible(true);
	}

	void startNewGame() {
		game = new Game(6, 5);
		ai = new StandardAI(game);
		uifield.setGame(game);
		updateStatus();
	}

	public void onMoveSelected(final int x, final int y) {
		if (blockMoves) {
			return;
		}

		blockMoves = true;

		if (game.getWinner() != 0) {
			startNewGame();
			return;
		}

		Thread moveThread = new Thread() {
			@Override
			public void run() {
				game.onMoveSelected(x, y);
				updateStatus();

				if (game.getWinner() == 0 && game.getCurrentPlayer() == 2 && uisettings.againstAI()) {
					try {
						AIThread t = new AIThread(ai, 1500);
						t.start();
						t.join();
					} catch (InterruptedException ex) {
						Logger.getLogger(UIGame.class.getName()).log(Level.SEVERE, null, ex);
					}
					updateStatus();
				}
				blockMoves = false;
			}
		};

		moveThread.start();
	}

	private void updateStatus() {
		final StringBuilder sb = new StringBuilder();

		if (game.getWinner() != 0) {
			sb.append("Player ").append(game.getWinner()).append(" won in round ").append(game.getRound());
		} else {
			sb.append("Round ").append(game.getRound()).append(" | Active player: ").append(game.getCurrentPlayer());
			sb.append(" | Current Score: ").append(game.getField().getPlayerAtoms((byte) 1));
			sb.append(":").append(game.getField().getPlayerAtoms((byte) 2));
		}

		Runnable updateRunner = new Runnable() {
			public void run() {
				status.setText(sb.toString());
			}
		};
		SwingUtilities.invokeLater(updateRunner);
	}

	public static void main(String[] args) {
		new UIGame();
	}
}
