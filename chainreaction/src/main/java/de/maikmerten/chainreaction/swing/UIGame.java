package de.maikmerten.chainreaction.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import de.maikmerten.chainreaction.Game;
import de.maikmerten.chainreaction.MoveListener;
import de.maikmerten.chainreaction.Player;
import de.maikmerten.chainreaction.Settings;
import de.maikmerten.chainreaction.SettingsLoader;
import de.maikmerten.chainreaction.ai.AI;
import de.maikmerten.chainreaction.ai.AIThread;
import de.maikmerten.chainreaction.ai.StandardAI;

/**
 *
 * @author maik
 * @author jonny
 */
public class UIGame extends JFrame implements MoveListener {

	private static final long serialVersionUID = -2178907135995785292L;
	private UIStatus status;

	private Game game;
	private UISettings uisettings;
	private AI ai;
	
	// TODO not thread save.
	private boolean blockMoves = false;
	private UIField uifield;
	private final Settings settings;

	public UIGame() {
		settings = SettingsLoader.loadSettings();
		initGUI();
		startNewGame();
		setVisible(true);
	}

	private void initGUI() {
//		setUndecorated(true);
		setTitle("ChainReaction");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		
		contentPane.setLayout(new BorderLayout(5, 5));
		contentPane.setBackground(Color.BLACK);
		
		uifield = new UIField(this);
		contentPane.add(uifield, BorderLayout.CENTER);
		
		status = new UIStatus();
		contentPane.add(status, BorderLayout.SOUTH);
		uisettings = new UISettings(this);
		contentPane.add(uisettings, BorderLayout.NORTH);
	}

	void startNewGame() {
		game = new Game(6, 5, settings);
		// TODO Use loaded AIs
//		List<AI> ais = SettingsLoader.loadAIs();
//		ai = ais.get(0);
		ai = new StandardAI();
		ai.setGame(game);
		uifield.setGame(game);
		blockMoves = false;
		status.setGame(game);
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
		SwingUtilities.invokeLater(status);
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
