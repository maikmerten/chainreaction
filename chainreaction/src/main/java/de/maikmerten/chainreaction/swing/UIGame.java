package de.maikmerten.chainreaction.swing;

import de.maikmerten.chainreaction.Game;
import de.maikmerten.chainreaction.MoveListener;
import de.maikmerten.chainreaction.Player;
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
 * @author jonny
 */
public class UIGame extends JFrame implements MoveListener {

	private static final long serialVersionUID = -2178907135995785292L;
	
	private JLabel status;
	private Game game;
	private UISettings uisettings;
	private AI ai;
	
	// TODO not thread save.
	private boolean blockMoves = false;
	private UIField uifield;

	public UIGame() {
		initGUI();
		startNewGame();
		setVisible(true);
	}
	
	private void initGUI() {
//		setUndecorated(true);
		setTitle("ChainReaction");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(5, 5));
		
		uifield = new UIField(this);
		add(uifield, BorderLayout.CENTER);
		
		status = new JLabel(" ");
		add(status, BorderLayout.SOUTH);
		uisettings = new UISettings(this);
		add(uisettings, BorderLayout.NORTH);
	}

	void startNewGame() {
		game = new Game(6, 5);
		ai = new StandardAI(game);
		uifield.setGame(game);
		blockMoves = false;
		updateStatus();
		pack();
	}

	public void onMoveSelected(final int x, final int y) {
		if (blockMoves) {
			return;
		}

		blockMoves = true;

		if (game.getWinner() != Player.NONE) {
			startNewGame();
			return;
		}

		Thread moveThread = new Thread() {
			@Override
			public void run() {
				game.onMoveSelected(x, y);
				updateStatus();

				if (game.getWinner() == Player.NONE && game.getCurrentPlayer() == Player.SECOND
						&& uisettings.againstAI()) {
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

		if (game.getWinner() != Player.NONE) {
			sb.append("Player ").append(game.getWinner()).append(" won in round ").append(game.getRound());
		} else {
			sb.append("Round ").append(game.getRound()).append(" | Active player: ").append(game.getCurrentPlayer());
			sb.append(" | Current Score: ").append(game.getField().getPlayerAtoms(Player.FIRST));
			sb.append(":").append(game.getField().getPlayerAtoms(Player.SECOND));
		}

		Runnable updateRunner = new Runnable() {
			public void run() {
				status.setText(sb.toString());
			}
		};
		SwingUtilities.invokeLater(updateRunner);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new UIGame();
			}
		});
	}
}
