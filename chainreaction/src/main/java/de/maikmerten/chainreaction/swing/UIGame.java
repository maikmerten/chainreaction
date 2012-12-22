package de.maikmerten.chainreaction.swing;

import de.maikmerten.chainreaction.Game;
import de.maikmerten.chainreaction.MoveListener;
import de.maikmerten.chainreaction.Player;
import de.maikmerten.chainreaction.Settings;
import de.maikmerten.chainreaction.ai.AI;
import de.maikmerten.chainreaction.ai.AIThread;
import de.maikmerten.chainreaction.ai.StandardAI;
import de.maikmerten.chainreaction.exceptions.ConfigUnreadableException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
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
		Settings settings = loadSettings();
		initGUI();
		startNewGame();
		setVisible(true);
	}

	private Settings loadSettings() {
		Properties properties = new Properties();
		try {
			checkConfigReadability();
			String configFileName = getConfigurationFileLocation().toString();
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(configFileName));
			properties.load(stream);
			int delay = Integer.valueOf(properties.getProperty("delay"));
			return new Settings(delay);
		} catch (ConfigUnreadableException e) {
			Settings settingsWithDefaultValues = new Settings();
			storeSettings(settingsWithDefaultValues);
			return settingsWithDefaultValues;
		} catch (IOException e) {
			System.err.println("Unable to load properties: " + e.getMessage());
			return new Settings();
		}
	}

	private void storeSettings(Settings settings) {
		String configFileName = getConfigurationFileLocation().toString();
		Properties properties = new Properties();
		properties.setProperty("delay", String.valueOf(settings.getDelay()));
		try {
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(configFileName));
			properties.store(stream, "Settings for Chain Reaction");
		} catch (IOException | ConfigUnreadableException e) {
			System.err.println("Unable to store properties: " + e.getMessage());
		}
	}

	private Path getConfigurationStorageLocation() {
		String homeDir = System.getProperties().getProperty("user.home");
		String configDir = ".chainReaction";
		return FileSystems.getDefault().getPath(homeDir, configDir);
	}

	private Path getConfigurationFileLocation() {
		String filename = "settings.properties";
		return FileSystems.getDefault().getPath(getConfigurationStorageLocation().toString(), filename);
	}

	private void checkConfigReadability() {
		Path path = getConfigurationStorageLocation();

		if (!Files.exists(path)) {
			try {
				Files.createDirectory(path);
			} catch (IOException e) {
				throw new ConfigUnreadableException(e);
			}
		}
		else if (!Files.isDirectory(path)) {
			System.err.println("Unable to read settings, " + path + " is not a directory!");
			throw new ConfigUnreadableException();
		}

		path = getConfigurationFileLocation();

		if (!Files.exists(path)) {
			throw new ConfigUnreadableException();
		}

		if (!Files.isRegularFile(path)) {
			System.err.println("Unable to read settings from " + path + ", is not a regular file");
			throw new ConfigUnreadableException();
		}
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
			sb.append(" | Current Score: ").append(game.getField().getTotalNumberOfAtomsForPlayer(Player.FIRST));
			sb.append(":").append(game.getField().getTotalNumberOfAtomsForPlayer(Player.SECOND));
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
