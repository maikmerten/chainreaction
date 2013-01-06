package de.freewarepoint.cr.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import de.freewarepoint.cr.Game;
import de.freewarepoint.cr.MoveListener;
import de.freewarepoint.cr.Player;
import de.freewarepoint.cr.Settings;
import de.freewarepoint.cr.SettingsLoader;
import de.freewarepoint.cr.ai.AI;
import de.freewarepoint.cr.ai.AIThread;
import de.freewarepoint.cr.ai.StandardAI;

/**
 * @author maik
 * @author jonny
 */
public class UIGame extends JFrame implements MoveListener {

	private static final long serialVersionUID = -2178907135995785292L;

	private UIStatus status;

	private static final String EXIT = "Exit";

	private Game game;

	private UISettings uisettings;

	private AI ai;

	// TODO not thread save.
	private boolean blockMoves = false;

	private UIField uifield;

	private final Settings settings;

	private Action exit = new AbstractAction(EXIT) {

		private static final long serialVersionUID = 1114904232100740338L;

		@Override
		public void actionPerformed(ActionEvent e) {
			UIGame.this.dispatchEvent(new WindowEvent(UIGame.this, WindowEvent.WINDOW_CLOSING));
		}
	};

	public UIGame() {
		settings = SettingsLoader.loadSettings();
		initGUI();
		startNewGame();
		final GraphicsDevice screenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		this.setVisible(true);
		if (screenDevice.isFullScreenSupported()) {
			screenDevice.setFullScreenWindow(this);
			this.validate();
		}
		else {
			this.setExtendedState(Frame.MAXIMIZED_BOTH);
		}
	}

	private void initGUI() {
		setUndecorated(true);
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

		contentPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), EXIT);
		contentPane.getActionMap().put(EXIT, exit);
	}

	void startNewGame() {
		game = new Game(6, 5, settings);
		// TODO Use loaded AIs
		// List<AI> ais = SettingsLoader.loadAIs();
		// ai = ais.get(0);
		ai = new StandardAI();
		ai.setGame(game);
		uifield.setGame(game);
		blockMoves = false;
		status.setGame(game);
		updateStatus();
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

				if (game.getWinner() == Player.NONE && game.getCurrentPlayer() == Player.SECOND && uisettings
						.againstAI()) {
					try {
						AIThread t = new AIThread(ai, 1500);
						t.start();
						t.join();
					}
					catch (InterruptedException ex) {
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
