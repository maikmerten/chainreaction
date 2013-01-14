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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import de.freewarepoint.cr.Game;
import de.freewarepoint.cr.Player;
import de.freewarepoint.cr.Settings;
import de.freewarepoint.cr.SettingsLoader;
import de.freewarepoint.cr.ai.AI;

/**
 * @author maik
 * @author jonny
 */
public class UIGame extends JFrame {

	private static final long serialVersionUID = -2178907135995785292L;

	private UIStatus uistatus;
	private UIPlayerStatus uiplayerstatus;
	private final ExecutorService execService;

	private Game game;

	private UISettings uisettings;

	private volatile boolean blockMoves = false;

	private UIField uifield;
	private UIChooseAI uichooseai1;
	private UIChooseAI uichooseai2;

	private final Settings settings;

	public UIGame() {
		settings = SettingsLoader.loadSettings();
		this.execService = Executors.newSingleThreadExecutor();
		initGUI();
		startNewGame();
		final GraphicsDevice screenDevice = 
				GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if (screenDevice.isFullScreenSupported()) {
			this.setResizable(false);
			this.setAlwaysOnTop(true);
			this.setVisible(true);
			screenDevice.setFullScreenWindow(this);
			this.validate();
		}
		else {
			this.setVisible(true);
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
		
		uichooseai1 = new UIChooseAI(this, Player.FIRST);
		uichooseai2 = new UIChooseAI(this, Player.SECOND);

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
	
	void showChooseAI(final Player p) {
		getContentPane().remove(uifield);
		getContentPane().remove(uiplayerstatus);
		getContentPane().remove(uistatus);
		switch(p) {
			case FIRST:
				getContentPane().add(uichooseai1);
				break;
			case SECOND:
				getContentPane().add(uichooseai2);
				break;
			default:
				throw new RuntimeException("Not a valid player: " + p); 
		}
		revalidate();
		repaint();
	}
	
	void chooseAI(final Player p, final AI ai) {
		if(ai != null) {
			ai.setGame(game);
		}
		if(game != null) {
			game.getPlayerStatus(p).setAI(ai);
		}
		final Container contentPane = getContentPane();
		switch(p) {
			case FIRST:
				getContentPane().remove(uichooseai1);
				break;
			case SECOND:
				getContentPane().remove(uichooseai2);
				break;
			default:
				throw new RuntimeException("Not a valid player: " + p); 
		}
		contentPane.add(uifield, BorderLayout.CENTER);
		contentPane.add(uiplayerstatus, BorderLayout.WEST);
		contentPane.add(uistatus, BorderLayout.SOUTH);
		if(ai != null && game != null && game.getCurrentPlayer() == p) {
			this.execService.submit(new Runnable() {

				@Override
				public void run() {
					doAI();
				}
				
			});
		}
		updateStatus();
		revalidate();
		repaint();
	}

	void startNewGame() {
		final Game oldGame = game;
		game = new Game(6, 5, settings);
		if(oldGame != null) {
			for(final Player player : Player.values()) {
				final AI ai = oldGame.getPlayerStatus(player).getAI();
				if(ai != null) {					
					ai.setGame(game);
					game.getPlayerStatus(player).setAI(ai);
				}
			}
		}
		uifield.setGame(game);
		uistatus.setGame(game);
		uiplayerstatus.setGame(game);
		uisettings.setGame(game);
		updateStatus();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if(game != null) {
					uifield.setNewGameAnim(game);
				}
			}
			
		});
		if(game.getPlayerStatus(game.getCurrentPlayer()).isAIPlayer()) {
			execService.submit(new Runnable() {

				@Override
				public void run() {
					doAI();
				}
				
			});
		}
		else {
			blockMoves = false;
		}
	}

	public void selectMove(final int x, final int y) {
		if (blockMoves) {
			return;
		}
		execService.submit(new Runnable() {

			@Override
			public void run() {
				

				if (game.getWinner() != Player.NONE) {
					startNewGame();
					return;
				}
				
				game.selectMove(x, y);
				updateStatus();

				doAI();
			}
		});
	}

	private void updateStatus() {
		SwingUtilities.invokeLater(uistatus);
		SwingUtilities.invokeLater(uiplayerstatus);
		SwingUtilities.invokeLater(uisettings);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if(game != null && game.getWinner() != Player.NONE) {
					uifield.setWonAnim(game.getWinner());
				}
			}
			
		});
	}

	private void doAI() {
		blockMoves = true;
		while (game.getWinner() == Player.NONE && game.getPlayerStatus(game.getCurrentPlayer()).isAIPlayer()) {
			try {
				Thread.sleep(2000);
			} 
			catch (InterruptedException ex) {
				Logger.getLogger(UIGame.class.getName()).log(Level.SEVERE, null, ex);
			}
			game.getPlayerStatus(game.getCurrentPlayer()).getAI().doMove();
			updateStatus();
		}
		blockMoves = false;
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
