package de.maikmerten.chainreaction.swing;

import java.io.IOException;
import java.util.Properties;

/**
 * @author jonny
 *
 */
public class UIPlayer {
	private final static Properties props;
	private final static UIPlayer[] players;
	
	static {
		props = new Properties();
		try {
			props.load(UIPlayer.class.getResourceAsStream("/player.properties"));
		}
		catch (IOException e) {
			System.err.println("could not load player properties");
		}
		players = new UIPlayer[2];
	}
	
	private final byte player;
	private final String propFN;
	
	private UIPlayer(byte player) {
		this.player = player;
		this.propFN = props.getProperty("player." + player) + "atom.properties";
	}
	
	public byte getPlayer() {
		return this.player;
	}
	
	public UIAtom createAtom() {
		return new UIAtom(propFN);
	}
	
	public static UIPlayer getPlayer(byte player) {
		if(players[player-1] == null) {
			players[player-1] = new UIPlayer((byte)player);
		}
		return players[player-1];
	}
}
