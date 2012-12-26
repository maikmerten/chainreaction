package de.maikmerten.chainreaction.swing;

import de.maikmerten.chainreaction.Player;

import java.awt.*;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

public class UIPlayer {
	private final static Properties props;
	private final static Map<Player, UIPlayer> players;
	
	static {
		props = new Properties();
		try {
			props.load(UIPlayer.class.getResourceAsStream("/player.properties"));
		}
		catch (IOException e) {
			System.err.println("could not load player properties");
		}
		players = new EnumMap<Player, UIPlayer>(Player.class);
	}
	
	private final Player player;
	private final String propFN;
	private final Map<Player, Color> bgColors;
	private final Map<Player, Color> fgColors;
	
	private UIPlayer(Player player) {
		this.player = player;
		this.propFN = props.getProperty("player." + player.name()) + "atom.properties";
		this.bgColors = new EnumMap<Player, Color>(Player.class);
		this.bgColors.put(Player.FIRST, new Color(21, 39, 99));
		this.bgColors.put(Player.SECOND, new Color(120, 0, 0));
		
		this.fgColors = new EnumMap<Player, Color>(Player.class);
		this.fgColors.put(Player.FIRST, new Color(111, 129, 189));
		this.fgColors.put(Player.SECOND, new Color(210, 90, 90));
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public UIAtom createAtom() {
		return new UIAtom(propFN);
	}
	
	public Color getBackground() {
		return bgColors.get(player);
	}
	
	public Color getForeground() {
		return fgColors.get(player);
	}
	
	public static UIPlayer getPlayer(Player player) {
		if(!players.containsKey(player)) {
			players.put(player, new UIPlayer(player));
		}
		return players.get(player);
	}
}
