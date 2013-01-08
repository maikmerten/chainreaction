package de.freewarepoint.cr.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import de.freewarepoint.cr.Game;
import de.freewarepoint.cr.Player;
import de.freewarepoint.cr.PlayerStatus;
import de.freewarepoint.cr.Settings;
import de.freewarepoint.cr.SettingsLoader;
import de.freewarepoint.cr.ai.AI;
import de.freewarepoint.cr.ai.AIThread;

/**
 * @author maik
 * @author jonny
 */
public class UIGame extends JFrame {

	private static final long serialVersionUID = -2178907135995785292L;

	private UIStatus uistatus;
	private UIPlayerStatus uiplayerstatus;

	private Game game;

	private UISettings uisettings;

	// TODO not thread save.
	private boolean blockMoves = false;

	private UIField uifield;
	private UIChooseAI uichooseai;

	private final Settings settings;

	public UIGame() {
		settings = SettingsLoader.loadSettings();
		initGUI();
		startNewGame();
		final GraphicsDevice screenDevice = 
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		this.setResizable(false);
		this.setAlwaysOnTop(true);
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
		// Add a nice empty border so that the components do not stuck to the screen edges.
		contentPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

		uifield = new UIField(this);
		contentPane.add(uifield, BorderLayout.CENTER);
		
		uichooseai = new UIChooseAI(this);

		uistatus = new UIStatus();
		contentPane.add(uistatus, BorderLayout.SOUTH);
		
		uiplayerstatus = new UIPlayerStatus();
		contentPane.add(uiplayerstatus, BorderLayout.WEST);
		
		uisettings = new UISettings(this);
		contentPane.add(uisettings, BorderLayout.NORTH);
		
		final KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_Q) {
					UIGame.this.dispatchEvent(new WindowEvent(UIGame.this, WindowEvent.WINDOW_CLOSING));
					e.consume();
					return true;
				}
				return false;
			}
			
		});
	}
	
	void showChooseAI() {
		getContentPane().remove(uifield);
		getContentPane().remove(uiplayerstatus);
		getContentPane().remove(uistatus);
		getContentPane().add(uichooseai);
		revalidate();
		repaint();
	}
	
	void chooseAI(final AI ai) {
		if(ai != null) {
			ai.setGame(game);
		}
		if(game != null) {
			// TODO possibility to choose an ai for both players (see issue #13):
			game.getPlayerStatus(Player.SECOND).setAI(ai);
		}
		final Container contentPane = getContentPane();
		contentPane.remove(uichooseai);
		contentPane.add(uifield, BorderLayout.CENTER);
		contentPane.add(uiplayerstatus, BorderLayout.WEST);
		contentPane.add(uistatus, BorderLayout.SOUTH);
		revalidate();
		repaint();
	}

	void startNewGame() {
		game = new Game(6, 5, settings);
		uifield.setGame(game);
		blockMoves = false;
		uistatus.setGame(game);
		uiplayerstatus.setGame(game);
		updateStatus();
	}

	public void selectMove(final int x, final int y) {
		if (blockMoves) {
			return;
		}

		blockMoves = true;

		if (game.getWinner() != Player.NONE) {
			startNewGame();
			return;
		}

		final Thread moveThread = new Thread() {

			@Override
			public void run() {
				game.selectMove(x, y);
				updateStatus();

				final PlayerStatus playerStatus = game.getPlayerStatus(game.getCurrentPlayer());
				if (game.getWinner() == Player.NONE && playerStatus.isAIPlayer()) {
					try {
						AIThread t = new AIThread(playerStatus.getAI(), 1500);
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
		SwingUtilities.invokeLater(uistatus);
		SwingUtilities.invokeLater(uiplayerstatus);
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
