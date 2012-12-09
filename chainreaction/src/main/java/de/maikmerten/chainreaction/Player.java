package de.maikmerten.chainreaction;

import java.io.IOException;
import java.util.Properties;

import de.maikmerten.chainreaction.swing.UIAtom;


/**
 * @author jonny
 *
 */
public class Player {
	private final static Properties props;
	private final static Player[] players;
	
	static {
		props = new Properties();
		try {
			props.load(Player.class.getResourceAsStream("/player.properties"));
		}
		catch (IOException e) {
			System.err.println("could not load player properties");
		}
		players = new Player[2];
	}
	
	private final byte player;
	private final UIAtom atom;
	
	private Player(byte player) {
		this.player = player;
		String propFN = props.getProperty("player." + player) + "atom.properties";
		atom = new UIAtom(propFN);
	}
	
	public byte getPlayer() {
		return this.player;
	}
	
	public UIAtom createAtom() {
		return atom.copy();
	}
	
	public static Player getPlayer(byte player) {
		if(players[player-1] == null) {
			players[player-1] = new Player((byte)player);
		}
		return players[player-1];
	}
}
